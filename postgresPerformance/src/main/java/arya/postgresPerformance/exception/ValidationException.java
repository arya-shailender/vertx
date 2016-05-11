package arya.postgresPerformance.exception;

import java.util.LinkedList;
import java.util.List;


public class ValidationException extends RuntimeException {

  private static final long serialVersionUID = 123L;
  private List<ValidationError> valError;
  
  public ValidationException(List<ValidationError> ValidationError, String errorMessage) {
    super(errorMessage);
    this.valError = ValidationError;
  }

  public ValidationException(List<ValidationError> ValidationError) {
    super();
    this.valError = ValidationError;
  }
  
  public ValidationException(ValidationError valError){
    super();
    this.valError = new LinkedList<ValidationError>();
    this.valError.add(valError);
  }
  
  public ValidationException() {
  }

  public List<ValidationError> getValidationError() {
    return valError;
  }
  
  
  
  public void addValidationError(ValidationError valError){
    if(null == valError){
      this.valError = new LinkedList<ValidationError>();
    }
    this.valError.add(valError);
  }

}
