package com.demo.proxy;

import com.demo.http.JsonRpcHttpRequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class DynamicHttpInvocationHandler implements InvocationHandler {
  private final JsonRpcHttpRequestHandler httpRequestHandler;
  private final ObjectMapper mapper;

  public DynamicHttpInvocationHandler(JsonRpcHttpRequestHandler httpRequestHandler, ObjectMapper mapper) {
    this.httpRequestHandler = httpRequestHandler;
    this.mapper = mapper;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    // generate body request
    String request = generateRequestBody(method.getName(), args);
    JSONParser parser = new JSONParser();
    JSONObject object = (JSONObject) parser.parse(request);
    byte[] body = object.toString().getBytes(StandardCharsets.UTF_8);

    String stringResult = null;
    try {
      stringResult = httpRequestHandler.handleRequest(body);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Object objectResult = null;
    try {
      objectResult = mapper.readValue(stringResult, method.getReturnType());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return objectResult;
  }

  private String generateRequestBody(String methodName, Object[] args) throws JsonProcessingException {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    builder.append("\"jsonrpc\": \"2.0\", ");
    builder.append("\"id\": 123456789, ");
    builder.append("\"method\": \"" + methodName + "\", ");
    builder.append("\"params\": ");
    builder.append(mapper.writer().writeValueAsString(args));
    builder.append("}");

    return builder.toString();
  }
}
