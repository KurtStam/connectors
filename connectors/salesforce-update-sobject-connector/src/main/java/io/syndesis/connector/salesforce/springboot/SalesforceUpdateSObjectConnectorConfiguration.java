package io.syndesis.connector.salesforce.springboot;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Generated("org.apache.camel.maven.connector.SpringBootAutoConfigurationMojo")
@ConfigurationProperties(prefix = "salesforce-update-sobject")
public class SalesforceUpdateSObjectConnectorConfiguration
        extends
            SalesforceUpdateSObjectConnectorConfigurationCommon {

    /**
     * Define additional configuration definitions
     */
    private Map<String, SalesforceUpdateSObjectConnectorConfigurationCommon> configurations = new HashMap<>();

    public Map<String, SalesforceUpdateSObjectConnectorConfigurationCommon> getConfigurations() {
        return configurations;
    }
}