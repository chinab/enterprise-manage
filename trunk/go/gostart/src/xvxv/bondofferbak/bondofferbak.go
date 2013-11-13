package main

import (
	"database/sql"
	"encoding/xml"
	"fmt"
	"github.com/astaxie/beedb"
	_ "github.com/go-sql-driver/mysql"
	"time"
	"xvxv/bondoffer"
	"xvxv/stomputils"
)

var db *sql.DB

const mysqlIp = "192.168.1.138"

func main() {
	//stomputils.JmsHost = "192.168.1.132"
	stomputils.JmsDest = "/topic/IDC.BondOffer"
	stomputils.JmsPort = "61612"

	var err error
	db, err = sql.Open("mysql", "artogrid:artogrid@tcp("+mysqlIp+":3306)/idb_center?charset=utf8")
	stomputils.CheckErr(err)
	defer db.Close()

	stomputils.Handle(handleMessage)
}

func handleMessage(body []byte) {
	bond := bondoffer.Bond{}
	err := xml.Unmarshal(body, &bond)
	stomputils.CheckErr(err)

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
	stomputils.CheckErr(err)
	fmt.Println(time.Now(), method, offer.CompanyName, offer.GoodsCode)
}
