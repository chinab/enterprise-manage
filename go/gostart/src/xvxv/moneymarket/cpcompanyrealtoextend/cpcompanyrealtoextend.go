/**
 * 把机构的真实数据 放入到extend表中，让测试环境中有更多的数据
 * TP
 **/
package main

import (
	"database/sql"
	//"fmt"
	_ "github.com/Go-SQL-Driver/MySQL"
	//"io/ioutil"
	//"strings"
	"xvxv/utils"
)

func main() {
	db, err := sql.Open("mysql", "artogrid:artogrid@tcp(192.168.1.105:3306)/idb_moneymarket?charset=utf8")
	checkErr(err)

	defer db.Close()

	//查询数据
	rows, err := db.Query(`select c.id,c.shortname_cn,c.fullname_cn_front from idb_center.idb_financial_company c where c.status='1' and c.id not in 
       (select company_id from idb_moneymarket.mm_extend_financial_company where broker_company_id='2' and status = '1')`)
	checkErr(err)

	tx, err := db.Begin()
	checkErr(err)

	//插入数据
	insertExtend, err := tx.Prepare(`INSERT mm_extend_financial_company 
SET id=?,company_id=?,level=?,display_name=?,is_fee=?,description=?,is_internal=?,broker_company_id=?,create_time=?,status=?`)
	checkErr(err)

	for rows.Next() {
		var companyId, shortName, fullName []byte
		err = rows.Scan(&companyId, &shortName, &fullName)
		checkErr(err)

		id, err := utils.GenUUID()
		checkErr(err)

		_, err = insertExtend.Exec(id, string(companyId), 0, string(shortName), "2", string(fullName), "2", "2", "2014-01-01 00:00:00", "1")
		checkErr(err)
	}

	tx.Commit()

}

func checkErr(err error) {
	if err != nil {
		panic(err)
	}
}
