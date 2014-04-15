/**
 *  create by go-model-creater
 **/
package models

import (
	"time"
)

type ArchivesGroup struct {
	Id          int64
	Title       string
	Keyword     string
	Description string
	CreateTime  time.Time
	ParentId    int64
	Level       int64
}
