
// packages
package com.example.ProjectFlow.modules.board.repository;

// imports
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;

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


   // get all boards
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

}