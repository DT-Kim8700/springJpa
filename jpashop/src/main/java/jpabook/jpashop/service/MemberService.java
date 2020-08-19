package jpabook.jpashop.service;

import jpabook.jpashop.domain.entity.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
// DB에 데이터를 추가하는 것이 아닌 단순히 읽기 전용으로 쓸 때 옵션을 추가한다. 리소스 관리 측면에서 이점이 있다.
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;
//
//     @Componemt(repository, service, controller, configuration)가 있는 클래스의 생성자가 하나 일때는 생성자가 자동으로 @Autowired 처리가 된다.
//     @RequiredArgsConstructor가 있는 클래스는 필드변수 타입의 매개변수들을 가진 생성자를 자동으로 생성해준다.
//    public MemberService(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }

    // 회원 가입
    @Transactional
    // 기본적으로 false로 되어있다. 단순 조회가 아닌 메서드에는 false로 설정한다.
    public Long join(Member member){
        validateDuplicateMember(member);    // 중복검사
        memberRepository.save(member);

        return member.getId();
    }

    private void validateDuplicateMember(Member member){
        List<Member> findMember = memberRepository.findByName(member.getName());

        if(!findMember.isEmpty()) {     // 가입하려는 이름이 이미 가입된 목록에 있다면...
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
    
    // 단일 회원 조회
    public Member findMemberId(Long id){
        return memberRepository.findOne(id);
    }

    public List<Member> findMemberName(String name){
        return memberRepository.findByName(name);
    }

    // 회원 전체 조회
    public List<Member> findMemberList(){
        return memberRepository.findAll();
    }

}

/** 스프링이 제어 역전인 이유
 * @, 어노테이션 자체는 아무런 기능이 없는 interface 이다.
 * 어노테이션을 마크하면 스프링 내부에서 어노테이션이 마크된 클래스 파일들을 불러와 작업하는 것.
 *
 * */
















