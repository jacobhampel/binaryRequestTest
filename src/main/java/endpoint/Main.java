package endpoint;

import com.test.ServiceController;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import util.Runner;

public class Main extends AbstractVerticle {

    public static final String EXTRA_HANDLER = "extraHandler" ;

    public static void main(String... args) {
        Runner.runExample(Main.class);
    }

    @Override
    public void start(Future<Void> fut) {

        vertx.deployVerticle(new RestEndpoint(), new DeploymentOptions().setConfig(config()),
                started ->
                        vertx.deployVerticle(new ServiceController(), new DeploymentOptions().setConfig(config()),
                                started2 ->
                                        fut.complete()));


    }
}
