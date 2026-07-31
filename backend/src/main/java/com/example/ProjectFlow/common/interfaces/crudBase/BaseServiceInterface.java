
// packages
package com.example.ProjectFlow.common.interfaces.crudBase;

// import list
import java.util.List;


public interface BaseServiceInterface<T, DTO, ID> {
 
   T findById(ID id);
   List<T> findAll();
   T create(DTO data);
   T update(ID id, DTO data);
   void delete(ID id);
   boolean existsById(ID id);

}