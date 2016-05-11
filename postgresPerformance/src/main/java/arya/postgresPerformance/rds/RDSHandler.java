package arya.postgresPerformance.rds;

import io.vertx.core.eventbus.Message;
import io.vertx.ext.sql.SQLConnection;

public interface RDSHandler {

  public void handle(SQLConnection connection,  Message<Object> message);
  
}
