
// packages
package com.example.ProjectFlow.common.mapper;

// imports
import java.util.Map;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;


@Component
public class ObjectToMap {
 
   // propertie
   private final ObjectMapper objectMapper;
   
   // constructor
   public ObjectToMap(ObjectMapper objectMapper) {
      this.objectMapper = objectMapper;
   }

   // Object -> Map
   public Map<String, Object> mapping(Object object) {
      return objectMapper.convertValue(
         object, 
         new TypeReference<Map<String, Object>>() {}
      );
   }

}