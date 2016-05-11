package arya.postgresPerformance.Utils;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class Utils {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final Logger logger = LoggerFactory.getLogger(Utils.class);


  static {
    OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false);
  }

  private Utils() {
  }

  public static JsonNode object2Json(Object object) {
    return OBJECT_MAPPER.valueToTree(object);
  }

  public static ObjectNode newJsonObject() {
    return OBJECT_MAPPER.createObjectNode();
  }

  public static <T> T jsonStr2Object(Class<T> clazz, String jsonString) {
    T t = null;
    try {
      t = OBJECT_MAPPER.readerFor(clazz).readValue(jsonString);
    } catch (Exception e) {
      logger.debug("Exception in covnerting the Request");
    }
    return t;
  }
  
  public static <T> T mmap2Object(MultiMap mmap, Class<T>clazz){
    if(mmap.isEmpty()){
      return null;
    }
    Map<String, String > map = new HashMap<String, String>(mmap.size());
    for(Entry<String, String> entry: mmap.entries()){
      map.put(entry.getKey(), entry.getValue());
    }
    return map2Object(map, clazz);
  }

  public static <T> T map2Object(Map<String, String> map, Class<T> clazz) {
    T t = null;
    t = OBJECT_MAPPER.convertValue(map, clazz);
    return t;

  }
  
  public static <T> T mapO2Object(Map<String, Object> map, Class<T> clazz) {
    T t = null;
    t = OBJECT_MAPPER.convertValue(map, clazz);
    return t;

  }
  public static <T> T json2Object(Class<T> clazz, JsonNode requestJson)
          throws IOException {
    T t = null;
    try {
      t = (T) OBJECT_MAPPER.readerFor(clazz).readValue(requestJson);
    } catch (IOException e) {

      throw e;
    }
    return t;
  }

  public static JsonNode jsonObject2Node(JsonObject jsonObject){
    ObjectNode node = OBJECT_MAPPER.convertValue(jsonObject, ObjectNode.class);
    return node;
  }
}
