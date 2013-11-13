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

const fields = "id, locale_id, rev, sent, company_id, company_name, dept, type, operate, body, goods_id, goods_code, description, status, create_time, modify_time"

func main() {
	running := true
	reader := bufio.NewReader(os.Stdin)
	for running {
		data, _, _ := reader.ReadLine()
		command := string(data)
		if command == "start" {
			running = false
		}
	}
	insertDataAll()
	running = true
	for running {
		data, _, _ := reader.ReadLine()
		command := string(data)
		if command == "exit" || command == "quit" {
			running = false
		}
	}
}

func insertDataAll() {
	//	db, err := sql.Open("mysql", "root:123456@tcp(192.168.1.220:3306)/idb_center1?charset=utf8")
	//	db, err := sql.Open("mysql", "artogrid:artogrid@tcp(localhost:3306)/idb_center?charset=utf8")
	for i := 0; i < 10; i++ {
		db, err := sql.Open("mysql", "artogrid:artogrid@tcp(192.168.1.128:3306)/idb_center?charset=utf8")
		checkErr(err)
		insertData(db, "1")
		insertData(db, "2")
		insertData(db, "4")
		insertData(db, "5")
		db.Close()
	}
}

func insertData(db *sql.DB, mkType string) {
	//	beedb.OnDebug=true
	orm := beedb.New(db)
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
		//		orm.SetTable(tableName).Insert(add)

		i++
		if i%100 == 0 {
			fmt.Printf("%v rows\n", i)
		}
	}

	fmt.Printf("%v rows end\n", i)
}

func checkErr(err error) {
	if err != nil {
		panic(err)
	}
}
