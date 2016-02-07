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
        assertEquals(0, EncoderUtils.splitFileSize(-1));
        assertEquals(0, EncoderUtils.splitFileSize(0));
        assertEquals(1, EncoderUtils.splitFileSize(1));
        assertEquals(2, EncoderUtils.splitFileSize(2001));
        assertEquals(2, EncoderUtils.splitFileSize(3999));
        assertEquals(3, EncoderUtils.splitFileSize(4001));
        assertEquals(3, EncoderUtils.splitFileSize(5000));
        assertEquals(5, EncoderUtils.splitFileSize(10000));
    }
}