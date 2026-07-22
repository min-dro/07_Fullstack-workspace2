package com.kh.jpa.board.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kh.jpa.board.model.vo.Board;

public interface BoardDao extends JpaRepository<Board, Integer>{

}