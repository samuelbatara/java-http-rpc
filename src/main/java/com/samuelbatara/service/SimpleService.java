package com.samuelbatara.service;

import com.samuelbatara.model.Product;

public interface SimpleService {
	String greeting(String name);
	Product getProduct(long id);
}
