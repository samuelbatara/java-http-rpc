package com.demo;

import com.demo.http.JsonRpcHttpServlet;
import com.demo.service.SimpleService;
import com.demo.service.SimpleServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.core.ApplicationServletRegistration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

  @Bean
  public SimpleService simpleService() {
    return new SimpleServiceImpl();
  }

  @Bean
  public ServletRegistrationBean<JsonRpcHttpServlet> servletRegistrationBean(SimpleService simpleService) {
    JsonRpcHttpServlet jsonRpcHttpServlet = new JsonRpcHttpServlet(
        SimpleService.class,
        simpleService,
        new ObjectMapper().writer()
    );

    ServletRegistrationBean<JsonRpcHttpServlet> bean =
        new ServletRegistrationBean<>(jsonRpcHttpServlet, "/simple-service");
    bean.setName(SimpleService.class.getSimpleName());

    return bean;
  }
}
