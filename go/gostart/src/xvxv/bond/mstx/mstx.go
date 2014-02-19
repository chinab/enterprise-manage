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

var bufSize = 2000

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

	//查询行数数据
	stmt, err := db.Prepare("SELECT count(1) as num FROM market_stream WHERE type=? ")
	checkErr(err)
	totalCount := 0
	stmt.QueryRow(mkType).Scan(&totalCount)
	stmt.Close()
	fmt.Printf("type %s totalcount : %v \n", mkType, totalCount)

	var myFileds []string
	stmt, err = db.Prepare("show columns from " + tableName)
	checkErr(err)
	rows, err := stmt.Query()
	checkErr(err)
	for rows.Next() {
		var fieldCol, typeCol, nullCol, keyCol, defaultCol, extraCol string
		rows.Scan(&fieldCol, &typeCol, &nullCol, &keyCol, &defaultCol, &extraCol)
		fmt.Print(fieldCol, "\n")
		myFileds = append(myFileds, fieldCol)
	}
	fmt.Print("\n")
	stmt.Close()

	i := 0

	for i < totalCount {
		rows, _ := orm.SetTable("market_stream").Where("type=?", mkType).Select(fields).Limit(bufSize, i).FindMap()

		tx, err := db.Begin()
		checkErr(err)

		for _, row := range rows {
			bstr := string(row["body"])
			bodys := strings.Split(bstr, ",")

			add := make(map[string]interface{})

			for k, v := range row {
				add[k] = v
			}

			for _, v := range bodys {
				vs := strings.Split(v, ":")
				if len(vs) < 2 || vs[0] == "" || vs[1] == "" {
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

			var args []interface{}
			sql := "insert into " + tableName + " set "
			for key, value := range add {
				if stringInSlice(key, myFileds) {
					sql += key + "=?,"
					args = append(args, value)
				}
			}
			sql = strings.TrimRight(sql, ",")
			stmtInsert, err := tx.Prepare(sql)
			checkErr(err)
			_, err = stmtInsert.Exec(args...)
			checkErr(err)
			stmtInsert.Close()

			i++
		}
		fmt.Printf("%v rows of %s\n", i, mkType)
		tx.Commit()
	}

	fmt.Printf("%v rows end  of %s\n", i, mkType)
}

func checkErr(err error) {
	if err != nil {
		panic(err)
	}
}

func stringInSlice(a string, list []string) bool {
	for _, b := range list {
		if strings.ToLower(a) == strings.ToLower(b) {
			return true
		}
	}
	return false
}
