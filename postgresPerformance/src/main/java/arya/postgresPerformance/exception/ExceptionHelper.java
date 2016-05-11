package arya.postgresPerformance.exception;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import arya.postgresPerformance.response.Response;

import arya.postgresPerformance.Utils.Utils;
import arya.postgresPerformance.response.Response.ResponseError;
import arya.postgresPerformance.response.ResponseFactory;



public class ExceptionHelper {

  public static void handleExceptionAndRespond(RoutingContext rc, Exception e) {
    HttpServerResponse resp = rc.response();
    resp.putHeader("ContentType", "application/json");

    List<ResponseError> errs = null;
    if (e instanceof ValidationException) {
      resp.setStatusCode(400);
      ValidationException valExp = (ValidationException) e;
      errs = externalizeValErrors(valExp.getValidationError());
    } else {
      resp.setStatusCode(500);
      errs = new LinkedList<ResponseError>();
      errs.add(new ResponseError("INTERNAL_SERVER_ERROR",
              "Error Occurred We are lonoking into it"));
    }
    Response output = ResponseFactory.createFailureResp(rc, errs);
    resp.end(Utils.object2Json(output).toString());
  }

  private static List<ResponseError> externalizeValErrors(
          List<ValidationError> valErrors) {
    List<ResponseError> errors = new ArrayList<ResponseError>(valErrors.size());
    valErrors.forEach(valError -> {
      ResponseError err = new ResponseError(valError.getErrorCode(), valError
              .getErrorMessage());
      errors.add(err);
    });
    return errors;
  }

}
