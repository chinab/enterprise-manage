package main

import (
	"fmt"
	"github.com/codegangsta/martini"
	"github.com/martini-contrib/render"
	"net/http"
	"xvxv/51jczj/base"
	"xvxv/51jczj/controllers/home"
	"xvxv/51jczj/controllers/manager"
	"xvxv/51jczj/controllers/user"
	"xvxv/51jczj/models"
)

func main() {
	models.CreateDb()

	m := martini.Classic()
	m.Use(martini.Static("assets"))

	m.Use(render.Renderer(render.Options{
		Layout: "layout",
	}))

	m.Get("/", home.HomeHandler)
	m.Get("/manager", manager.ManagerHandler)
	m.Post("/login", user.LoginHandler)
	m.Get("/login", user.GoLoginHandler)
	m.Get("/login/:path", user.GoLoginHandler)

	http.ListenAndServe(fmt.Sprintf(":%s", base.WebPort), m)

}
