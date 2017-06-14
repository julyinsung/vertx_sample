package vertx.web.sample;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * vertx web, mongo-client, eventBus 사용한 예제
 * 
 * 메뉴얼 참조 http://vertx.io/docs/vertx-web/java/
 * 
 * 예제소스 참조
 * https://github.com/vert-x3/vertx-examples/blob/master/web-examples/src/main/java/io/vertx/example/web/rest/SimpleREST.java
 * 
 * @author july
 *
 */
public class WebVerticle extends AbstractVerticle {

	EventBus eb;

	@Override
	public void start() {
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		router.route().consumes("application/json").produces("application/json");

		router.get("/book/title/:title").handler(this::handleGet);
		router.put("/book/title").handler(this::handlePut);

		eb = vertx.eventBus();
		
		vertx.deployVerticle("vertx.mongo.sample.MongodbpersisterVerticle");
		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	}

	public void handleGet(RoutingContext routingContext) {
		String title = routingContext.request().getParam("title");
		eb.send("db.get", title, ar -> {
			if (ar.succeeded()) {
				response(routingContext, ar);
			}
		});
	}

	public void handlePut(RoutingContext routingContext) {
		JsonObject pJo = routingContext.getBodyAsJson();
		String title = pJo.getString("title");

		eb.send("db.put", title, ar -> {
			if (ar.succeeded()) {
				response(routingContext, ar);
			}
		});
	}
	
	private void response(RoutingContext routingContext, AsyncResult<Message<Object>> ar) {
		System.out.println("Received reply: " + ar.result().body());
		HttpServerResponse response = routingContext.response();
		response.putHeader("content-type", "application/json");
		response.end(((JsonObject) ar.result().body()).encodePrettily());
	}

}
