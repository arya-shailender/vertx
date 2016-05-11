package arya.postgresPerformance.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartupVerticle extends AbstractVerticle {
  
  private static final Logger logger = LoggerFactory.getLogger(StartupVerticle.class);

  @Override
  public void start(Future<Void> fut) {
    DeploymentOptions options = new DeploymentOptions();
    JsonObject ob = config(); 

    
    options.setConfig(ob);
    options.setInstances(Runtime.getRuntime().availableProcessors());
    System.out.println("StartupVerticle ThreadId = " + Thread.currentThread().getId());
    vertx.deployVerticle("arya.postgresPerformance.verticle.HttpVerticle", options);
    vertx.deployVerticle("arya.postgresPerformance.verticle.PostgresVerticle", options);

  }

}
