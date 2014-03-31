/**
 * 为mm的机构添加默认交易员
 * TP
 **/
package main

import (
	"database/sql"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	//"io/ioutil"
	//"strings"
	"xvxv/utils"
)

func main() {
	brokerCompanyId := "1"

	db, err := sql.Open("mysql", "artogrid:artogrid@tcp(10.10.2.4:3306)/idb_moneymarket?charset=utf8")
	checkErr(err)

	defer db.Close()

	//查询数据
	rows, err := db.Query(fmt.Sprintf(`select company_id,display_name from mm_extend_financial_company c where c.company_id not in (
       select a.company_id from mm_temp_account a,mm_extend_trader t
       where a.id=t.account_id and a.status = '1' and a.broker_company_id='%s'
) and c.company_id not in (
       select a.company_id from idb_center.idb_account a,mm_extend_trader t
       where a.id=t.account_id and a.status = '1'
) and c.status='1' and c.broker_company_id='%s'`, brokerCompanyId, brokerCompanyId))
	checkErr(err)

	tx, err := db.Begin()
	checkErr(err)

	//插入数据
	insertTemp, err := tx.Prepare(`INSERT mm_temp_account
	SET ID=?,COMPANY_ID=?,COMPANY_NAME=?,ACCOUNT_CODE=?,USERNAME=?,PASSWORD=?,DISPLAY_NAME=?,
	ACCOUNT_TYPE=?,IS_FORBIDDEN=?,CREATE_TIME=?,MODIFY_TIME=?,STATUS=?,process_status=?,broker_company_id=?,pinyin_keyword=?`)
	checkErr(err)
	insertExtend, err := tx.Prepare(`INSERT mm_extend_trader
	SET id=?,account_id=?,display_name=?,broker_company_id=?,create_time=?,status=?`)
	checkErr(err)

	i := 500
	for rows.Next() {
		var companyId string
		var companyName string
		err = rows.Scan(&companyId, &companyName)
		checkErr(err)

		id, err := utils.GenUUID()
		checkErr(err)

		name := fmt.Sprintf("trader%v", i)
		ctime := "2014-02-20 11:00:00"

		_, err = insertTemp.Exec(id, companyId, companyName, name, name, "123456", name, "2", "1", ctime, ctime, "1", "2", brokerCompanyId, name)
		checkErr(err)

		_, err = insertExtend.Exec(id, id, name, brokerCompanyId, ctime, "1")
		checkErr(err)

		i++
	}

	tx.Commit()
}

func checkErr(err error) {
	if err != nil {
		panic(err)
	}
}
