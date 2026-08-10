package com.kh.myweb.board.model.vo;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.kh.myweb.member.model.vo.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// @Alias("board")

@Schema(description="게시판 엔티티 (첨부파일 1개 이상 가능)")

@Entity
@Table(name="BOARD")

@DynamicInsert
@DynamicUpdate

@NoArgsConstructor
@Setter
@Getter
@ToString
public class Board {

	@Schema(description="게시글 번호(자동 생성)", example="1", accessMode=Schema.AccessMode.READ_ONLY)
	@Id
	@Column(name="BOARD_NO")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int boardNo;			//	BOARD_NO	NUMBER
	
	@Schema(description="게시글 제목", example="오늘 날씨 덥네요.", requiredMode=Schema.RequiredMode.REQUIRED)
	@Column(name="BOARD_TITLE", length=255, nullable=false)
	private String boardTitle;		//	BOARD_TITLE	VARCHAR2(100 BYTE)
	
	// @Column(name="BOARD_WRITER", length=30, nullable=false)
	// private String boardWriter;		//	BOARD_WRITER	VARCHAR2(4000 BYTE)
	
	@Schema(description="작성자 정보 (Member 객체)")
	// @ManyToOne 적용
	@ManyToOne
	@JoinColumn(name="BOARD_WRITER")
	private Member member;
	
	@Schema(description="게시글 내용", example="햇볕, 더위 조심하세요!", requiredMode=Schema.RequiredMode.REQUIRED)
	@Column(name="BOARD_CONTENT", length=255, nullable=false)
	private String boardContent;	//	BOARD_CONTENT	VARCHAR2(4000 BYTE)
	
	@Schema(description="원본 파일명", example="bono.jpg", accessMode=Schema.AccessMode.READ_ONLY)
	// > 게시글 작성 시, 첨부파일에 대한 정보는 사실 originName, changeName 문자열 형태로 넘어노는게 아니라
	//   실제 Controller 에서 MultipartFile 객체 형태로 받는다!!
	// > MultipartFile 객체로부터 originName, changeName 을 별도로 처리해서 Insert 해주는 원리이기 때문에
	//   (즉, 입력 품에서 originName, changeName 을 직접 키보드로 입력 받지 않기 때문에) READ_ONLY 임
	@Column(name="ORIGIN_NAME", length=255)
	private String originName;		//	ORIGIN_NAME	VARCHAR2(100 BYTE)
	
	@Schema(description="수정 파일명 (중복방지용 변경명)", example="2026080411234098765.jpg",
			accessMode=Schema.AccessMode.READ_ONLY)
	@Column(name="CHANGE_NAME", length=255)
	private String changeName;		//	CHANGE_NAME	VARCHAR2(100 BYTE)
	// > 만약, 게시글 1개 당 첨부파일이 여러개 들어갈 수 있다면? BOARD 따로 ATTACHMENT 따로 만들어야함!!
	//   자식 Entity 인 Attachment 쪽에서 private Board board; 로 @ManyToOne 관계를 지정하면 된다!!
	
	@Schema(description="조회수 (서버 자동 관리)", example="0", accessMode=Schema.AccessMode.READ_ONLY,
			defaultValue="0")
	@Column(name="COUNT", columnDefinition="INT DEFAULT 0")
	private int count;				//	COUNT	NUMBER
	
	@Schema(description="작성일 (DB 자동 입력)", example="2026-08-04T10:00:00",
			accessMode=Schema.AccessMode.READ_ONLY)
	@Column(name="CREATE_DATE", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createDate;		//	CREATE_DATE	DATE
	
	@Schema(description="게시글 상태 (Y : 게시 / N : 삭제)", example="Y", allowableValues={"Y", "N"},
			defaultValue="Y")
	@Column(name="STATUS", columnDefinition="CHAR(1) DEFAULT 'Y'")
	private String status;			//	STATUS	VARCHAR2(1 BYTE)
	
}