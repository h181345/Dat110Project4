package no.hvl.dat110.aciotdevice.client;

import static no.hvl.dat110.aciotdevice.client.Configuration.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

public class RestClient {

	public RestClient() {
		// TODO Auto-generated constructor stub
	}

	private static String logpath = "/accessdevice/log/";

	public void doPostAccessEntry(String message) {

		// TODO: implement a HTTP POST on the service to post the message

		try (Socket s = new Socket(host, port)) {
			
			AccessMessage accessmessage = new AccessMessage(message);
			String jsonbody = accessmessage.toJson();
			
			String httppostrequest = 
					"POST " + logpath + " HTTP/1.1\r\n" + 
					"Host: " + host + "\r\n" +
					"Content-type: application/json\r\n" + 
			        "Content-length: " + jsonbody.length() + "\r\n" +
					"Connection: close\r\n" + 
			        "\r\n" + 
					jsonbody + 
					"\r\n";

			OutputStream output = s.getOutputStream();

			PrintWriter pw = new PrintWriter(output, false);

			pw.print(httppostrequest);
			pw.flush();

			InputStream in = s.getInputStream();
			
			Scanner scan = new Scanner(in);
			StringBuilder jsonrespone = new StringBuilder();
			boolean header = true;

			while (scan.hasNext()) {
				String nextline = scan.nextLine();
				if (header) {
					System.out.println(nextline);
				} else {
					jsonrespone.append(nextline);
				}
				if (nextline.isEmpty()) {
					header = false;
				}
			}

			System.out.println("BODY:");
			System.out.println(jsonrespone.toString());

			scan.close();
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	private static String codepath = "/accessdevice/code";

	public AccessCode doGetAccessCode() {

		AccessCode code = null;

		// TODO: implement a HTTP GET on the service to get current access code

		try (Socket s = new Socket(host, port)) {
			String httpgetrequest = "GET " + codepath + " HTTP/1.1\r\n" + 
									"Accept: application/json\r\n" + "Host: localhost\r\n" +
									"Connection: close\r\n" + "\r\n";

			OutputStream output = s.getOutputStream();

			PrintWriter pw = new PrintWriter(output, false);

			pw.print(httpgetrequest);
			pw.flush();

			InputStream in = s.getInputStream();
			Scanner scan = new Scanner(in);
			StringBuilder jsonrespone = new StringBuilder();
			boolean header = true;

			while (scan.hasNext()) {
				String nextline = scan.nextLine();
				if (header) {
					System.out.println(nextline);
				} else {
					jsonrespone.append(nextline);
				}
				if (nextline.isEmpty()) {
					header = false;
				}
			}

			System.out.println("BODY:");
			System.out.println(jsonrespone.toString());

			scan.close();

			Gson gson = new Gson();
			String json = jsonrespone.toString();
			code = gson.fromJson(json, AccessCode.class);

		} catch (IOException ex) {
			System.err.println(ex);
		}
		
		return code;
	}
}
