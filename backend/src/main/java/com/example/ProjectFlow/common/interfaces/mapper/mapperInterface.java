
// packages
package com.example.ProjectFlow.common.interfaces.mapper;

// import list
import java.util.List;


public interface mapperInterface<DTO, E> {
 
   DTO toDto(E entity);
   E toEntity(DTO dto);
   List<DTO> toDtoList(List<E> entities);
   List<E> toEntityList(List<DTO> dtos);

}