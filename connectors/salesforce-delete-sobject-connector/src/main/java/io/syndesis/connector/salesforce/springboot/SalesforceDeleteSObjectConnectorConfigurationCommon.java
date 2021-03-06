package io.syndesis.connector.salesforce.springboot;

import javax.annotation.Generated;

/**
 * Delete Salesforce SObject
 * 
 * Generated by camel-package-maven-plugin - do not edit this file!
 */
@Generated("org.apache.camel.maven.connector.SpringBootAutoConfigurationMojo")
public class SalesforceDeleteSObjectConnectorConfigurationCommon {

    /**
     * URL of the Salesforce instance used for authentication by default set to
     * https://login.salesforce.com
     */
    private String loginUrl = "https://login.salesforce.com";
    /**
     * OAuth Consumer Key of the connected app configured in the Salesforce
     * instance setup. Typically a connected app needs to be configured but one
     * can be provided by installing a package.
     */
    private String clientId;
    /**
     * OAuth Consumer Secret of the connected app configured in the Salesforce
     * instance setup.
     */
    private String clientSecret;
    /**
     * Refresh token already obtained in the refresh token OAuth flow. One needs
     * to setup a web application and configure a callback URL to receive the
     * refresh token or configure using the builtin callback at
     * https://login.salesforce.com/services/oauth2/success or
     * https://test.salesforce.com/services/oauth2/success and then retrive the
     * refresh_token from the URL at the end of the flow. Note that in
     * development organizations Salesforce allows hosting the callback web
     * application at localhost.
     */
    private String refreshToken;
    /**
     * Username used in OAuth flow to gain access to access token. It's easy to
     * get started with password OAuth flow but in general one should avoid it
     * as it is deemed less secure than other flows.
     */
    private String userName;
    /**
     * Password used in OAuth flow to gain access to access token. It's easy to
     * get started with password OAuth flow but in general one should avoid it
     * as it is deemed less secure than other flows. Make sure that you append
     * security token to the end of the password if using one.
     */
    private String password;
    /**
     * SObject name if required or supported by API
     */
    private String sObjectName = "Contact";

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSObjectName() {
        return sObjectName;
    }

    public void setSObjectName(String sObjectName) {
        this.sObjectName = sObjectName;
    }
}