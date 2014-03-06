package main

import (
	"fmt"
	"github.com/codegangsta/martini"
	"github.com/codegangsta/martini-contrib/render"
	"net/http"
	"xvxv/bond/etfnav/handlers"
)

func main() {
	//defer utils.SetLogOutPut("logs/etfnav.log")()

	m := martini.Classic()
	m.Use(martini.Static("assets"))

	m.Use(render.Renderer())

	m.Get("/showInfo/:id", handlers.ShowInfo)
	m.Get("/", handlers.HomeHandler)
	m.Get("/iframe_en", handlers.IframeEnHandler)
	m.Get("/iframeData_en", handlers.IframeEnDataHandler)

	http.ListenAndServe(fmt.Sprintf(":%s", handlers.WebPort), m)
}
