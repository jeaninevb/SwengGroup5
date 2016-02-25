package soft.swenggroup5;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * PhysicalTestSuite
 *
 * A suite that only contains the tests that can only be
 * guaranteed to function on the Emulator (only the Nexus 4 Emulator at the moment)
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        DecoderUtilsTest.class,
        DecoderUtilsTest.class,
        EncoderUtilsTest.class,
})
public class EmulatorTestSuite{}
