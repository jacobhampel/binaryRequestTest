package com.test.api;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;

import java.nio.charset.StandardCharsets;

public class PetsApiImpl implements PetsApi {

    boolean useExtra;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    public PetsApiImpl(boolean useExtra) {
        this.useExtra = useExtra;
    }

    @Override
    public void createPets(String body, OperationRequest context, Handler<AsyncResult<OperationResponse>> handler) {
        try {
            byte[] response;
            if (useExtra) {
                response = context.getExtra().getBinary("extra");
            } else {
                response = body.getBytes(StandardCharsets.UTF_8);
            }
            handler.handle(Future.succeededFuture(
                    new OperationResponse(200, "OK", Buffer.buffer(response),
                            MultiMap
                                    .caseInsensitiveMultiMap()
                                    .add("content-type", "application/octet-stream"))));
        } catch (Throwable e) {
            log.error("", e);
            handler.handle(Future.failedFuture(e));
        }
    }

}
