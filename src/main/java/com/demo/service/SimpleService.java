package com.demo.service;

import com.demo.model.Request;
import com.demo.model.Response;

public interface SimpleService {
  long handleLong(long number);
  String handleString(String text);
  Response handleObject(Request request);
  long calculate(int x, int[] array);
}
