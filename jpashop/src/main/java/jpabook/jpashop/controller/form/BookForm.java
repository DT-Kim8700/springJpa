package jpabook.jpashop.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class BookForm {

    private Long id;

    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    @Positive(message = "가격을 입력해주세요")
    private Integer price;
    @Positive(message = "재고를 입력해주세요")
    private Integer stockQuantity;
    @NotBlank(message = "저자 명을 입력해주세요")
    private String author;
    @NotBlank(message = "도서번호를 입력해주세요")
    private String isbn;
}
