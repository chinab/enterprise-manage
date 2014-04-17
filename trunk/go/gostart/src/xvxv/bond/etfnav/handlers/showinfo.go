package handlers

import (
	"fmt"
	"github.com/codegangsta/martini"
	"github.com/martini-contrib/render"
	"log"
)

func ShowInfo(r render.Render, params martini.Params, log *log.Logger) {
	if CheckRoot(r, params) {
		return
	}

	tableName, _, _ := getValueByType(params["root"], params["type"])

	rows, err := db.Query("select info from "+tableName+" where id=?", params["id"])
	if checkErr(err, log) {
		return
	}

	info := ""
	fmt.Println(params["id"])

	for rows.Next() {
		err = rows.Scan(&info)
		checkErr(err, log)
	}

	result := make(map[string]interface{})
	result["info"] = info
	result["root"], _ = BaseUrlMap[params["root"]]
	r.HTML(200, "showInfo", result)
}
