package com.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
	
}