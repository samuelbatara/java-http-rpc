package com.samuelbatara.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonNodeParser {
  public static Object parse(Class<?> clazz, JsonNode node) {
    if (clazz.isPrimitive()) {
      return parsePrimitiveType(clazz, node);
    } else if (clazz.isArray()) {
      return parseArrayType(clazz, node);
    } else if (clazz.equals(String.class)) {
      return parseStringType(clazz, node);
    } else if (clazz.getSuperclass().equals(Object.class)) {
      return parseObjectType(clazz, node, new ObjectMapper());
    } else {
      throw new RuntimeException(clazz.getSimpleName() + " is not supported");
    }
  }

  private static Object parseObjectType(Class<?> clazz, JsonNode node, ObjectMapper objectMapper) {
    JsonNode fieldNodes;
    try {
      fieldNodes = objectMapper.readTree(node.toString());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    Field[] fields = clazz.getDeclaredFields();
    List<Object> fieldValues = new ArrayList<>();
    for (Field field : fields) {
      field.setAccessible(true);
      JsonNode fieldNode = fieldNodes.get(field.getName());
      Object fieldValue;
      try {
        fieldValue = parse(field.getType(), fieldNode);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      fieldValues.add(fieldValue);
    }

    Object object;
    try {
      object = clazz.getConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException(
          String.format("failed to create new instance of class %s", clazz.getSimpleName()),
          e
      );
    }

    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      Object fieldValue = fieldValues.get(i);
      String setterName = getSetterName(field.getName());
      Method method;
      try {
        method = clazz.getMethod(setterName, field.getType());
      } catch (Exception e) {
        throw new RuntimeException(String.format("method %s is not found", setterName),e);
      }

      try {
        method.invoke(object, fieldValue);
      } catch (Exception e) {
        throw new RuntimeException(String.format("failed to invoke method %s", method.getName()), e);
      }
    }

    return object;
  }

  private static Object parseStringType(Class<?> clazz, JsonNode node) {
    if (!clazz.equals(String.class)) {
      throw new RuntimeException("class is expected string, but " + clazz.getSimpleName());
    }
    return node.asText();
  }

  private static Object parseArrayType(Class<?> clazz, JsonNode node) {
    if (!clazz.isArray()) {
      throw new RuntimeException("class is expected array, but " + clazz.getSimpleName());
    }

    List<JsonNode> elements = getElements(node);
    Object object = Array.newInstance(clazz.getComponentType(), elements.size());
    for (int i= 0; i < elements.size(); i++) {
      Object value;
      try {
        value = parse(clazz.getComponentType(), elements.get(i));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      Array.set(object, i, value);
    }
    return object;
  }

  private static Object parsePrimitiveType(Class<?> clazz, JsonNode node) {
    if (clazz.equals(int.class)) {
      return node.asInt();
    } else if (clazz.equals(long.class)) {
      return node.asLong();
    } else if (clazz.equals(float.class)) {
      return Float.parseFloat(node.asText());
    } else if (clazz.equals(double.class)) {
      return node.asDouble();
    } else if (clazz.equals(boolean.class)) {
      return node.asBoolean();
    } else {
      throw new RuntimeException(clazz.getSimpleName() + " is not primitive type");
    }
  }

  private static String getSetterName(String field) {
    return "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
  }

  public static List<JsonNode> getElements(JsonNode node) {
    List<JsonNode> elements = new ArrayList<>();
    Iterator<JsonNode> it = node.elements();
    while (it.hasNext()) {
      elements.add(it.next());
    }
    return elements;
  }
}
