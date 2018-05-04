package com.github.andreendo.fourmaextensions;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andreendo
 */
public class PageObjectRefactorTest {
    
    public PageObjectRefactorTest() {
    }

    @Test
    public void test01() {
        PageObjectRefactor poRefactor = new PageObjectRefactor();
        List<TestClass> testClasses = new ArrayList<TestClass>();
        poRefactor.extractPOs(testClasses);
    }
}
