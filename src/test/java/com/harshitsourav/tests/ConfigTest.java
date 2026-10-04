package com.harshitsourav.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.harshitsourav.framework.config.Config;

public class ConfigTest {
    @Test
    public void readDefaultsFromEnvironmentFiles() {
        Assert.assertEquals(Config.env(), "qa");
        Assert.assertTrue(Config.apiBaseUrl().startsWith("http"));
        Assert.assertEquals(Config.explicitWait().toSeconds(), 10L);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void missingKeyFailsWithClearMessage() {
        Config.get("this.key.doesnt.exist");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void missingSecretFailsFast() {
        Config.secret("NOT_SET");
    }

    @Test
    public void printingApiUrls() {
        System.out.println("ENV_VAR_SEEN:" + System.getenv("API_BASE_URL"));
        System.out.println("API_URL:" + Config.apiBaseUrl());
    }
}
