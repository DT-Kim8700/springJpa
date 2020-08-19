package jpabook.jpashop.domain.embedded;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {}      // JPA 스펙상 맞춰주기 위해서 만들어놓은 생성자. 쓰지는 않는다. 대신 함부로 쓸 수 없도록 접근 제어를 protected로 설정한다.

    // 주소를 등록할 때만 등록할 수 있도록 한다.
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
