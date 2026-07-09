package com.kh.myweb.notice.model.service;

import java.util.ArrayList;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.myweb.notice.model.dao.NoticeDao;
import com.kh.myweb.notice.model.vo.Notice;

@Service
public class NoticeServiceImpl implements NoticeService{

	@Autowired
	private NoticeDao noticeDao;
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Override
	public ArrayList<Notice> selectNoticeList() {
		
		return noticeDao.selectNoticeList(sqlSession);
	}

	@Override
	public Notice selectNotice(int noticeNo) {
		
		return noticeDao.selectNotice(sqlSession, noticeNo);
	}

	@Transactional
	@Override
	public int insertNotice(Notice n) {
		
		return noticeDao.insertNotice(sqlSession, n);
	}

	@Transactional
	@Override
	public int updateNotice(Notice n) {
		
		return noticeDao.updateNotice(sqlSession, n);
	}

	@Transactional
	@Override
	public int deleteNotice(int noticeNo) {
		
		return noticeDao.deleteNotice(sqlSession, noticeNo);
	}

}
