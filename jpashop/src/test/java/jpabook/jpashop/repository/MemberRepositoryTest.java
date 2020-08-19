//package jpabook.jpashop.repository;
//
//import jpabook.jpashop.domain.entity.Member;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class MemberRepositoryTest {
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Test
//    @Transactional      // 테스트 케이스에서 @Transactional 으로 인해 데이터를 transection 하게 되면 테스트가 끝난 후 commit 하지 않고 rollback 한다.
//    @Rollback(value = false)        // 기본적으로 true로 설정되어 있어 롤백한다. 데이터가 db에 적용된것을 직접 확인하고 싶을 때 false로 설정한다.
//    public void testMember() throws Exception{
//        //given
//        Member member = new Member();
//        member.setUsername("루비");
//
//        //when
//        Long savedId = memberRepository.save(member);
//        Member findMember = memberRepository.find(savedId);
//
//        //then
//        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
//        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
//        Assertions.assertThat(findMember).isEqualTo(member);
//
//        System.out.println("findMember == member : " + (findMember == member));
//        // 결과 값을 true가 나온다. findMember 는 영속성 컨텍스트에 저장되어 있던 member의 값을 가져왔기 때문.
//    }
//
//    @Test
//    public void find() {
//    }
//
//}