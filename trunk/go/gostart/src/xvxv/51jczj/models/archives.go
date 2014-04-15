/**
 *  create by go-model-creater
 **/
package models

import (
	"time"
)

type Archives struct {
	Id         int64
	UserId     int64
	GroupId    int64
	Title      string
	Content    string
	Keyword    string
	Author     string
	CreateTime time.Time
	Published  int16
}
