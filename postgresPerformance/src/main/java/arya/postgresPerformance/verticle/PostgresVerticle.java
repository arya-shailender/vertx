package arya.postgresPerformance.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.SQLConnection;

import arya.postgresPerformance.rds.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import arya.postgresPerformance.Utils.Utils;
import arya.postgresPerformance.rds.RDSHandler;

import com.fasterxml.jackson.databind.node.ObjectNode;


public class PostgresVerticle extends AbstractVerticle{
  private static final Logger logger = LoggerFactory.getLogger(PostgresVerticle.class);
  
  @Override
  public void start(Future<Void> startFuture) throws Exception {
    JsonObject dbClientConfig = new JsonObject();
    System.out.println("PostgresVerticle ThreadId = " + Thread.currentThread().getId());
    
    dbClientConfig.put("host", config().getString("db.host"));
    dbClientConfig.put("port", config().getInteger("db.port",5432));
    dbClientConfig.put("maxPoolSize", config().getInteger("db.maxPoolSize", 10));
    dbClientConfig.put("username", config().getString("db.username"));
    dbClientConfig.put("password", config().getString("db.password"));
    dbClientConfig.put("database",config().getString("db.dbName"));

   
    AsyncSQLClient dbClient = PostgreSQLClient.createShared(vertx, dbClientConfig, "PostgreSQLPool1");
    EventBus eventBus = vertx.eventBus();
    registerMessageHandlers(dbClient, eventBus);

  }
  
  private void registerMessageHandlers( AsyncSQLClient dbClient, EventBus eventBus ){
    MessageConsumer<Object> consumer = eventBus.consumer("postGres.orderById");
    consumer.handler(message -> {
      System.out.println("consuming postGres.orderById with thread +  " + Thread.currentThread().getId());
      
      dbClient.getConnection(result -> { 
        executeWithHandler(new OrderByIdRDSHandler(), result, message);
      });
    }); 
    
//    eventBus.consumer("postGres.saveOrder").handler(message ->{
//      dbClient.getConnection(result -> {
//        executeWithHandler(new SaveOrderRDSHandler(), result, message);
//      });
//    });
  }
  
  private void executeWithHandler(RDSHandler handler,AsyncResult<SQLConnection> getConnAsyncRes,  Message<Object> message){
    SQLConnection connection = null;
    try{
       connection = extractConnection(getConnAsyncRes);
    }catch(Exception e){
      errorResponse("Exception Occured while getting connection", message, e);
      return;
    }
    
    try{
      handler.handle(connection, message);
    }catch(Exception e){
      errorResponse("Exception occured while fetching from DB",message, e);
    }finally{
      
      connection.close();
    }
  }
  
  
  private String getErrorCode(Exception e){
    return "500";
  }
  
  private void errorResponse(String exceptionMessage, Message<Object>message, Exception e){
    logger.debug(exceptionMessage, e);
    ObjectNode node = Utils.newJsonObject();
    String errorCode = getErrorCode(e);
    node.put("success", false );
    node.put("errorCode",errorCode );
    message.reply(node.toString());
  }
  
  private SQLConnection extractConnection(AsyncResult<SQLConnection>asyncResult){
    if (!asyncResult.succeeded()){
      throw new RuntimeException("couldn't find any DB Connection for the request.",asyncResult.cause());
    }
    return asyncResult.result();
  }
    
}
