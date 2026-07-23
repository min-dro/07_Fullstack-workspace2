package com.kh.jpa.board.model.vo;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.kh.jpa.member.model.vo.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	// > PK 가 숫자일 경우 AUTO_INCREMENT 를 사용하기 위해 @GeneratedValue 어노테이션을 작성
	//	 strategy=GenerationType.IDENTITY : MySQL 의 AUTO_INCREMENT 전략을 사용하겠다.
	//	 (INSERT 시 PK 값을 직접 넣지 않더라도 자동으로 1, 2, 3, 4, ... 증가)
	private int boardNo;
	
	@Column(name="BOARD_TITLE", nullable=false, length=35)
	private String boardTitle;
	
	// * 기존의 방식 - 외래키로 지정 X
	// @Column(name="BOARD_WRITER", nullable=false)
	// private String boardWriter;
	// > 단순히 String borderWriter; 필드로 작성자의 아이디를 컬럼으로 생성하면
	//   실제 만들어진 테이블들의 ERD 를 살펴봤을 때 (DDL 구문을 뜯어봤을 때) 외래키 설정이 안되어있음!!
	// > 누군가가 잘못된 경로 등으로 게시글을 작성했을 때, 실제 회원 정보에 없는 아이디로 작성자 정보가 들어갈 수 없음
	//	 (데이터의 무결성이 깨짐, 데이터의 정합성이 맞지 않음)
	
	// * 외래키를 지정하려면? - @JoinColumn, 카디널리티를 나타내는 어노테이션 둘 다 작성
	@ManyToOne
	// > 지금 내가 코드 작업중인 이 곳은 Board Entity (N), 여기를 기준으로 시작해야함
	//   (게시글 N개는 1명의 회원에 의해 씌어질 수 있음)
	@JoinColumn(name="BOARD_WRITER")
	// > @JoinColumn 어노테이션은 외래키가 걸린 컬럼임을 의미한다.
	//   기존의 @Column 어노테이션과 동일하게 name 속성에 실제로 쓸 컬럼명을 정의한다.
	private Member member;
	// > 참조하고자 하는 부모 Entity를 통째로 필드로 정의한다!!
	// > 이 때, 이 컬럼의 자료형은 Member의 @Id 컬럼에 대한 자료형으로 잡히게 된다!!
	
	@Column(name="BOARD_CONTENT", nullable=false, length=200)
	private String boardContent;
	
	@Column(name="COUNT", columnDefinition="INT DEFAULT 0")
	private int count;
	
	@Column(name="CREATE_DATE", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createDate;
	
	@Column(name="STATUS", length=1, columnDefinition="CHAR(1) DEFAULT 'Y'")
	private String status;
	
	// ---------------------------------
	
	// Board Entity 에 
	// 게시글 1개을 조회할 때, 그 게시글에 딸린 댓글들도 같이 조회하겠다.
	@OneToMany(mappedBy="board")
	private List<Reply> replyList;
	// > 양방향 Mapping : 자식인 Reply 의 board 필드랑 연결한 것
	// > @Column, @JoinColumn 은 쓰지 않는다. (컬럼으로 안만들겠다)
	// > 이 필드는 읽기 전용임 (조회해온 댓글 목록들을 담고 수정 불가)
	
}