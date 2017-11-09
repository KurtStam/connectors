/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.syndesis.connector.sql.stored;

import java.util.Map;

import org.apache.camel.component.extension.verifier.DefaultComponentVerifierExtension;

import io.syndesis.connector.sql.SqlVerifier;

public class SqlStoredStartConnectorVerifierExtension extends DefaultComponentVerifierExtension {

    private SqlVerifier sqlVerifierExtension;
    
    public SqlStoredStartConnectorVerifierExtension() {
        super("sql-stored-connector");
        sqlVerifierExtension = new SqlVerifier();
    }

    public SqlStoredStartConnectorVerifierExtension(String scheme) {
        super(scheme);
        sqlVerifierExtension = new SqlVerifier();
    }

    // *********************************
    // Parameters validation
    // *********************************

    @Override
    protected Result verifyParameters(Map<String, Object> parameters) {
        return sqlVerifierExtension.verifyParameters(parameters);
    }

    // *********************************
    // Connectivity validation
    // *********************************
    @Override
    protected Result verifyConnectivity(Map<String, Object> parameters) {
        return sqlVerifierExtension.verifyConnectivity(parameters);
    }
}
