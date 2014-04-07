package base

import (
	"fmt"
	"github.com/codegangsta/martini"
	_ "github.com/go-sql-driver/mysql"
	"github.com/sbinet/go-config/config"
	"strings"
	"xvxv/osm"
)

var HomeWebPort, AdminWebPort, RedisHost, RedisPort string
var Osm *osm.Osm

func init() {
	setting, err := config.ReadDefault("config/server.cfg")
	handlerErr(err)

	dbHost, err := setting.String("mysql", "host")
	handlerErr(err)

	dbPort, err := setting.String("mysql", "port")
	handlerErr(err)

	dbUsername, err := setting.String("mysql", "username")
	handlerErr(err)

	dbPassword, err := setting.String("mysql", "password")
	handlerErr(err)

	dbSchema, err := setting.String("mysql", "schema")
	handlerErr(err)

	xmlPathsStr, err := setting.String("osm", "xmlpaths")
	xmlPaths := strings.Split(xmlPathsStr, ",")

	url := fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8", dbUsername, dbPassword, dbHost, dbPort, dbSchema)
	Osm, err = osm.NewOsm("mysql", url, xmlPaths)
	if err != nil {
		fmt.Println(err)
	}

	HomeWebPort, err = setting.String("home-web", "port")
	handlerErr(err)

	AdminWebPort, err = setting.String("admin-web", "port")
	handlerErr(err)

	RedisHost, err = setting.String("redis", "host")
	handlerErr(err)

	RedisPort, err = setting.String("redis", "port")
	handlerErr(err)

	appEnv, err := setting.String("app", "env")
	handlerErr(err)

	martini.Env = appEnv

	fmt.Println("home port     : ", HomeWebPort)
	fmt.Println("admin port    : ", AdminWebPort)
	fmt.Println("database info : ", dbHost, dbPort, dbUsername, dbPassword, dbSchema)
	fmt.Println("redis info    : ", RedisHost, RedisPort)
}

func handlerErr(err error) {
	if err != nil {
		panic(err)
	}
}
