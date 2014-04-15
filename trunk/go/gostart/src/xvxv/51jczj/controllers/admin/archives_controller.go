package admin

import (
	"github.com/martini-contrib/render"
	"github.com/martini-contrib/sessions"
	"net/http"
)

func ArchivesHandler(w http.ResponseWriter, req *http.Request, r render.Render, session sessions.Session) {
	username, ok := session.Get("username").(string)
	if ok && username != "" {
		r.HTML(200, "admin/archives", map[string]string{"username": username})
	} else {
		http.Redirect(w, req, "/login/archives", http.StatusFound)
	}

}
