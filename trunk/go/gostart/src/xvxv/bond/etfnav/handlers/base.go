package handlers

import (
	"database/sql"
	"fmt"
	"github.com/codegangsta/martini"
	_ "github.com/go-sql-driver/mysql"
	"github.com/martini-contrib/render"
	"github.com/sbinet/go-config/config"
	"log"
	"net/http"
	"strings"
	"time"
	"xvxv/utils"
)

var db *sql.DB
var WebPort string
var etfWebLogStmt *sql.Stmt
var tableMap map[string]([]string)

const (
	TABLE_SIZE = 4
)

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

	tablemappingKeys, err := setting.Options("tablemapping")
	handlerErr(err)

	tableMap = make(map[string]([]string))
	for _, option := range tablemappingKeys {
		tableStr, err := setting.String("tablemapping", option)
		handlerErr(err)

		tables := strings.Split(tableStr, ",")
		if len(tables) == TABLE_SIZE {
			tableMap[option] = tables
		}
	}

	url := fmt.Sprintf("%v:%v@tcp(%v:%v)/ss_product?charset=utf8", username, password, host, port)

	db, err = sql.Open("mysql", url)
	handlerErr(err)

	etfWebLogStmt, err = db.Prepare("INSERT INTO etf_web_log (path,client_id,remote_addr,time) VALUES (?,?,?,?);")
	handlerErr(err)
}

func CheckRoot(r render.Render, params martini.Params) bool {
	tables, ok := tableMap[params["root"]]
	if !ok || len(tables) != TABLE_SIZE {
		r.Redirect("/html/404.html", 404)
		return true
	}
	return false
}

func getValueByType(root string, infotype string) (string, string, string) {
	tableName := "ETF_NAV"
	titleName := "CSOP China 5-Year Treasury Bond ETF"

	switch {
	case infotype == "0" || infotype == "china_bond":
		tableName = tableMap[root][0]
		titleName = "CSOP China 5-Year Treasury Bond ETF"
	case infotype == "1" || infotype == "china_A50_etf":
		tableName = tableMap[root][1]
		titleName = "CSOP FTSE China A50 ETF"
	case infotype == "2" || infotype == "china_A80_etf":
		tableName = tableMap[root][2]
		titleName = "CSOP CES China A80 ETF"
	default:
		infotype = "0"
	}

	return tableName, titleName, infotype
}

func WriteLog(w http.ResponseWriter, r *http.Request, log *log.Logger) {
	go func() {
		cookie, _ := r.Cookie("client_uuid")
		if cookie == nil {
			expiration := time.Now()
			expiration = expiration.AddDate(5, 0, 0)
			uuid, _ := utils.GenUUID()
			cookie = &http.Cookie{Name: "client_uuid", Value: uuid, Expires: expiration}
			http.SetCookie(w, cookie)
		}

		path := ""
		clientId := ""
		remoteAddr := ""
		if r != nil {
			if r.URL != nil {
				path = r.URL.Path
			}
			remoteAddr = r.RemoteAddr
		}

		if cookie != nil {
			clientId = cookie.Value
		}

		_, err := etfWebLogStmt.Exec(path, clientId, remoteAddr, time.Now().Format("2006-01-02 15:04:05"))
		checkErr(err, log)
	}()
}

func checkErr(err error, log *log.Logger) bool {
	if err != nil {
		log.Println(err)
		return true
	}
	return false
}

func handlerErr(err error) {
	if err != nil {
		panic(err)
	}
}
