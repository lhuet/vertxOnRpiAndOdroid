package fr.lhuet.breizhcamp;

import io.vertx.core.AbstractVerticle;

/**
 * Created by lhuet on 17/03/16.
 */
public class LCDverticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {

        try
        {
            I2CLcdDisplay lcd = new I2CLcdDisplay( 1, 0x27, 4, 20);

            lcd.sendText( 0, "prvni radka" );
            lcd.setBacklight( true );
//            lcd.sendText( 1, "1234567890123456" );
//            for( int i = 0; i<2; i++ )
//            {
//                Thread.sleep(500);
//                lcd.setBacklight( false );
//                Thread.sleep(500);
//                lcd.setBacklight( true );
//            }
//
//            for( int i = 0; i < 16; i++ )
//            {
//                String str = "";
//                for( int j = 0; j<16; j++ )
//                {
//                    str = str + (char)((i*16)+j);
//                }
//                lcd.sendText(0, ""+i + ": " + (i*16) + "-" + ((i*16)+15) );
//                lcd.sendText(1,  str );
//                Thread.sleep(500);
//            }

            lcd.sendText( 1, "Raspberry Pi" );
            lcd.sendText( 2, "!!!!!!!!!!!!" );
        }
        catch( Exception ee )
        {
            ee.printStackTrace();
        }

    }
}
