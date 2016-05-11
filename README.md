# vertx
This is a test project for testing high throughput with postgresNosql library of vertx.
MVN base directory is postgresPerformance/
arya.postgresPerformance.verticle.StartupVerticle => is the first verticle started by the maven shade plugin
It starts the HttpVerticle plugin which starts listening on the ports and the urls defined in conf/dev/config.json

Command to start the project is in startup.sh


logs are genereted in <projectRoot>/logs directory



