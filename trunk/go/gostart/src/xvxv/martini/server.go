package main

import (
	"fmt"
	"github.com/codegangsta/inject"
)

// 原定义Foo(s1,s2 string), 改成
func Foo(s1 string, s2 int) {
	fmt.Println(s1, s2) // type assertion
}

func main() {
	ij := inject.New()
	ij.Map("a")
	// 注意第二个参数的固定写法
	ij.Map(1)
	ij.Invoke(Foo)
}
