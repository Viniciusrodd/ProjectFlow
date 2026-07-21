
// packages
package com.example.ProjectFlow.common.interfaces.crudBase;

// import list
import java.util.List;

// http imports
import org.springframework.http.ResponseEntity;

// import annotations
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

// import api response
import com.example.ProjectFlow.common.responses.ApiResponse;


public interface baseController<T, DTO, ID> {
 
   ResponseEntity<ApiResponse<T>> findById(@PathVariable ID id);
   ResponseEntity<ApiResponse<List<T>>> findAll();
   ResponseEntity<ApiResponse<T>> create(@RequestBody DTO dto);
   ResponseEntity<ApiResponse<T>> update(@PathVariable ID id, @RequestBody DTO dto);
   ResponseEntity<ApiResponse<Void>> delete(@PathVariable ID id);

}