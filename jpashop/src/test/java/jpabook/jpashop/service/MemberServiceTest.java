package jpabook.jpashop.service;

import jpabook.jpashop.domain.entity.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)        // junit 실행할 떄 스프링과 같이 엮어서 실행할 것인가를 설정.
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;


    @Test
//    @Rollback(value = false)
//    // @Transactional 에 의해 @Test가 붙어있다면 기본적으로 테스트 종료시점에 DB에 commit 되지 않고 롤백되어버린다. 때문에 DB에 데이터가 INSERT 되는 것을 확인하기 위헤서는
//    // @Rollback(value = false) 설정을 해주어야 한다.
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("Ruby");

        // when
        Long savedId = memberService.join(member);

        // then
        assertEquals(member, memberRepository.findOne(savedId));
    }


    @Test(expected = IllegalStateException.class)
    public void 중복회원확인() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("Ruby");

        Member member2 = new Member();
        member2.setName("Ruby");

        // when
        memberService.join(member1);
        memberService.join(member2);      // member2에 저장된 이름이 member1에 저장된 이름과 같으므로 회원가입이 되어서는 안된다. 즉, 예외가 발생해야한다.

//        @Test(expected = IllegalStateException.class) : 아래의 예외처리를 어노테이션으로 처리
//        try {
//            memberService.join(member2);      // member2에 저장된 이름이 member1에 저장된 이름과 같으므로 회원가입이 되어서는 안된다. 즉, 예외가 발생해야한다.
//        } catch (IllegalStateException e){
//            return;     // 예외가 발생시 return 되어 테스트가 정상적으로 종료된다.
//        }

        // then
        fail("예외가 발생해야 한다.");
    }

}