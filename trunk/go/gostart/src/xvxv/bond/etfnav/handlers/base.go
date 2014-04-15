package handlers

import (
	"database/sql"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"github.com/sbinet/go-config/config"
	"log"
	"net/http"
	"time"
	"xvxv/utils"
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

func getValueByType(infotype string) (string, string, string) {
	tableName := "ETF_NAV"
	titleName := "CSOP China 5-Year Treasury Bond ETF"

	switch {
	case infotype == "0" || infotype == "china_bond":
		tableName = "ETF_NAV_NEW_NEW"
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
	cookie, _ := r.Cookie("client_uuid")
	if cookie == nil {
		expiration := time.Now()
		expiration = expiration.AddDate(5, 0, 0)
		uuid, _ := utils.GenUUID()
		cookie = &http.Cookie{Name: "client_uuid", Value: uuid, Expires: expiration}
		http.SetCookie(w, cookie)
	}

	stmt, err := db.Prepare("INSERT INTO etf_web_log (path,client_id,remote_addr,time) VALUES (?,?,?,?);")
	checkErr(err, log)

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

	_, err = stmt.Exec(path, clientId, remoteAddr, time.Now().Format("2006-01-02 15:04:05"))
	checkErr(err, log)
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
