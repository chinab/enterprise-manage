package base

import (
	"fmt"
	"github.com/sbinet/go-config/config"
)

var DbHost, DbPort, DbUsername, DbPassword, DbSchema, HomeWebPort, AdminWebPort, RedisHost, RedisPort string

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

	HomeWebPort, err = setting.String("home-web", "port")
	handlerErr(err)

	AdminWebPort, err = setting.String("admin-web", "port")
	handlerErr(err)

	RedisHost, err = setting.String("redis", "host")
	handlerErr(err)

	RedisPort, err = setting.String("redis", "port")
	handlerErr(err)

	fmt.Println("home port     : ", HomeWebPort)
	fmt.Println("admin port    : ", AdminWebPort)
	fmt.Println("database info : ", DbHost, DbPort, DbUsername, DbPassword, DbSchema)
	fmt.Println("redis info    : ", RedisHost, RedisPort)

}

func handlerErr(err error) {
	if err != nil {
		panic(err)
	}
}
