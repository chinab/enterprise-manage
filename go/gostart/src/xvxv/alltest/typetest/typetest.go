package main

import (
	"fmt"
	"io/ioutil"
	"net"
)

func main() {
	conn, _ := net.Dial("tcp4", "www.baidu.com:80")

	conn.Write([]byte("HEAD / HTTP/1.0\r\n\r\n"))

	result, _ := ioutil.ReadAll(conn)

	fmt.Println(string(result))
}
