package vertx.mongo.sample;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

/**
 * http://vertx.io/blog/my-first-vert-x-3-application/
 * 
 * @author july
 *
 */
//@RunWith(VertxUnitRunner.class)
public class MongodbpersisterVerticleTest {
/*
	private Vertx vertx;
	
	@Before
	public void setUp(TestContext context){
		vertx = Vertx.vertx();
		vertx.deployVerticle(MongodbpersisterVerticle.class.getName());
	}
	
	@After
	public void tearDown(TestContext context){
		vertx.close(context.asyncAssertSuccess());
	}
	
	@Test
	public void test(TestContext context) {
		final Async async = context.async();
	    vertx.createHttpClient().getNow(8080, "localhost", "/",
	     response -> {
	      response.handler(body -> {
	    	context.assertNotNull("not null!");
	        async.complete();
	      });
	    });
	}
*/
}
