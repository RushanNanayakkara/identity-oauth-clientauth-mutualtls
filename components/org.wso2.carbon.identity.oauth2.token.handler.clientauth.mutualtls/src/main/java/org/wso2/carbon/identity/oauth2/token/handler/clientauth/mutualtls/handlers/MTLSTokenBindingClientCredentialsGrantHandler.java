/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.oauth2.token.handler.clientauth.mutualtls.handlers;

import org.apache.commons.lang.ArrayUtils;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.dto.OAuth2AccessTokenRespDTO;
import org.wso2.carbon.identity.oauth2.token.OAuthTokenReqMessageContext;
import org.wso2.carbon.identity.oauth2.token.handler.clientauth.mutualtls.utils.CommonConstants;
import org.wso2.carbon.identity.oauth2.token.handlers.grant.ClientCredentialsGrantHandler;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is used to bound the MTLS certificate of the client to the access token issued. Here, the certificate is
 * bounded to the access token using a hidden scope.
 *
 * @see <href="https://tools.ietf.org/html/draft-ietf-oauth-mtls-17">IETF OAuth MTLS</>
 */
public class MTLSTokenBindingClientCredentialsGrantHandler extends ClientCredentialsGrantHandler {

    @Override
    public OAuth2AccessTokenRespDTO issue(OAuthTokenReqMessageContext tokReqMsgCtx)
            throws IdentityOAuth2Exception {

        OAuth2AccessTokenRespDTO oAuth2AccessTokenRespDTO = super.issue(tokReqMsgCtx);
        tokReqMsgCtx.setScope(getReducedResponseScopes(tokReqMsgCtx.getScope()));
        return oAuth2AccessTokenRespDTO;
    }

    @Override
    public boolean validateScope(OAuthTokenReqMessageContext tokReqMsgCtx) throws IdentityOAuth2Exception {

        boolean validateScope = super.validateScope(tokReqMsgCtx);
        AbstractMTLSTokenBindingGrantHandler abstractMTLSTokenBindingGrantHandler =
                new AbstractMTLSTokenBindingGrantHandler();
        validateScope = abstractMTLSTokenBindingGrantHandler.validateScope(tokReqMsgCtx, validateScope);
        return validateScope;
    }

    /**
     * Remove the certificate thumbprint prefixed scope from the space delimited list of authorized scopes.
     *
     * @param scopes Authorized scopes of the token.
     * @return scopes by removing the custom scope.
     */
    private String[] getReducedResponseScopes(String[] scopes) {

        if (ArrayUtils.isNotEmpty(scopes) && scopes.length > 0) {
            List<String> scopesList = new LinkedList<>(Arrays.asList(scopes));
            if (scopes.length == 1) {
                scopesList.add(0, "default");
            }
            scopesList.removeIf(scope -> scope.startsWith(CommonConstants.CERT_THUMBPRINT + CommonConstants.SEPARATOR));
            return scopesList.toArray(new String[0]);
        }
        return scopes;
    }
}
