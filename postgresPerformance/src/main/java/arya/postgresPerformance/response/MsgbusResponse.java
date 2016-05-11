package arya.postgresPerformance.response;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class MsgbusResponse {

  private boolean success = true;
  private boolean userError;
  private boolean systemError;
  private String errorCode ;
  private JsonNode data;
}
