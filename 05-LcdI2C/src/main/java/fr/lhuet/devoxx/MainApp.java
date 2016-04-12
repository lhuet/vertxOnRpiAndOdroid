package fr.lhuet.devoxx;

import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformManager;
import com.pi4j.wiringpi.Gpio;
import io.vertx.core.AbstractVerticle;

/**
 * Created by lhuet on 14/03/16.
 */
public class MainApp extends AbstractVerticle {


    @Override
    public void start() throws Exception {

        // Default platform is Raspberry -> Explicit assign the target platform
        PlatformManager.setPlatform(Platform.ODROID);

        vertx.deployVerticle(LCDverticle.class.getName());

    }

}
