package com.kh.myweb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j // lombok 에서 제공하는 "코드를 자동완성" 해주는 어노테이션
@SpringBootTest
class MyWebBeApplicationTests {

	// Logger 타입의 객체를 전역변수로 셋팅하기
	// private Logger logger = LoggerFactory.getLogger(MyWebBeApplicationTests.class);
	// > import 시 반드시 slf4j.Logger, LoggerFactory 로 해야함!!
	
	// @Slf4j 어노테이션이 하는 일
	// > 이 클래스의 필드부에 Logger 객체를 전역변수로 자동 생성해준다!!
	// > 내부적으로 위의 코드가 실행되는 꼴 (단, 객체명은 log 로 고정)
	// Logger log = LoggerFactory.getLogger(MyWebBeApplicationTests.class)
	
	@Test
	void contextLoads() {
		
		log.debug("난 debug야");
		log.info("난 info야");
		log.warn("난 warn이야");
		log.error("난 error야");
		// > 메세지같은거의 종류와, 어디서 찍힌 메세지인지도 나오고, 그 뒤에 인지값으로 넘긴 메세지 내용이 같이 출력된다. (콘솔에)
		// > debug 메소드로 출력해야할 내용은 정작 안나오는 것도 볼 수 있다!!
		
		/*
		 * * Logging
		 * 
		 * - 서버에서 필요에 의해 "기록 (log)" 을 남기는 행위
		 * - 기록을 남겨두면, 추후 운영 상의 이슈 또는 장애 들이 발생했을 때 로그를 토대로 원인을 파악할 수 있음!!
		 *   (실무에서는 필수임)
		 * - 기록을 어디에 남기는지? 콘솔, 파일 (제일 대표적), 이메일 등
		 * 
		 * * Logger
		 * 
		 * - 로깅을 수행하는 객체 (도구)
		 * - 종류 : Log4j (옛날에 유행하던것, Spring Legacy 에서 주로 쓰임), Logback (요즘 많이 쓰임, Spring Boot 에서 쓰임)
		 *   둘의 사용법이 거의 똑같고, 자바 베이스로 로깅을 수행할 때 쓰인다는 공통점이 있다.
		 *   (우리 수업시간에는 Spring Boot 이므로 Logback 을 쓸 예정)
		 *   
		 * 참고) Log4j 와 Logback 의 사용법이 거의 똑같은 이유
		 *   
		 * - Spring 에서는 Log4j 를 쓰든, Logback 을 쓰든 간에 Slf4j 로 통합해서 코드를 관리해준다!!
		 * - Slf4j 는 부모 "인터페이스" / Log4j 와 Logback 은 "구현체 클래스"
		 * - 주로 실제 코드를 작성할 때에는 부모타입인 Slf4j 타입으로 선언하고 실제 자식 구현체 타입의 코드로 동작하는 원리 (다형성)
		 * - 설정 상 Logback 으로 돌아가던 코드를 Log4j 로 갈아끼더라도 코드는 안변한다!!
		 *   
		 * * Spring Boot 는 별도의 라이브러리 연동 필요 없이 바로 쓰면 됨!!
		 *   (에초에 Logback 이 포함되어있음)
		 *   
		 * - 디테일한 환경 설정 정도만 개발자인 내가 직접 잡아주면 된다!! (Logback 사용법의 핵심은 환경설정!!)
		 * - src/main/resources 폴더에 logback-spring.xml 파일로 만들어서 설정값들을 작성한다.
		 *   (파일명은 고정!! 파일명이 다르면 안됨!!)
		 */
		
	}

}