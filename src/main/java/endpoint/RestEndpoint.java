package endpoint;

import com.test.api.PetsApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.ext.web.api.contract.openapi3.impl.OpenAPI3RouterFactoryImpl;
import io.vertx.ext.web.impl.Utils;

import static endpoint.Main.EXTRA_HANDLER;

public class RestEndpoint extends AbstractVerticle {
    private static final String EVENT_BUS_ADDRESS = "address" ;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    public static String getEventBusAddress() {
        return EVENT_BUS_ADDRESS;
    }

    @Override
    public void start(Future<Void> fut) {
        log.info("Started");
        OpenAPI3RouterFactory.create(vertx, "openapi.json", ar -> {
            if (ar.succeeded()) {
                // Spec loaded with success
                OpenAPI3RouterFactory routerFactory = ar.result();

                Boolean extraHandler = config().getBoolean(EXTRA_HANDLER);
                if (Boolean.TRUE.equals(extraHandler)) {
                    OpenAPI3RouterFactoryImpl impl = (OpenAPI3RouterFactoryImpl) routerFactory;
                    impl.setExtraOperationContextPayloadMapper(routingContext -> {
                        JsonObject extraPayload = new JsonObject();
                        String contentType = routingContext.request().getHeader("Content-Type");
                        if (contentType == null || !(Utils.isJsonContentType(contentType) || Utils.isXMLContentType(contentType))) { //Body is not extracted
                            extraPayload.put("extra", routingContext.getBody().getBytes());
                        }
                        return extraPayload;
                    });
                }
                routerFactory.mountServiceInterface(PetsApi.class, EVENT_BUS_ADDRESS);

                Router router = routerFactory.getRouter();
                HttpServer httpServer = vertx.createHttpServer();
                httpServer.requestHandler(router);
                httpServer.listen(8080);
                fut.complete();
            } else {
                // Something went wrong during router factory initialization
                Throwable exception = ar.cause();
                log.error("", exception);
                fut.fail(exception);
            }
        });


    }
}
