package com.samuelbatara.service;

import com.samuelbatara.model.Product;

public class SimpleServiceImpl implements SimpleService {

	@Override
	public String greeting(String name) {
		return String.format("Hello " + name);
	}

	@Override
	public Product getProduct(long id) {
		return new Product(id, "Ancol", 150000);
	}
}
