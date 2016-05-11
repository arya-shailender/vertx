package arya.postgresPerformance.routes.handlers;

import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.RoutingContext;

public final class HandlerHelpers {

  
   
  public static EventBus getEventBus(RoutingContext routingContext) {

    Object eventBus = routingContext.get("eventBus");
    if (eventBus != null) {
      return (EventBus) eventBus;
    } else {
      throw new RuntimeException("");
    }
  }
}
