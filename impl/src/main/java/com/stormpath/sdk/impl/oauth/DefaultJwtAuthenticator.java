/*
* Copyright 2015 Stormpath, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.stormpath.sdk.impl.oauth;

import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.ds.DataStore;
import com.stormpath.sdk.oauth.JwtAuthenticationResult;
import com.stormpath.sdk.impl.ds.InternalDataStore;
import com.stormpath.sdk.lang.Assert;
import com.stormpath.sdk.oauth.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.SigningKeyResolver;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @since 1.0.RC5.1
 */
public class DefaultJwtAuthenticator implements JwtAuthenticator {

    private Application application;

    private InternalDataStore dataStore;

    final static String OAUTH_TOKEN_PATH = "/authTokens/";

    private SigningKeyResolver audienceApiKeySigningKeyResolver;

    public DefaultJwtAuthenticator(Application application, DataStore dataStore) {
        this.application = application;
        this.dataStore = (InternalDataStore) dataStore;
    }
    @Override
    public JwtAuthenticationResult authenticate(JwtAuthenticationRequest jwtRequest) {
        Assert.notNull(application, "application cannot be null or empty");
        Assert.notNull(jwtRequest, "jwtRequest cannot be null or empty");

        if (jwtRequest.getForLocalValidation()){
            byte[] bytes = null;
            String apiKeySecret = dataStore.getApiKey().getSecret();
            try {
                bytes = apiKeySecret.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e){
                return null;
            }
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(bytes)
                        .parseClaimsJws(jwtRequest.getJwt()).getBody();
                Map<String, Object> properties = new HashMap<String, Object>();
                properties.put("jwt", jwtRequest.getJwt());
                properties.put("account", claims.get("sub"));
                properties.put("application", claims.get("iss"));
                AccessToken accessToken = new DefaultAccessToken(dataStore, properties);

                JwtAuthenticationResultBuilder builder = new DefaultJwtAuthenticationResultBuilder(accessToken);
                return builder.build();
            } catch (SignatureException e){
                return null;
            }

        } else {
            StringBuilder stringBuilder = new StringBuilder(application.getHref());
            stringBuilder.append(OAUTH_TOKEN_PATH);
            stringBuilder.append(jwtRequest.getJwt());
            AccessToken accessToken = dataStore.getResource(stringBuilder.toString(), AccessToken.class);
            JwtAuthenticationResultBuilder builder = new DefaultJwtAuthenticationResultBuilder(accessToken);
            return builder.build();
        }
    }
}