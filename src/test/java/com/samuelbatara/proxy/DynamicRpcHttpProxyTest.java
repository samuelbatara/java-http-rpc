package com.samuelbatara.proxy;

import com.samuelbatara.http.JsonRpcHttpRequestHandler;
import com.samuelbatara.service.SimpleService;
import com.samuelbatara.service.SimpleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DynamicRpcHttpProxyTest {

	private SimpleService simpleService;

	@BeforeEach
	public void setUp() {
		JsonRpcHttpRequestHandler httpRequestHandler = new JsonRpcHttpRequestHandler(
				"http://localhost:9000/"
		);
		DynamicHttpInvocationHandler httpInvocationHandler = new DynamicHttpInvocationHandler(
				httpRequestHandler
		);
		DynamicRpcHttpProxy<SimpleService> httpProxy = new DynamicRpcHttpProxy<>(
				SimpleServiceImpl.class, httpInvocationHandler
		);
		simpleService = httpProxy.getProxy();
	}

	@Test
	public void testGreeting() {
		long start = System.currentTimeMillis();
		String result = simpleService.greeting("Samuel");
		long end = System.currentTimeMillis();
		System.out.println("Done in " + (end - start) + " ms");
	}

}
