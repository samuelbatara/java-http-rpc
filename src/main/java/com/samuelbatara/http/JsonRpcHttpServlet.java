package com.samuelbatara.http;

import com.samuelbatara.util.JsonNodeParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonRpcHttpServlet extends HttpServlet {
  private static String PARAMS = "params";
  private static String METHOD = "method";
  private final ObjectWriter objectWriter;
  private final Class serviceInterface;
  private final Object serviceImpl;
  private static final Logger log = LoggerFactory.getLogger(JsonRpcHttpServlet.class);

  public JsonRpcHttpServlet(Class serviceInterface, Object serviceImpl, ObjectWriter objectWriter) {
    this.serviceInterface = serviceInterface;
    this.serviceImpl = serviceImpl;
    this.objectWriter = objectWriter;
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject;

    try {
      jsonObject = (JSONObject) jsonParser.parse(
          new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8));
    } catch (ParseException e) {
      log.error("failed to parse input stream");
      throw new RuntimeException(e);
    }

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(jsonObject.toString());
    if (jsonNode.get(PARAMS) == null) {
      log.error("\t[x] params was not found");
      log.info("\t [x] " + jsonNode.toString());
      return;
    }

    List<JsonNode> paramNodes = JsonNodeParser.getElements(jsonNode.get(PARAMS));

    Object[] params = new Object[paramNodes.size()];
    Class<?>[] paramTypes = null;
    Method[] methods;
    Method fMethod = null;

    try {
      methods = serviceInterface.getMethods();
      for (Method method : methods) {
        if (method.getName().equals(jsonNode.get(METHOD).textValue())) {
          paramTypes = method.getParameterTypes();
          fMethod = method;
          break;
        }
      }

      if (fMethod == null) {
        throw new RuntimeException(
            String.format("method %s is not found", jsonNode.get(METHOD).textValue())
        );
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException(e);
    }

    try {
      for (int i = 0; i < paramTypes.length; i++) {
        params[i] = JsonNodeParser.parse(paramTypes[i], paramNodes.get(i));
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    try {
      Object result = fMethod.invoke(serviceImpl, params);
      objectWriter.writeValue(resp.getOutputStream(), result);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    log.info("Incoming request to " + serviceImpl.getClass().getName() + "." + fMethod.getName());
  }
}
