package jpabook.jpashop.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    /**
     * 기본키
     */
    @Id
    @GeneratedValue
    @Column(name = "order_Item_id")
    private Long id;
    private int orderPrice;
    private int count;

    /**
     * 연관관계
     */
    // 주인
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    /** 설정자 */

    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * 연관관계 편의 메서드
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 비즈니스 로직
     */
    // 생성 매서드 - 어떤 상품인가, 상품의 가격, 구매수량
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);    // 주문 받은 만큼 현재 재고에서 감소 시켜준다.
        return orderItem;
    }

    // 물건 취소
    public void cancel() {
        getItem().addStock(count);      // 취소시 count의 값만큼 물건재고 수를 다시 늘려준다.
    }

    // 주문한 물건 한 종류의 총 가격
    public int getTotalPrice() {
        return getOrderPrice() * getCount();    // 물건 1개의 가격 * 해당물품을 주문한 개수
    }


    /**
     * 제약 메서드 - OrderItem 객체를 생성할 때 비즈니스 상으로 반드시 필요한 값들이 없이 디폴트 생성자로 만드는 것을 막기 위해서 만들어놓는다.
     *            protected 로 설정함으로서 외부에서 해당 생성자를 사용하지 못한다.
     *            @NoArgsConstructor(access = AccessLevel.PROTECTED) 로 대신 설정할 수 있다.
     */
//    protected OrderItem() {
//    }
}
