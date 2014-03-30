package admin

import (
	"github.com/martini-contrib/render"
	"github.com/martini-contrib/sessions"
	"net/http"
)

func ManagerHandler(w http.ResponseWriter, req *http.Request, r render.Render, session sessions.Session) {
	username := session.Get("username")
	if username != nil && username != "" {
		r.HTML(200, "admin/manager", "")
	} else {
		http.Redirect(w, req, "/login/manager", http.StatusFound)
	}

}
