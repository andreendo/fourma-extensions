package com.github.andreendo.fourmaextensions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author andreendo
 */
public class PageObjectRefactor {

    private BlockIdentifier blockIdentifier;
    private Instrumenter instrumenter;
    private TestRunner testRunner;
    private Utils utils;
    private Refactor refactor;
    
    public void setBlockIdentifier(BlockIdentifier blockIdentifier) {
        this.blockIdentifier = blockIdentifier;
    }

    public void setInstrumenter(Instrumenter instrumenter) {
        this.instrumenter = instrumenter;
    }

    public void setTestRunner(TestRunner testRunner) {
        this.testRunner = testRunner;
    }

    public void setUtils(Utils utils) {
        this.utils = utils;
    }

    public void extractPOs(List<TestClass> testClasses) {
        Map<TestClass, List<Method>> map = new TreeMap<>();
        List<Class> adapters = new ArrayList<>();
        //Step 1 - isolate test framework calls in internal methods
        for (TestClass clazz : testClasses) {
            for(TestMethod method : clazz.getTestMethods()) {
                //it identifies blocks to be refactored
                blockIdentifier.process(method);
                while(blockIdentifier.hasBlockToExtract()) {
                    //apply extract method
                    Method extractedMethod = refactor
                            .extractMethod(clazz, method, blockIdentifier.nextBlock());
                    //record extracted methods by class
                    map.get(clazz).add(extractedMethod);
                }
            }
        }
        
        //Step 2 - extract adapter class
        for (TestClass clazz : testClasses) {
            List<Method> extractedMethods = map.get(clazz);
            if(! extractedMethods.isEmpty()) {
                Class adapter = refactor.extractClass(clazz, extractedMethods);
                adapters.add(adapter);
            }
        }
        
        //Step 3 - Collect Pages information
        //instrument Adapters to record page identification information
        for (Class adapter : adapters) {
            instrumenter.instrument(adapter);
        }
        testRunner.runTests( testClasses );
        
        //Step 4 - divide adapter classes in POs
        for (Class adapter : adapters) {
            for(Method method : adapter.getMethods()) {
                String pageLabel = instrumenter.getPage(adapter, method);
                Class pageObjectClass = utils.getClass(pageLabel);
                if(pageObjectClass == null)
                    pageObjectClass = utils.createClass(pageLabel);
                
                refactor.extractMethodFromTo(adapter, method, pageObjectClass);
            }
            utils.removeClass(adapter);
        }
    }
    
}
