package fr.lhuet.breizhcamp;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

/**
 * Created by lhuet on 20/03/16.
 */
public class I2CLcdDisplay {

    private final int[] LCD_LINE_ADDRESS = {
            0x80,
            0xC0,
            0x94,
            0xD4
    };

    private final boolean LCD_CHR = true;
    private final boolean LCD_CMD = false;

    private int rows;
    private int cols;

    private int tmpData = 0;
    private boolean tmpRS = false;
    private boolean tmpE = false;
    private boolean tmpBacklight = false;

    private I2CDevice dev = null;

    public I2CLcdDisplay(int i2cBus, int i2cAddress,  int rows, int cols) throws Exception {

        System.out.println(LCD_LINE_ADDRESS.length);
        if( rows > LCD_LINE_ADDRESS.length )
            throw new IllegalArgumentException( "rows" );

        this.rows = rows;
        this.cols = cols;

        I2CBus bus = I2CFactory.getInstance(i2cBus);
        dev = bus.getDevice( i2cAddress );

        init();
    }


    private void setRS( boolean val )
    {
        tmpRS = val;
    }

    private void setE( boolean val )
    {
        tmpE = val;
    }

    private void setData( int val )
    {
        tmpData = val;
    }

    public void setBacklight( boolean val )
            throws Exception
    {
        tmpBacklight = ! val;
        write();
    }

    private void write()
            throws Exception
    {
        byte out = (byte)( tmpData | (tmpBacklight ? 128 : 0)  | (tmpRS ? 64 : 0)  | (tmpE ? 16 : 0) );
        dev.write( out );
    }

    private void init( )
            throws Exception
    {
        lcd_byte(0x33,LCD_CMD);		// 4 bit
        lcd_byte(0x32,LCD_CMD);		// 4 bit
        lcd_byte(0x28,LCD_CMD);		// 4bit - 2 line
        lcd_byte(0x08,LCD_CMD);  	// don't shift, hide cursor
        lcd_byte(0x01,LCD_CMD); 	// clear and home display
        lcd_byte(0x06,LCD_CMD);		// move cursor right
        lcd_byte(0x0c,LCD_CMD);		// turn on

    }

    private void pulse_en( boolean type )
            throws Exception
    {
        setE( true );
        write();

        setE( false );
        write();

        if( type == LCD_CMD ) Thread.sleep( 1 );

    }


    private void lcd_byte( int val, boolean type )
            throws Exception
    {
        setRS(type);
        setData( val >> 4 );
        write();
        pulse_en( type );
        setData( val & 0x0f );
        write();
        pulse_en( type );

    }

    private static String padRight(String s, int n)
    {
        return String.format("%1$-" + n + "s", s);
    }

    public void sendText( int line, String text )
            throws Exception
    {
        if( (line >= LCD_LINE_ADDRESS.length) || (line>rows) )
            throw new IllegalArgumentException( "line" );

        if( text.length() > cols )
            text = text.substring(0, cols);

        lcd_byte(  LCD_LINE_ADDRESS[line], LCD_CMD);

        String outData = padRight( text, cols );
        for( int i = 0; i<outData.length(); i++ )
        {
            lcd_byte( outData.charAt(i), LCD_CHR );
        }

    }


}
