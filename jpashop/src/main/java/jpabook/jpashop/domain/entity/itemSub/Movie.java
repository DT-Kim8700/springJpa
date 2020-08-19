package jpabook.jpashop.domain.entity.itemSub;

import jpabook.jpashop.domain.entity.Item;
import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@DiscriminatorValue("M")
public class Movie extends Item {

    private String director;
    private String actor;
}
