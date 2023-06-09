package com.samuelbatara.proxy;

import com.samuelbatara.http.JsonRpcHttpRequestHandler;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicHttpInvocationHandler extends AbstractHttpInvocationHandler implements InvocationHandler {
  private final JsonRpcHttpRequestHandler httpRequestHandler;

  public DynamicHttpInvocationHandler(JsonRpcHttpRequestHandler httpRequestHandler) {
    this.httpRequestHandler = httpRequestHandler;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    // generate body request
    String request = generateRequestBody(method.getName(), args);

    // get output
    String stringResult;
    try {
      stringResult = httpRequestHandler.handleRequest(request);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // convert output to json
    Object objectResult;
    try {
      objectResult = gson.fromJson(stringResult, method.getReturnType());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return objectResult;
  }
}
