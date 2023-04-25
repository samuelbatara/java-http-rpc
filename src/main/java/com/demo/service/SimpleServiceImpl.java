package com.demo.service;

import com.demo.model.Request;
import com.demo.model.Response;

public class SimpleServiceImpl implements SimpleService {

  @Override
  public long handleLong(long number) {
    return number;
  }

  @Override
  public String handleString(String text) {
    return "Incoming text: " + text;
  }

  @Override
  public Response handleObject(Request request) {
    String[] texts = new String[request.getTopics().length];
    for (int i = 0; i < texts.length; i++) {
      texts[i] = "Incoming request: " + request.getTopics()[i];
    }

    return new Response(texts);
  }

}
