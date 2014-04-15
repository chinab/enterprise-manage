package services

import (
	"xvxv/51jczj/base"
	"xvxv/51jczj/models"
)

func Login(username string, password string) string {
	param := map[string]string{"Username": username}
	u := models.User{}
	base.Osm.Query("selectUserByUsername", param)(&u)
	return u.Password
}
