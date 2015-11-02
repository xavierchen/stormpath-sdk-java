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

import com.stormpath.sdk.oauth.JwtAuthenticationRequest;

/**
 * @since 1.0.RC5.1
 */
public class DefaultJwtAuthenticationRequest implements JwtAuthenticationRequest {

    private final String jwt;

    private final Boolean forLocalValidation;

    public DefaultJwtAuthenticationRequest(String jwt, Boolean forLocalValidation, String apikey){
        this.jwt = jwt;
        this.forLocalValidation = forLocalValidation;
    }

    public String getJwt() {
        return jwt;
    }

    public Boolean getForLocalValidation() {
        return forLocalValidation;
    }
}