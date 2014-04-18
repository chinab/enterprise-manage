package handlers

import (
	"github.com/codegangsta/martini"
	"github.com/martini-contrib/render"
	"log"
	"net/http"
)

func HomeHandler(r render.Render, params martini.Params, w http.ResponseWriter, req *http.Request, log *log.Logger) {
	if CheckRoot(r, params, w, req, log) {
		return
	}
	r.HTML(200, "home", map[string]string{"baseurl": BaseUrlMap[params["root"]]})
}
