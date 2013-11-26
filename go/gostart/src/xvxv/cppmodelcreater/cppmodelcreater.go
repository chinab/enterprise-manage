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
			createModel(model)
			file.Close()
		}
	}
}

func getSetterName(name string) string {
	fmt.Print("")
	return "set" + strings.ToUpper(string(name[0])) + string(name[1:])
}

func createModel(model string) {
	rows := strings.Split(model, "\r\n")
	packageName := rows[0]
	name := rows[1]

	attrRows := make([][]string, len(rows)-2)
	for i := 2; i < len(rows); i++ {
		attrRows[i-2] = strings.Split(rows[i], " ")
	}

	_, err := os.Stat("models/" + packageName + "")
	if err != nil && os.IsNotExist(err) {
		os.Mkdir("models/"+packageName, os.ModeDir)
	}

	fileName := strings.ToLower(name)
	upperFileName := strings.ToUpper(name)
	hFile, _ := os.Create("models/" + packageName + "/" + fileName + ".h")
	defer hFile.Close()

	hFile.WriteString("#ifndef " + upperFileName + "_H\r\n#define " + upperFileName + "_H\r\n\r\n#include <QString>\r\n#include <QDateTime>\r\n#include <QVariantMap>\r\n")
	hFile.WriteString("#include \"../model_global.h\"\r\n\r\n")
	hFile.WriteString("class MODELSHARED_EXPORT " + name + "\r\n{\r\npublic:\r\n")
	hFile.WriteString("    " + name + "();\r\n\r\n")

	for _, attrRow := range attrRows {
		hFile.WriteString("    " + attrRow[0] + " " + attrRow[1] + "();\r\n")
	}
	hFile.WriteString("\r\n")
	for _, attrRow := range attrRows {
		hFile.WriteString("    void " + getSetterName(attrRow[1]) + " (const " + attrRow[0] + ");\r\n")
	}
	hFile.WriteString("\r\n")
	hFile.WriteString("    void fromMap(const QVariantMap &map);\r\n")
	hFile.WriteString("    void toMap(QVariantMap &map);\r\n")
	hFile.WriteString("\r\n")
	hFile.WriteString("private:\r\n")
	for _, attrRow := range attrRows {
		hFile.WriteString("    " + attrRow[0] + " _" + attrRow[1] + ";\r\n")
	}
	hFile.WriteString("};\r\n")
	hFile.WriteString("\r\n")
	hFile.WriteString("#endif // " + upperFileName + "_H\r\n")

	cppFile, _ := os.Create("models/" + packageName + "/" + fileName + ".cpp")
	defer cppFile.Close()

	cppFile.WriteString("#include \"" + fileName + ".h\"\r\n")
	cppFile.WriteString("\r\n")
	cppFile.WriteString(name + "::" + name + "()\r\n")
	cppFile.WriteString("{\r\n")
	cppFile.WriteString("}\r\n")
	cppFile.WriteString("\r\n")

	for _, attrRow := range attrRows {
		cppFile.WriteString(attrRow[0] + " " + name + "::" + attrRow[1] + "(){\r\n")
		cppFile.WriteString("    return _" + attrRow[1] + ";\r\n")
		cppFile.WriteString("}\r\n")
		cppFile.WriteString("\r\n")
	}

	for _, attrRow := range attrRows {
		cppFile.WriteString("void " + name + "::" + getSetterName(attrRow[1]) + "(" + attrRow[0] + " " + attrRow[1] + "){\r\n")
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
		cppFile.WriteString("    map[\"" + attrRow[1] + "\"] = _" + attrRow[1] + ";\r\n")
	}
	cppFile.WriteString("}\r\n")

	cppFile.WriteString("\r\n")
}
