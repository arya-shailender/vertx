package arya.postgresPerformance.routes;

import lombok.Data;

@Data
public class RouteEntry {

  private String httpMethod;
  private String type;
  private String url;
  private String handler;
  
  public RouteEntry(String httpMethod, String type, String url, String handler){
    this.httpMethod = httpMethod;
    this.type = type;
    this.url = url;
    this.handler = handler;
  }
}
