/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com) All Rights Reserved.
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

package org.wso2.carbon.identity.oauth2.token.handler.clientauth.mutualtls.introspection;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.oauth2.dto.OAuth2IntrospectionResponseDTO;
import org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationRequestDTO;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertNotNull;

/**
 * Test class for IntrospectionResponseInterceptorTest class.
 */
public class IntrospectionResponseInterceptorTest {

    IntrospectionResponseInterceptor introspectionResponseInterceptor;

    @BeforeTest
    public void setup() {

        introspectionResponseInterceptor = new IntrospectionResponseInterceptor();
    }

    @Test
    public void testGetIntrospectionData() {

        OAuth2IntrospectionResponseDTO oAuth2IntrospectionResponseDTO = new OAuth2IntrospectionResponseDTO();
        OAuth2IntrospectionResponseDTO oAuth2IntrospectionResponseDTO2 = new OAuth2IntrospectionResponseDTO();
        OAuth2IntrospectionResponseDTO oAuth2IntrospectionResponseDTO3 = new OAuth2IntrospectionResponseDTO();
        OAuth2TokenValidationRequestDTO oAuth2TokenValidationRequestDTO = new OAuth2TokenValidationRequestDTO();

        Map<String, Object> introspectionData = new HashMap<String, Object>();
        introspectionData.put("nbf", 1585749816);
        introspectionData.put("active", true);
        introspectionData.put("iss", "https://server.example.com");
        introspectionData.put("exp", 1585753416);
        introspectionData.put("sub", "ty.webb@example.com");
        introspectionData.put("cnf", new String[]{"x5t#S256", "bwcK0esc3ACC3DB2Y5_lESsXE8o9ltc05O89jdN-dg2"});

        oAuth2IntrospectionResponseDTO.setScope("openid");
        oAuth2IntrospectionResponseDTO.setProperties(introspectionData);
        oAuth2IntrospectionResponseDTO3.setScope("x5t#S256:bwcK0esc3ACC3DB2Y5_lESsXE8o9ltc05O89jdN-dg2");

        introspectionResponseInterceptor.onPostTokenValidation(oAuth2TokenValidationRequestDTO,
                oAuth2IntrospectionResponseDTO, introspectionData);
        assertNotNull(oAuth2IntrospectionResponseDTO.getProperties());

        introspectionResponseInterceptor.onPostTokenValidation(oAuth2TokenValidationRequestDTO,
                oAuth2IntrospectionResponseDTO2, introspectionData);
        assertNotNull(oAuth2IntrospectionResponseDTO2.getProperties());

        introspectionResponseInterceptor.onPostTokenValidation(oAuth2TokenValidationRequestDTO,
                oAuth2IntrospectionResponseDTO3, introspectionData);
        assertNotNull(oAuth2IntrospectionResponseDTO3.getProperties());
    }
}
