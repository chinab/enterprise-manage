package main

import (
	"bufio"
	"fmt"
	"io/ioutil"
	"os"
	"strings"
)

func main() {
	fs, _ := ioutil.ReadDir("models")
	for _, fileInfo := range fs {
		fileName := fileInfo.Name()
		if strings.LastIndex(fileName, ".model") == (len([]rune(fileName)) - 6) {
			file, _ := os.Open("models/" + fileName)
			r := bufio.NewReader(file)
			model, _ := r.ReadString(0)
			file.Close()
			createAsModel(model)
			createCppModel(model)
		}
	}
}

func getSetterName(name string) string {
	fmt.Print("")
	return "set" + strings.ToUpper(string(name[0])) + string(name[1:])
}

func createAsModel(model string) {
	rows := strings.Split(model, "\r\n")
	//packageName := rows[0]
	javaPackageName := rows[1]
	name := rows[2]

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

	cppFile.WriteString("/**\r\n")
	cppFile.WriteString(" *  create by tools,\r\n")
	cppFile.WriteString(" *  http://192.168.1.107/svn/guoliclient/moneymarket/docs/as-model生成器\r\n")
	cppFile.WriteString(" **/\r\n")
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
}

func createCppModel(model string) {
	rows := strings.Split(model, "\r\n")
	packageName := rows[0]
	javaPackageName := rows[1]
	name := rows[2]

	attrRows := make([][]string, len(rows)-3)
	for i := 3; i < len(rows); i++ {
		attrRows[i-3] = strings.Split(rows[i], " ")
	}

	_, err := os.Stat("models/cpp/")
	if err != nil && os.IsNotExist(err) {
		os.Mkdir("models/cpp/", os.ModeDir)
	}

	_, err = os.Stat("models/cpp/" + packageName + "")
	if err != nil && os.IsNotExist(err) {
		os.Mkdir("models/cpp/"+packageName, os.ModeDir)
	}

	fileName := strings.ToLower(name)
	upperFileName := strings.ToUpper(name)
	hFile, _ := os.Create("models/cpp/" + packageName + "/" + fileName + ".h")
	defer hFile.Close()

	hFile.WriteString("/**\r\n")
	hFile.WriteString(" *  create by tools,\r\n")
	hFile.WriteString(" *  http://192.168.1.107/svn/guoliclient/moneymarket/docs/cpp-model生成器\r\n")
	hFile.WriteString(" **/\r\n")
	hFile.WriteString("#ifndef " + upperFileName + "_H\r\n#define " + upperFileName + "_H\r\n\r\n#include <QString>\r\n#include <QDateTime>\r\n#include <QVariantMap>\r\n")
	hFile.WriteString("#include \"../model_global.h\"\r\n\r\n")
	hFile.WriteString("class MODELSHARED_EXPORT " + name + "\r\n{\r\npublic:\r\n")
	hFile.WriteString("    " + name + "();\r\n\r\n")

	for _, attrRow := range attrRows {
		hFile.WriteString("    " + attrRow[0] + " " + attrRow[1] + "() const;\r\n")
	}
	hFile.WriteString("\r\n")
	for _, attrRow := range attrRows {
		hFile.WriteString("    void " + getSetterName(attrRow[1]) + " (const " + attrRow[0] + " &);\r\n")
	}
	hFile.WriteString("\r\n")
	hFile.WriteString("    void fromMap(const QVariantMap &map);\r\n")
	hFile.WriteString("    void toMap(QVariantMap &map);\r\n")
	hFile.WriteString("\r\n")
	hFile.WriteString("    bool operator==(const " + name + " &other) const;\r\n")
	hFile.WriteString("    bool operator!=(const " + name + " &other) const;\r\n")
	hFile.WriteString("\r\n")
	hFile.WriteString("private:\r\n")
	for _, attrRow := range attrRows {
		hFile.WriteString("    " + attrRow[0] + " _" + attrRow[1] + ";\r\n")
	}
	hFile.WriteString("};\r\n")
	hFile.WriteString("\r\n")
	hFile.WriteString("#endif // " + upperFileName + "_H\r\n")

	cppFile, _ := os.Create("models/cpp/" + packageName + "/" + fileName + ".cpp")
	defer cppFile.Close()

	cppFile.WriteString("/**\r\n")
	cppFile.WriteString(" *  create by tools,\r\n")
	cppFile.WriteString(" *  http://192.168.1.107/svn/guoliclient/moneymarket/docs/cpp-model生成器\r\n")
	cppFile.WriteString(" **/\r\n")
	cppFile.WriteString("#include \"" + fileName + ".h\"\r\n")
	cppFile.WriteString("#include \"../model.h\"\r\n")
	cppFile.WriteString("\r\n")
	cppFile.WriteString(name + "::" + name + "()\r\n")
	cppFile.WriteString("{\r\n")
	for _, attrRow := range attrRows {
		if attrRow[0] == "QDateTime" {
			cppFile.WriteString("    _" + attrRow[1] + " = Model::instance().currentDateTime();\r\n")
		} else if attrRow[0] == "double" {
			cppFile.WriteString("    _" + attrRow[1] + " = 0.0;\r\n")
		} else if attrRow[0] == "int" {
			cppFile.WriteString("    _" + attrRow[1] + " = 0;\r\n")
		} else if attrRow[0] == "bool" {
			cppFile.WriteString("    _" + attrRow[1] + " = false;\r\n")
		} else {
			cppFile.WriteString("    _" + attrRow[1] + " = \"\";\r\n")
		}
	}
	cppFile.WriteString("}\r\n")
	cppFile.WriteString("\r\n")

	for _, attrRow := range attrRows {
		cppFile.WriteString(attrRow[0] + " " + name + "::" + attrRow[1] + "() const{\r\n")
		cppFile.WriteString("    return _" + attrRow[1] + ";\r\n")
		cppFile.WriteString("}\r\n")
		cppFile.WriteString("\r\n")
	}

	for _, attrRow := range attrRows {
		cppFile.WriteString("void " + name + "::" + getSetterName(attrRow[1]) + "(const " + attrRow[0] + " &" + attrRow[1] + "){\r\n")
		cppFile.WriteString("    _" + attrRow[1] + " = " + attrRow[1] + ";\r\n")
		cppFile.WriteString("}\r\n")
		cppFile.WriteString("\r\n")
	}

	cppFile.WriteString("void " + name + "::fromMap(const QVariantMap &map){\r\n")
	for _, attrRow := range attrRows {
		if attrRow[0] == "QDateTime" {
			cppFile.WriteString("    _" + attrRow[1] + " = QDateTime::fromMSecsSinceEpoch(map[\"" + attrRow[1] + "\"].toULongLong());\r\n")
		} else if attrRow[0] == "double" {
			cppFile.WriteString("    _" + attrRow[1] + " = map[\"" + attrRow[1] + "\"].toDouble();\r\n")
		} else if attrRow[0] == "int" {
			cppFile.WriteString("    _" + attrRow[1] + " = map[\"" + attrRow[1] + "\"].toInt();\r\n")
		} else if attrRow[0] == "bool" {
			cppFile.WriteString("    _" + attrRow[1] + " = map[\"" + attrRow[1] + "\"].toBool();\r\n")
		} else {
			cppFile.WriteString("    _" + attrRow[1] + " = map[\"" + attrRow[1] + "\"].toString();\r\n")
		}
	}
	cppFile.WriteString("}\r\n")
	cppFile.WriteString("\r\n")

	cppFile.WriteString("void " + name + "::toMap(QVariantMap &map){\r\n")
	for _, attrRow := range attrRows {
		if attrRow[0] == "QDateTime" {
			cppFile.WriteString("    map[\"" + attrRow[1] + "\"] = _" + attrRow[1] + ".toMSecsSinceEpoch();\r\n")
		} else {
			cppFile.WriteString("    map[\"" + attrRow[1] + "\"] = _" + attrRow[1] + ";\r\n")
		}
	}
	cppFile.WriteString("    map[\"javaname\"] = \"" + javaPackageName + "." + name + "\";\r\n")

	cppFile.WriteString("}\r\n")
	cppFile.WriteString("\r\n")

	cppFile.WriteString("bool " + name + "::operator==(const " + name + " &other) const{\r\n")
	for _, attrRow := range attrRows {
		cppFile.WriteString("    if(_" + attrRow[1] + "!=other._" + attrRow[1] + "){\r\n")
		cppFile.WriteString("        return false;\r\n")
		cppFile.WriteString("    }\r\n")
	}
	cppFile.WriteString("    return true;\r\n")
	cppFile.WriteString("}\r\n")

	cppFile.WriteString("bool " + name + "::operator!=(const " + name + " &other) const{\r\n")
	for _, attrRow := range attrRows {
		cppFile.WriteString("    if(_" + attrRow[1] + "!=other._" + attrRow[1] + "){\r\n")
		cppFile.WriteString("        return true;\r\n")
		cppFile.WriteString("    }\r\n")
	}
	cppFile.WriteString("    return false;\r\n")
	cppFile.WriteString("}\r\n")

	cppFile.WriteString("\r\n")
}
