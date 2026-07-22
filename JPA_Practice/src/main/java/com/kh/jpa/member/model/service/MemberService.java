package com.kh.jpa.member.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.jpa.member.model.dao.MemberDao;
import com.kh.jpa.member.model.vo.Member;

/*
 * * JPA 에서의 Service
 * 
 * - 기존 MyBatis 에서는 Service -> Dao 호출 시 SqlSessionTemplate 을 같이 넘겼음
 * - JPA 에서는 SqlSessionTemplate 이 필요 없음!!
 * - 대신, Repository (DAO) 의 메소드를 바로 호출한다.
 */

@Service
public class MemberService {

	@Autowired
	private MemberDao memberDao;

	// 1) 전체 회원 조회
	public List<Member> selectMemberList() {
		
		// MyBatis : memberDao.selectMemberList(sqlSession);
		// JPA : memberDao.finaAll();
		return memberDao.findAll();
	}

	// 2) 회원 추가 (INSERT)
	@Transactional
	public Member insertMember(Member m) {
		
		// MyBatis : memberDao.insertMember(sqlSession, m); - int 리턴 (처리된 행의 갯수)
		// JPA : memberDao.save(m); - 방금 insert 된 그 Entity 가 그대로 리턴
		return memberDao.save(m);
	}

	// 3) 회원 상세 조회
	public Member selectMember(String userId) {
		
		// MyBatis : memberDao.selectMember(sqlSession, userId);
		// JPA : memberDao.findById(userId).orEles(null);
		// > findById 는 Optional<Member> 을 리턴함
		//	 .orElse(null) : 조회 결과가 없으면 null 반환
		return memberDao.findById(userId).orElse(null);
	}

	// 4) 회원 정보 수정
	@Transactional
	public Member updateMember(Member m) {
		
		// MyBatis : memberDao.updateMember(sqlSession, m)
		// JPA : memberDao.save(m); - 마찬가지로 갱신된 Entity 를 다시 조회해서 리턴
		return memberDao.save(m);
	}

	// 5) 회원 삭제
	@Transactional
	public void deleteMember(String userId) {
		
		// MyBatis : memberDao.deleteMember(sqlSession, userId);
		// JPA : deleteById(userId);
		
		memberDao.deleteById(userId);
	}

	// 6) PK 존재 확인
	public boolean existById(String userId) {
		
		return memberDao.existsById(userId);
	}

	// 7) 이름으로 조회 (이름이 일치하는지)
	public List<Member> selectByUserName(String userName) {
		
		return memberDao.findByUserName(userName);
	}

	// 8) 이름 키워드 검색 (키워드가 포함되었는지)
	public List<Member> searchMemberList(String keyword) {
		
		return memberDao.findByUserNameContaining(keyword);
	}

	// 9) 나이 범위 검색
	public List<Member> selectByAge(int age) {

		return memberDao.findByAgeGreaterThan(age);
	}

	// 10) 전체 조회 (최신순 정렬)
	public List<Member> selectMemberListLastest() {
		
		return memberDao.findAllByOrderByEnrollDateDesc();
	}

	// 11) 성별 별 회원 수
	public long countByGender(String gender) {
		
		return memberDao.countByGender(gender);
	}
	
}