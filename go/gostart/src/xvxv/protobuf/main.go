package main

import (
	"code.google.com/p/goprotobuf/proto"
	"fmt"
	"io"
	"os"
	"xvxv/protobuf/example"
)

var path = "./data"

func main() {
	write()
	read()
}

func read() {
	file, err := os.Open(path)
	if err != nil {
		fmt.Printf("failed: %s\n", err)
		return
	}

	defer file.Close()
	fi, err := file.Stat()
	CheckError(err)
	buffer := make([]byte, fi.Size())
	_, err = io.ReadFull(file, buffer) //read all content
	CheckError(err)
	msg := &example.User{}
	err = proto.Unmarshal(buffer, msg) //unSerialize
	CheckError(err)
	fmt.Printf("read: %s\n", msg.String())
}

func write() {
	msg := &example.User{
		Username: proto.String("xvxv"),
		Password: proto.String("123456"),
	} //msg init

	f, err := os.Create(path)
	if err != nil {
		fmt.Printf("failed: %s\n", err)
		return
	}

	defer f.Close()
	buffer, err := proto.Marshal(msg) //SerializeToOstream
	f.Write(buffer)
}

func CheckError(err error) {
	if err != nil {
		fmt.Println(err.Error())
		os.Exit(-1)
	}
}
