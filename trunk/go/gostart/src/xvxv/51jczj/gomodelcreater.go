package main

import (
	"database/sql"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"os"
	"strings"
	"xvxv/51jczj/base"
)

var db *sql.DB
var err error

func main() {
	url := fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8", base.DbUsername, base.DbPassword, base.DbHost, base.DbPort, base.DbSchema)

	db, err = sql.Open("mysql", url)
	checkErr(err)

	rows, err := db.Query("select table_name from information_schema.tables where table_schema=?;", base.DbSchema)
	checkErr(err)

	for rows.Next() {
		var tableName string
		err = rows.Scan(&tableName)
		checkErr(err)

		createGoModel(strings.ToLower(tableName))
	}

}

func toGoName(name string) string {
	names := strings.Split(name, "_")

	newName := ""
	for _, ntemp := range names {
		newName += strings.ToUpper(ntemp[0:1]) + strings.ToLower(ntemp[1:len(ntemp)])
	}
	return newName
}

func createGoModel(tableName string) {
	structName := toGoName(tableName)

	rows, err := db.Query("show full columns from " + tableName + ";")
	checkErr(err)

	path := "models/"
	_, err = os.Stat(path)
	if err != nil && os.IsNotExist(err) {
		os.Mkdir(path, os.ModeDir)
	}
	goFile, _ := os.Create(path + tableName + ".go")
	defer goFile.Close()
	goFile.WriteString("/**\r\n")
	goFile.WriteString(" *  create by go-model-creater\r\n")
	goFile.WriteString(" **/\r\n")
	goFile.WriteString("package models\r\n")

	fieldNames := make([]string, 0)
	columnNames := make([]string, 0)
	setSqls := make([]string, 0)
	types := make([]string, 0)
	maxFieldNameSize := 0

	hasTime := false

	for rows.Next() {
		var fname, ftype, fcollation, fnull, fkey, fdefault, fextra, fprivileges, fcomment []byte
		err = rows.Scan(&fname, &ftype, &fcollation, &fnull, &fkey, &fdefault, &fextra, &fprivileges, &fcomment)
		checkErr(err)

		columnName := string(fname)
		fieldName := toGoName(columnName)

		fieldNames = append(fieldNames, fieldName)
		columnNames = append(columnNames, columnName)
		columnType := strings.ToLower(string(ftype))
		types = append(types, columnType)
		if maxFieldNameSize < len(fieldName) {
			maxFieldNameSize = len(fieldName)
		}

		if columnType == "datetime" || columnType == "date" {
			hasTime = true
		}

		setSqls = append(setSqls, columnName+"=#{"+fieldName+"}")
	}

	if hasTime {
		goFile.WriteString("\r\n")
		goFile.WriteString("import (\r\n")
		goFile.WriteString("	\"time\"\r\n")
		goFile.WriteString(")\r\n")
	}

	goFile.WriteString("\r\n")
	goFile.WriteString("type " + structName + " struct {\r\n")

	for k, fieldName := range fieldNames {
		columnType := types[k]
		sSize := maxFieldNameSize - len(fieldName)
		spaces := " "
		for i := 0; i < sSize; i++ {
			spaces += " "
		}

		switch {
		case strings.Index(columnType, "int(") == 0:
			columnType = "int64"
		case strings.Index(columnType, "tinyint(") == 0:
			columnType = "int16"
		case strings.Index(columnType, "varchar(") == 0:
			columnType = "string"
		case columnType == "datetime" || columnType == "date":
			columnType = "time.Time"
		}

		goFile.WriteString("	" + fieldName + spaces + columnType + "\r\n")
	}
	goFile.WriteString("}\r\n")

	xmlPath := "config/models/"
	_, err = os.Stat(xmlPath)
	if err != nil && os.IsNotExist(err) {
		os.Mkdir(xmlPath, os.ModeDir)
	}
	xmlFile, _ := os.Create(xmlPath + tableName + ".xml")
	defer xmlFile.Close()
	xmlFile.WriteString("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n")
	xmlFile.WriteString("<osm>\r\n")

	xmlFile.WriteString("	<select id=\"select" + structName + "ById\" result=\"struct\">\r\n")
	xmlFile.WriteString("SELECT " + strings.Join(columnNames, ",") + "\r\n")
	xmlFile.WriteString("FROM " + tableName + "\r\n")
	xmlFile.WriteString("WHERE id=#{Id};\r\n")
	xmlFile.WriteString("	</select>\r\n")

	xmlFile.WriteString("	<insert id=\"insert" + structName + "\">\r\n")
	xmlFile.WriteString("INSERT INTO " + tableName + "\r\n")
	xmlFile.WriteString("(" + strings.Join(columnNames, ",") + ")\r\n")
	xmlFile.WriteString("VALUES\r\n")
	xmlFile.WriteString("(#{" + strings.Join(fieldNames, "},#{") + "});\r\n")
	xmlFile.WriteString("	</insert>\r\n")

	xmlFile.WriteString("	<update id=\"update" + structName + "ById\">\r\n")
	xmlFile.WriteString("UPDATE " + tableName + "SET\r\n")
	xmlFile.WriteString(strings.Join(setSqls, ",") + "\r\n")
	xmlFile.WriteString("WHERE id=#{Id};\r\n")
	xmlFile.WriteString("	</update>\r\n")

	xmlFile.WriteString("	<delete id=\"delete" + structName + "ById\">\r\n")
	xmlFile.WriteString("DELETE FROM " + tableName + " WHERE id=#{Id};\r\n")
	xmlFile.WriteString("	</delete>\r\n")

	xmlFile.WriteString("</osm>\r\n")

}

func checkErr(err error) {
	if err != nil {
		panic(err)
	}
}
