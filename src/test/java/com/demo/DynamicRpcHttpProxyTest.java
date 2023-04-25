package com.demo;

import com.demo.http.JsonRpcHttpRequestHandler;
import com.demo.model.Request;
import com.demo.model.Response;
import com.demo.proxy.DynamicHttpInvocationHandler;
import com.demo.proxy.DynamicRpcHttpProxy;
import com.demo.service.SimpleService;
import com.demo.service.SimpleServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DynamicRpcHttpProxyTest {

  private DynamicRpcHttpProxy<SimpleService> dynamicRpcHttpProxy;
  SimpleService service;
  private final String URL = "http://localhost:9000/simple-service";

  @BeforeEach
  public void setUp() {
    JsonRpcHttpRequestHandler httpRequestHandler = new JsonRpcHttpRequestHandler(URL);
    DynamicHttpInvocationHandler httpInvocationHandler = new DynamicHttpInvocationHandler(
        httpRequestHandler, new ObjectMapper()
    );
    dynamicRpcHttpProxy = new DynamicRpcHttpProxy(SimpleServiceImpl.class, httpInvocationHandler);
    service = dynamicRpcHttpProxy.getProxy();
    Assertions.assertNotNull(service);
  }

  @Test
  public void testProxyHandleLong() {
    long start = System.currentTimeMillis();
    long response = service.handleLong(123);
    long end = System.currentTimeMillis();
    System.out.println("Done in " + (end-start) + " ms");
    System.out.println("Response: " + response);
  }

  @Test
  public void testProxyHandleObject() {
    String[] topics = new String[]{"Hello", "Hello"};
    Request request = new Request(topics);
    long start = System.currentTimeMillis();
    Response response = service.handleObject(request);
    long end = System.currentTimeMillis();
    System.out.println("Done in " + (end-start) + " ms");
    System.out.println("Response: " + response);
  }
}
