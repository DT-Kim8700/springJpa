package jpabook.jpashop.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.embedded.Address;
import jpabook.jpashop.domain.enumC.DeliveryStatus;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Delivery {

    /**
     * 기본 키
     */
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;
    // enum 타입
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;
    // 값 타입
    @Embedded
    private Address address;



    /**
     * 연관 관계
     */
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "delivery")
    @JsonIgnore
    private Order order;


    /**
     * 설정자
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
