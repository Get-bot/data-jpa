package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entitiy.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        return memberRepository.findById(id).get().getUsername();
    }

    /*     도메인 클래스 컨버터
         도메인 클래스 컨버터는 리포지토리를 사용해서 엔티티를 찾음
         도메인 클래스 컨버터로 엔티티를 파라미터로 받으면, 이 엔티티는 단순 조회용으로만 사용해야 한다.
         트랜잭션이 없는 범위에서 엔티티를 조회했으므로, 엔티티를 변경해도 DB에 반영되지 않는다.
         엔티티를 변경해야 하면, 엔티티를 파라미터로 받아서는 안된다.
         (파라미터로 받은 엔티티는 단순 조회용으로만 사용해야 한다.)
         컨트롤러에서 엔티티를 생성하지 말자.
         엔티티를 외부에 노출하지 말자.
         트랜잭션이 있는 서비스 계층에 식별자( id )와 변경할 데이터를 명확하게 전달하자.*/
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("userA"));
    }

}
