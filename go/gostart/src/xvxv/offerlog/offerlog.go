package main

import (
	"bufio"
	"bytes"
	"compress/gzip"
	"database/sql"
	"encoding/xml"
	"fmt"
	"github.com/astaxie/beedb"
	_ "github.com/go-sql-driver/mysql"
	"io/ioutil"
	"os"
	"time"
	"xvxv/bondoffer"
)

var db, err = sql.Open("mysql", "artogrid:artogrid@tcp(192.168.1.105:3306)/idb_center?charset=utf8")

func main() {
	checkErr(err)
	defer db.Close()

	version := 343284
	orm := beedb.New(db)

	for version >= 343284 {
		rows, _ := orm.SetTable("sync_message_data").Where("VERSION=? and COMPANY_ID=?", version, "1").FindMap()
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

			handleMessage(data)
		}
		version--
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

func handleMessage(body []byte) {
	bond := bondoffer.Bond{}
	err := xml.Unmarshal(body, &bond)
	checkErr(err)

	for _, offer := range bond.MethodUpdate {
		handleOffer(offer, bond, "methodUpdate")
	}

	for _, offer := range bond.MethodAdd {
		handleOffer(offer, bond, "methodAdd")
	}

	for _, offer := range bond.MethodRefer {
		handleOffer(offer, bond, "methodRefer")
	}

	for _, offer := range bond.MethodDeal {
		handleOffer(offer, bond, "methodDeal")
	}

}

func handleOffer(offer bondoffer.BondOffer, bond bondoffer.Bond, method string) {
	offer.Version = bond.Version
	offer.LastVersion = bond.LastVersion
	offer.Method = method

	offerMap := offer.ToMap()

	orm := beedb.New(db)
	_, err := orm.SetTable("bond_offer_log").Insert(offerMap)
	checkErr(err)
	fmt.Println(time.Now(), method, offer.CompanyName, offer.Version)
}

func checkErr(err error) {
	if err != nil {
		panic(err)
	}
}
