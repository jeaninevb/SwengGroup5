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
        assertEquals(0, MainActivity.splitFileSize(1));
        assertEquals(1, MainActivity.splitFileSize(2001));
    }
}