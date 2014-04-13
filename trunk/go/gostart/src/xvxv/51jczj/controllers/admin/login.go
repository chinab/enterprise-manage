package admin

import (
	// "fmt"
	"github.com/codegangsta/martini"
	"github.com/martini-contrib/render"
	"github.com/martini-contrib/sessions"
	"net/http"
	// "strings"
	"xvxv/51jczj/services"
)

func LoginHandler(w http.ResponseWriter, req *http.Request, r render.Render, session sessions.Session) {
	username := req.PostFormValue("username")
	password := req.PostFormValue("password")
	path := req.PostFormValue("path")

	dbPassword := services.Login(username, password)
	msg := ""

	if len(dbPassword) == 0 {
		msg = "用户名不存在!"
	} else if dbPassword == password {
		session.Set("username", username)
		http.Redirect(w, req, "/"+path, http.StatusFound)
		return
	} else {
		msg = "密码错误!"
	}

	r.HTML(200, "admin/login", map[string]string{"path": path, "msg": msg})
}

func GoLoginHandler(r render.Render, params martini.Params) {
	path := params["path"]
	if path == "" {
		path = "/"
	}
	r.HTML(200, "admin/login", map[string]string{"path": path})
}

func LogoutHandler(w http.ResponseWriter, req *http.Request, session sessions.Session) {
	session.Delete("username")
	http.Redirect(w, req, "/login", http.StatusFound)
}
