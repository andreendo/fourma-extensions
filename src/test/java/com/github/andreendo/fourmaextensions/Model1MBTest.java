package com.github.andreendo.fourmaextensions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.Parameter;

/**
 *
 * @author andreendo
 */
@RunWith(Parameterized.class)
public class Model1MBTest {
    @Parameter public String fTestCase;

    @Parameters
    public static Object[] data() {
        MBTGenerator gen = new MBTGenerator();
        gen.setCoverageCriterion(Criterion.ALL_EDGES);
        gen.setTestModel("./res/model1.esg");
        return gen.generateTestCases();
    }

    @Test
    public void test() {
        MBTRunner.runMBTestCase(fTestCase);
    }
}