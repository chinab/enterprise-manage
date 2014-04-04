package main

import (
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"xvxv/osm"
	// "xvxv/osm/models"
)

func main() {
	s, err := osm.NewOsm("mysql", "root:root@/51jczj?charset=utf8", []string{"test.xml"})
	if err != nil {
		fmt.Println(err.Error())
	}

	user := make(map[string]int16)
	user["Id"] = 1

	err = s.Query("selectUsers", user)
	if err != nil {
		fmt.Println(err.Error())
	}
}
