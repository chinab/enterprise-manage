package main

import (
	"github.com/codegangsta/martini"
	"log"
	"net/http"
	"xvxv/utils"
)

func main() {
	defer utils.SetLogOutPut("log/logfile.txt")()

	log.Println("hahahahahahah")

	m := martini.Classic()

	m.Get("/", func(log *log.Logger) string {
		log.Println("你好")
		return "Hello world!"
	})
	//http.Handle("/", m)
	http.ListenAndServe(":8080", m)
}
