package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entitiy.Member;
import study.datajpa.entitiy.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void testMember() throws Exception {
        // given
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Optional<Member> findMember = memberRepository.findById(savedMember.getId());

        if (findMember.isPresent()) {
            Member member1 = findMember.get();
            assertThat(member1.getId()).isEqualTo(member.getId());
            assertThat(member1.getUsername()).isEqualTo(member.getUsername());
            assertThat(member1).isEqualTo(member);
        } else {
            throw new Exception("Member not found");
        }
    }

    @Test
    public void BasicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        assertThat(memberRepository.findAll()).hasSize(2);

        // 카운트 검증
        assertThat(memberRepository.count()).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        // 삭제 후 카운트 검증
        assertThat(memberRepository.count()).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result).hasSize(1);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findMember("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result).hasSize(1);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findUsernameList();

        assertThat(result.get(0)).isEqualTo("AAA");
        assertThat(result.get(1)).isEqualTo("AAA");
        assertThat(result).hasSize(2);
    }

    @Test
    public void findMemberDto() {
        Team teamA = new Team("AAATeam");
        Member m1 = new Member("AAA", 10, teamA);

        Team teamB = new Team("BBBTeam");
        Member m2 = new Member("AAA", 20, teamB);

        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<MemberDto> result = memberRepository.findMemberDto();

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getTeamName()).isEqualTo("AAATeam");
        assertThat(result.get(1).getUsername()).isEqualTo("AAA");
        assertThat(result.get(1).getTeamName()).isEqualTo("BBBTeam");
        assertThat(result).hasSize(2);
    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "AAA"));

        result.forEach(member -> System.out.println("member = " + member));

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(1).getUsername()).isEqualTo("AAA");
        assertThat(result).hasSize(2);
    }

    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findListByUsername("AAA");
        Member result2 = memberRepository.findMemberByUsername("AAA");
        Optional<Member> result3 = memberRepository.findOptionalByUsername("AAA");

        //entity가 없으면 null이 아니라 empty collection을 반환한다.
        List<Member> result4 = memberRepository.findListByUsername("CCC");

        //entity가 없으면 null을 반환한다.
        Member result5 = memberRepository.findMemberByUsername("CCC");

        //entity가 없으면 Optional.empty를 반환한다.
        Optional<Member> result6 = memberRepository.findOptionalByUsername("CCC");

        System.out.println("result4 = " + result4);
        System.out.println("result5 = " + result5);
        System.out.println("result6 = " + result6);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result2.getUsername()).isEqualTo("AAA");
        assertThat(result3.get().getUsername()).isEqualTo("AAA");
    }

    @Test
    public void paging() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> members = memberRepository.findByAge(age, pageRequest);
        // Slice<Member> members = memberRepository.findSliceByAge(age, pageRequest);
        // List<Member> members = memberRepository.findListByAge(age, pageRequest);
        // List<Member> members = memberRepository.findListByAge(age, Sort.by(Sort.Direction.DESC, "username"));

        Page<MemberDto> list = members.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        // then
        List<Member> content = members.getContent();
        long totalElements = members.getTotalElements();

        assertThat(content).hasSize(3);
        assertThat(totalElements).isEqualTo(5);
        assertThat(members.getNumber()).isEqualTo(0); // 페이지 번호
        assertThat(members.getTotalPages()).isEqualTo(2); // 전체 페이지 번호
        assertThat(members.isFirst()).isTrue(); // 첫번째 항목인가?
        assertThat(members.hasNext()).isTrue(); // 다음 페이지가 있는가?
    }

    @Test
    public void bulkUpdate() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);
        em.clear();

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("member5", 40);
        System.out.println("result = " + result);

        // then
        assertThat(resultCount).isEqualTo(3);

    }

    @Test
    public void findMemberLazy() {
        // given
        // member1 -> teamA
        // member2 -> teamB
        Team teamA = new Team("AAATeam");
        Team teamB = new Team("BBBTeam");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // when
        List<Member> members = memberRepository.findAll();

        // then
        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.teamClass = " + member.getTeam().getClass()); // 프록시 객체
            System.out.println("member.team = " + member.getTeam().getName()); // 실제 객체
        }

        List<Member> memberFetchJoin = memberRepository.findMemberFetchJoin();

        for (Member member : memberFetchJoin) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.teamClass = " + member.getTeam().getClass()); // 실제 객체
            System.out.println("member.team = " + member.getTeam().getName()); // 실제 객체
        }

        List<Member> memberEntityGraph = memberRepository.findAll();

        for (Member member : memberEntityGraph) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.teamClass = " + member.getTeam().getClass()); // 실제 객체
            System.out.println("member.team = " + member.getTeam().getName()); // 실제 객체
        }
    }

    @Test
    public void queryHint() {
        // given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    public void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();

        assertThat(result).hasSize(0);
    }

}