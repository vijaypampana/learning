package learning.BDD.utilities.api

import cucumber.api.DataTable
import cucumber.api.Transform
import cucumber.api.java.en.Given
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.slurpersupport.GPathResult
import groovy.xml.XmlUtil
import io.restassured.RestAssured
import io.restassured.response.Response
import learning.BDD.utilities.Context
import learning.BDD.utilities.util.DataUtil
import learning.BDD.utilities.transformer.TransformTextUsingYAML
import org.xml.sax.SAXException

import javax.xml.parsers.ParserConfigurationException
import java.nio.file.Files
import java.nio.file.Paths

class CommonSteps {

    @Given("^I set API Server as \"(.*)\"\$")
    void setBaseURL(String sBaseUrl) {
        Context.getInstance().setsHost(sBaseUrl)
    }

    @Given("^I set API Base Path as \"(.*)\"\$")
    void setBasePath(String sBasePath) {
        Context.getInstance().setsBasePath(sBasePath)
    }

    @Given("^I set API Basic Authentication using \"(.*)\" and \"(.*)\"\$")
    void setBasicAuth(String sUserName, String sPassword) {
        Context.getInstance().setBasicAuthScheme(sUserName, sPassword)
    }

    @Given("^I Log API Response Time\$")
    void getAPIResponseTime() {
        Context.getInstance().getReports().stepInfo("Response Time : " + Context.getInstance().getResponse().getTime())
    }

    @Given("^I verify API response status code is (\\d+)\$")
    void verifyStatusCode(int iStatus) {
        Context.getInstance().setValidatableResponse(Context.getInstance().getResponse().then().statusCode(iStatus))
    }

    @Given("^I store API response as \"(.*)\"\$")
    void storeAPIResponse(String storeKey) {
        DataUtil.store(storeKey, Context.getInstance().getValidatableResponse())
    }

    @Given("^I make API GET call using URL \"(.*)\"\$")
    void getCall(@Transform(TransformTextUsingYAML.class) String sURL) {

        Response resp = RestAssured.given().relaxedHTTPSValidation().spec(Context.getInstance().getRequestSpecification()).when().get(sURL)
        Context.getInstance().getReports().stepInfo(sURL)
        Context.getInstance().getReports().stepCode(resp.asString())
        Context.getInstance().setResponse(resp)
        resetSpec()
    }

    @Given("^I make API DELETE call using URL \"(.*)\"\$")
    void deleteCall(@Transform(TransformTextUsingYAML.class) String sURL) {

        Response resp = RestAssured.given().relaxedHTTPSValidation().spec(Context.getInstance().getRequestSpecification()).when().delete(sURL)
        Context.getInstance().getReports().stepInfo(sURL)
        Context.getInstance().getReports().stepCode(resp.asString())
        Context.getInstance().setResponse(resp)
        resetSpec()
    }

    @Given("^I make API PUT call using URL \"(.*)\"\$")
    void putCall(@Transform(TransformTextUsingYAML.class) String sURL) {

        Response resp = RestAssured.given().relaxedHTTPSValidation().spec(Context.getInstance().getRequestSpecification()).when().put(sURL)
        Context.getInstance().getReports().stepInfo(sURL)
        Context.getInstance().getReports().stepCode(resp.asString())
        Context.getInstance().setResponse(resp)
        resetSpec()
    }

    @Given("^I make API POST call using URL \"(.*)\"\$")
    void postCall(@Transform(TransformTextUsingYAML.class) String sURL) {

        Response resp = RestAssured.given().relaxedHTTPSValidation().spec(Context.getInstance().getRequestSpecification()).when().post(sURL)
        Context.getInstance().getReports().stepInfo(sURL)
        Context.getInstance().getReports().stepCode(resp.asString())
        Context.getInstance().setResponse(resp)
        resetSpec()
    }

    @Given("^I make API POST call using URL \"(.*)\" and below table\$")
    void postCall(@Transform(TransformTextUsingYAML.class) String sURL, DataTable table) {
        buildRequest(table)
        postCall(sURL)
    }

    void buildRequest(DataTable table) {

        String sValAt00 = table.topCells().get(0)
        String sValAt01 = table.topCells().get(1)
        String sContentType = ""
        Object oBody = null

        if(sValAt00.equalsIgnoreCase("_REFFILE")) {

            File filePath = new File(getClass().getClassLoader().getResource("api/" + sValAt01).getFile())
            String resource = getResourceAsString(filePath)

            if(sValAt01.toLowerCase().endsWith(".json")) {
                sContentType = "application/json"
                oBody = getModifiedJSON(resource, table)
            } else if (sValAt01.toLowerCase().endsWith(".xml")) {
                sContentType = "text/xml;charset=UTF-8"
                oBody = getModifiedXML(resource, table)
            } else {
               Context.getInstance().getReports().stepError("_REFFILE is neither a json or xml file")
            }
        } else {

            Map<String, Object> mapReturn = new HashMap<>()
            table.raw().each { row ->
                String sKey = row.get(0)
                String sValue = row.get(1)

                if(row.size()==3) {
                    mapReturn.put(sKey, JSONSteps.getJsonType(sValue, row.get(2)))
                } else {
                    switch (JSONSteps.getJsonNodeType(sValue)) {
                        case NULL:
                            mapReturn.put(sKey, null)
                            break
                        case BOOLEAN:
                            mapReturn.put(sKey, Boolean.valueOf(sValue))
                            break
                        case NUMBER:
                            try {
                                mapReturn.put(sKey, Long.valueOf(sValue))
                            } catch(NumberFormatException e) {
                                mapReturn.put(sKey, Double.valueOf(sValue))
                            }
                            break
                        case STRING:
                            mapReturn.put(sKey, String.valueOf(sValue))
                            break

                    }
                }
            }

            sContentType = "application/json"
            oBody = mapReturn

        }

        Context.getInstance().setsContentType(sContentType)
        Context.getInstance().setBody(oBody)

    }

    String getResourceAsString(File filePath) {
        String sReturn = ""
        try {
            sReturn = new String(Files.readAllBytes(Paths.get(filePath.getPath())))
        } catch (Exception e) {
            Context.getInstance().getReports().stepFail(e.getMessage())
        }
        return sReturn
    }

    //Replace value in json files
    private String getModifiedJSON(String resource, DataTable table) {
        if(table.raw().size()==1) {
            return resource
        } else {
            boolean skip = true
            JsonBuilder builder = new JsonBuilder(new JsonSlurper().parseText(resource))

            table.raw().each { row ->
                if(skip) {
                    skip = false
                } else {
                    String query = row.get(0)
                    String value = Context.getInstance().getData(row.get(1))
                    if(row.size()==3) {
                        Eval.xy(builder, JSONSteps.getJsonType(value, row.get(2)), "x.content." + query + " = y")
                    } else {
                        Eval.xy(builder, value, "x.content." + query + " = y")
                    }
                }
            }
            Context.getInstance().getReports().stepCode(builder.toString())
            return builder.toString()
        }
    }

    //Replace value in XML files
    private String getModifiedXML(String resource, DataTable table) {
        if(table.raw().size()==1) {
            return resource
        } else {

            boolean skip = true
            GPathResult builder = null
            try {

                builder = new XmlSlurper().parseText(resource)
                table.raw().each { row ->
                    String query = Context.getInstance().getData(row.get(0))
                    String value = Context.getInstance().getData(row.get(1))
                    if(skip) {
                        skip = false
                    } else {
                        Eval.x(builder, "x" + query.substring(query.indexOf(".")) + " = '" + value + "'")
                    }
                }
            } catch (IOException | SAXException | ParserConfigurationException e) {
                Context.getInstance().getReports().stepFail(e.getMessage())
            }

            Context.getInstance().getReports().stepCode(XmlUtil.serialize(builder).toString())

            return org.apache.commons.text.StringEscapeUtils.unescapeHtml4(XmlUtil.serialize(builder))

        }
    }

    void resetSpec() {
        Context.getInstance().clearContentType()
        Context.getInstance().clearBody()
        Context.getInstance().clearQueryParams()
        Context.getInstance().clearFormParams()
        Context.getInstance().clearHeaders()
        Context.getInstance().setBasicAuthScheme(null, null)
    }

}
