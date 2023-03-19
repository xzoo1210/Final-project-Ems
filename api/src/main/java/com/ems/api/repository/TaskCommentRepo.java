package com.ems.api.repository;

import com.ems.api.entity.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskCommentRepo extends JpaRepository<TaskComment, Long> {
    List<TaskComment> findAllByTaskIdOrderByIdDesc(Long taskId);
}
