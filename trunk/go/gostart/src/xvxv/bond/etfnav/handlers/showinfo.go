package handlers

import (
	"fmt"
	"github.com/codegangsta/martini"
	"github.com/martini-contrib/render"
	"log"
	"net/http"
	"strings"
)

func ShowInfo(r render.Render, params martini.Params, log *log.Logger, w http.ResponseWriter, req *http.Request) {
	if CheckRoot(r, params, w, req, log) {
		return
	}
	root := params["root"]

	tableName, _, _ := getValueByType(root, params["type"])

	rows, err := db.Query("select info from "+tableName+" where id=? and company=? ", params["id"], strings.ToUpper(root))
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
	result["root"], _ = BaseUrlMap[root]
	r.HTML(200, "showInfo", result)
}
