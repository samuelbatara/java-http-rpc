package com.samuelbatara.service;

import com.samuelbatara.model.Product;

import java.util.ArrayList;
import java.util.List;

public class SimpleServiceImpl implements SimpleService {

	private final List<Product> products = new ArrayList<>();

	@Override
	public String greeting(String name) {
		return String.format("Hello " + name);
	}

	@Override
	public List<Product> getProducts() {
		return products;
	}
}
