package com.demo.http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonRpcHttpRequestHandler {
  private final String url;
  private final int defaultConnectTimeOut = 1000; // 1 second
  private final int defaultReadTimeout = 1000; // 1 second
  private final String method = "POST";
  private HttpURLConnection connection;

  public JsonRpcHttpRequestHandler(String url) {
    this.url = url;
    try {
      connection = (HttpURLConnection) new URL(url).openConnection();
      connection.setRequestMethod(method);
      connection.setDoOutput(true);
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setConnectTimeout(defaultConnectTimeOut);
      connection.setReadTimeout(defaultReadTimeout);
    } catch (Exception e) {
      System.out.println("failed to open connection to " + url);
    }
  }

  public String handleRequest(byte[] body) throws IOException {
    // additional connection settings
    connection.setRequestProperty("Content-Length", Integer.toString(body.length));

    // send request
    OutputStream outputStream = connection.getOutputStream();
    outputStream.write(body);
    outputStream.flush();
    outputStream.close();

    // read response
    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String line;
    StringBuilder builder = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      builder.append(line);
    }
    reader.close();

    return builder.toString();
  }

  public void setConnectTimeout(int connectTimeout) {
    if (connectTimeout <= 0) {
      throw new RuntimeException("Connect time out cannot be negative");
    }
//    connection.setConnectTimeout(connectTimeout);
  }

  public void setReadTimeout(int readTimeout) {
   if (readTimeout <= 0) {
     throw new RuntimeException("Read time out cannot be negative");
   }
//   connection.setReadTimeout(readTimeout);
  }
}
