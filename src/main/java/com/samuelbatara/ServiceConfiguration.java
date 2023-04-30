package com.samuelbatara;

import com.samuelbatara.http.JsonRpcHttpRequestHandler;
import com.samuelbatara.http.JsonRpcHttpServlet;
import com.samuelbatara.proxy.DynamicHttpInvocationHandler;
import com.samuelbatara.proxy.DynamicRpcHttpProxy;
import com.samuelbatara.service.SimpleService;
import com.samuelbatara.service.SimpleServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

	@Bean
	public SimpleService simpleService() {
		return new SimpleServiceImpl();
	}

	@Bean("simple-service-proxy")
	public SimpleService simpleServiceProxy() {
		JsonRpcHttpRequestHandler httpRequestHandler = new JsonRpcHttpRequestHandler(
				"http://localhost:8080/" + SimpleServiceImpl.class.getName()
		);
		DynamicHttpInvocationHandler httpInvocationHandler = new DynamicHttpInvocationHandler(
				httpRequestHandler
		);
		DynamicRpcHttpProxy<SimpleService> httpProxy = new DynamicRpcHttpProxy<>(
				SimpleServiceImpl.class, httpInvocationHandler
		);
		SimpleService simpleService = httpProxy.getProxy();
		return simpleService;
	}

	@Bean
	public ServletRegistrationBean<JsonRpcHttpServlet> servletRegistrationBean(SimpleService simpleService) {
		JsonRpcHttpServlet jsonRpcHttpServlet = new JsonRpcHttpServlet(
				SimpleService.class,
				simpleService,
				new ObjectMapper().writer()
		);
		String url = "/" + SimpleServiceImpl.class.getName();
		ServletRegistrationBean<JsonRpcHttpServlet> bean =
				new ServletRegistrationBean<>(jsonRpcHttpServlet, url);
		bean.setName(SimpleServiceImpl.class.getName());

		return bean;
	}
}
