package handlers

import (
	"github.com/codegangsta/martini"
	"github.com/martini-contrib/render"
)

func HomeHandler(r render.Render, params martini.Params) {
	if CheckRoot(r, params) {
		return
	}
	r.HTML(200, "home", map[string]string{"baseurl": BaseUrlMap[params["root"]]})
}
