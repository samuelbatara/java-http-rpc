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

import java.util.Random;

public class DynamicRpcHttpProxyTest {

  private DynamicRpcHttpProxy<SimpleService> dynamicRpcHttpProxy;
  SimpleService service;
  SimpleServiceImpl serviceImpl;
  private final String URL = "http://localhost:9000/simple-service";

  @BeforeEach
  public void setUp() {
    JsonRpcHttpRequestHandler httpRequestHandler = new JsonRpcHttpRequestHandler(URL);
    DynamicHttpInvocationHandler httpInvocationHandler = new DynamicHttpInvocationHandler(
        httpRequestHandler
    );
    dynamicRpcHttpProxy = new DynamicRpcHttpProxy(SimpleServiceImpl.class, httpInvocationHandler);
    service = dynamicRpcHttpProxy.getProxy();
    Assertions.assertNotNull(service);

    serviceImpl = new SimpleServiceImpl();
  }

  @Test
  public void testProxyHandleLong() {
    long start = System.currentTimeMillis();
    long response = service.handleLong(123);
    long end = System.currentTimeMillis();
    long expected = serviceImpl.handleLong(123);

    System.out.println("Done in " + (end-start) + " ms");
    Assertions.assertEquals(expected, response);
  }

  @Test
  public void testProxyHandleObject() {
    final int size = 100;
    String[] topics = new String[size];
    for (int i = 1; i <= size; i++) {
      topics[i-1] = "Topic #" + i;
    }
    Request request = new Request(topics);

    long start = System.currentTimeMillis();
    Response response = service.handleObject(request);
    long end = System.currentTimeMillis();

    Response expected = serviceImpl.handleObject(request);

    Assertions.assertNotNull(response);
    Assertions.assertTrue(response.getTexts().length > 0);
    Assertions.assertEquals(size, response.getTexts().length);
    Assertions.assertEquals(expected, response);
    System.out.println("\t[x] Done in " + (end-start) + " ms");
  }

  @Test
  public void testProxyCalculate() {
    final int size = 100;
    final int x = 139;
    int[] array = new int[size];
    Random random = new Random();
    for (int i = 1; i <= size; i++) {
      array[i-1] = random.nextInt(2 * i);
    }

    long start = System.currentTimeMillis();
    long result = service.calculate(x, array);
    long end = System.currentTimeMillis();
    long expected = serviceImpl.calculate(x, array);
    Assertions.assertEquals(expected, result);
    System.out.println("\t[x] Done in " + (end-start) + " ms");
  }
}
