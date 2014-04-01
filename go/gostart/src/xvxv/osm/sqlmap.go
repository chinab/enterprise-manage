package osm

import (
	"database/sql"
	"encoding/xml"
	"fmt"
	"os"
	"strings"
)

const (
	select_mapper = 1
	update_mapper = 2
	insert_mapper = 3
	delete_mapper = 4
)

type sqlMapper struct {
	id     string
	stmt   sql.Stmt
	sql    string
	params []string
}

type stmtXml struct {
	Id  string `xml:"id,attr"`
	Sql string `xml:",chardata"`
}

type osmXml struct {
	Selects []stmtXml `xml:"select"`
	Deletes []stmtXml `xml:"delete"`
	Updates []stmtXml `xml:"update"`
	Inserts []stmtXml `xml:"insert"`
}

func TestReadMappers() {
	sqlMappers, _ := readMappers("test.xml")
	for _, sqlMapperObj := range sqlMappers {
		fmt.Println(sqlMapperObj.id)
		fmt.Println(sqlMapperObj.sql)
		fmt.Println(sqlMapperObj.params)
	}
}

func readMappers(path string) (sqlMappers []*sqlMapper, err error) {
	sqlMappers = make([]*sqlMapper, 0)
	err = nil

	xmlFile, err := os.Open(path)
	if err != nil {
		fmt.Println("Error opening file: ", err)
		return
	}
	defer xmlFile.Close()

	osmXmlObj := osmXml{}

	decoder := xml.NewDecoder(xmlFile)

	if err = decoder.Decode(&osmXmlObj); err != nil {
		fmt.Println("Error decode file: ", err)
		return
	}

	for _, deleteStmt := range osmXmlObj.Deletes {
		sqlMappers = append(sqlMappers, newMapper(deleteStmt))
	}
	for _, insertStmt := range osmXmlObj.Inserts {
		sqlMappers = append(sqlMappers, newMapper(insertStmt))
	}
	for _, selectStmt := range osmXmlObj.Selects {
		sqlMappers = append(sqlMappers, newMapper(selectStmt))
	}
	for _, updateStmt := range osmXmlObj.Updates {
		sqlMappers = append(sqlMappers, newMapper(updateStmt))
	}
	return
}

func newMapper(stmt stmtXml) (sqlMapperObj *sqlMapper) {
	sqlMapperObj = new(sqlMapper)
	sqlMapperObj.id = stmt.Id
	sqls := make([]string, 0)
	params := make([]string, 0)
	startFlag := 0

	sql := stmt.Sql

	errorIndex := 0

	for strings.Contains(sql, "#{") || strings.Contains(sql, "}") {
		si := strings.Index(sql, "#{")
		ei := strings.Index(sql, "}")
		if si != -1 && si < ei {
			sqls = append(sqls, sql[0:si])
			sql = sql[si+2:]
			startFlag++
			errorIndex += si
		} else if (ei != -1 && si != -1 && ei < si) || (ei != -1 && si == -1) {
			sqls = append(sqls, "?")
			params = append(params, sql[0:ei])
			sql = sql[ei+1:]
			startFlag--
			errorIndex += ei
		} else {
			fmt.Printf("sql read error '''%v''' %v\n", stmt.Sql, errorIndex)
			return
		}
	}

	if startFlag != 0 {
		fmt.Printf("sql read error '''%v''' %v\n", stmt.Sql, errorIndex)
		return
	}
	sqlMapperObj.sql = strings.Join(sqls, "")
	sqlMapperObj.params = params

	return
}
