package vertx.mongo.sample;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * http://vertx.io/docs/vertx-mongo-client/java/
 * 
 * @author july
 *
 */
public class MongodbpersisterVerticle extends AbstractVerticle {

	MongoClient mongoClient = null;
	
	@Override
	public void start() {
		
		mongoClient = MongoClient.createShared(vertx, 
				new JsonObject().put("host", "localhost")
				.put("port", 27017));

		EventBus eb = vertx.eventBus();
		eb.consumer("db.get", handlerGet());
		eb.consumer("db.put", handlerPut());
	}

	private Handler<Message<String>> handlerPut() {
		return message -> {
			String title = message.body();
			JsonObject document = new JsonObject().put("title", title);

			mongoClient.save("books", document, res -> {
				JsonObject json = new JsonObject();
				if (res.succeeded()) {
					json.put("id", res.result()).put("result", "success");
					message.reply(json);
				} else {
					json.put("result", "fail");
					res.cause().printStackTrace();
				}
			});
		};
	}

	private Handler<Message<JsonObject>> handlerGet() {
		return message -> {
			JsonObject query = new JsonObject();
			
			mongoClient.find("books", query, res -> {
				if (res.succeeded()) {
					JsonObject js = new JsonObject();
					js.put("list", res.result());
					js.put("result", "success");
					message.reply(js);
				} else {
					res.cause().printStackTrace();
				}
			});
		};
	}

	// Optional - called when verticle is undeployed
	public void stop() {
	}

}
