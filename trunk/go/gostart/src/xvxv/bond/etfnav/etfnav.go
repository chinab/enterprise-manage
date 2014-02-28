package main

import (
	"database/sql"
	"fmt"
	_ "github.com/Go-SQL-Driver/MySQL"
	"github.com/codegangsta/martini"
	"github.com/codegangsta/martini-contrib/render"
	"github.com/sbinet/go-config/config"
	"log"
	"net/http"
	"strings"
	"time"
	//"xvxv/utils"
)

func main() {
	//defer utils.SetLogOutPut("logs/etfnav.log")()

	setting, err := config.ReadDefault("etfnav.cfg")
	handlerErr(err)

	host, err := setting.String("mysql", "host")
	handlerErr(err)

	port, err := setting.String("mysql", "port")
	handlerErr(err)

	username, err := setting.String("mysql", "username")
	handlerErr(err)

	password, err := setting.String("mysql", "password")
	handlerErr(err)

	webPort, err := setting.String("web", "port")
	handlerErr(err)

	url := fmt.Sprintf("%v:%v@tcp(%v:%v)/ss_product?charset=utf8", username, password, host, port)

	db, err := sql.Open("mysql", url)
	if err != nil {
		panic(err)
	}

	m := martini.Classic()
	m.Use(martini.Static("assets"))

	m.Use(render.Renderer())

	m.Get("/showInfo/:id", func(r render.Render, params martini.Params, log *log.Logger) {
		rows, err := db.Query("select info from ETF_NAV where id=?", params["id"])
		result := ""
		fmt.Println(params["id"])

		for rows.Next() {
			err = rows.Scan(&result)
			checkErr(err, log)
		}
		r.HTML(200, "showInfo", result)
	})

	m.Get("/", func(r render.Render, log *log.Logger) {
		var maxDate string
		dateStr := time.Now().Format("2006-01-02")
		rows, err := db.Query("select max(create_time) from ETF_NAV")

		for rows.Next() {
			err = rows.Scan(&maxDate)
			checkErr(err, log)
			if strings.Contains(maxDate, " ") {
				dateStr = maxDate[0:strings.Index(maxDate, " ")]
			}
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
		result["maxDate"] = maxDate
		result["datas"] = datas
		r.HTML(200, "index", result)

	})
	http.ListenAndServe(fmt.Sprintf(":%s", webPort), m)
}

func checkErr(err error, log *log.Logger) {
	if err != nil {
		log.Println(err)
	}
}

func handlerErr(err error) {
	if err != nil {
		panic(err)
	}
}
