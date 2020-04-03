package com.test.api;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;
import io.vertx.ext.web.api.generator.WebApiServiceGen;

@ProxyGen
@WebApiServiceGen
public interface PetsApi {

    void createPets(String body, OperationRequest context, Handler<AsyncResult<OperationResponse>> handler);

}
