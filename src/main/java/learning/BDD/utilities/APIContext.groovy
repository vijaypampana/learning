package learning.BDD.utilities

import io.restassured.authentication.BasicAuthScheme
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.JsonConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.config.SSLConfig
import io.restassured.path.json.config.JsonPathConfig
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSpecification

class APIContext {

    private RequestSpecBuilder requestSpecBuilder = null
    private RequestSpecification requestSpecification = null
    private Response response = null
    private ValidatableResponse validatableResponse = null
    private BasicAuthScheme basicAuthScheme = null

    private Map<String, String> headers = new HashMap<>()
    private Map<String, String> params = new HashMap<>()
    private Map<String, String> formParams = new HashMap<>()

    private String sHost = "", sBasePath = "", sContentType = "", body = ""

    RequestSpecBuilder getRequestSpecBuilder() {
        return requestSpecBuilder
    }

    void setRequestSpecBuilder(RequestSpecBuilder requestSpecBuilder) {
        this.requestSpecBuilder = requestSpecBuilder
    }

    RequestSpecification getRequestSpecification() {
        requestSpecBuilder = new RequestSpecBuilder()
        requestSpecBuilder.setBaseUri(sHost)
        requestSpecBuilder.setBasePath(sBasePath)

        if(headers.size() > 0)
            requestSpecBuilder.addHeaders(headers)

        if(params.size() > 0)
            requestSpecBuilder.addParams(params)

        if(formParams.size() > 0)
            requestSpecBuilder.addFormParam(formParams)

        if(!body.isEmpty())
            requestSpecBuilder.setBody(body)

        if(sContentType.isEmpty())
            requestSpecBuilder.setContentType(sContentType)

        //Ignore SSL Certificate Validation
        requestSpecBuilder.setConfig(RestAssuredConfig.config().sslConfig(SSLConfig.sslConfig().allowAllHostnames().relaxedHTTPSValidation()))
        // JSON - Converts numbers with decimal to Double
        requestSpecBuilder.setConfig(RestAssuredConfig.config().jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE)))
        // set URL encoding as false to avoid URL encoding
        requestSpecBuilder.setUrlEncodingEnabled(false)

        requestSpecification = requestSpecBuilder.build()
        return requestSpecification
    }

    void setRequestSpecification(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification
    }

    Response getResponse() {
        return response
    }

    void setResponse(Response response) {
        this.response = response
    }

    ValidatableResponse getValidatableResponse() {
        return validatableResponse
    }

    void setValidatableResponse(ValidatableResponse validatableResponse) {
        this.validatableResponse = validatableResponse
    }

    Map<String, String> getHeaders() {
        return headers
    }

    void setHeaders(Map<String, String> headers) {
        this.headers = headers
    }

    void clearHeaders() {
        this.headers.clear()
    }

    Map<String, String> getParams() {
        return params
    }

    void setParams(Map<String, String> params) {
        this.params = params
    }

    void clearQueryParams() {
        this.params.clear()
    }

    Map<String, String> getFormParams() {
        return formParams
    }

    void setFormParams(Map<String, String> formParams) {
        this.formParams = formParams
    }

    void clearFormParams() {
        this.formParams.clear()
    }

    String getsHost() {
        return sHost
    }

    void setsHost(String sHost) {
        this.sHost = sHost
    }

    String getsBasePath() {
        return sBasePath
    }

    void setsBasePath(String sBasePath) {
        this.sBasePath = sBasePath
    }

    String getsContentType() {
        return sContentType
    }

    void setsContentType(String sContentType) {
        this.sContentType = sContentType
    }

    void clearContentType() {
        this.sContentType = ""
    }

    String getBody() {
        return body
    }

    void setBody(String body) {
        this.body = body
    }

    void clearBody() {
        this.body ""
    }

    BasicAuthScheme getBasicAuthScheme() {
        return basicAuthScheme
    }

    void setBasicAuthScheme(String userName, String password) {
        this.basicAuthScheme.setUserName(userName)
        this.basicAuthScheme.setPassword(password)
    }
}