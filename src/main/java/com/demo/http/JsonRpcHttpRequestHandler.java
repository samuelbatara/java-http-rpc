package com.demo.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JsonRpcHttpRequestHandler {
  private final String url;
  private final int defaultConnectTimeOut = 5000; // 1 second
  private final int defaultReadTimeout = 2000; // 1 second
  private final String method = "POST";

  public JsonRpcHttpRequestHandler(String url) {
    this.url = url;
  }

  public String handleRequest(String request) throws IOException {
    // additional connection settings
    byte[] body = request.getBytes(StandardCharsets.UTF_8);
    HttpURLConnection connection = null;
    try {
      connection = (HttpURLConnection) new URL(url).openConnection();
      connection.setRequestMethod(method);
      connection.setDoOutput(true);
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setConnectTimeout(defaultConnectTimeOut);
      connection.setReadTimeout(defaultReadTimeout);
      connection.setRequestProperty("Content-Length", Integer.toString(body.length));
    } catch (Exception e) {
      System.out.println("failed to open connection to " + url);
    }

    // send request
    OutputStream outputStream = connection.getOutputStream();
    outputStream.write(body);
    outputStream.flush();
    outputStream.close();

    // read response
    String result;
    InputStream inputStream = null;
    ByteArrayOutputStream byteArrayOutputStream = null;
    try {
      inputStream = connection.getInputStream();
      byteArrayOutputStream = new ByteArrayOutputStream();
      final int size = 32 * 1024;
      byte[] buffer = new byte[size];
      int byteReads;
      while ((byteReads = inputStream.read(buffer)) != -1) {
        byteArrayOutputStream.write(buffer, 0, byteReads);
      }
    } finally {
      result = byteArrayOutputStream.toString("UTF-8");
      inputStream.close();
      byteArrayOutputStream.close();
    }

    return result;
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
