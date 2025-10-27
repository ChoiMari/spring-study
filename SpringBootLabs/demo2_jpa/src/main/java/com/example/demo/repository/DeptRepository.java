package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Dept;
/*
 Mybatis 설정은
 UserMapper 인터페이스 : List<User> selectAll()
 와 xml 매퍼 파일과 동기화 시킴
 select * from User.. 객체화
 
 JPA : 
 엔터티라는 자원을 주면 이걸 기반으로 CRUD 메서드를 자동으로 만들어준다
 JpaRepository<Dept, Integer>기반으로 CRUD 메서드..
 자동 만듬
 
 	<자동으로 만들어 주는 메서드 설명>
	save(entity)	INSERT 또는 UPDATE 수행
	findById(id)	PK로 단건 조회 (Optional 반환)
	findAll()		전체 조회
	count()			데이터 개수 조회
	existsById(id)	해당 ID 존재 여부 확인
	delete(entity)	엔티티 삭제
	deleteById(id)	ID로 삭제
	deleteAll()		전체 삭제
	
	
	// 저장 (INSERT)
	memberRepository.save(new Member("홍길동", 20));

	// 조회 (SELECT)
	Optional<Member> member = memberRepository.findById(1L);

	// 전체 조회
	List<Member> members = memberRepository.findAll();

	// 수정 (UPDATE: JPA는 save로 동작)
	Member m = memberRepository.findById(1L).orElseThrow();
	m.setAge(30);
	memberRepository.save(m);

	// 삭제
	memberRepository.deleteById(1L);
 */
public interface DeptRepository extends JpaRepository<Dept, Integer>{
	//엔터티 클래스 타입과, PK 타입을 줌
	
	//end 자동으로 메서드 생성
}
