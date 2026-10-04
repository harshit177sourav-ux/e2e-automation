package com.harshitsourav.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SanityTest {
    @Test
    public void framworkRuns() {
        Assert.assertEquals(2 + 2, 4, "Works well!");
    }

}
