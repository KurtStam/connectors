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
package io.syndesis.connector.sql;

import java.util.Map;
import java.util.Properties;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.component.connector.DefaultConnectorComponent;

import io.syndesis.connector.sql.stored.JSONBeanUtil;

/**
 * Camel SqlConnector connector
 */
public class SqlConnectorComponent extends DefaultConnectorComponent {

    public SqlConnectorComponent() {
        super("sql-connector", SqlConnectorComponent.class.getName());
        registerExtension(SqlConnectorVerifierExtension::new);
//        registerExtension(SqlStoredConnectorMetaDataExtension::new);
        System.out.println("constructed");
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        // TODO Auto-generated method stub
        return super.createEndpoint(uri, remaining, parameters);
    }
    @Override
    public Processor getBeforeConsumer() {
        // TODO Auto-generated method stub
        return super.getBeforeConsumer();
    }

    @Override
    public Processor getBeforeProducer() {

        final Processor processor = exchange -> {
            final String body = (String) exchange.getIn().getBody();
            final Properties properties = JSONBeanUtil.parsePropertiesFromJSONBean(body);
            exchange.getIn().setBody(properties);
        };
        return processor;
    }

    @Override
    public Processor getAfterProducer() {
        final Processor processor = exchange -> {
            @SuppressWarnings("unchecked")
            Map<String,Object> map = (Map<String,Object>) exchange.getIn().getBody();
            String jsonBean = JSONBeanUtil.mapToJSONBean(map);
            exchange.getIn().setBody(jsonBean);
        };
        return processor;
    }

}
