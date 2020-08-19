package jpabook.jpashop.repository;

import jpabook.jpashop.domain.enumC.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {
    
    private String memberName;              // 회원이름
    private OrderStatus orderStatus;        // 주문상태
}
