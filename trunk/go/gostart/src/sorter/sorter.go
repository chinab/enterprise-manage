package main

import (
	"bufio"
	"flag"
	"fmt"
	"io"
	"os"
	"sorter/algorithms/bubblesort"
	"sorter/algorithms/qsort"
	"strconv"
	"time"
)

var infile *string = flag.String("i", "unsorted.data", "File contains values for sorting")
var outfile *string = flag.String("o", "sorted.data", "File to receive sorted values")
var algorithm *string = flag.String("a", "qsort", "Sort algorithm")

func readValues(infile string) (values []int, err error) {
	file, errTemp := os.Open(infile)
	if errTemp != nil {
		fmt.Println("Failed to open the input file ", infile)
		err = errTemp
		return
	}

	defer file.Close()

	br := bufio.NewReader(file)

	values = make([]int, 0)

	for {
		line, isPrefix, errTemp := br.ReadLine()

		if errTemp != nil {
			if errTemp != io.EOF {
				err = errTemp
			}
			break
		}

		if isPrefix {
			fmt.Println("A too long line, seems unexpected.")
			return
		}

		str := string(line)

		value, errTemp := strconv.Atoi(str)

		if errTemp != nil {
			err = errTemp
			return
		}

		values = append(values, value)
	}
	return
}

func writeValues(values []int, outfile string) (err error) {
	file, errTemp := os.Create(outfile)
	if errTemp != nil {
		err = errTemp
	}

	defer file.Close()

	for _, value := range values {
		v := strconv.Itoa(value)
		file.WriteString(v + "\n")
	}
	return nil
}

func main() {
	flag.Parse()

	if infile != nil {
		fmt.Println("infile=", *infile, ",  outfile=", *outfile, ",  algorithm=", *algorithm)
		values, err := readValues(*infile)
		if err == nil {
			fmt.Println("Read values:", values)
		} else {
			fmt.Println(err)
			return
		}
		t1 := time.Now()
		if *algorithm == "qsort" {
			qsort.QuickSort(values)
		} else if *algorithm == "bubblesort" {
			bubblesort.BubbleSort(values)
		} else {
			fmt.Println("Sorting algorithm", *algorithm, "is either unknown or unsupported.")
		}
		t2 := time.Now()
		fmt.Println("The sorting process costs", t2.Sub(t1), "to complete.")

		err = writeValues(values, *outfile)
		if err == nil {
			fmt.Println("Write values:", values)
		} else {
			fmt.Println(err)
			return
		}
	}
}
