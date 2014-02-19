package main

import (
	"database/sql"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"strconv"
	"strings"
	"time"
)

func main() {
	db, err := sql.Open("mysql", "artogrid:artogrid@tcp(192.168.1.105:3306)/idb_center?charset=utf8")
	checkErr(err)

	tx, err := db.Begin()
	checkErr(err)

	//查询数据
	rows, err := tx.Query("select ID,GOODS_TERM,OPTION_TYPE from bond_goods") //where STATUS='1' limit 10
	checkErr(err)
	now := time.Now()
	tyy, tmm, tdd := now.Year(), now.Month(), now.Day()
	today := time.Date(tyy, tmm, tdd, 0, 0, 0, 0, time.Local)
	todayLastYear := time.Date(tyy-1, tmm, tdd, 0, 0, 0, 0, time.Local)
	subDay := today.Sub(todayLastYear).Hours() / 24

	datas := []map[string]string{}
	for rows.Next() {
		var idNull sql.NullString
		var goodsTermNull sql.NullString
		var optionTypeNull sql.NullString
		err = rows.Scan(&idNull, &goodsTermNull, &optionTypeNull)
		checkErr(err)

		id := getNullString(idNull)
		goodsTerm := getNullString(goodsTermNull)
		optionType := getNullString(optionTypeNull)
		optionType = strings.Trim(optionType, " ")

		datas = append(datas, map[string]string{"id": id, "goodsTerm": goodsTerm, "optionType": optionType})
	}
	err = rows.Close()
	checkErr(err)

	//更新数据
	stmt, err := tx.Prepare("update bond_goods set REMAIN_YEAR=?,OPTION_TYPE=? where ID=?")
	checkErr(err)

	start := time.Now()
	for _, v := range datas {
		id := v["id"]
		goodsTerm := v["goodsTerm"]
		optionType := v["optionType"]

		if strings.Trim(goodsTerm, " ") == "" {
			continue
		}

		ymd := strings.Split(goodsTerm, ".")

		yy, err := strconv.Atoi(ymd[0])
		checkErr(err)

		mm, err := strconv.Atoi(ymd[1])
		checkErr(err)

		dd, err := strconv.Atoi(ymd[2])
		checkErr(err)

		term := time.Date(yy, time.Month(mm), dd, 0, 0, 0, 0, time.Local)

		if optionType != "" {
			optionTypes := strings.Split(optionType, "+")
			if len(optionTypes) == 2 {
				leftYear, err := strconv.Atoi(optionTypes[1])
				checkErr(err)
				termNew := time.Date(yy-leftYear, time.Month(mm), dd, 0, 0, 0, 0, time.Local)
				if term.After(today) {
					term = termNew
				}
			}
		}

		suby := term.Year() - tyy
		term = time.Date(tyy, time.Month(mm), dd, 0, 0, 0, 0, time.Local)

		if term.Before(today) {
			term = time.Date(tyy+1, time.Month(mm), dd, 0, 0, 0, 0, time.Local)
			suby--
		}

		remainYear := float64(suby) + term.Sub(today).Hours()/24/subDay

		_, err = stmt.Exec(remainYear, optionType, id)
		checkErr(err)
	}

	tx.Commit()
	fmt.Println("耗时", time.Now().Sub(start).Seconds(), "s")
}

func getNullString(ns sql.NullString) string {
	if ns.Valid {
		return ns.String
	}
	return ""
}

func checkErr(err error) {
	if err != nil {
		panic(err)
	}
}
