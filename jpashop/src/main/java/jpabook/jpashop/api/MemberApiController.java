package jpabook.jpashop.api;

import jpabook.jpashop.domain.entity.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

//@Controller @ResponseBody
@RestController     // @Controller 와 @ResponseBody 를 모두 포함한 @
@RequiredArgsConstructor        // 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성해 준다
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/ver1/members")
    public CreateMemberResponse saveMemberVer1(@RequestBody @Valid Member member) {
        // 엔티티를 API에서 그대로 맵핑시켜서 쓰는 것은 좋은 선택이 아닐 수 있다. 엔티티는 여러 곳에서 쓰이며 때문에 유지보수 단계에서 변동이 될 가능성이 크다.
        // 거기에 영향을 받아 API를 대폭 수정해야하는 문제가 생길 수 있다. 때문에 별도의 DTO를 만들어서 작업하는 것이 좋다.

        // @ResponseBody : 응답 본문. 보통 JSON 형식으로 만들어진다.
        // @Valid : 검증을 위한 @

        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }



    @PostMapping("/api/ver2/members")
    public CreateMemberResponse saveMemberVer2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }




    @Data
////    클래스안의 모든 private 필드에 대해 @Getter와 @Setter를 적용하여 세터/게터를 만들어주고
////    클래스내에 @ToString 과 @EqualsAndHashCode를 적용시켜 메소드를 오버라이드 해주며
////    @RequiredArgsConstructor를 지정해 준다.
    static class CreateMemberResponse {

        @NonNull
        private Long id;

    }

    @Data
    static class CreateMemberRequest {

        private String name;

    }
}
