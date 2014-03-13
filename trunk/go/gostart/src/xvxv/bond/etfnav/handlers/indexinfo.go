package handlers

import (
	"fmt"
	"github.com/codegangsta/martini"
	"github.com/martini-contrib/render"
	"log"
	"time"
)

func IndexInfoHandler(r render.Render, params martini.Params, log *log.Logger) {
	infotype := params["type"]
	tableName := "ETF_NAV"
	titleName := "CSOP China 5-Year Treasury Bond ETF"
	columnName := "NAV per Share"
	switch infotype {
	case "0":
		tableName = "ETF_NAV"
		titleName = "CSOP China 5-Year Treasury Bond ETF"
		columnName = "NAV per Share"
	case "1":
		tableName = "csop_a50"
		titleName = "CSOP China 5-Year Treasury Bond A50"
		columnName = "A50 per Share"
	case "2":
		tableName = "csop_a80"
		titleName = "CSOP China 5-Year Treasury Bond A80"
		columnName = "A80 per Share"
	default:
		infotype = "0"
	}

	nowTime := time.Now().Format("2006-01-02 15:04:05")
	dateStr := time.Now().Format("2006-01-02")
	rows, err := db.Query("select date_format(max(create_time), '%Y-%m-%d') from " + tableName)

	for rows.Next() {
		err = rows.Scan(&dateStr)
		checkErr(err, log)
	}

	begin := fmt.Sprintf("%v 00:00:00", dateStr)
	end := fmt.Sprintf("%v 23:59:59", dateStr)
	rows, err = db.Query("select nav_per_share,create_time,id from "+tableName+" where create_time > ? and create_time < ? order by create_time desc", begin, end)

	checkErr(err, log)
	datas := make([]map[string]string, 0)

	for rows.Next() {
		var value, createTime, id string
		err = rows.Scan(&value, &createTime, &id)
		checkErr(err, log)

		data := make(map[string]string)
		data["value"] = value
		data["time"] = createTime
		data["id"] = id

		datas = append(datas, data)
	}

	result := make(map[string]interface{})
	result["nowTime"] = nowTime
	result["titleName"] = titleName
	result["columnName"] = columnName
	result["infotype"] = infotype
	result["datas"] = datas
	r.HTML(200, "indexinfo", result)
}
