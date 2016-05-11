package arya.postgresPerformance.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationError {

  private String errorCode;
  private String errorMessage;

  public ValidationError(String errorCode, String errorMessage) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  public static ValidationError ID_MISSING = new ValidationError("ID_MISSING",
          "Id is a mandatory ");
  public static ValidationError ID_INVALID = new ValidationError("ID_INVALID",
          "Id specified is not valid");
}
