package com.samuelbatara.controller;

import com.samuelbatara.ServiceConfiguration;
import com.samuelbatara.model.Product;
import com.samuelbatara.service.SimpleService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class SimpleServiceController {

	private final SimpleService simpleService;

	public SimpleServiceController() {
		ApplicationContext context = new AnnotationConfigApplicationContext(ServiceConfiguration.class);
		this.simpleService = context.getBean("simple-service-proxy", SimpleService.class);
	}

	@GetMapping
	public String greetingProxy() {
		String name = "Samuel";
		return simpleService.greeting(name);
	}

	@GetMapping("/product")
	public List<Product> getProductsProxy() {
		return simpleService.getProducts();
	}
}
