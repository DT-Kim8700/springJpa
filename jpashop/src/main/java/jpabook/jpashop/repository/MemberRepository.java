package jpabook.jpashop.repository;

import jpabook.jpashop.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {     // DB와 연관되어 처리하는 로직

//    @PersistenceContext     // 스프링에서 EntityManager를 만들어서 해당 변수에 주입해준다.
//    private EntityManager em;

    private final EntityManager em;

    public Long save(Member member){    // DB에 멤버를 등록
        em.persist(member);
        return member.getId();
    }

    // 해당 아이디를 통해 특정 회원 조회
    public Member findOne(Long id){        // DB에서 id를 통해 멤버를 찾아온다.
        return em.find(Member.class, id);
    }

    // 모든 회원을 조회
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    // 이름으로 조회
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
