package main

import (
	"github.com/lxn/walk"
	. "github.com/lxn/walk/declarative"
	//"strings"
	"xvxv/patrtest/stomputils"
)

type MyMainWindow struct {
	*walk.MainWindow
}

func main() {
	mw := new(MyMainWindow)
	var openButton *walk.PushButton
	var hostEdit, destEdit, portEdit *walk.LineEdit
	var logTextEdit *walk.TextEdit
	closeMessage := make(chan int)

	MainWindow{
		AssignTo: &mw.MainWindow,
		Title:    "SCREAMO",
		MinSize:  Size{600, 400},
		Layout:   VBox{},
		Children: []Widget{
			Composite{
				Layout: Grid{},
				Children: []Widget{
					Label{
						Row:    0,
						Column: 0,
						Text:   "JmsHost:",
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
						Text:   "JmsDest:",
					},
					LineEdit{
						AssignTo: &destEdit,
						Row:      1,
						Column:   1,
						Text:     "/topic/IDC.QuoteExchange.bond2app",
					},
					Label{
						Row:    2,
						Column: 0,
						Text:   "JmsPort:",
					},
					LineEdit{
						AssignTo: &portEdit,
						Row:      2,
						Column:   1,
						Text:     "61612",
					},
				},
			},

			Composite{
				Layout: HBox{},
				Children: []Widget{
					PushButton{
						AssignTo: &openButton,
						Text:     "Open",
						OnClicked: func() {
							if openButton.Text() == "Open" {
								go openstomp(hostEdit.Text(), destEdit.Text(), portEdit.Text(), logTextEdit, closeMessage)
								openButton.SetText("Close")
							} else {
								go stopstomp(closeMessage)
								openButton.SetText("Open")
							}
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

func stopstomp(closeMessage chan int) {
	closeMessage <- 1
}

func openstomp(host, dest, port string, logTextEdit *walk.TextEdit, closeMessage chan int) {
	stomputils.JmsHost = host
	stomputils.JmsDest = dest
	stomputils.JmsPort = port

	handleMessage := func(body []byte) {
		//fmt.Println(string(body))
		logTextEdit.SetText(logTextEdit.Text() + string(body))
	}

	stomputils.Handle(handleMessage, closeMessage)
}
