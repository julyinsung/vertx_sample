package vertx.web.sample;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * http://vertx.io/docs/vertx-web/java/
 * 
 * @author july
 *
 */
public class WebVerticle extends AbstractVerticle {

	@Override
	public void start() {
		HttpServer server = vertx.createHttpServer();

		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		//router.route().consumes("application/json").produces("application/json");

		router.put("/from/to").handler(routingContext -> {
			JsonObject pJo = routingContext.getBodyAsJson();
			String from = pJo.getString("from");
			String to = pJo.getString("to");

			JsonObject jo = new JsonObject();
			jo.put("from", from);
			jo.put("to", to);

			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "application/json");
			response.end(jo.encodePrettily());
		});

		router.get("/from/:from/to/:to").handler(routingContext -> {

			String from = routingContext.request().getParam("from");
			String to = routingContext.request().getParam("to");

			JsonObject jo = new JsonObject();
			jo.put("from", from);
			jo.put("to", to);

			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "application/json");
			response.end(jo.encodePrettily());
		});

		server.requestHandler(router::accept).listen(8080);
	}

	public void sstart() {

	}

}
