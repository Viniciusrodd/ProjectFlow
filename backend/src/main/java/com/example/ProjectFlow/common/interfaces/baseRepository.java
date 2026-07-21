
// packages
package com.example.ProjectFlow.common.interfaces;

// imports
import java.util.List;


public interface baseRepository<T, ID> {
 
   T findById(ID id);
   List<T> findAll();
   T create(T entity);
   T update(ID id, T entity);
   void delete(ID id);
   boolean existsById(ID id);
   
}