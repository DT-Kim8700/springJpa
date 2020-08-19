package jpabook.jpashop.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class MemberForm {

    @NotBlank(message = "회원 이름은 필수입니다.")
    private String name;
    @NotBlank(message = "도시명을 입력해주세요.")
    private String city;
    @NotBlank(message = "거리명을 입력해주세요.")
    private String street;
    @NotBlank(message = "우편번호를 입력해주세요.")
    private String zipcode;
}
