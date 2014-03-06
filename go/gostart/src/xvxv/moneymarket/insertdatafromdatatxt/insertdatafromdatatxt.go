/**
 * 把Data.txt中的数据导入到Financial_Company中
 * Tp
 **/
package main

import (
	"database/sql"
	//"fmt"
	_ "github.com/Go-SQL-Driver/MySQL"
	"io/ioutil"
	"strings"
	"xvxv/utils"
)

func main() {
	db, err := sql.Open("mysql", "root:root@tcp(localhost:3306)/idb_mm?charset=utf8")
	checkErr(err)

	defer db.Close()

	//插入数据
	insertTemp, err := db.Prepare(`INSERT mm_temp_financial_company 
SET ID=?,NAME=?,fullname_cn_front=?,shortname_en=?,fullname_en_front=?,CREATE_TIME=?,MODIFY_TIME=?,STATUS=?,PROCESS_STATUS=?,BROKER_COMPANY_ID=?`)
	checkErr(err)
	insertExtend, err := db.Prepare(`INSERT mm_extend_financial_company 
SET id=?,company_id=?,level=?,display_name=?,is_fee=?,description=?,is_internal=?,broker_company_id=?,create_time=?,status=?`)
	checkErr(err)

	dataByte, _ := ioutil.ReadFile("data.txt")
	fileContent := string(dataByte)

	lines := strings.Split(fileContent, "\r\n")

	for _, line := range lines {
		enCnDatas := strings.Split(line, "[")
		if len(enCnDatas) == 2 {
			cnData := strings.Trim(enCnDatas[1], " ")
			cnData = strings.Trim(cnData, "]")
			cnData = strings.Trim(cnData, " ")

			lastIndex := strings.LastIndex(enCnDatas[0], "-")

			var enData = ""
			var enShortData = ""
			if lastIndex != -1 {
				enData = strings.Trim(enCnDatas[0][0:lastIndex], " ")
				enShortData = strings.Trim(enCnDatas[0][lastIndex+1:], " ")
			} else {
				enData = strings.Trim(enCnDatas[0], " ")
			}

			id, err := utils.GenUUID()
			checkErr(err)

			_, err = insertTemp.Exec(id, cnData, cnData, enShortData, enData, "2014-01-01 00:00:00", "2014-01-01 00:00:00", "1", "2", "1")
			checkErr(err)

			_, err = insertExtend.Exec(id, id, 0, cnData, "2", line, "2", "1", "2014-01-01 00:00:00", "1")
			checkErr(err)
		}
	}

}

func checkErr(err error) {
	if err != nil {
		panic(err)
	}
}
