package admin

import (
	// "fmt"
	"github.com/codegangsta/martini"
	"github.com/martini-contrib/render"
	"github.com/martini-contrib/sessions"
	"net/http"
)

func LoginHandler(w http.ResponseWriter, req *http.Request, r render.Render, session sessions.Session) {
	username := req.PostFormValue("username")
	password := req.PostFormValue("password")
	path := req.PostFormValue("path")

	if username == "xvxv" && password == "123456" {
		session.Set("username", username)
		http.Redirect(w, req, "/"+path, http.StatusFound)
		return
	}
	r.HTML(200, "admin/login", "login")
}

func GoLoginHandler(r render.Render, params martini.Params) {
	path := params["path"]
	if path == "" {
		path = "/"
	}
	r.HTML(200, "admin/login", path)
}
