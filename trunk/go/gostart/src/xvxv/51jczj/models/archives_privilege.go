/**
 *  create by go-model-creater
 **/
package models

type ArchivesPrivilege struct {
	Id              int64
	RoleId          int64
	ArchivesGroupId int64
	Editable        int16
	Addable         int16
	Publishable     int16
	Deleteable      int16
}
