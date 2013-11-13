package main

import (
	"bufio"
	"database/sql"
	"fmt"
	"github.com/astaxie/beedb"
	_ "github.com/go-sql-driver/mysql"
	"os"
	"strings"
)

var fields = "id, locale_id, rev, sent, company_id, company_name, dept, type, operate, body, goods_id, goods_code, description, status, create_time, modify_time"

func main() {
	db, err := sql.Open("mysql", "root:123456@tcp(192.168.1.220:3306)/idb_center1030?charset=utf8")
	//	db, err := sql.Open("mysql", "artogrid:artogrid@tcp(localhost:3306)/idb_center?charset=utf8")
	//	db, err := sql.Open("mysql", "artogrid:artogrid@tcp(192.168.1.128:3306)/idb_center?charset=utf8")
	checkErr(err)
	defer db.Close()

	go insertData(db, "1")
	go insertData(db, "2")
	go insertData(db, "4")
	go insertData(db, "5")

	running := true
	reader := bufio.NewReader(os.Stdin)
	for running {
		data, _, _ := reader.ReadLine()
		command := string(data)
		if command == "exit" || command == "quit" {
			running = false
		}
	}

}

func insertData(db *sql.DB, mkType string) {
	orm := beedb.New(db)
	//	beedb.OnDebug=true
	tableName := "market_stream" + mkType
	fmt.Printf("start type%v:\n", mkType)
	orm.SetTable(tableName).DeleteRow()

	rows, _ := orm.SetTable("market_stream").Where("type=?", mkType).Select(fields).FindMap()

	i := 0
	for _, row := range rows {
		bstr := string(row["body"])
		bodys := strings.Split(bstr, ",")

		add := make(map[string]interface{})

		for k, v := range row {
			add[k] = v
		}

		for _, v := range bodys {
			vs := strings.Split(v, ":")
			if len(vs) == 0 || vs[0] == "" {
				continue
			}

			if vs[0] == "id" {
				vs[0] = "bodyId"
			}
			if len(vs) == 1 || vs[1] == "" {
				add[vs[0]] = []byte("")
			} else if len(vs) == 2 {
				add[vs[0]] = []byte(vs[1])
			}
		}

		add["body"] = ""
		orm.SetTable(tableName).Insert(add)

		i++
		if i%100 == 0 {
			fmt.Printf("%v rows of %s\n", i, mkType)
		}
	}

	fmt.Printf("%v rows end  of %s\n", i, mkType)
}

func checkErr(err error) {
	if err != nil {
		panic(err)
	}
}
