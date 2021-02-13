package android.ivo.bake_it;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class HttpMediaFormatTest {
    @Test
    public void httpMediaFormat_providesMP4Format() {
        HttpMediaFormat httpMediaFormat = new HttpMediaFormat("http://hello.mp4");
        assertEquals(httpMediaFormat.getFormat(), HttpMediaFormat.MP4);
    }

    @Test
    public void httpMediaFormat_providesUnknownFormat() {
        HttpMediaFormat httpMediaFormat = new HttpMediaFormat("http://hello.mp41");
        assertEquals(httpMediaFormat.getFormat(), HttpMediaFormat.UNKNOWN);
    }

    @Test
    public void httpMediaFormat_providesJPEGFormat1() {
        HttpMediaFormat httpMediaFormat = new HttpMediaFormat("http://hello.jpg");
        assertEquals(httpMediaFormat.getFormat(), HttpMediaFormat.JPEG);
    }

    @Test
    public void httpMediaFormat_providesJPEGFormat2() {
        HttpMediaFormat httpMediaFormat = new HttpMediaFormat("http://hello.jpeg");
        assertEquals(httpMediaFormat.getFormat(), HttpMediaFormat.JPEG);
    }
}