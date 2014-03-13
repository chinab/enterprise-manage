package handlers

import (
	"github.com/martini-contrib/render"
)

func HomeHandler(r render.Render) {
	r.HTML(200, "home", "")
}
