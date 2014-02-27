package utils

import (
	"log"
	"os"
	"strings"
)

/**
 * defer utils.SetLogOutPut("logs/logfile.txt")()
 **/
func SetLogOutPut(path string) func() {
	path = strings.Replace(path, "\\", "/", -1)

	_, err := os.Stat(path)
	if err != nil && os.IsNotExist(err) {
		err = checkAndMkParentDir(path)
		if err != nil {
			panic(err)
		}

		f, err := os.Create(path)
		if err != nil {
			panic(err)
		}
		f.Close()
	}

	f, err := os.OpenFile(path, os.O_APPEND, os.ModeAppend)
	if err != nil {
		panic(err)
	}

	log.SetOutput(f)

	return func() {
		f.Close()
	}
}

func checkAndMkParentDir(path string) error {
	if strings.Contains(path, "/") {
		dir := path[0:strings.LastIndex(path, "/")]
		_, err := os.Stat(path)
		if err != nil && os.IsNotExist(err) {
			checkAndMkParentDir(dir)
			return os.Mkdir(dir, os.ModeDir)
		}
	}
	return nil
}
