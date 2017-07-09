package uk.ac.ebi.jimmy.ebiprogrammingtaskWeb.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.jimmy.ebiprogrammingtaskWeb.logic.Processor;

public class EBIProgrammingTaskWebTest {

    private final RestTemplate restTemplate = new TestRestTemplate();
    //Required to Generate JSON content from Java objects
    public static final ObjectMapper objectMapper = new ObjectMapper();
    
    Processor processorObj = new Processor();

    @Test
    public void testFilter() {

        // Should only allow valid L..LN..N patterns without strange symbols
        String validStr = "ABCDEF123456";
        assertThat(processorObj.filter(validStr), is("ABCDEF123456"));

        // Ignore when formatting errors are present at the input
        String invalidStr = "ABCDEF1255AD666";
        assertThat(processorObj.filter(invalidStr), is(""));

        // Further test for errors present at the input
        invalidStr = "ABCD*EF%ZZ155";
        assertThat(processorObj.filter(invalidStr), is(""));
    }

    @Test
    public void testSeparator() {

        // Should only allow valid L..LN..N patterns without strange symbols
        String validStr = "ABCDEF123456";
        String[] resultArray = {"ABCDEF", "123456"};
        assertArrayEquals(processorObj.lettersDigitSeparator(validStr).toArray(), resultArray);

    }

    @Test
    public void testEndpoint() throws JsonProcessingException {

        String testInputString = "ERR000111, AB!C458, ERR000112, ERR000113, ERR000115, ERR000116, ERR100114";
        String expectedResultStr = "ERR000111-ERR000113, ERR000115-ERR000116, ERR100114";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("message", testInputString);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        objectMapper.findAndRegisterModules();
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange("http://localhost:8080" + "/EBIProgrammingTaskWebEndpoint", HttpMethod.POST, entity, Map.class, Collections.EMPTY_MAP);
         
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getBody().get("message"),expectedResultStr);
    }
}
