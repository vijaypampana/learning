package interview;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FileUtils {

    //JSON Node example
    @Test
    void readJsonFile() throws IOException {
        File f1 = new File("src/test/java/interview/files/sample.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        JsonNode jsonNode = (JsonNode) mapper.readValue(f1, JsonNode.class);
        System.out.println(jsonNode.get("result").get("experiences"));
        mapper.writeValue(new File("src/test/java/interview/files/outputJson.json"), jsonNode.get("result").get("experiences"));
    }

    //JsonSluper
    @Test
    void readJsonSluper() {
        JsonBuilder builder = new JsonBuilder(new JsonSlurper().parse(new File("src/test/java/interview/files/sample.json")));
        JsonSlurper jsonSlurper = new JsonSlurper();
        Object object = jsonSlurper.parseText(builder.toPrettyString());
        System.out.println(builder.toPrettyString());
    }

    @Test
    void jsonJsonObject() {
        Gson gson = new Gson();
        gson.toJson(new File("src/test/java/interview/files/sample.json"));
        System.out.println();
    }

    @Test
    Integer getnThMinInt(List<Integer> i, Integer position) {
        //Collections.sort(i);
        Collections.sort(i, Collections.reverseOrder());
        return i.get(position -1);
    }

}
