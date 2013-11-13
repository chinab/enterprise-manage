package main

import (
	"fmt"
	"xvxv/main/autoinc"
)

func main() {
	a := autoinc.New(0, 1)
	defer a.Close()
	c0 := 0
	c1 := 0
	for i := 0; i < 1000000; i++ {
		fmt.Println(c0)
	}
	fmt.Println(c0)
	fmt.Println(c1)
}
