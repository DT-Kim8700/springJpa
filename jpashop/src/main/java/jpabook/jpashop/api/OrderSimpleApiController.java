package jpabook.jpashop.api;

import jpabook.jpashop.domain.embedded.Address;
import jpabook.jpashop.domain.entity.Order;
import jpabook.jpashop.domain.enumC.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * xToOne (ManyToOne, OneToOne)
 * Order
 * Order - > Member
 * Order - > Delivery
 * */


@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    // 리스트 조회
    @GetMapping("/api/ver1/simple-orders")      // 무한 루프에 빠지게 된다. 양방향 연관관계의 한 쪽에 @JsonIgnore를 설정해줘야 한다.
    public List<Order> orderV1(){       // 엔티티에 그대로 맵핑 시키는 것은 좋은 선택이 아니다.
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        // Member 와 Delivery는 아직 DB로부터 값을 찾아오지 않았다. 때문에 프록시 객체로 값이 설정되어있다.

        for(Order order : all){
            order.getMember().getName();      // Lazy 강제 초기화. 프록시 객체였던 order 가 getMember().getName()로 인해 DB에서 값을 찾아와 진짜 객체가 된다.
            order.getDelivery().getAddress();    // Lazy 강제 초기화
        }

        return all;
    }

    @GetMapping("/api/ver2/simple-orders")
    public ResultMemberList orderV2(){       // 엔티티에 그대로 맵핑 시키는 것은 좋은 선택이 아니다.

        // N + 1 문제가 발생한다. N은 조회의 결과 수.
        // 그런데 여기서는 member와 delivery에서 지연로딩이 발생하므로 최악의 경우 1 + N(member의 수) + N(delivery의 수) 만큼 쿼리를 보내게 된다.
        // N의 데이터 중에 member나 delivery의 값이 같으면 그만큼 해당되는 N이 감소.
        List<Order> orderList = orderRepository.findAllByString(new OrderSearch());

        List<SimpleOrderDto> collect = orderList.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return new ResultMemberList(collect);
    }

    @GetMapping("/api/ver3/simple-orders")
    public ResultMemberList orderV3(){

        List<Order> orderList = orderRepository.findAllMemberWithDelivery();    // fetch join을 통해 member와 delivery의 데이터까지 한번에 가져온다.

        List<SimpleOrderDto> collect = orderList.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return new ResultMemberList(collect);
    }

    @GetMapping("/api/ver4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4(){

        return orderSimpleQueryRepository.findOrderDtoList();
    }

    // ver3을 주로 사용하고 조회하는 컬럼수가 너무 많아 성능 이슈가 발생되면 ver4 방법을 사용한다.
    // ver4 방법으로도 문제가 해결이 안되면 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접 사용한다.



    @Data
    @AllArgsConstructor
    static class ResultMemberList<T> {
        private T data;
    }

    @Data
    static class SimpleOrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderdate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order){
            this.orderId = order.getId();
            this.name = order.getMember().getName();     // 이 과정에서 LAZY 초기화가 진행된다. member에 대한 데이터를 찾기위해 db에 쿼리를 보내게 된다.
            this.orderdate = order.getOrderDate();
            this.orderStatus = order.getOrderStatus();
            this.address = order.getDelivery().getAddress();    // 이 과정에서 LAZY 초기화가 진행된다. delivery 대한 데이터를 찾기위해 db에 쿼리를 보내게 된다.
        }
    }


}
