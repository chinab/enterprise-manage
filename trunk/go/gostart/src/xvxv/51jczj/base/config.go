package base

import (
	"fmt"
	"github.com/sbinet/go-config/config"
)

var DbHost, DbPort, DbUsername, DbPassword, DbSchema, WebPort string

func init() {
	setting, err := config.ReadDefault("config/server.cfg")
	handlerErr(err)

	DbHost, err = setting.String("mysql", "host")
	handlerErr(err)

	DbPort, err = setting.String("mysql", "port")
	handlerErr(err)

	DbUsername, err = setting.String("mysql", "username")
	handlerErr(err)

	DbPassword, err = setting.String("mysql", "password")
	handlerErr(err)

	DbSchema, err = setting.String("mysql", "schema")
	handlerErr(err)

	WebPort, err = setting.String("web", "port")
	handlerErr(err)

	fmt.Println("listening port : ", WebPort)
	fmt.Println("database info  : ", DbHost, DbPort, DbUsername, DbPassword, DbSchema)

}

func handlerErr(err error) {
	if err != nil {
		panic(err)
	}
}
