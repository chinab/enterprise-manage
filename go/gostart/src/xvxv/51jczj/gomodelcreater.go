package main

import (
	"database/sql"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	//"time"
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
	goFile.WriteString(" *  create by tools,\r\n")
	goFile.WriteString(" *  go-model生成器\r\n")
	goFile.WriteString(" **/\r\n")
	goFile.WriteString("package models\r\n")
	goFile.WriteString("\r\n")

	goFile.WriteString("type " + structName + " struct {\r\n")

	fieldNames := make([]string, 0)
	maxFieldNameSize := 0

	// int(11)
	// varchar(255)
	// datetime
	// tinyint(1)

	for rows.Next() {
		var fname, ftype, fcollation, fnull, fkey, fdefault, fextra, fprivileges, fcomment []byte
		err = rows.Scan(&fname, &ftype, &fcollation, &fnull, &fkey, &fdefault, &fextra, &fprivileges, &fcomment)
		checkErr(err)

		fieldName := toGoName(string(fname))

		fmt.Println(tableName, string(fname), string(ftype))

		fieldNames = append(fieldNames, fieldName)
		if maxFieldNameSize < len(fieldName) {
			maxFieldNameSize = len(fieldName)
		}
	}

	for _, fieldName := range fieldNames {
		sSize := maxFieldNameSize - len(fieldName)
		spaces := " "
		for i := 0; i < sSize; i++ {
			spaces += " "
		}
		goFile.WriteString("	" + fieldName + spaces + "int\r\n")
	}

	goFile.WriteString("}\r\n")
	/**
	  	attrRows := make([][]string, len(rows)-3)
	  	for i := 3; i < len(rows); i++ {
	  		attrRows[i-3] = strings.Split(rows[i], " ")
	  	}

	  	packages := strings.Split(javaPackageName, ".")

	  	path := "models/as/"

	  	_, err := os.Stat(path)
	  	if err != nil && os.IsNotExist(err) {
	  		os.Mkdir(path, os.ModeDir)
	  	}

	  	for _, p := range packages {
	  		path = path + p + "/"
	  		_, err = os.Stat(path)
	  		if err != nil && os.IsNotExist(err) {
	  			os.Mkdir(path, os.ModeDir)
	  		}
	  	}

	  	cppFile, _ := os.Create(path + name + ".as")
	  	defer cppFile.Close()

	  //	cppFile.WriteString("/**\r\n")
	  //	cppFile.WriteString(" *  create by tools,\r\n")
	  //	cppFile.WriteString(" *  http://192.168.1.107/svn/guoliclient/moneymarket/docs/as-model生成器\r\n")
	  //	cppFile.WriteString(" **\r\n")
	  	cppFile.WriteString("package " + javaPackageName + " {\r\n")
	  	cppFile.WriteString("\r\n")
	  	cppFile.WriteString("	public class " + name + " {\r\n")
	  	cppFile.WriteString("\r\n")

	  	for _, attrRow := range attrRows {
	  		if attrRow[0] == "QDateTime" {
	  			cppFile.WriteString("		private var _" + attrRow[1] + ":Date=new Date();\r\n")
	  		} else if attrRow[0] == "double" {
	  			cppFile.WriteString("		private var _" + attrRow[1] + ":Number=0.0;\r\n")
	  		} else if attrRow[0] == "int" {
	  			cppFile.WriteString("		private var _" + attrRow[1] + ":int=0;\r\n")
	  		} else if attrRow[0] == "bool" {
	  			cppFile.WriteString("		private var _" + attrRow[1] + ":Boolean=false;\r\n")
	  		} else {
	  			cppFile.WriteString("		private var _" + attrRow[1] + ":String=\"\";\r\n")
	  		}
	  	}

	  	cppFile.WriteString("\r\n")

	  	for _, attrRow := range attrRows {
	  		if attrRow[0] == "QDateTime" {
	  			cppFile.WriteString("		public function get " + attrRow[1] + "():Date {\r\n")
	  		} else if attrRow[0] == "double" {
	  			cppFile.WriteString("		public function get " + attrRow[1] + "():Number {\r\n")
	  		} else if attrRow[0] == "int" {
	  			cppFile.WriteString("		public function get " + attrRow[1] + "():int {\r\n")
	  		} else if attrRow[0] == "bool" {
	  			cppFile.WriteString("		public function get " + attrRow[1] + "():Boolean {\r\n")
	  		} else {
	  			cppFile.WriteString("		public function get " + attrRow[1] + "():String {\r\n")
	  		}
	  		cppFile.WriteString("			return _" + attrRow[1] + ";\r\n")
	  		cppFile.WriteString("		}\r\n")
	  		cppFile.WriteString("\r\n")
	  	}

	  	for _, attrRow := range attrRows {
	  		if attrRow[0] == "QDateTime" {
	  			cppFile.WriteString("		public function set " + attrRow[1] + "(value:Date):void {\r\n")
	  		} else if attrRow[0] == "double" {
	  			cppFile.WriteString("		public function set " + attrRow[1] + "(value:Number):void {\r\n")
	  		} else if attrRow[0] == "int" {
	  			cppFile.WriteString("		public function set " + attrRow[1] + "(value:int):void {\r\n")
	  		} else if attrRow[0] == "bool" {
	  			cppFile.WriteString("		public function set " + attrRow[1] + "(value:Boolean):void {\r\n")
	  		} else {
	  			cppFile.WriteString("		public function set " + attrRow[1] + "(value:String):void {\r\n")
	  		}
	  		cppFile.WriteString("			_" + attrRow[1] + "=value;\r\n")
	  		cppFile.WriteString("		}\r\n")
	  		cppFile.WriteString("\r\n")
	  	}

	  	cppFile.WriteString("		public function fromObject(obj:Object):void {\r\n")
	  	cppFile.WriteString("			if (obj == null) {\r\n")
	  	cppFile.WriteString("				return;\r\n")
	  	cppFile.WriteString("			}\r\n")
	  	for _, attrRow := range attrRows {
	  		cppFile.WriteString("			if (obj.hasOwnProperty(\"" + attrRow[1] + "\")) {\r\n")
	  		if attrRow[0] == "QDateTime" {
	  			cppFile.WriteString("				_" + attrRow[1] + " = new Date(obj[\"" + attrRow[1] + "\"]);\r\n")
	  		} else {
	  			cppFile.WriteString("				_" + attrRow[1] + "=obj[\"" + attrRow[1] + "\"];\r\n")
	  		}
	  		cppFile.WriteString("			}\r\n")
	  	}
	  	cppFile.WriteString("		}\r\n")
	  	cppFile.WriteString("\r\n")

	  	cppFile.WriteString("		public function toObject():Object{\r\n")
	  	cppFile.WriteString("			var obj:Object = {};\r\n")
	  	for _, attrRow := range attrRows {
	  		if attrRow[0] == "QDateTime" {
	  			cppFile.WriteString("			if(_" + attrRow[1] + " != null ){\r\n")
	  			cppFile.WriteString("				obj[\"" + attrRow[1] + "\"]=_" + attrRow[1] + ".time;\r\n")
	  			cppFile.WriteString("			}\r\n")
	  		} else {
	  			cppFile.WriteString("			obj[\"" + attrRow[1] + "\"]=_" + attrRow[1] + ";\r\n")
	  		}
	  	}
	  	cppFile.WriteString("			obj[\"javaname\"] = \"" + javaPackageName + "." + name + "\";\r\n")

	  	cppFile.WriteString("			return obj;\r\n")
	  	cppFile.WriteString("		}\r\n")
	  	cppFile.WriteString("\r\n")

	  	cppFile.WriteString("	}\r\n")
	  	cppFile.WriteString("\r\n")
	  	cppFile.WriteString("}\r\n")

	  	cppFile.WriteString("\r\n")
	  	**/
}

func checkErr(err error) {
	if err != nil {
		panic(err)
	}
}
