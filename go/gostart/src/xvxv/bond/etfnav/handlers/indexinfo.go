package handlers

import (
	"fmt"
	"github.com/codegangsta/martini"
	"github.com/martini-contrib/render"
	"log"
	"time"
)

func IndexInfoHandler(r render.Render, params martini.Params, log *log.Logger) {
	if CheckRoot(r, params) {
		return
	}

	tableName, titleName, infotype := getValueByType(params["root"], params["type"])

	nowTime := time.Now().Format("2006-01-02 15:04:05")
	dateStr := time.Now().Format("2006-01-02")
	rows, err := db.Query("select date_format(max(create_time), '%Y-%m-%d') from " + tableName)
	checkErr(err, log)

	for rows != nil && rows.Next() {
		var dateBytes []byte
		err = rows.Scan(&dateBytes)
		checkErr(err, log)

		if len(dateBytes) > 0 {
			dateStr = string(dateBytes)
		}
	}

	begin := fmt.Sprintf("%v 00:00:00", dateStr)
	end := fmt.Sprintf("%v 23:59:59", dateStr)
	rows, err = db.Query("select nav_per_share,create_time,id from "+tableName+" where create_time > ? and create_time < ? order by seq desc,create_time desc LIMIT 0,1000", begin, end)
	checkErr(err, log)

	datas := make([]map[string]string, 0)
	for rows != nil && rows.Next() {
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

	row := db.QueryRow("select max(MessageInfo) from ETF_Message where Date=? and ProductCode='83199'", dateStr)
	mi := ""
	if row != nil {
		var messageInfo []byte
		row.Scan(&messageInfo)

		mi = string(messageInfo)
	}

	if mi != "" {
		mi = fmt.Sprintf("[*%s]", mi)
	}
	result["messageInfo"] = mi

	result["root"] = params["root"]
	r.HTML(200, "indexinfo", result)
}
