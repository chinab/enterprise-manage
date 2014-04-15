/**
 *  create by go-model-creater
 **/
package models

import (
	"time"
)

type User struct {
	Id          int64
	Email       string
	Mobile      string
	Nickname    string
	Password    string
	Description string
	Name        string
	Birth       time.Time
	Province    string
	City        string
	Company     string
	Address     string
	Sex         string
	ContactInfo string
	CreateTime  time.Time
}
