package net;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import org.joml.Vector3f;

import entity.BasicEntity;
import gui.GUI;

public class Client implements NetworkValues{
	Socket sock;
	ArrayList<String> input = new ArrayList<String>();

	public Client(String adrees, int port) {
		try {
			sock = new Socket(adrees, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String readString() throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
		} catch (SocketException e) {
			System.err.println("Diconnected from server");
		}
		
		return br.readLine();
	}
	
	private int readIdentifier() throws IOException{
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(sock.getInputStream());
		} catch (SocketException e) {
			System.err.println("Diconnected from server");
		}
		int id=dis.readInt();
		return id;
	}

	public void send(String data) {
		if (data != null) {
			try {
				PrintStream pr;
				pr = new PrintStream(sock.getOutputStream());
				pr.println(data);
			} catch (Exception e) {
				System.out.println("Error while sending " + data
						+ " to the server");
			}

		}
	}
	
	private boolean isInput() throws IOException{
		if(sock.getInputStream().available()>0){
			return true;
		}
		return false;
	}
	
	
	public void Handleinput(ArrayList<BasicEntity> ents, ArrayList<GUI> guis) throws IOException{
		while(isInput()){
			int id=readIdentifier();
			
			if(id==Client.command){
				doCommand(ents, guis, readString());
			}
			if(id==Client.universe){
				ents.clear();
				ents.addAll(ReadUniverse());
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<BasicEntity> ReadUniverse() {
			
				try {
					ObjectInputStream dos;
					dos=new ObjectInputStream(sock.getInputStream());
					
					Object obj = dos.readObject();
					if(obj instanceof ArrayList){
						return (ArrayList<BasicEntity>) obj;
					}
				} catch (IOException e) {
					System.out.println("Player Diconnected");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		return null;
	}

	private void doCommand(ArrayList<BasicEntity> ents, ArrayList<GUI> guis,String command) {
		
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
