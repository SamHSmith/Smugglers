package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import org.joml.Vector3f;

import entity.BasicEntity;
import entity.GUI;

public class Client {
	Socket sock;
	ArrayList<String> input = new ArrayList<String>();

	public Client(String adrees, int port) {
		try {
			sock = new Socket(adrees, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> read() throws IOException {
		ArrayList<String> input = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
		} catch (SocketException e) {
			System.err.println("Diconnected from server");

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
		return input;
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

	public void DoCommands(ArrayList<BasicEntity> ents, ArrayList<GUI> guis) {

		ArrayList<String> coms;
		try {
			System.out.println("Got here");
			coms = read();
		} catch (IOException e) {
			System.err.println("error while reading");
			return;
		}
		for (String command : coms) {
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

	public ArrayList<String> getInput() {
		ArrayList<String> temp = new ArrayList<String>(input);

		input.clear();
		return temp;
	}

}
