package main

import (
	"fmt"
	"io"
	"os"
	"path/filepath"
	"strings"
)

const (
	SWF_PATH = "F:\\svn\\osgi\\business\\sourcecode\\com.artogrid.app.idbqebootfast\\WebContent\\swfs\\"
	QE_PATH  = "F:\\svn\\QE2"
)

func copyFile(path string) {
	err := filepath.Walk(path, func(path string, f os.FileInfo, err error) error {
		if f == nil {
			return err
		}
		if f.IsDir() {
			return nil
		}
		name := f.Name()
		if strings.Contains(path, "bin-release") && strings.Contains(name, "IDB") && strings.Contains(name, ".swf") {
			fmt.Println(f.Name())

			src, err := os.Open(path)
			if err != nil {
				return nil
			}

			dst, err := os.Create(SWF_PATH + name)
			if err != nil {
				return nil
			}

			io.Copy(dst, src)
			dst.Close()
			src.Close()
		}
		return nil
	})
	if err != nil {
		fmt.Printf("filepath.Walk() returned %v\n", err)
	}
}

func removeFile(path string) {
	err := filepath.Walk(path, func(path string, f os.FileInfo, err error) error {
		if f == nil {
			return err
		}
		if f.IsDir() {
			return nil
		}
		name := f.Name()
		if strings.Contains(path, "swfs\\IDB") && strings.Contains(name, ".swf") {
			os.Remove(path)
		}
		return nil
	})
	if err != nil {
		fmt.Printf("filepath.Walk() returned %v\n", err)
	}
}

func main() {
	removeFile(SWF_PATH)
	copyFile(QE_PATH)
}
