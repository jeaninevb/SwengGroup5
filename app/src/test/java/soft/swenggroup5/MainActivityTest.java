package soft.swenggroup5;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TODO temporary storage until we split MainActivity
 */
public class MainActivityTest {

    /**
     *
     */
    @Test
    public void test_splitFileSize()  {
        assertEquals(0, MainActivity.splitFileSize(-1));
        assertEquals(0, MainActivity.splitFileSize(0));
        assertEquals(1, MainActivity.splitFileSize(1));
        assertEquals(2, MainActivity.splitFileSize(2001));
        assertEquals(2, MainActivity.splitFileSize(3999));
        assertEquals(3, MainActivity.splitFileSize(4001));
        assertEquals(3, MainActivity.splitFileSize(5000));
        assertEquals(5, MainActivity.splitFileSize(10000));

    }
}