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
      System.out.println("\t[x] String result '" + stringResult + "'");
      System.out.println("\t[x] will converted to " + method.getReturnType());
      objectResult = gson.fromJson(stringResult, method.getReturnType());
      System.out.println("\t[x] Object result '" + objectResult.toString() + "'");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return objectResult;
  }
}
