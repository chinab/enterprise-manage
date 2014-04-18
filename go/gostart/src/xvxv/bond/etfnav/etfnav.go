package main

import (
	"fmt"
	"github.com/codegangsta/martini"
	"github.com/martini-contrib/render"
	"net/http"
	"xvxv/bond/etfnav/handlers"
)

func main() {
	m := martini.Classic()
	m.Use(martini.Static("assets"))

	m.Use(render.Renderer())

	// m.Use(handlers.WriteLog)

	m.Get("/:root", handlers.HomeHandler)
	m.Get("/:root/", handlers.HomeHandler)
	m.Get("/:root/indexInfo/:type", handlers.IndexInfoHandler)
	m.Get("/:root/showInfo/:type/:id", handlers.ShowInfo)
	m.Get("/:root/iframe_en", handlers.IframeEnHandler)
	m.Get("/:root/iframe_en/:type", handlers.IframeEnHandler)
	m.Get("/:root/iframeData_en/:type", handlers.IframeEnDataHandler)

	http.ListenAndServe(fmt.Sprintf(":%s", handlers.WebPort), m)
}
