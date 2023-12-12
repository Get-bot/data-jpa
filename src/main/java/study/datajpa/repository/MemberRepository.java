package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entitiy.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
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

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username); // 컬렉션

    Member findMemberByUsername(String username); // 단건

    Optional<Member> findOptionalByUsername(String username); // 단건 Optional

    // 페이징
    Page<Member> findByAge(int age, Pageable pageable);

    Slice<Member> findSliceByAge(int age, Pageable pageable);

    List<Member> findListByAge(int age, Pageable pageable);

    //count query 사용안함
    List<Member> findListByAge(int age, Sort sort);

    // count query 분리
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findMemberAllCountBy(Pageable pageable);

    // top3
    List<Member> findTop3ByUsername(String username);

    /*   벌크성 수정 쿼리
        이 어노테이션을 사용하면 executeUpdate()를 실행하고, 영향을 받은 엔티티 수를 반환한다.
        벌크성 수정, 삭제 쿼리는 영속성 컨텍스트를 무시하고 실행하기 때문에, 영속성 컨텍스트에 있는 엔티티의 상태와 DB에 엔티티 상태가 달라질 수 있다.
        따라서 벌크성 수정, 삭제 쿼리를 실행하고 나면 영속성 컨텍스트를 초기화 해주는 것이 좋다.*/
    @Modifying(clearAutomatically = true) // 벌크성 수정, 삭제 쿼리는 @Modifying 어노테이션을 사용해야 한다.
    //clearAutomatically = true 옵션을 주면 영속성 컨텍스트를 초기화 해준다.
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    public List<Member> findMemberFetchJoin();

/*     @EntityGraph
     @EntityGraph는 JPA 표준 스펙이 아니라 스프링 데이터 JPA가 제공하는 기능이다.
     @EntityGraph는 쿼리를 수행할 때 연관된 엔티티들을 SQL 한 번에 조회하는 기능이다.
     @EntityGraph는 쿼리 메서드에 적용할 수 있고, 또는 @NamedEntityGraph를 사용해서 이름을 부여한 후에 쿼리 메서드에 적용할 수도 있다.
     @EntityGraph는 엔티티에 페치 전략을 설정하거나, 쿼리 힌트를 제공할 때도 사용할 수 있다.
     @EntityGraph는 엔티티에 적용하는 것이기 때문에, 엔티티 자체에 적용하는 것이다.
     따라서 Member 엔티티에 적용하면 Member와 연관된 모든 엔티티에 적용된다.*/

    //entity graph
//    @Override
//    @EntityGraph(attributePaths = {"team"})
//    List<Member> findAll();

    //jpql + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);


    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String member1);

    // select for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

}
