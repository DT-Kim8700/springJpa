package jpabook.jpashop.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.embedded.Address;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter     // 엔티티에는 가급적 setter를 사용하지 않는다. 값의 변경이 필요할 때에는 목적에 맞는 메서드를 따로 만들어서 사용한다.
public class Member {

    /**
     *  기본 키 
     */
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    // 값 타입
    @Embedded
    private Address address;

    /**
     *  연관관계 
     */
    // 피주인
    @OneToMany(mappedBy = "member")     // // order 테이블에 있는 member 필드에 의해 맵핑 되었다는 의미
    @JsonIgnore
    private List<Order> orderList = new ArrayList<>();


    /**
     *  설정자
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
