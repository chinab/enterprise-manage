package handlers

import (
	"encoding/json"
	"fmt"
	"github.com/go-martini/martini"
	"github.com/martini-contrib/render"
	"log"
	"net/http"
	"strconv"
	"strings"
	"time"
)

func IframeEnHandler(r render.Render, params martini.Params, log *log.Logger, w http.ResponseWriter, req *http.Request) {
	if CheckRoot(r, params, w, req, log) {
		return
	}
	root := params["root"]

	tableName, _, infotype := getValueByType(root, params["type"])

	result := iframeEnData(log, tableName, root)
	result["infotype"] = infotype
	result["root"], _ = BaseUrlMap[root]

	r.HTML(200, "iframe_en", result)
}

func IframeEnDataHandler(r render.Render, params martini.Params, log *log.Logger, w http.ResponseWriter, req *http.Request) string {
	if CheckRoot(r, params, w, req, log) {
		return ""
	}
	root := params["root"]

	tableName, _, _ := getValueByType(root, params["type"])

	result := iframeEnData(log, tableName, root)
	b, err := json.Marshal(result)
	checkErr(err, log)
	return string(b)
}

func iframeEnData(log *log.Logger, tableName string, root string) map[string]string {
	result := make(map[string]string)

	rows, err := db.Query("SELECT nav_per_share,create_time FROM "+tableName+" where company=? order by seq desc,create_time desc LIMIT 0,1", strings.ToUpper(root))
	checkErr(err, log)

	createTime := "2014-01-01 00:00:00"
	cnDate := "2014-01-01 00:00am"
	nIopvPrice := "--"
	nExchangeRate := "--"
	for rows != nil && rows.Next() {
		err = rows.Scan(&nIopvPrice, &createTime)
		checkErr(err, log)
	}

	rows, err = db.Query("SELECT cn_value,cn_date FROM ss_product.t_cnhhkd order by create_date desc LIMIT 0,1")
	checkErr(err, log)

	for rows != nil && rows.Next() {
		err = rows.Scan(&nExchangeRate, &cnDate)
		checkErr(err, log)
	}

	t1, _ := time.Parse("2006-01-02 15:04:05", createTime)
	t2, _ := time.Parse("2006-01-02 03:04pm", cnDate)

	result["nIopvPrice"] = nIopvPrice
	result["nExchangeRate"] = nExchangeRate

	if t1.After(t2) {
		result["sIopvDate"] = t1.Format("02 Jan.2006")
		result["sIopvTime"] = t1.Format("03:04pm")
	} else {
		result["sIopvDate"] = t2.Format("02 Jan.2006")
		result["sIopvTime"] = t2.Format("03:04pm")
	}

	nExchangeRateFloat, _ := strconv.ParseFloat(nExchangeRate, 64)
	nIopvPriceFloat, _ := strconv.ParseFloat(nIopvPrice, 64)

	nIopvPriceHKD := nExchangeRateFloat * nIopvPriceFloat

	if nIopvPriceHKD != 0.0 {
		result["nIopvPriceHKD"] = fmt.Sprintf("%9.4f", nIopvPriceHKD)
	} else {
		result["nIopvPriceHKD"] = "--"
	}

	result["sCurrentTime"] = fmt.Sprintf("Hong Kong Time: %s", time.Now().Format("03:04pm"))

	return result
}
