package org.project.api;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import org.project.entities.POSRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

    // ./mvnw compile quarkus:dev
@Path("/analyze-value")
@ApplicationScoped
public class POSRequestAPI {

    Logger logger = LoggerFactory.getLogger(POSRequestAPI.class);

    @GET
    public Uni<List<POSRequest>> get() {
        return POSRequest.listAll(Sort.by("original_string"));
    }

    @POST
    @Produces("application/json")
    @ReactiveTransactional
    public Uni<POSRequest> analyzeString(String jsonString) throws IOException {
        JSONObject originalJson = new JSONObject(jsonString);

        logger.info("This is what was received on the request: " + originalJson);

        JSONObject spacyJson = new JSONObject();
        spacyJson.put("text", originalJson.get("string"));
        spacyJson.put("model", "en");
        spacyJson.put("collapse_punctuation", 0);
        spacyJson.put("collapse_phrases", 1);

        logger.info("This is what will be requested from spaCy: " + spacyJson);

        URL url = new URL("http://spacy:8000/dep");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setDoOutput(true);
        con.setDoInput(true);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = spacyJson.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        JSONObject posResponse;

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            posResponse = new JSONObject(response.toString());
        }

        logger.info("This is what was received from spaCy: " + posResponse);

        POSRequest posRequest = new POSRequest();
        posRequest.setOriginalString(originalJson.get("string").toString());
        posRequest.setPosString(posResponse.toString());
        return POSRequest.persist(posRequest).replaceWith(posRequest);
    }
}