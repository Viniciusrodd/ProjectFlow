
// packages
package com.example.ProjectFlow.modules.board.repository;

// imports
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

// import DTOs
import com.example.ProjectFlow.modules.board.dto.BoardDTO;
import com.example.ProjectFlow.modules.board.dto.BoardResponseDTO;

// import entity
import com.example.ProjectFlow.modules.board.entity.BoardEntity;
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;


@Repository
public class BoardRepository {
 
   // properties
   @PersistenceContext
   private EntityManager entityManager;


   // board creation
   @Transactional
   public BoardResponseDTO create(BoardDTO data, ProjectEntity projectEntity) {
      BoardEntity board = new BoardEntity();

      // creation
      board.setProject(projectEntity);
      board.setName(data.name());
      this.entityManager.persist(board);

      return BoardResponseDTO.get(board);
   }


   // get all
   public List<BoardResponseDTO> getAll() {
      List<BoardEntity> boardsDocument = this.entityManager
         .createQuery("SELECT b FROM BoardEntity b ORDER BY b.createdAt ASC", BoardEntity.class)
         .getResultList();

      List<BoardResponseDTO> boards = new ArrayList<>();

      for(BoardEntity board : boardsDocument) {
         boards.add(BoardResponseDTO.get(board));
      }

      return boards;
   }


   // get by id
   public BoardResponseDTO getById(UUID id) throws NoResultException {
      BoardEntity board = this.entityManager
         .createQuery("SELECT b FROM BoardEntity b WHERE b.id = :id", BoardEntity.class)
         .setParameter("id", id)
         .getSingleResult();

      return BoardResponseDTO.get(board);
   }

}