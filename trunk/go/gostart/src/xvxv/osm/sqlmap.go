package osm

import (
	// "database/sql"
	"encoding/xml"
	"fmt"
	"os"
	"strings"
)

const (
	type_select = 1
	type_update = iota
	type_insert = iota
	type_delete = iota

	result_value   = "value"   //查出的结果为单行,并存入不定长的变量上(...)
	result_struct  = "struct"  //查出的结果为单行,并存入struct
	result_structs = "structs" //查出的结果为多行,并存入struct array
	result_map     = "map"     //查出的结果为单行,并存入map
	result_maps    = "maps"    //查出的结果为多行,并存入map array
	result_array   = "array"   //查出的结果为单行,并存入array
	result_arrays  = "arrays"  //查出的结果为多行,并存入array array
	result_kvs     = "kvs"     //查出的结果为多行,每行有两个字段,前者为key,后者为value,存入map
)

type sqlMapper struct {
	id         string
	sql        string
	paramNames []string
	sqlType    int
	// param      string
	result string
}

type stmtXml struct {
	Id string `xml:"id,attr"`
	// Param  string `xml:"param,attr"`
	Result string `xml:"result,attr"`
	Sql    string `xml:",chardata"`
}

type osmXml struct {
	Selects []stmtXml `xml:"select"`
	Deletes []stmtXml `xml:"delete"`
	Updates []stmtXml `xml:"update"`
	Inserts []stmtXml `xml:"insert"`
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
		sqlMappers = append(sqlMappers, newMapper(deleteStmt, type_delete))
	}
	for _, insertStmt := range osmXmlObj.Inserts {
		sqlMappers = append(sqlMappers, newMapper(insertStmt, type_insert))
	}
	for _, selectStmt := range osmXmlObj.Selects {
		sqlMappers = append(sqlMappers, newMapper(selectStmt, type_select))
	}
	for _, updateStmt := range osmXmlObj.Updates {
		sqlMappers = append(sqlMappers, newMapper(updateStmt, type_update))
	}
	return
}

func newMapper(stmt stmtXml, sqlType int) (sqlMapperObj *sqlMapper) {
	sqlMapperObj = new(sqlMapper)
	sqlMapperObj.id = stmt.Id
	sqlMapperObj.sqlType = sqlType
	// sqlMapperObj.param = stmt.Param
	sqlMapperObj.result = stmt.Result

	sqls := make([]string, 0)
	paramNames := make([]string, 0)
	startFlag := 0

	sql := strings.Trim(stmt.Sql, "\t\n ")
	sql = strings.Replace(sql, "\n", " ", -1)
	sql = strings.Replace(sql, "\t", " ", -1)
	for strings.Contains(sql, "  ") {
		sql = strings.Replace(sql, "  ", " ", -1)
	}

	errorIndex := 0

	for strings.Contains(sql, "#{") || strings.Contains(sql, "}") {
		si := strings.Index(sql, "#{")
		ei := strings.Index(sql, "}")
		if si != -1 && si < ei {
			sqls = append(sqls, sql[0:si])
			sql = sql[si+2:]
			startFlag++
			errorIndex += si + 2
		} else if (ei != -1 && si != -1 && ei < si) || (ei != -1 && si == -1) {
			sqls = append(sqls, "?")
			paramNames = append(paramNames, sql[0:ei])
			sql = sql[ei+1:]
			startFlag--
			errorIndex += ei + 1
		} else {
			if ei > -1 {
				errorIndex += ei
			} else {
				errorIndex += si
			}
			fmt.Printf("sql read error \"%v\"\n", markSqlError(stmt.Sql, errorIndex))
			return
		}

	}
	sqls = append(sqls, sql)

	if startFlag != 0 {
		fmt.Printf("sql read error \"%v\"\n", markSqlError(stmt.Sql, errorIndex))
		return
	}
	sqlMapperObj.sql = strings.Join(sqls, "")
	sqlMapperObj.paramNames = paramNames

	return
}

func markSqlError(sql string, index int) string {
	result := fmt.Sprintf("%s[****ERROR****]->%s", sql[0:index], sql[index:])
	return result
}
