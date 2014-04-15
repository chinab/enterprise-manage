/**
 *  create by go-model-creater
 **/
package models

import (
	"time"
)

type LoginLog struct {
	Id          int64
	UserId      int64
	LoginTime   time.Time
	LogoutTime  time.Time
	LoginIp     string
	LoginName   string
	LoginStatus string
}
