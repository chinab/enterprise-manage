package main

import (
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"xvxv/osm"
)

func main() {
	s, err := osm.NewOsm("mysql", "root:root@/51jczj?charset=utf8", []string{"test.xml"})
	if err != nil {
		fmt.Println(err.Error())
	}

	paramMap := make(map[string]interface{})
	paramMap["uid"] = 1

	err = s.Query("selectUsers", paramMap)
	if err != nil {
		fmt.Println(err.Error())
	}
}
