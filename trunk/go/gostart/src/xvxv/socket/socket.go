package main

import (
	"fmt"
	"net"
)

func main() {
	client, err := net.Dial("tcp", "192.168.1.230:9899")
	if err != nil {
		fmt.Println("服务端连接失败", err.Error())
		return
	}
	defer client.Close()
	message := `{"cmd":1000,"userName":"candle","pwd":"3fde6bb0541387e4ebdadf7c2ff31123"}`
	client.Write(getMessage(message))

	fmt.Println(string(readByBuf(client, 1024)))
}

func readByBuf(client net.Conn, bufsize int) []byte {
	buf, end := read(client, bufsize)

	if end {
		return buf
	} else {
		return append(buf, readByBuf(client, bufsize)...)
	}
}

func read(client net.Conn, bufsize int) (data []byte, end bool) {
	b := make([]byte, 1)
	buf := make([]byte, bufsize)
	c := 0

	for c < bufsize {
		client.Read(b)
		buf[c] = b[0]
		c++
		if b[0] == 0 {
			//return buf[0:c], true
			data, end = buf[0:c], true
			return
		}
	}

	data, end = buf, false
	return
	//return buf, false
}

func getMessage(message string) []byte {
	m := []byte(message)
	return append(m, 0)
}
