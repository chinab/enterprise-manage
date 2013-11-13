package stomputils

import (
	"bytes"
	"compress/gzip"
	"errors"
	"fmt"
	"github.com/gmallard/stompngo"
	"io/ioutil"
	"log"
	"net"
	"time"
)

var (
	JmsVHost = "localhost"
	JmsHost  = "114.80.142.37"
	JmsPort  = "61612"
	JmsDest  = "/topic/IDC.SyncBond"
	JmsId    = "go-idc-sync-bond-offer-log"
)

func Handle(handler func(xml []byte)) {
	n, err := net.Dial("tcp", net.JoinHostPort(JmsHost, JmsPort))
	CheckErr(err)
	ch := stompngo.Headers{"accept-version", "1.1", "host", JmsVHost}
	conn, err := stompngo.Connect(n, ch)
	CheckErr(err)

	s := stompngo.Headers{"destination", JmsDest, "id", JmsId}

	r, e := conn.Subscribe(s)
	if e != nil {
		log.Fatalln(e)
	}

	func() {
		for {
			select {
			case m := <-r:
				if m.Error != nil {
					log.Fatalln(m.Error)
				}
				if m.Message.Command != stompngo.MESSAGE {
					log.Fatalln(m)
				}

				err := hand(handler, m.Message.Body)
				if err != nil {
					log.Fatalln(err)
					continue
				}

			case <-time.After(600 * time.Second):
				println("timeout")
				return
			}
		}
	}()
	err = conn.Unsubscribe(s)
	CheckErr(err)

	err = conn.Disconnect(stompngo.Headers{})
	CheckErr(err)

	err = n.Close()
	CheckErr(err)
}

func hand(handler func(xml []byte), body []byte) error {
	defer func() {
		if x := recover(); x != nil {
			fmt.Println(errors.New("reade message error!"))
		}
	}()

	inBuffer := bytes.NewBuffer(body)
	greader, err := gzip.NewReader(inBuffer)
	if err != nil {
		return err
	}
	defer greader.Close()

	data, err := ioutil.ReadAll(greader)
	if err != nil {
		return err
	}

	go doHand(handler, data)
	return nil
}

func doHand(handler func(xml []byte), data []byte) {
	defer func() {
		if x := recover(); x != nil {
			fmt.Println(errors.New("handler message error!"))
		}
	}()
	handler(data)
}

func CheckErr(err error) {
	if err != nil {
		log.Fatalln(err)
		panic(err)
	}
}
