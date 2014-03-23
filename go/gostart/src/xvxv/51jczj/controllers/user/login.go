package user

import (
	// "fmt"
	"github.com/codegangsta/martini"
	"github.com/martini-contrib/render"
	"net/http"
	"xvxv/utils"
)

func LoginHandler(w http.ResponseWriter, req *http.Request, r render.Render) {

	sess := utils.GetSession(w, req)

	username := req.PostFormValue("username")
	password := req.PostFormValue("password")
	path := req.PostFormValue("path")

	if username == "xvxv" && password == "123456" {
		token, err := utils.GenUUID()
		if err == nil {
			sess.Set("token", token)

			http.Redirect(w, req, "/"+path, http.StatusFound)
			return
		}
	}
	r.HTML(200, "user/login", "login")
}

func GoLoginHandler(r render.Render, params martini.Params) {
	path := params["path"]
	if path == "" {
		path = "home"
	}
	r.HTML(200, "user/login", path)
}
