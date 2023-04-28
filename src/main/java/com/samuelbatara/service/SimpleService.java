package com.samuelbatara.service;

import com.samuelbatara.model.Product;

import java.util.List;

public interface SimpleService {
	String greeting(String name);
	List<Product> getProducts();
}
