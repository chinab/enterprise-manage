package main

import (
	"fmt"
)

func main() {
	defer func() {
		if r := recover(); r != nil {
			fmt.Println("haha")
			fmt.Println(r)
		}
	}()

	i := maxNum(14, 12)
	fmt.Println(i)
}

func maxNum(a, b int) int {
	if a < 10 && b < 10 {
		panic("num must >= 10")
	}
	if a > b {
		return a
	} else {
		return b
	}
}
