package jpabook.jpashop.domain.entity.itemSub;

import jpabook.jpashop.domain.entity.Item;
import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@DiscriminatorValue("A")    // 셋팅하지 않으면 DTYPE의 값으로 클래스 이름이 들어간다.
public class Album extends Item {

    private String artist;
    private String ect;

}
