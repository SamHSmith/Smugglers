package net;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import math3d.Vector3f;
import entity.BasicEntity;
import entity.PhiEntity;
import gui.GUI;

public class Server implements Runnable,NetworkValues {

	private ServerSocket sock;
	private ArrayList<Socket> clients = new ArrayList<Socket>();
	Thread thread;

	public Server(int port) {
		try {
			sock = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		thread = new Thread(this);
		thread.start();
	}

	public void send(String data) {
		if (data != null) {
			
			ArrayList<Socket> socks=new ArrayList<>();

			for (Socket csock : clients) {
				try {
					DataOutputStream dos;
					dos=new DataOutputStream(csock.getOutputStream());
					
					dos.writeInt(command);
					
					PrintStream pr;
					pr = new PrintStream(csock.getOutputStream());
					pr.println(data);
				} catch (IOException e) {
					socks.add(csock);
					System.out.println("Player Diconnected");
				}
			}
			
			for(Socket csock:socks){
				clients.remove(csock);
			}

		}
	}
	
	public void sendUniverse(ArrayList<BasicEntity> data) {
		if (data != null) {
			
			ArrayList<Socket> socks=new ArrayList<>();

			for (Socket csock : clients) {
				try {
					ObjectOutputStream dos;
					dos=new ObjectOutputStream(csock.getOutputStream());
					
					dos.writeObject(data);
				} catch (IOException e) {
					socks.add(csock);
					System.out.println("Player Diconnected");
				}
			}
			
			for(Socket csock:socks){
				clients.remove(csock);
			}

		}
	}

	public ArrayList<String> read() throws IOException {
		ArrayList<String> input = new ArrayList<String>();
		BufferedReader br = null;
		for (Socket sock : clients) {
			try {
				br = new BufferedReader(new InputStreamReader(
						sock.getInputStream()));
			} catch (SocketException e) {
				clients.remove(sock);
				continue;
			}
			if (sock.getInputStream() != null) {
				while (true) {
					if (sock.getInputStream().available() < 1) {
						break;
					}
					String in = br.readLine();

					input.add(in);

					if (in == null) {
						System.out.println("nothing to read");
					}
				}
			}
		}
		return input;
	}

	public void run() {
		while (true) {
			try {
				clients.add(sock.accept());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void DoCommands(ArrayList<BasicEntity> ents, ArrayList<GUI> guis) {

		ArrayList<String> coms;
		try {
			coms = read();
		} catch (IOException e) {
			return;
		}

		for (String command : coms) {
			send(command);
			String[] s = command.split(" ");
			if (s[0].startsWith("move")) {
				ents.get(Integer.parseInt(s[1])).move(Float.parseFloat(s[2]),
						Float.parseFloat(s[3]), Float.parseFloat(s[4]));
			}
			if (s[0].startsWith("rotate")) {
				ents.get(Integer.parseInt(s[1])).rotate(Float.parseFloat(s[2]),
						Float.parseFloat(s[3]), Float.parseFloat(s[4]));
			}
			if (s[0].startsWith("force")) {
				ents.get(Integer.parseInt(s[1])).ModifyVelocity(
						new Vector3f(Float.parseFloat(s[2]), Float
								.parseFloat(s[3]), Float.parseFloat(s[4])));
				System.out.println("Doing command: " + command);
			}
			if (s[0].startsWith("forcerot")) {
				ents.get(Integer.parseInt(s[1])).ModifyRotVelocity(
						new Vector3f(Float.parseFloat(s[2]), Float
								.parseFloat(s[3]), Float.parseFloat(s[4])));
				System.out.println("Doing command: " + command);
			}
		}
	}

	public void stop() throws InterruptedException {
		thread.join(999);
	}

}
