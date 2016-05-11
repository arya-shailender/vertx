package arya.postgresPerformance.response;

import java.util.List;

import arya.postgresPerformance.response.Response.Meta;
import arya.postgresPerformance.response.Response.ResponseError;
import io.vertx.ext.web.RoutingContext;


public final class ResponseFactory {
  
  public static  Response createSuccessResp(RoutingContext context){
    Meta meta = new Meta();
    meta.setRequestId(context.get("loggingId").toString());
    Response resp = new Response();
    resp.setMeta(meta);
    return resp;
  }
  
  public static Response createFailureResp(RoutingContext context, List<ResponseError> errors){
    Meta meta = new Meta();
    meta.setRequestId(context.get("loggingId").toString());
    Response resp = new Response();
    meta.setSuccess(false);
    meta.setErrors(errors);
    resp.setMeta(meta);
    
    return resp;
  }
  
  
  
}
