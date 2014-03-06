package handlers

import (
	"database/sql"
	"fmt"
	_ "github.com/Go-SQL-Driver/MySQL"
	"github.com/sbinet/go-config/config"
	"log"
)

var db *sql.DB
var WebPort string

func init() {
	setting, err := config.ReadDefault("etfnav.cfg")
	handlerErr(err)

	host, err := setting.String("mysql", "host")
	handlerErr(err)

	port, err := setting.String("mysql", "port")
	handlerErr(err)

	username, err := setting.String("mysql", "username")
	handlerErr(err)

	password, err := setting.String("mysql", "password")
	handlerErr(err)

	WebPort, err = setting.String("web", "port")
	handlerErr(err)

	url := fmt.Sprintf("%v:%v@tcp(%v:%v)/ss_product?charset=utf8", username, password, host, port)

	db, err = sql.Open("mysql", url)
	if err != nil {
		panic(err)
	}
}

func checkErr(err error, log *log.Logger) {
	if err != nil {
		log.Println(err)
	}
}

func handlerErr(err error) {
	if err != nil {
		panic(err)
	}
}
