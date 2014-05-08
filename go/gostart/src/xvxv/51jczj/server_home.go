package main

import (
	"fmt"
	"github.com/boj/redistore"
	"github.com/go-martini/martini"
	"github.com/martini-contrib/render"
	"github.com/martini-contrib/sessions"
	"net/http"
	"xvxv/51jczj/base"
	"xvxv/51jczj/controllers/home"
)

func main() {
	homeserver()
}

func homeserver() {
	m := martini.Classic()
	// m.Use(martini.Static("assets"))

	m.Use(render.Renderer(render.Options{
		Layout: "home_layout",
	}))

	/***********static*************/
	m.Get("/css/**", http.FileServer(http.Dir("./assets")).ServeHTTP)
	m.Get("/editor/**", http.FileServer(http.Dir("./assets")).ServeHTTP)
	m.Get("/fonts/**", http.FileServer(http.Dir("./assets")).ServeHTTP)
	m.Get("/img/**", http.FileServer(http.Dir("./assets")).ServeHTTP)
	m.Get("/js/**", http.FileServer(http.Dir("./assets")).ServeHTTP)

	/***********biz*************/
	m.Get("/", home.HomeHandler)

	http.ListenAndServe(fmt.Sprintf(":%s", base.HomeWebPort), m)
}
