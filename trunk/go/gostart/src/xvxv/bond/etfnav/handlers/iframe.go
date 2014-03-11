package handlers

import (
	"encoding/json"
	"fmt"
	"github.com/martini-contrib/render"
	"log"
	"strconv"
	"time"
)

func IframeEnHandler(r render.Render, log *log.Logger) {
	result := iframeEnData(log)
	r.HTML(200, "iframe_en", result)
}

func IframeEnDataHandler(log *log.Logger) string {
	result := iframeEnData(log)
	b, err := json.Marshal(result)
	checkErr(err, log)
	return string(b)
}

func iframeEnData(log *log.Logger) map[string]string {
	result := make(map[string]string)

	result["nIopvPrice"] = "--"
	result["sIopvDate"] = "--"
	result["sIopvTime"] = "--"
	result["nExchangeRate"] = "--"
	result["nIopvPriceHKD"] = "--"
	result["sCurrentTime"] = "--"

	rows, err := db.Query("SELECT nav_per_share,DATE_FORMAT(create_time, '%d %b.%Y'),LOWER(DATE_FORMAT(create_time, '%h:%i%p')),id FROM ETF_NAV order by create_time desc LIMIT 0,1")

	checkErr(err, log)

	for rows.Next() {
		var value, createDate, createTime, id string
		err = rows.Scan(&value, &createDate, &createTime, &id)
		checkErr(err, log)

		result["nIopvPrice"] = value
		result["sIopvDate"] = createDate
		result["sIopvTime"] = createTime
	}

	rows, err = db.Query("SELECT cn_value FROM t_cnhhkd order by create_date desc LIMIT 0,1")

	checkErr(err, log)

	for rows.Next() {
		var value string
		err = rows.Scan(&value)
		checkErr(err, log)

		result["nExchangeRate"] = value
	}

	nExchangeRate, _ := strconv.ParseFloat(result["nExchangeRate"], 64)
	nIopvPrice, _ := strconv.ParseFloat(result["nIopvPrice"], 64)

	nIopvPriceHKD := nExchangeRate * nIopvPrice

	if nIopvPriceHKD != 0.0 {
		result["nIopvPriceHKD"] = fmt.Sprintf("%9.4f", nIopvPriceHKD)
	}

	result["sCurrentTime"] = fmt.Sprintf("Hong Kong Time: %s", time.Now().Format("03:04pm"))

	return result
}
