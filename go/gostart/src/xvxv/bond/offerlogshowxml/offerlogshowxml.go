package main

import (
	"bytes"
	"compress/gzip"
	"database/sql"
	"fmt"
	"github.com/astaxie/beedb"
	_ "github.com/go-sql-driver/mysql"
	"github.com/lxn/walk"
	. "github.com/lxn/walk/declarative"
	"io/ioutil"
	"strconv"
)

var hostEdit, portEdit, usernameEdit, passwordEdit, databaseEdit, tableEdit, versionfieldEdit, valuefieldEdit, startversionEdit, endversionEdit *walk.LineEdit
var messageLabel *walk.Label
var openButton *walk.PushButton
var logTextEdit *walk.TextEdit

type MyMainWindow struct {
	*walk.MainWindow
}

func main() {
	mw := new(MyMainWindow)

	MainWindow{
		AssignTo: &mw.MainWindow,
		Title:    "Read XML",
		MinSize:  Size{800, 900},
		Layout:   VBox{},
		Children: []Widget{
			Composite{
				Layout: Grid{},
				Children: []Widget{
					Label{
						Row:    0,
						Column: 0,
						Text:   "DBHost:",
					},
					LineEdit{
						AssignTo: &hostEdit,
						Row:      0,
						Column:   1,
						Text:     "192.168.1.105",
					},
					Label{
						Row:    1,
						Column: 0,
						Text:   "DBPort:",
					},
					LineEdit{
						AssignTo: &portEdit,
						Row:      1,
						Column:   1,
						Text:     "3306",
					},
					Label{
						Row:    2,
						Column: 0,
						Text:   "DBUsername:",
					},
					LineEdit{
						AssignTo: &usernameEdit,
						Row:      2,
						Column:   1,
						Text:     "artogrid",
					},
					Label{
						Row:    3,
						Column: 0,
						Text:   "DBPassword:",
					},
					LineEdit{
						AssignTo: &passwordEdit,
						Row:      3,
						Column:   1,
						Text:     "artogrid",
					},
					Label{
						Row:    4,
						Column: 0,
						Text:   "Database:",
					},
					LineEdit{
						AssignTo: &databaseEdit,
						Row:      4,
						Column:   1,
						Text:     "idb_center",
					},
					Label{
						Row:    5,
						Column: 0,
						Text:   "Table:",
					},
					LineEdit{
						AssignTo: &tableEdit,
						Row:      5,
						Column:   1,
						Text:     "sync_message_data",
					},
					Label{
						Row:    6,
						Column: 0,
						Text:   "VersionField:",
					},
					LineEdit{
						AssignTo: &versionfieldEdit,
						Row:      6,
						Column:   1,
						Text:     "VERSION",
					},
					Label{
						Row:    7,
						Column: 0,
						Text:   "ValueField:",
					},
					LineEdit{
						AssignTo: &valuefieldEdit,
						Row:      7,
						Column:   1,
						Text:     "VALUE",
					},
					Label{
						Row:    8,
						Column: 0,
						Text:   "StartVersion:",
					},
					LineEdit{
						AssignTo: &startversionEdit,
						Row:      8,
						Column:   1,
						Text:     "1",
					},
					Label{
						Row:    9,
						Column: 0,
						Text:   "EndVersion:",
					},
					LineEdit{
						AssignTo: &endversionEdit,
						Row:      9,
						Column:   1,
						Text:     "2",
					},
				},
			},
			Label{
				AssignTo: &messageLabel,
				Text:     "",
			},
			Composite{
				Layout: HBox{},
				Children: []Widget{
					PushButton{
						AssignTo: &openButton,
						Text:     "ReadData",
						OnClicked: func() {
							go readData(hostEdit.Text(), portEdit.Text(), usernameEdit.Text(), passwordEdit.Text(), databaseEdit.Text(), tableEdit.Text(), versionfieldEdit.Text(), valuefieldEdit.Text(), startversionEdit.Text(), endversionEdit.Text())
						},
					},
					PushButton{
						Text: "Clear",
						OnClicked: func() {
							logTextEdit.SetText("")
						},
					},
				},
			},

			TextEdit{
				AssignTo: &logTextEdit,
				MinSize:  Size{100, 50},
				Text:     "",
				ReadOnly: true,
			},
		},
	}.Run()
}

func readData(host, port, username, password, database, table, versionfield, valuefield, startversion, endversion string) {
	messageLabel.SetText("")
	url := fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8", username, password, host, port, database)

	db, err := sql.Open("mysql", url)
	if checkErr(err, "连接数据库出错") {
		return
	}
	defer db.Close()

	version, err := strconv.Atoi(startversion)
	if checkErr(err, "StartVersion 必须数字，不能为空") {
		return
	}
	maxVersion, err := strconv.Atoi(endversion)
	if checkErr(err, "EndVersion 必须数字，不能为空") {
		return
	}

	orm := beedb.New(db)

	for version <= maxVersion {
		rows, err := orm.SetTable(table).Where(versionfield+"=?", version).FindMap()
		if checkErr(err, "Sql执行错误") {
			return
		}

		for _, row := range rows {
			inBuffer := bytes.NewBuffer(row[valuefield])
			greader, err := gzip.NewReader(inBuffer)
			if checkErr(err, "数据解压错误") {
				return
			}
			defer greader.Close()

			data, err := ioutil.ReadAll(greader)
			if checkErr(err, "数据读取错误") {
				return
			}

			logTextEdit.SetText(logTextEdit.Text() + string(data))
		}
		version++
	}

}

func checkErr(err error, msg string) bool {
	if err != nil {
		messageLabel.SetText(msg + "(" + err.Error() + ")")
		return true
	}
	return false
}
