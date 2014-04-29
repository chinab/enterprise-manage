package handlers

import (
	"github.com/go-martini/martini"
	"github.com/martini-contrib/render"
	"log"
	"net/http"
)

func HomeHandler(r render.Render, params martini.Params, w http.ResponseWriter, req *http.Request, log *log.Logger) {
	if CheckRoot(r, params, w, req, log) {
		return
	}
	root := params["root"]
	r.HTML(200, "home", map[string]interface{}{"baseurl": BaseUrlMap[root], "tabTexts": TabTextMap[root], "titles": TitleMap[root]})
}
