package learning.Other.yaml

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.testng.annotations.Test

class ReadYaml {

    @Test
    void readyaml() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)

        def yamlObject = mapper.readValue(new File("/src/main/java/learning/Other/yaml/firstYaml.yml"), Object.class)
    }
}
