/*
 * Copyright 2016 Stormpath, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stormpath.sdk.servlet.config;

import com.stormpath.sdk.lang.Assert;
import com.stormpath.sdk.servlet.http.Resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @since 1.0.4
 */
public class SecureForwardedProtoAwareResolver implements Resolver<Boolean> {

    private final Resolver<Boolean> isHTTPSForwardedProtoResolver;
    private final Resolver<Boolean> secureRequiredExceptForLocalhostResolver;

    public SecureForwardedProtoAwareResolver(Resolver<Boolean> isHTTPSForwardedProtoResolver, Resolver<Boolean> secureRequiredExceptForLocalhostResolver) {
        Assert.notNull(isHTTPSForwardedProtoResolver, "isHTTPSForwardedProtoResolver resolver cannot be null.");
        Assert.notNull(secureRequiredExceptForLocalhostResolver, "secureRequiredExceptForLocalhost resolver cannot be null.");
        this.isHTTPSForwardedProtoResolver = isHTTPSForwardedProtoResolver;
        this.secureRequiredExceptForLocalhostResolver = secureRequiredExceptForLocalhostResolver;
    }

    @Override
    public Boolean get(HttpServletRequest request, HttpServletResponse response) {
        if (this.isHTTPSForwardedProtoResolver.get(request, response)) {
            return false;
        }
        return this.secureRequiredExceptForLocalhostResolver.get(request, response);
    }
}
