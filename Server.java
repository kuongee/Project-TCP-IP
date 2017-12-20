/* 
Author: Jeesoo Min 
Email: kuongee@gmail.com
Completion date
Version 1: 2014-01-11 ~ 2014-01-14
Updated: 2017-12-21
*/
import java.net.*;
import java.io.*;
import java.util.*;

public class Server{
	HashMap clients;
	int clientnum=0;
	
	Server(){
		clients = new HashMap();
		Collections.synchronizedMap(clients);
	}
	
	void start(){
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		try{
			serverSocket = new ServerSocket(7777);
			System.out.println("Server Started...");
			
			while(true){
				socket = serverSocket.accept();
				System.out.println("["+socket.getInetAddress()+":"+socket.getPort()+"]"+"is accessing to server");
				ServerReceiver thread = new ServerReceiver(socket);
				thread.start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	void sendToAll(String msg){
		Iterator it = clients.keySet().iterator();
		while(it.hasNext()){
			try{
				DataOutputStream out = (DataOutputStream)clients.get(it.next());
				if(msg.startsWith("[message]"))
					out.writeUTF(msg.substring(9));
				else if(msg.startsWith("[start]")){
					String nn = msg.substring(7);
					if(clientnum < 3){
						if(clientnum == 1){
							out.writeUTF("startB" + nn);	// °ËÀº µ¹
						}
						else if(clientnum == 2){
							out.writeUTF("startW" + nn);	// Èò µ¹ 
						}
					}
					else
						out.writeUTF("full" + nn);
				}
				else if(msg.startsWith("[location]")){
					out.writeUTF(msg);					
				}
				else
					out.writeUTF(msg);
			}catch(IOException e){}
		}
	}
	
	class ServerReceiver extends Thread{
		Socket socket;
		DataInputStream in;
		DataOutputStream out;
		
		ServerReceiver(Socket socket){
			this.socket = socket;
			try{
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			}catch(IOException e){}
		}
		
		public void run(){
			String name = "";
			try{
				name = in.readUTF();
				sendToAll("Welcome #"+name+"!");
				clients.put(name, out);
				System.out.println("The number of clients " +clients.size()+".");
				
				while(in!=null){
					String s = in.readUTF();
					if(s.startsWith("[start]")){
						clientnum++;
						sendToAll(s);
					}
					else
						sendToAll(s);
				}
				
			}catch(IOException e){
				
			}finally{
				sendToAll("#"+name+"is out");
				clients.remove(name);
				System.out.println("["+socket.getInetAddress()+":"+socket.getPort()+"]"+"closed");
				System.out.println("The number of clients"+clients.size()+".");
				clientnum -= 1;
			}
		}
	}
	
	public static void main(String args[]){
		new Server().start();
	}
}
