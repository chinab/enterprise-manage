/**
 * 为mm的机构添加默认交易员
 * TP
 **/
package main

import (
	"database/sql"
	"fmt"
	_ "github.com/Go-SQL-Driver/MySQL"
	//"io/ioutil"
	//"strings"
	"xvxv/uuid"
)

func main() {
	db, err := sql.Open("mysql", "artogrid:artogrid@tcp(192.168.1.105:3306)/idb_moneymarket?charset=utf8")
	checkErr(err)

	defer db.Close()

	//插入数据
	insertTemp, err := db.Prepare(`INSERT mm_temp_account
	SET ID=?,COMPANY_ID=?,COMPANY_NAME=?,ACCOUNT_CODE=?,USERNAME=?,PASSWORD=?,DISPLAY_NAME=?,
	ACCOUNT_TYPE=?,IS_FORBIDDEN=?,CREATE_TIME=?,MODIFY_TIME=?,STATUS=?,process_status=?,broker_company_id=?,pinyin_keyword=?`)
	checkErr(err)
	insertExtend, err := db.Prepare(`INSERT mm_extend_trader
	SET id=?,account_id=?,display_name=?,broker_company_id=?,create_time=?,status=?`)
	checkErr(err)

	//查询数据
	rows, err := db.Query(`select company_id,display_name from mm_extend_financial_company c where c.company_id not in (
       select a.company_id from mm_temp_account a,mm_extend_trader t
       where a.id=t.account_id and a.status = '1' and a.broker_company_id='1'
) and c.company_id not in (
       select a.company_id from idb_center.idb_account a,mm_extend_trader t
       where a.id=t.account_id and a.status = '1'
) and c.status='1' and c.broker_company_id='1'`)
	checkErr(err)
	i := 0
	for rows.Next() {
		var companyId string
		var companyName string
		err = rows.Scan(&companyId, &companyName)
		checkErr(err)

		id, err := uuid.GenUUID()
		checkErr(err)

		name := fmt.Sprintf("trader%v", i)
		ctime := "2014-02-20 11:00:00"

		_, err = insertTemp.Exec(id, companyId, companyName, name, name, "123456", name, "2", "1", ctime, ctime, "1", "2", "1", name)
		checkErr(err)

		_, err = insertExtend.Exec(id, id, name, "1", ctime, "1")
		checkErr(err)

		i++
	}
}

func checkErr(err error) {
	if err != nil {
		panic(err)
	}
}
