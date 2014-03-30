package main

import (
	"fmt"
	"github.com/boj/redistore"
	"github.com/codegangsta/martini"
	"github.com/martini-contrib/render"
	"github.com/martini-contrib/sessions"
	"net/http"
	"xvxv/51jczj/base"
	"xvxv/51jczj/controllers/admin"
	"xvxv/51jczj/controllers/home"
	// "xvxv/51jczj/models"
)

func main() {
	go adminserver()
	homeserver()
}

func homeserver() {
	m := martini.Classic()
	m.Use(martini.Static("assets"))

	m.Use(render.Renderer(render.Options{
		Layout: "home_layout",
	}))

	m.Get("/", home.HomeHandler)

	http.ListenAndServe(fmt.Sprintf(":%s", base.HomeWebPort), m)
}

func adminserver() {
	m := martini.Classic()
	m.Use(martini.Static("assets"))

	store := redistore.NewRediStore(10, "tcp", fmt.Sprintf(":%s", base.RedisPort), base.RedisHost, []byte("secret-key"))
	store.SetMaxAge(3600)
	defer store.Close()
	m.Use(sessions.Sessions("51jczj_session", store))

	m.Use(render.Renderer(render.Options{
		Layout: "admin_layout",
	}))

	m.Get("/", admin.ManagerHandler)
	m.Post("/login", admin.LoginHandler)
	m.Get("/login", admin.GoLoginHandler)
	m.Get("/login/:path", admin.GoLoginHandler)

	http.ListenAndServe(fmt.Sprintf(":%s", base.AdminWebPort), m)
}
