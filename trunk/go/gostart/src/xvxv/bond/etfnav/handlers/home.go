package handlers

import (
	"fmt"
	"github.com/codegangsta/martini-contrib/render"
	"log"
	"time"
)

func HomeHandler(r render.Render, log *log.Logger) {
	nowTime := time.Now().Format("2006-01-02 15:04:05")
	dateStr := time.Now().Format("2006-01-02")
	rows, err := db.Query("select date_format(max(create_time), '%Y-%m-%d') from ETF_NAV")

	for rows.Next() {
		err = rows.Scan(&dateStr)
		checkErr(err, log)
	}

	log.Println(dateStr)
	begin := fmt.Sprintf("%v 00:00:00", dateStr)
	end := fmt.Sprintf("%v 23:59:59", dateStr)
	rows, err = db.Query("select nav_per_share,create_time,id from ETF_NAV where create_time > ? and create_time < ? order by create_time desc", begin, end)

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
	result["datas"] = datas
	r.HTML(200, "index", result)

}
