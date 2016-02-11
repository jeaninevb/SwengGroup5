package soft.swenggroup5;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * TODO temporary storage until we split MainActivity
 */
public class EncoderUtilsTest {

    /**
     *
     */
    @Test
    public void test_splitFileSize() {
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

    @Test
    public void testGetFileBytes()throws FileNotFoundException {

        List<String[]> expected = new ArrayList<>();
        expected.add(new String[]{"abc", "bar"});
        expected.add(new String[]{"bcd", "efg"});

        List<String[]> actual = new ArrayList<>();
        actual.add(new String[]{"abc", "bar"});
        actual.add(new String[]{"bcd", "efg"});

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            for (int j = 0; j < expected.get(i).length; j++) {
                Assert.assertEquals(expected.get(i)[j], actual.get(i)[j]);
            }
        }
    }

}

