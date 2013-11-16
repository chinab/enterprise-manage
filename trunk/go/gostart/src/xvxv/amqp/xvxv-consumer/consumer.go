package main

import (
	"code.google.com/p/goprotobuf/proto"
	"github.com/streadway/amqp"
	"log"
	"time"
	"xvxv/protobuf/example"
)

var (
	uri       = "amqp://guest:guest@192.168.1.103:5672/"
	queueName = "xvxv-queue"
	lifetime  = 600 * time.Second
)

func main() {
	done := make(chan int, 1)
	conn, _ := amqp.Dial(uri)
	channel, _ := conn.Channel()

	queue, _ := channel.QueueDeclare(
		queueName, // name of the queue
		false,     // durable
		false,     // delete when usused
		false,     // exclusive
		false,     // noWait
		nil,       // arguments
	)

	deliveries, _ := channel.Consume(
		queue.Name,    // name
		"consumerTag", // consumerTag,
		true,          // noAck
		false,         // exclusive
		false,         // noLocal
		false,         // noWait
		nil,           // arguments
	)

	go handle(deliveries, done)
	go timeout(done)

	<-done
}

func timeout(done chan int) {
	if lifetime > 0 {
		time.Sleep(lifetime)
	}
	log.Printf("timeout")
	done <- 0
}

func handle(deliveries <-chan amqp.Delivery, done chan int) {
	for d := range deliveries {
		msg := example.User{}
		proto.Unmarshal(d.Body, &msg)
		log.Printf(
			"got %dB delivery: [%v] username:%v password:%v",
			len(d.Body),
			d.DeliveryTag,
			msg.GetUsername(),
			msg.GetPassword(),
		)
	}
	log.Printf("handle: deliveries channel closed")
	done <- 0
}
