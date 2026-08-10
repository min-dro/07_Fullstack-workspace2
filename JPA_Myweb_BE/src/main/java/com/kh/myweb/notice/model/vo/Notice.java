package com.kh.myweb.notice.model.vo;

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

// @Alias("notice")

@Schema(description="공지사항 엔티티")

@Entity
@Table(name="NOTICE")

@DynamicInsert
@DynamicUpdate

@NoArgsConstructor
@Setter
@Getter
@ToString
public class Notice {

	@Schema(description="공지사항 번호 (자동 생성)", example="1", accessMode=Schema.AccessMode.READ_ONLY)
	@Id
	@Column(name="NOTICE_NO")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int noticeNo;				//	NOTICE_NO	NUMBER

	@Schema(description="공지사항 제목", example="공지서비스를 오픈합니다.", requiredMode=Schema.RequiredMode.REQUIRED)
	@Column(name="NOTICE_TITLE", nullable=false, length=35)
	private String noticeTitle;			//	NOTICE_TITLE	VARCHAR2(100 BYTE)
	
	// @Column(name="NOTICE_WRITER", length=30)
	// private String noticeWriter;	//	NOTICE_WRITER	VARCHAR2(30 BYTE)
	// > 우리가 DDL 로 직접 테이블을 생성했고, DDL 구문 내에서 직접 외래키도 미리 걸어줬었음!!
	//   이 경우에는 평소대로 쓰면된다!! 즉, @ManyToOne 을 안써도 된다!! (이미 테이블에 외래키가 걸려있으니까)

	@Schema(description="공지사항 작성자 정보 (Member 객체)")
	// > 연관관계인 필드에 @Schema 를 붙이면 Swagger 문서 상에서 부모엔티티인 Member 스키마 전체가
	//	 객체 안의 객체 형식으로 표시됨!!
	// > 기존에 Member 쪽에서 작성했었던 설명들이 그대로 들어가있음!!
	// @ManyToOne 적용
	@ManyToOne
	@JoinColumn(name="NOTICE_WRITER")
	private Member member;
	
	@Schema(description="공지사항 내용", example="2026년 8월 4일 날짜로 공지사항 서비스를 개시합니다. 많은 관심 바랍니다.",
			requiredMode=Schema.RequiredMode.REQUIRED)
	@Column(name="NOTICE_CONTENT", nullable=false, length=200)
	private String noticeContent;		//	NOTICE_CONTENT	VARCHAR2(200 BYTE)
	
	@Schema(description="공지사항 작성일 (DB 자동 입력)", example="2026-08-04T10:00:00",
			accessMode=Schema.AccessMode.READ_ONLY)
	@Column(name="CREATE_DATE", nullable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createDate;			//	CREATE_DATE	DATE
	
	@Schema(description="공지사항 상태값 (Y : 게시 / N : 삭제)", example="Y",
			allowableValues={"Y", "N"}, defaultValue="Y")
	@Column(name="STATUS", columnDefinition="CHAR(1) DEFAULT 'Y'")
	private String status;				//	STATUS	VARCHAR2(1 BYTE)
	
}