package fr.lhuet.devoxx;

import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformManager;
import com.pi4j.wiringpi.Gpio;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;

/**
 * Created by lhuet on 14/03/16.
 */
public class MainApp extends AbstractVerticle {


    @Override
    public void start() throws Exception {

        // Default platform is Raspberry -> Explicit assign the target platform
        PlatformManager.setPlatform(Platform.ODROID);

        // Get config parameter from main verticle and déploy LCD verticle with the same config parameters
        DeploymentOptions options = new DeploymentOptions().setConfig(config());
        vertx.deployVerticle(LCDverticle.class.getName(), options);

        // Deploy only if 1-wire is present
        boolean is1wire = config().getBoolean("1-wire");
        if (is1wire) {
            vertx.deployVerticle(Ds18b20Verticle.class.getName());
        }

    }

}
