package com.vargens.api_todo_list.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vargens.api_todo_list.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
