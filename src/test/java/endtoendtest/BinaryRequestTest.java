package endtoendtest;

import endpoint.Main;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.WebClient;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(VertxUnitRunner.class)
public class BinaryRequestTest {

    @Test
    public void sendBinaryDataToServer(TestContext context) {
        Async serviceDeployed = context.async();
        Vertx vertx = Vertx.vertx();
        JsonObject config = new JsonObject().put(Main.EXTRA_HANDLER, false);
        vertx.deployVerticle(new Main(), new DeploymentOptions().setConfig(config),
                deployed -> serviceDeployed.complete());

        serviceDeployed.await();
        performRestRequest(context, vertx);
    }

    private void performRestRequest(TestContext context, Vertx vertx) {
        Async testComplete = context.async();
        WebClient client = WebClient.create(vertx);

        byte[] bytesToSent = RandomUtils.nextBytes(5);

        client.post(8080, "localhost", "/pets").sendBuffer(Buffer.buffer(bytesToSent), result -> {
            context.assertTrue(result.succeeded());
            Buffer receivedBuffer = result.result().bodyAsBuffer();
            assertNotNull(receivedBuffer);
            assertArrayEquals(bytesToSent, receivedBuffer.getBytes());

            vertx.close(closed ->
                    testComplete.complete());

        });
    }

    @Test
    public void sendBinaryDataToServerWithExtraHandler(TestContext context) {
        Async serviceDeployed = context.async();
        Vertx vertx = Vertx.vertx();
        JsonObject config = new JsonObject().put(Main.EXTRA_HANDLER, true);
        vertx.deployVerticle(new Main(), new DeploymentOptions().setConfig(config),
                deployed -> serviceDeployed.complete());

        serviceDeployed.await();
        performRestRequest(context, vertx);
    }

}
