package soft.swenggroup5;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TODO temporary storage until we split MainActivity
 */
public class EncoderUtilsTest {

    /**
     *
     */
    @Test
    public void test_splitFileSize()  {
        assertEquals(0, EncoderUtils.numberOfQRCodes(-1));
        assertEquals(0, EncoderUtils.numberOfQRCodes(0));
        assertEquals(1, EncoderUtils.numberOfQRCodes(1));
        assertEquals(2, EncoderUtils.numberOfQRCodes(2001));
        assertEquals(2, EncoderUtils.numberOfQRCodes(3999));
        assertEquals(3, EncoderUtils.numberOfQRCodes(4001));
        assertEquals(3, EncoderUtils.numberOfQRCodes(5000));
        assertEquals(5, EncoderUtils.numberOfQRCodes(10000));
    }

    // LINGFENG TODO
}