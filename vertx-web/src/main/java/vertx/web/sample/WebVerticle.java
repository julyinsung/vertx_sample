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
 * vertx web, mongo-client, eventBus를 이용한 예제
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
		// router를 생성한다. router에 여러 설정을 할 수 있다.
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		//router.route().handler(BodyHandler.create().setBodyLimit(1));
		//router.route().consumes("application/json").produces("application/json");
		
		// url 매핑
		router.get("/book").handler(this::handlerGet);
		router.put("/book/title").handler(this::handlerPut);

		// event bus
		eb = vertx.eventBus();

		// 다른 verticle를 deploy한다.
		vertx.deployVerticle("vertx.mongo.sample.MongodbpersisterVerticle");
		
		// http 서버를 띄운다.
		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
		
	}

	public void handlerGet(RoutingContext routingContext) {
		String title = routingContext.request().getParam("title");
		
		eb.send("db.get", title, ar -> {
			if (ar.succeeded()) {
				response(routingContext, ar);
			}
		});
	}

	public void handlerPut(RoutingContext routingContext) {
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
