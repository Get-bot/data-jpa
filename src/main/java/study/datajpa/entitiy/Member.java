package study.datajpa.entitiy;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    // 연관 관계의 주인
    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩
    @JoinColumn(name = "team_id")
    @ToString.Exclude // 연관 관계가 걸린 필드는 ToString에서 제외
    private Team team;

    public Member(String userName, int age, Team team) {
        this.username = userName;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public void changeTeam(Team team) {
        this.team = team; // 내 자신의 팀을 바꿔줌
        team.getMembers().add(this); // 팀에도 나를 변경 해줌
    }

}
