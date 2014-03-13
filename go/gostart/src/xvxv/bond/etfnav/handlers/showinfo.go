package handlers

import (
	"fmt"
	"github.com/codegangsta/martini"
	"github.com/martini-contrib/render"
	"log"
)

func ShowInfo(r render.Render, params martini.Params, log *log.Logger) {
	infotype := params["type"]
	tableName := "ETF_NAV"
	//titleName := "CSOP China 5-Year Treasury Bond ETF"
	//columnName := "NAV per Share"
	switch infotype {
	case "0":
		tableName = "ETF_NAV"
		//titleName = "CSOP China 5-Year Treasury Bond ETF"
		//columnName = "NAV per Share"
	case "1":
		tableName = "csop_a50"
		//titleName = "CSOP China 5-Year Treasury Bond A50"
		//columnName = "A50 per Share"
	case "2":
		tableName = "csop_a80"
		//titleName = "CSOP China 5-Year Treasury Bond A80"
		//columnName = "A80 per Share"
	default:
		infotype = "0"
	}

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
