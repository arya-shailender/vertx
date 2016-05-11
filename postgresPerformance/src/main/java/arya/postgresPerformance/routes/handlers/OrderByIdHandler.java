package arya.postgresPerformance.routes.handlers;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.RoutingContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import arya.postgresPerformance.Utils.Utils;
import arya.postgresPerformance.exception.ExceptionHelper;
import arya.postgresPerformance.request.OrderByIdRequest;



public class OrderByIdHandler implements Handler<RoutingContext> {

  private static final Logger logger = LoggerFactory
          .getLogger(OrderByIdHandler.class);
  private MsgbusPostgresClientHelper msgBusHelper = new MsgbusPostgresClientHelper();

  @Override
  public void handle(RoutingContext routingContext) {
    try {
      handleInternal(routingContext);
    } catch (Exception exp) {
      logger.debug("Exception occurred ", exp);
      ExceptionHelper.handleExceptionAndRespond(routingContext, exp);
    }
  }

  private void handleInternal(RoutingContext routingContext) {
    MultiMap mmap = routingContext.request().params();
    OrderByIdRequest req = (OrderByIdRequest) Utils.mmap2Object(mmap, OrderByIdRequest.class);
    EventBus eventBus = HandlerHelpers.getEventBus(routingContext);
    DeliveryOptions options = new DeliveryOptions();
    options.setSendTimeout(1000);

    eventBus.send("postGres.orderById", Utils.object2Json(req).toString(),
            options, resp -> {
              try {
                msgBusHelper.handlePostgresAndSendResp(routingContext, resp);
              } catch (Exception e) {
                ExceptionHelper.handleExceptionAndRespond(routingContext, e);
              }
            });

  }

}
