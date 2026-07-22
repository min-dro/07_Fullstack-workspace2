package com.kh.jpa.board.model.vo;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="BOARD")

@DynamicInsert
@DynamicUpdate

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Board {

	@Id
	@Column(name="BOARD_NO")
	private int boardNo; // + AUTO_INCREMENT 에 대한 옵션 지정
	
	@Column(name="BOARD_TITLE", nullable=false, length=35)
	private String boardTitle;
	
	@Column(name="BOARD_WRITER", nullable=false)
	private String boardWriter; // + 외래키 처리
	
	@Column(name="BOARD_CONTENT", nullable=false, length=200)
	private String boardContent;
	
	@Column(name="COUNT", columnDefinition="INT DEFAULT 0")
	private int count;
	
	@Column(name="CREATE_DATE", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createDate;
	
	@Column(name="STATUS", columnDefinition="CHAR(1) DEFAULT 'Y'")
	private String status;
	
}