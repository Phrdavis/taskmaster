package com.tasksmaster.taskmaster.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tasksmaster.taskmaster.model.Tasks;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Long> {

    Page<Tasks> findByDeleted(String deleted, Pageable pageable);

    boolean existsByTitle(String title);
    
    @Query("SELECT DISTINCT t FROM Tasks t " +
           "JOIN t.owner u " +
           "LEFT JOIN Team team ON team.coordinator.id = :userId " +
           "LEFT JOIN Team memberTeam ON u.id IN (SELECT m.id FROM Team t2 JOIN t2.members m WHERE t2.id = memberTeam.id) " +
           "WHERE (u.id = :userId " +
           "   OR team.coordinator.id = :userId " +
           "   OR EXISTS (SELECT 1 FROM Team ut JOIN ut.members utm WHERE utm.id = :userId AND u.id IN (SELECT m2.id FROM ut.members m2))) " +
           "AND t.deleted = :deleted")
    Page<Tasks> findMyTasks(@Param("userId") Long userId, @Param("deleted") String deleted, Pageable pageable);

}
