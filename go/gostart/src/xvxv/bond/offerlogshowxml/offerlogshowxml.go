package main

import (
	"bufio"
	"bytes"
	"compress/gzip"
	"database/sql"
	"fmt"
	"github.com/astaxie/beedb"
	_ "github.com/go-sql-driver/mysql"
	"io/ioutil"
	"os"
)

var db, err = sql.Open("mysql", "artogrid:artogrid@tcp(192.168.1.105:3306)/idb_center?charset=utf8")

func main() {
	checkErr(err)
	defer db.Close()

	userFile := "F:\\message_test.xml"
	fout, err := os.Create(userFile)
	defer fout.Close()

	if err != nil {
		fmt.Println(userFile, err)
		return
	}

	version := 348809
	orm := beedb.New(db)

	for version <= 348823 {
		rows, _ := orm.SetTable("sync_message_data").Where("VERSION=?", version).FindMap()
		for _, row := range rows {
			inBuffer := bytes.NewBuffer(row["VALUE"])
			greader, err := gzip.NewReader(inBuffer)
			if err != nil {
				checkErr(err)
			}
			defer greader.Close()

			data, err := ioutil.ReadAll(greader)
			if err != nil {
				checkErr(err)
			}

			fout.Write(data)
			fmt.Println(string(row["VERSION"]))
		}
		version++
	}

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

func checkErr(err error) {
	if err != nil {
		panic(err)
	}
}
