package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entitiy.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 인터페이스를 확인하고 스프링 데이터 JPA가 인터페이스를 보고 구현체를 만들어서 스프링 빈에 등록해준다.
    // 스프링 데이터 JPA가 제공하는 공통 인터페이스를 사용하면 기본적인 CRUD 기능을 기본으로 제공한다.

//    @Query("select m from Member m where m.username = ?1 and m.age > ?2")
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query("select m from Member m where m.username = :username and m.age > :age")
    List<Member> findMember(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

}
