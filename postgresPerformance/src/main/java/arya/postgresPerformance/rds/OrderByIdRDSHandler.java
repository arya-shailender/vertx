package arya.postgresPerformance.rds;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import arya.postgresPerformance.Utils.MsgbusUtils;
import arya.postgresPerformance.Utils.Utils;
import arya.postgresPerformance.request.OrderByIdRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class OrderByIdRDSHandler implements RDSHandler {

  private static Logger logger = LoggerFactory
          .getLogger(OrderByIdRDSHandler.class);

  @Override
  public void handle(SQLConnection connection, Message<Object> message) {

    OrderByIdRequest req = Utils.jsonStr2Object(OrderByIdRequest.class,
            (String) message.body());
    String id = req.getId();
    JsonArray param = new JsonArray();
    param.add(id);
    connection.queryWithParams(
            "select * from dummy where id =?", param, res -> {
              if (!res.succeeded()) {
                logger.info("Exception in executing the query", res.cause());
                MsgbusUtils.sendErrorResponse(message, true,
                        "INTERNAL_SERVER_ERROR");
              }
              
              ResultSet resultSet = res.result();
              if (resultSet == null || resultSet.getResults().isEmpty()) {
                logger.info("No result or from the DB");
                MsgbusUtils.sendErrorResponse(message, false, "INVALID_ID");
              }
              List<JsonArray> results = resultSet.getResults();
              if (results.isEmpty()) {
                MsgbusUtils.sendErrorResponse(message, false, "INVALID_ID");
              }
              ObjectNode node = Utils.newJsonObject();
              results.stream().forEach(
                      jsOb -> {
                        String uuId = jsOb.getString(0);
                        JsonNode jNode = Utils.jsonStr2Object(JsonNode.class,
                                jsOb.getString(1));
                        node.put("id", uuId);
                        node.put("order", jNode);

                      });
              MsgbusUtils.sendSuccessResponse(message, node);
            });

  }
}
