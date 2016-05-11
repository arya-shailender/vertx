package arya.postgresPerformance.routes.handlers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import arya.postgresPerformance.Utils.Utils;
import arya.postgresPerformance.exception.ValidationError;
import arya.postgresPerformance.exception.ValidationException;
import arya.postgresPerformance.response.MsgbusResponse;
import arya.postgresPerformance.response.Response;
import arya.postgresPerformance.response.ResponseFactory;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;



class MsgbusPostgresClientHelper {

  private static final Logger logger = LoggerFactory
          .getLogger(MsgbusPostgresClientHelper.class);

  void handlePostgresAndSendResp(RoutingContext routingContext,
          AsyncResult<Message<Object>> response) {
    logger.debug("response received = " + response.succeeded() + " " + response);

    Optional<MsgbusResponse> msgBusResponse = getMsgbusResponse(response);
    if (!msgBusResponse.isPresent()) { throw new RuntimeException(
            "Invalid Response from PostgresVerticle"); }
    MsgbusResponse msgBusResp = msgBusResponse.get();
    if (msgBusResp.isSuccess()) {
      sendSuccesResp(routingContext, msgBusResp);
      return;
    }
    throwFailureException(msgBusResp);
    return;

  }

  Optional<MsgbusResponse> getMsgbusResponse(
          AsyncResult<Message<Object>> response) {
    if (!response.succeeded()) { return Optional.empty(); }
    Object output = response.result().body();
    return Optional.of(Utils.jsonStr2Object(MsgbusResponse.class,
            output.toString()));
  }

  void throwFailureException(MsgbusResponse msgBusResp) {
    if (msgBusResp.isUserError()) { throw new ValidationException(
            ValidationError.ID_INVALID); }
    throw new RuntimeException("System error received from MsgbusResponse");
  }

  private void sendSuccesResp(RoutingContext routingContext,
          MsgbusResponse msgBusResp) {
    HttpServerResponse resp = routingContext.response();
    resp.headers().add("Content-Type", "application/json");

    Response response = ResponseFactory.createSuccessResp(routingContext);
    response.setData(msgBusResp.getData());
    resp.end(Utils.object2Json(response).toString());
  }
}
