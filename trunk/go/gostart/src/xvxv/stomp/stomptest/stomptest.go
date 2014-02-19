package main

import (
	"fmt"
	"xvxv/stomputils"
)

func main() {
	stomputils.JmsHost = "192.168.1.105"
	stomputils.JmsDest = "/queue/listenerBond.IDBCenter"
	stomputils.JmsPort = "61612"

	stomputils.Handle(handleMessage)
}

func handleMessage(body []byte) {
	fmt.Println(string(body))
}
