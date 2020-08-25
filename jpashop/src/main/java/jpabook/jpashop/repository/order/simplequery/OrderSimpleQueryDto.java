package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.embedded.Address;
import jpabook.jpashop.domain.entity.Order;
import jpabook.jpashop.domain.enumC.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate,
                               OrderStatus orderStatus, Address address){
        this.orderId = orderId;
        this.name = name;     // 이 과정에서 LAZY 초기화가 진행된다. member에 대한 데이터를 찾기위해 db에 쿼리를 보내게 된다.
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;    // 이 과정에서 LAZY 초기화가 진행된다. delivery 대한 데이터를 찾기위해 db에 쿼리를 보내게 된다.
    }
}
