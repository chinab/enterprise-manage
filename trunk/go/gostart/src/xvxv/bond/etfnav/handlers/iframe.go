package handlers

import (
	"encoding/json"
	"fmt"
	"github.com/martini-contrib/render"
	"log"
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

	rows, err := db.Query("SELECT nav_per_share,DATE_FORMAT(create_time, '%d %b.%Y'),LOWER(DATE_FORMAT(create_time, '%h:%i%p')),id FROM ETF_NAV order by create_time desc LIMIT 0,1")

	checkErr(err, log)

	for rows.Next() {
		var value, createDate, createTime, id string
		err = rows.Scan(&value, &createDate, &createTime, &id)
		checkErr(err, log)

		result["nIopvPrice"] = value
		result["sIopvDate"] = createDate
		result["sIopvTime"] = createTime
		result["nIopvPriceHKD"] = "--"
		//result["id"] = id
	}

	result["nExchangeRate"] = "--"
	result["sCurrentTime"] = fmt.Sprintf("Hong Kong Time: %s", time.Now().Format("03:04pm"))

	return result
}
