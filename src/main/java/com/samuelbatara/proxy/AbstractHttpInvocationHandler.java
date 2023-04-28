package com.samuelbatara.proxy;

import com.google.gson.Gson;

public abstract class AbstractHttpInvocationHandler {
  protected final Gson gson;

  public AbstractHttpInvocationHandler() {
    this.gson = new Gson();
  }

  protected String generateRequestBody(String methodName, Object[] args) {
    JsonRpcPayload payload = new JsonRpcPayload(methodName, args);
    return gson.toJson(payload);
  }

  private static class JsonRpcPayload {
    private final static String JSON_RPC = "2.0";
    private final static String ID = "1";
    private String jsonRpc;
    private String id;
    private String method;
    private Object[] params;
    public JsonRpcPayload(String method, Object[] args) {
      this.jsonRpc = JSON_RPC;
      this.id = ID;
      this.method = method;
      this.params = args;
    }

    public void setJsonRpc(String jsonRpc) {
      this.jsonRpc = jsonRpc;
    }
    public String getJsonRpc() {
      return jsonRpc;
    }
    public void setId(String id) {
      this.id = id;
    }
    public String getId() {
      return id;
    }
    public void setMethod(String method) {
      this.method = method;
    }
    public String getMethod() {
      return method;
    }
    public void setParams(Object[] args) {
      this.params = args;
    }
    public Object[] getParams() {
      return params;
    }
  }
}
