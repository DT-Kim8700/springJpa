package jpabook.jpashop.domain.entity;

import jpabook.jpashop.domain.enumC.DeliveryStatus;
import jpabook.jpashop.domain.enumC.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "orderT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    /** 기본 키 */
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;
    private LocalDateTime orderDate;
    // enum 타입
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


    /** 연관관계 */
    // 주인
    @ManyToOne(fetch = LAZY)        // 연관 관계는 모두 LAZY로 설정한다. 즉시로딩(EAGER)는 예측이 어렵고, 어떤 SQL이 실행될지 추적하기 어렵다.
    @JoinColumn(name = "member_id")     // DB의 order 테이블에서의 참조키 이름을 적어주면 된다.
    private Member member;
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    // 피주인
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItemList = new ArrayList<>();      // 컬렉션은 필드에서 초기화해야 null 문제에서 안전하다.



    /** 설정자 */
    public void setOrderStatus(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }

    public void setOrderDate(LocalDateTime orderDate){
        this.orderDate = orderDate;
    }


    /** 연관관계 편의 메서드 */
    public void setMember(Member member){
        this.member = member;
        this.member.getOrderList().add(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        this.delivery.setOrder(this);
    }

    public void addOrderItem(OrderItem orderItem){
        this.orderItemList.add(orderItem);
        orderItem.setOrder(this);
    }


    /** 비즈니스 로직 */

    // 주문 생성매서드    - 누가 샀는가, 주문 목록, 구매한 상품들
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItemList){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItemList){
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    // 주문취소
    public void cancel(){
        if(delivery.getDeliveryStatus() == DeliveryStatus.COMP){    // 이미 물품 배송이 완료된 경우
            throw new IllegalStateException("이미 배송이 완료된 상품입니다.");
        }

        this.setOrderStatus(OrderStatus.CANCLE);
        for(OrderItem orderItem: this.orderItemList) {
            orderItem.cancel();
        }
    }

    // 전체 주문 가격 조회
    public int getTotalPrice() {
//        int totalPrice = 0;
//        for(OrderItem orderItem : this.orderItemList){
//            totalPrice += orderItem.getTotalPrice();
//        }

        return this.orderItemList.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }

}
