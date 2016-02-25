package soft.swenggroup5;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * PhysicalTestSuite
 *
 * A suite that only contains the tests that should be able
 * to function on any Android device, no matter make / model.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        DecoderUtilsTest.class,
        EncoderUtilsTest.class
})
public class PhysicalTestSuite {}
