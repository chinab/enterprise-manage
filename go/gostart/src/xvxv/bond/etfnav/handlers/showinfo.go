package handlers

import (
	"fmt"
	"github.com/codegangsta/martini"
	"github.com/codegangsta/martini-contrib/render"
	"log"
)

func ShowInfo(r render.Render, params martini.Params, log *log.Logger) {
	rows, err := db.Query("select info from ETF_NAV where id=?", params["id"])
	result := ""
	fmt.Println(params["id"])

	for rows.Next() {
		err = rows.Scan(&result)
		checkErr(err, log)
	}
	r.HTML(200, "showInfo", result)
}
