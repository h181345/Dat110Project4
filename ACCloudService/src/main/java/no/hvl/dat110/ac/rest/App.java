package no.hvl.dat110.ac.rest;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;

import java.util.Stack;

import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App {

	static AccessLog accesslog = null;
	static AccessCode accesscode = null;

	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service

		accesslog = new AccessLog();
		accesscode = new AccessCode();

		after((req, res) -> {
			res.type("application/json");
		});

		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {

			Gson gson = new Gson();

			return gson.toJson("IoT Access Control Device");
		});

		// TODO: implement the routes required for the access control service
		post("/accessdevice/log/", (req, res) -> {

			Gson gson = new Gson();
			AccessMessage message = gson.fromJson(req.body(), AccessMessage.class);
			int id = accesslog.add(message.getMessage());

			return gson.toJson(accesslog.get(id));
		});

		get("/accessdevice/log/", (req, res) -> {

			String json = accesslog.toJson();

			return json;
		});

		get("/accessdevice/log/:id", (req, res) -> {
			int id = Integer.parseInt(req.params(":id"));
			Gson gson = new Gson();

			return gson.toJson(accesslog.get(id));
		});

		put("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();
			accesscode = gson.fromJson(req.body(), AccessCode.class);

			res.body(gson.toJson(accesscode));
			return res;
		});

		get("/accessdevice/code", (req, res) -> {

			Gson gson = new Gson();

			return gson.toJson(accesscode);
		});

		delete("/accessdevice/log/", (req, res) -> {
			accesslog.clear();
			res.body(accesslog.toJson());

			return res;
		});
	}

}
