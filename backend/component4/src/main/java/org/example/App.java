package org.example;

import org.apache.commons.cli.*;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


//java -jar target/component4-1.0-SNAPSHOT-jar-with-dependencies.jar -h localhost -p 8080 -s "My name is Daniel. I believe the reptilians are out to get us."
public class App 
{
    public static void main( String[] args ) throws IOException, ParseException {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("s", "string", true, "String to analyze");
        options.addOption("p", "port", true, "Port to use");
        options.addOption("h", "host", true, "Host to use");

        CommandLine commandLine = parser.parse(options, args);
        String host = commandLine.getOptionValue("h");
        String port = commandLine.getOptionValue("p");
        String value = commandLine.getOptionValue("s");

        JSONObject json = new JSONObject();

        json.put("host", host);
        json.put("port", port);
        json.put("string", value);

        URL url = new URL("http://" + host + ":" + port + "/analyze-value");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        con.setDoInput(true);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        JSONObject jsonResponse;

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            jsonResponse = new JSONObject(response.toString());
        }

        System.out.println(jsonResponse);

    }
}
