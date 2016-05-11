package arya.postgresPerformance.Utils;

import io.vertx.core.eventbus.Message;
import arya.postgresPerformance.response.MsgbusResponse;

import com.fasterxml.jackson.databind.JsonNode;

public final class MsgbusUtils {
  private MsgbusUtils(){};
  
  public static void sendSuccessResponse(Message<Object> message, JsonNode data) {
    MsgbusResponse resp = new MsgbusResponse();
    resp.setSuccess(true);
    resp.setSystemError(false);
    resp.setUserError(false);
    resp.setData(data);
    message.reply(Utils.object2Json(resp).toString());

  }

  public static void sendErrorResponse(Message<Object> message, boolean systemError,
          String errorCode) {
    MsgbusResponse resp = new MsgbusResponse();
    resp.setSuccess(false);
    resp.setSystemError(systemError);
    resp.setUserError(!systemError);
    resp.setErrorCode(errorCode);
    message.reply(Utils.object2Json(resp).toString());

  }
}
