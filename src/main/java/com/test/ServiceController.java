package com.test;

import com.test.api.PetsApi;
import com.test.api.PetsApiImpl;
import endpoint.RestEndpoint;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.serviceproxy.ServiceBinder;

import static endpoint.Main.EXTRA_HANDLER;

public class ServiceController extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {
        PetsApi service = new PetsApiImpl(config().getBoolean(EXTRA_HANDLER));
        final ServiceBinder serviceBinder =
                new ServiceBinder(vertx).setAddress(RestEndpoint.getEventBusAddress());
        serviceBinder.register(PetsApi.class, service);
        fut.complete();
    }
}
