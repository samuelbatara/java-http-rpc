package com.demo.proxy;

import java.lang.reflect.Proxy;

public class DynamicRpcHttpProxy<T extends Object> {
  private final DynamicHttpInvocationHandler dynamicHttpInvocationHandler;
  private T proxy;
  private Class _instance;

  public DynamicRpcHttpProxy(Class _instance,
                             DynamicHttpInvocationHandler dynamicHttpInvocationHandler) {
    this._instance = _instance;
    this.dynamicHttpInvocationHandler = dynamicHttpInvocationHandler;
  }

  public T getProxy() {
    if (proxy == null) {
      try {
        proxy = (T) Proxy.newProxyInstance(
            _instance.getClassLoader(),
            _instance.getInterfaces(),
            dynamicHttpInvocationHandler
        );
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    return proxy;
  }
}
