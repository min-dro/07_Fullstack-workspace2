package com.kh.myweb.auth.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.myweb.member.model.vo.Member;

@Repository
public class AuthDao {

	public Member loginMember(SqlSessionTemplate sqlSession, String userId) {
		
		return sqlSession.selectOne("authMapper.loginMember", userId);
	}
	
}