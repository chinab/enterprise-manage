package main

import (
	"fmt"
	"github.com/boj/redistore"
	"github.com/go-martini/martini"
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

func adminserver() {
	m := martini.Classic()
	// m.Use(martini.Static("./assets/"))

	store, _ := redistore.NewRediStore(10, "tcp", fmt.Sprintf(":%s", base.RedisPort), base.RedisHost, []byte("secret-key"))

	store.SetMaxAge(3600)
	defer store.Close()
	m.Use(sessions.Sessions("51jczj_session", store))

	m.Use(render.Renderer(render.Options{
		Layout: "admin_layout",
	}))

	/***********static*************/
	m.Get("/css/**", http.FileServer(http.Dir("./assets")).ServeHTTP)
	m.Get("/editor/**", http.FileServer(http.Dir("./assets")).ServeHTTP)
	m.Get("/fonts/**", http.FileServer(http.Dir("./assets")).ServeHTTP)
	m.Get("/img/**", http.FileServer(http.Dir("./assets")).ServeHTTP)
	m.Get("/js/**", http.FileServer(http.Dir("./assets")).ServeHTTP)

	/***********biz*************/
	m.Get("/", admin.ArchivesHandler)
	m.Get("/archives", admin.ArchivesHandler)

	m.Post("/login", admin.LoginHandler)
	m.Get("/logout", admin.LogoutHandler)
	m.Get("/login", admin.GoLoginHandler)
	m.Get("/login/:path", admin.GoLoginHandler)

	http.ListenAndServe(fmt.Sprintf(":%s", base.AdminWebPort), m)
}
