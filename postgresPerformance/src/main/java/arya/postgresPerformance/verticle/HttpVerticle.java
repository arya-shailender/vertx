package arya.postgresPerformance.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import arya.postgresPerformance.Utils.Utils;
import arya.postgresPerformance.routes.RouteEntry;
import arya.postgresPerformance.routes.RouteTypes;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class HttpVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(HttpVerticle.class);

  
  private void initRoutingContext(RoutingContext context){
    UUID loggingId = java.util.UUID.randomUUID();
    context.put("loggingId", loggingId.toString());
    context.put("eventBus",vertx.eventBus());
    context.next();
  } 

  private RouteEntry getRouteEntry(JsonObject jsObject) {
    return new RouteEntry(jsObject.getString("httpMethod"),
            jsObject.getString("type"), jsObject.getString("url"),
            jsObject.getString("handler"));
  }

  @SuppressWarnings("unchecked")
  private void startHttpServer() {
    HttpServer httpServer = vertx.createHttpServer();

    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.route().handler(this::initRoutingContext);
    for (Object confOb : config().getJsonArray("routes")) {

      RouteEntry re = getRouteEntry((JsonObject) confOb);
      RouteTypes inputRouteTypes = RouteTypes.fromVal(re.getType());
      Route route = null;
      if (RouteTypes.NORMAL == inputRouteTypes) {
        route = router.route(HttpMethod.valueOf(re.getHttpMethod()),
                re.getUrl());
      } else if (RouteTypes.REGEX == inputRouteTypes) {
        route = router.routeWithRegex(HttpMethod.valueOf(re.getHttpMethod()),
                re.getUrl());
      }
      try {
        route.handler(
                (Handler<RoutingContext>) Class.forName(re.getHandler())
                        .newInstance());
        logger.debug("route added successfully + " + re);
      } catch (InstantiationException | IllegalAccessException
              | ClassNotFoundException e) {
        e.printStackTrace();
      }

    }
    router.route().handler(routingContext->{
      sendWrongRequest(routingContext);
    });

    httpServer.requestHandler(router::accept).listen(config().getInteger("http.port"));

  }

  private void sendWrongRequest(RoutingContext context){
    HttpServerResponse response = context.response();
    response.setStatusCode(404);
    response.putHeader("Content-Type", "application/json");
    ObjectNode node = Utils.newJsonObject();
    node.put("errorCode", "RESOURCE_NOT_FOUND");
    node.put("errorMessage", "Requested Resource is not available");
    node.put("requestId", context.get("loggingId").toString());
    response.end(node.toString());
    
  }
  @Override
  public void start(Future<Void> startFuture) throws Exception {
    System.out.println("starting HttpVerticle");
    startHttpServer();
  
  }

  @Override
  public void stop(Future<Void> stopFuture) throws Exception {

  }
}
