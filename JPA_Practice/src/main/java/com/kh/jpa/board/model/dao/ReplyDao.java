package com.kh.jpa.board.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kh.jpa.board.model.vo.Reply;

public interface ReplyDao extends JpaRepository<Reply, Integer> {

	List<Reply> findByStatusOrderByReplyNoDesc(String status);
	
}