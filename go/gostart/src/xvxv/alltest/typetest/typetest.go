package main

import (
	"fmt"
	"github.com/go-martini/martini"
	"net/http"
	"time"
	"xvxv/utils"
)

func main() {
	m := martini.Classic()
	m.Get("/", func(w http.ResponseWriter, r *http.Request) string {

		cookie, _ := r.Cookie("client_uuid")
		if cookie == nil {
			expiration := time.Now()
			expiration = expiration.AddDate(5, 0, 0)
			uuid, _ := utils.GenUUID()
			cookie = &http.Cookie{Name: "client_uuid", Value: uuid, Expires: expiration}
			http.SetCookie(w, cookie)
		}

		fmt.Println(cookie.Value, r.RemoteAddr)

		return "Hello world!"
	})
	http.ListenAndServe(":7070", m)
}
