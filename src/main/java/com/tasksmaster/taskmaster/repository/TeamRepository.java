package com.tasksmaster.taskmaster.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tasksmaster.taskmaster.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Page<Team> findByDeleted(String deleted, Pageable pageable);

    Boolean existsByName(String name);

    @Query("SELECT DISTINCT t FROM Team t " +
        "LEFT JOIN t.members m " +
        "WHERE (t.coordinator.id = :userId OR m.id = :userId) " +
        "AND t.deleted = :deleted")
    Page<Team> findMyTeams(@Param("userId") Long userId, @Param("deleted") String deleted, Pageable pageable);
}
