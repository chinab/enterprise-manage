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
var BaseUrlMap map[string]string

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

	baseUrlMapping, err := setting.Options("baseurlmapping")
	handlerErr(err)

	BaseUrlMap = make(map[string]string)
	for _, option := range baseUrlMapping {
		baseUrl, err := setting.String("baseurlmapping", option)
		handlerErr(err)

		BaseUrlMap[option] = baseUrl
	}

	url := fmt.Sprintf("%v:%v@tcp(%v:%v)/nav_etf?charset=utf8", username, password, host, port)

	db, err = sql.Open("mysql", url)
	handlerErr(err)

	etfWebLogStmt, err = db.Prepare("INSERT INTO etf_web_log (path,client_id,remote_addr,time) VALUES (?,?,?,?);")
	handlerErr(err)
}

func CheckRoot(r render.Render, params martini.Params, w http.ResponseWriter, req *http.Request, log *log.Logger) bool {
	_, ok := BaseUrlMap[params["root"]]
	if !ok {
		r.Redirect("/html/404.html", 404)
		return true
	}
	WriteLog(w, req, log)
	return false
}

func getValueByType(root string, infotype string) (string, string, string) {
	tableName := "ETF_NAV"
	titleName := "CSOP China 5-Year Treasury Bond ETF"

	switch {
	case infotype == "0" || infotype == "china_bond":
		tableName = "bond_etf"
		titleName = "CSOP China 5-Year Treasury Bond ETF"
	case infotype == "1" || infotype == "china_A50_etf":
		tableName = "csop_a50"
		titleName = "CSOP FTSE China A50 ETF"
	case infotype == "2" || infotype == "china_A80_etf":
		tableName = "csop_a80"
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
			ra := r.RemoteAddr

			remoteAddr = r.Header.Get("X-Forwarded-For")
			if len(remoteAddr) == 0 {
				remoteAddr = strings.Split(ra, ":")[0]
			}
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
