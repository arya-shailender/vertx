package arya.postgresPerformance.response;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class Response {
  private JsonNode data;
  private Meta meta;
  
  @Data
  public static class Meta{
    private String requestId;
    private boolean success = true;
    private List<ResponseError> errors;
  }
  
  @Data 
  public static class ResponseError{
    private String errorCode;
    private String errorMessage;
    
    public ResponseError(String errCode, String errMessage){
      this.errorCode = errCode;
      this.errorMessage = errMessage;
    }
  }
  
}
