package study.datajpa.entitiy;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Team {

    @ToString.Include
    @Id @GeneratedValue
    @Column(name = "team_id", nullable = false)
    private Long id;
    @ToString.Include
    private String name;

    @OneToMany(mappedBy = "team") // fk가 있는 곳이 연관관계의 주인
    @ToString.Exclude
    private List<Member> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}
