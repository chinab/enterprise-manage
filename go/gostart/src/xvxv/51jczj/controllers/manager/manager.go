package manager

import (
	"github.com/martini-contrib/render"
	"net/http"
	"xvxv/utils"
)

func ManagerHandler(w http.ResponseWriter, req *http.Request, r render.Render) {
	sess := utils.GetSession(w, req)
	token := sess.Get("token")
	if token != nil && token != "" {
		r.HTML(200, "manager/manager", "manager")
	} else {
		http.Redirect(w, req, "/login/manager", http.StatusFound)
	}

}
