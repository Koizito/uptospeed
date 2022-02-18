package org.firststeps.tests;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class POSRequestAPITest {

    @Test
    public void testPostEndpoint() {
        given()
                .body("{\"string\":\"My name is Daniel. I believe the reptilians are out to get us.\",\"port\":\"8080\",\"host\":\"localhost\"}")
          .when().post("/analyze-value")
          .then()
             .statusCode(200)
             .assertThat()
                .body("originalString", is(equalTo("My name is Daniel. I believe the reptilians are out to get us.")))
                .body("posString", is(equalTo("{\"words\":[{\"tag\":\"NN\",\"text\":\"My name\"},{\"tag\":\"VBZ\",\"text\":\"is\"},{\"tag\":\"NNP\",\"text\":\"Daniel\"},{\"tag\":\".\",\"text\":\".\"},{\"tag\":\"PRP\",\"text\":\"I\"},{\"tag\":\"VBP\",\"text\":\"believe\"},{\"tag\":\"NNS\",\"text\":\"the reptilians\"},{\"tag\":\"VBP\",\"text\":\"are\"},{\"tag\":\"RB\",\"text\":\"out\"},{\"tag\":\"TO\",\"text\":\"to\"},{\"tag\":\"VB\",\"text\":\"get\"},{\"tag\":\"PRP\",\"text\":\"us\"},{\"tag\":\".\",\"text\":\".\"}],\"arcs\":[{\"start\":0,\"end\":1,\"label\":\"nsubj\",\"text\":\"My name\",\"dir\":\"left\"},{\"start\":1,\"end\":2,\"label\":\"attr\",\"text\":\"Daniel\",\"dir\":\"right\"},{\"start\":1,\"end\":3,\"label\":\"punct\",\"text\":\".\",\"dir\":\"right\"},{\"start\":4,\"end\":5,\"label\":\"nsubj\",\"text\":\"I\",\"dir\":\"left\"},{\"start\":6,\"end\":7,\"label\":\"nsubj\",\"text\":\"the reptilians\",\"dir\":\"left\"},{\"start\":5,\"end\":7,\"label\":\"ccomp\",\"text\":\"are\",\"dir\":\"right\"},{\"start\":7,\"end\":8,\"label\":\"advmod\",\"text\":\"out\",\"dir\":\"right\"},{\"start\":9,\"end\":10,\"label\":\"aux\",\"text\":\"to\",\"dir\":\"left\"},{\"start\":7,\"end\":10,\"label\":\"advcl\",\"text\":\"get\",\"dir\":\"right\"},{\"start\":10,\"end\":11,\"label\":\"dobj\",\"text\":\"us\",\"dir\":\"right\"},{\"start\":5,\"end\":12,\"label\":\"punct\",\"text\":\".\",\"dir\":\"right\"}]}")));
    }

}