package jpabook.jpashop.domain.entity;

import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)   // 상속관계로 테이블을 구성할 떄에는 테이블 구성 전략을 지정해줘야한다.
@DiscriminatorColumn(name = "dtype")    // 서브 클래스로 구분해놓은 타입을 구분해주는 컬럼을 추가해준다.
public abstract class Item {

    /**
     * 기본 키
     * */
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;
    private String title;
    private int price;
    private int stockQuantity;   // 재고 수량


    /**
     * 연관 관계
     * */
    @ManyToMany(mappedBy = "itemList")
    private List<Category> categoryList = new ArrayList<>();


    /**
     * 설정자
     * */
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /**
     * 비즈니스 로직  - 값을 조정하는 메서드를 해당 값이 있는 클래스 내에 작성. 응집도 상승. 객체지향적.
     * */
    // 재고 증가
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    // 재고 감소
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more Stock");
        }

        this.stockQuantity = restStock;
    }
}
