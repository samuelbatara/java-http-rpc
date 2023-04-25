package com.demo;

import com.demo.model.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JsonRpcHttpServletTest {
  private static String URL = "http://localhost:9000/simple-service";
  private HttpURLConnection connection;
  private JSONParser jsonParser;
  @BeforeEach
  public void setUp() throws IOException {
    connection = (HttpURLConnection) new URL(URL).openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setDoOutput(true);
    connection.setReadTimeout(500);

    jsonParser = new JSONParser();
  }

  @Test
  public void testHandleLong() throws IOException, ParseException {
    // set request body
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    builder.append("\"jsonrpc\": \"2.0\", ");
    builder.append("\"id\": 123456789, ");
    builder.append("\"method\": \"handleLong\", ");
    builder.append("\"params\": [123]");
    builder.append("}");
    JSONObject object = (JSONObject) jsonParser.parse(builder.toString());
    byte[] postData = object.toString().getBytes(StandardCharsets.UTF_8);

    // start time
    long startTime = System.currentTimeMillis();

    // additional connection settings
    connection.setRequestProperty("Content-Length", Integer.toString(postData.length));

    // send request
    OutputStream outputStream = connection.getOutputStream();
    outputStream.write(postData);
    outputStream.flush();
    outputStream.close();

    // read response
    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String line;
    builder = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      builder.append(line);
    }
    reader.close();

    // end time
    long endTime = System.currentTimeMillis();

    // print response
    System.out.println("Done in " + (endTime - startTime) + "ms");
    System.out.println("Response: " + builder);
  }
}
