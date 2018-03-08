// tcpServer.go
// Jeesoo Min
// This program has the same function of server that I have programmed with Java.

package main

import (
	"fmt"
	"net"
	"strings"
)

func closeHandler(c net.Conn) {
	/* closing handler for processing client */
	for k, v := range clientList {
		if v.addr == c {
			fmt.Println("Closing ", v.addr.RemoteAddr())
			delete(clientList, k)
			client_number--
			break
		}
	}
	c.Close()
}

func sendToAll(n int, data []byte) {
	for _, v := range clientList {

		var length int = 0
		msg := string(data[:n])
		sendMessage := data[length:n]
		if strings.Index(msg, "[message]") == 0 {
			length = len("[message]")
			sendMessage = data[length:n]
		} else if strings.Index(msg, "[start]") == 0 {
			length = len("[start]")
			nn := data[length:n]
			start := []byte("")
			if game_client < 3 {
				if game_client == 1 {
					start = []byte("startB")
				} else if game_client == 2 {
					start = []byte("startW")
				}
			} else {
				start = []byte("full")
			}
			start = append(start, nn...)
			sendMessage = start
		} else if strings.Index(msg, "[location]") == 0 {
			length = len("[location]")
			sendMessage = data[length:n]
		}

		fmt.Println(sendMessage)
		_, err := v.addr.Write(sendMessage)
		if err != nil {
			fmt.Println(err)
			return
		}
	}
}

func requestHandler(c net.Conn) {
	defer closeHandler(c)

	data := make([]byte, 4096)

	/** Get name and Welcome message **/
	n, err := c.Read(data) // read name first
	if err != nil {
		fmt.Println(err)
		return
	}
	name := []byte("Welcome #")
	temp := []byte("!!")
	length := len(name) + n + len(temp)
	name = append(name, data[:n]...)
	name = append(name, temp...)
	fmt.Print(c.RemoteAddr(), length, string(name[:length]))
	sendToAll(length, name)

	/** Handling Messages **/
	for {
		n, err := c.Read(data)
		if err != nil {
			fmt.Println(err)
			return
		}
		//fmt.Print(c.RemoteAddr(), string(data[:n]))
		msg := string(data[:n])
		if strings.Index(msg, "[start]") == 0 {
			game_client++
			fmt.Print("Start")
		}
		sendToAll(n, data)
	}

}

type Address struct {
	addr net.Conn
}

var clientList map[int]Address // client list
var client_number int = 0
var game_client int = 0

func main() {
	fmt.Println("Server Start!")
	ln, err := net.Listen("tcp", ":7777")
	if err != nil {
		fmt.Println(err)
		return
	}
	defer ln.Close()

	clientList = make(map[int]Address)

	for {
		conn, err := ln.Accept()
		if err != nil {
			fmt.Println(err)
			continue
		}

		clientList[client_number] = Address{conn}
		client_number = client_number + 1

		go requestHandler(conn) // request handler go routine like thread -> 연결연결 별로
	}
}
