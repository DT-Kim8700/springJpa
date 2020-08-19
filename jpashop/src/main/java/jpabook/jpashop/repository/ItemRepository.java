package jpabook.jpashop.repository;

import jpabook.jpashop.domain.entity.Item;
import jpabook.jpashop.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
// 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성해 준다.
public class ItemRepository {

    private final EntityManager em;


    // 상품 등록 및 갱신
    public void save(Item item) {
        if (item.getId() == null) {     // 새로 등록할 아이템은 id값을 가지고 있지 않다. 때문에 새로 등록하기 위해 persist를 해준다.
            em.persist(item);
        } else {
            em.merge(item);
            // 이미 id가 등록되어 있는 아이템. 즉, DB에서 값을 얻어온 아이템을 갱신 시키는 경우.
            // merge(병합) : 준영속 상태의 엔티티를 영속 상태로 변경할 때 시용하는 기능
            //              merge는 필드의 모든 값을 바꾼다. 때문에 필드의 일부 값이 null 이 될 위험이 존재한다.
            //              되도록 merge를 쓰지 않고 변경감지 방향으로 원하는 값만 변경하여 작업하는 것이 안전.
        }
    }

    // 상품 하나 조회
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findByTitle(String title){
        return em.createQuery("select i from Item i where i.title = :title", Item.class)
                .setParameter("title", title)
                .getResultList();
    }

    // 모든 상품 조회
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
