package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entitiy.Member;
import study.datajpa.entitiy.Team;

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

    @Test
    public void testMember() throws Exception {
        // given
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Optional<Member> findMember = memberRepository.findById(savedMember.getId());

        if(findMember.isPresent()) {
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

}