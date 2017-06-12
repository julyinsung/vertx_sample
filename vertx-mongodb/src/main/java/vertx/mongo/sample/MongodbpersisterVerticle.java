package vertx.mongo.sample;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * http://vertx.io/docs/vertx-mongo-client/java/
 * 
 * @author july
 *
 */
public class MongodbpersisterVerticle extends AbstractVerticle {

	@Override
	public void start() {
		JsonObject config = new JsonObject();
		config.put("host", "localhost");
		config.put("port", 27017);

		System.out.println(">>>  ");
		
		MongoClient mongoClient = MongoClient.createShared(vertx, config);
		
		JsonObject document = new JsonObject().put("title", "The Hobbit");

		mongoClient.save("books", document, res -> {
		  if (res.succeeded()) {
		    String id = res.result();
		    System.out.println("Saved book with id " + id);
		  } else {
		    res.cause().printStackTrace();
		  }
		});

		JsonObject query = new JsonObject();
		mongoClient.find("books", query, res -> {
			if (res.succeeded()) {
				for (JsonObject json : res.result()) {
					System.out.println(json.encodePrettily());
				}
			} else {
				res.cause().printStackTrace();
			}
		});
	}

	// Optional - called when verticle is undeployed
	public void stop() {
	}

}
