package arya.postgresPerformance.routes;

import org.apache.commons.lang3.StringUtils;

public enum RouteTypes {
  NORMAL("normal"),
  REGEX("regex");
  
  private String value;
  RouteTypes(String value){
    this.value = value;
  }
  
  public static RouteTypes fromVal(String val){
    if (StringUtils.isEmpty(val)){
      return null;
    }
    for( RouteTypes type : RouteTypes.values()){
      if (StringUtils.equals(type.value, val)){
        return type;
      }
    }
    return null;
  }
}
