package com.stormpath.sdk.impl.client

import com.stormpath.sdk.client.AuthenticationScheme
import com.stormpath.sdk.client.Clients
import com.stormpath.sdk.impl.cache.DefaultCache
import com.stormpath.sdk.lang.Duration
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import java.util.concurrent.TimeUnit

import static org.testng.Assert.assertEquals
import static org.testng.Assert.assertTrue

class DefaultClientBuilderTest {

    def builder, client

    @BeforeMethod
    void before() {
        builder = Clients.builder()
        client = builder.build()
    }

    @Test
    void testBuilder() {
        assertTrue(builder instanceof DefaultClientBuilder)
    }

    @Test
    void testConfigureCacheManager() {
        assertEquals client.dataStore.cacheManager.defaultTimeToLive, new Duration(10000, TimeUnit.SECONDS)
        assertEquals client.dataStore.cacheManager.defaultTimeToIdle, new Duration(10000, TimeUnit.SECONDS)
        DefaultCache cache = (DefaultCache) client.dataStore.cacheManager.getCache(Account.class.getName())
        assertEquals cache.timeToIdle, new Duration(1000, TimeUnit.SECONDS)
        assertEquals cache.timeToLive, new Duration(1500, TimeUnit.SECONDS)
    }

    @Test
    void testConfigureApiKey() {
        // remove key.txt from src/test/resources and this test will fail
        assertEquals client.dataStore.apiKey.id, "12"
        assertEquals client.dataStore.apiKey.secret, "13"
    }

    @Test
    void testConfigureBaseProperties() {
        DefaultClientBuilder clientBuilder = (DefaultClientBuilder) builder
        assertEquals clientBuilder.clientConfiguration.baseUrl, "https://api.stormpath.com/v42"
        assertEquals clientBuilder.clientConfiguration.connectionTimeout, 10 * 1000
        assertEquals clientBuilder.clientConfiguration.authenticationScheme, AuthenticationScheme.BASIC
    }

    @Test
    void testConfigureProxy() {
        DefaultClientBuilder clientBuilder = (DefaultClientBuilder) builder
        assertEquals clientBuilder.clientConfiguration.proxyHost, "localhost"
        assertEquals clientBuilder.clientConfiguration.proxyPort, 9000
        assertEquals clientBuilder.clientConfiguration.proxyUsername, "foo"
        assertEquals clientBuilder.clientConfiguration.proxyPassword, "bar"
    }

    @Test
    void testConfigureApplication() {
        DefaultClientBuilder clientBuilder = (DefaultClientBuilder) builder
        assertEquals clientBuilder.clientConfiguration.applicationName, "appName"
        assertEquals clientBuilder.clientConfiguration.applicationHref, "appHref"
    }
}
