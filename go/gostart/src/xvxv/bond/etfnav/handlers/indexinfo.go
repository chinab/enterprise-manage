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
	switch infotype {
	case "0":
		tableName = "ETF_NAV"
		titleName = "CSOP China 5-Year Treasury Bond ETF"
	case "1":
		tableName = "csop_a50"
		titleName = "CSOP FTSE China A50 ETF"
	case "2":
		tableName = "csop_a80"
		titleName = "CSOP CES China A80 ETF"
	default:
		infotype = "0"
	}

	nowTime := time.Now().Format("2006-01-02 15:04:05")
	dateStr := time.Now().Format("2006-01-02")
	rows, err := db.Query("select date_format(max(create_time), '%Y-%m-%d') from " + tableName)

	for rows.Next() {
		var dateBytes []byte
		err = rows.Scan(&dateBytes)
		checkErr(err, log)

		if len(dateBytes) > 0 {
			dateStr = string(dateBytes)
		}
	}

	begin := fmt.Sprintf("%v 00:00:00", dateStr)
	end := fmt.Sprintf("%v 23:59:59", dateStr)
	rows, err = db.Query("select nav_per_share,create_time,id from "+tableName+" where create_time > ? and create_time < ? order by seq desc,create_time desc", begin, end)

	checkErr(err, log)
	datas := make([]map[string]string, 0)

	for rows.Next() {
		var value, createTime, id []byte
		err = rows.Scan(&value, &createTime, &id)
		checkErr(err, log)

		data := make(map[string]string)
		data["value"] = string(value)
		data["time"] = string(createTime)
		data["id"] = string(id)

		datas = append(datas, data)
	}

	result := make(map[string]interface{})
	result["nowTime"] = nowTime
	result["titleName"] = titleName
	result["infotype"] = infotype
	result["datas"] = datas
	r.HTML(200, "indexinfo", result)
}
