package handlers

import (
	"fmt"
	"github.com/codegangsta/martini"
	"github.com/martini-contrib/render"
	"log"
)

func ShowInfo(r render.Render, params martini.Params, log *log.Logger) {
	tableName, _, _ := getValueByType(params["type"])

	rows, err := db.Query("select info from "+tableName+" where id=?", params["id"])
	info := ""
	fmt.Println(params["id"])

	for rows.Next() {
		err = rows.Scan(&info)
		checkErr(err, log)
	}

	result := make(map[string]interface{})
	result["info"] = info
	//result["titleName"] = titleName
	//result["columnName"] = columnName
	r.HTML(200, "showInfo", result)
}
