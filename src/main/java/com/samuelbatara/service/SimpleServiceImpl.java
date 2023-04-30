package com.samuelbatara.service;

import com.samuelbatara.model.Product;

import java.util.ArrayList;
import java.util.List;

public class SimpleServiceImpl implements SimpleService {

	private final Product[] products = new Product[2];

	public SimpleServiceImpl() {
		products[0] = new Product(1, "Ancol", 110000);
		products[1] = new Product(1, "Ancol", 110000);
	}

	@Override
	public String greeting(String name) {
		return String.format("Hello " + name);
	}

	@Override
	public Product[] getProducts() {
		return products;
	}
}
