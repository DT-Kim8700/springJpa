package jpabook.jpashop.repository;

import jpabook.jpashop.domain.embedded.Address;
import jpabook.jpashop.domain.entity.Order;
import jpabook.jpashop.domain.enumC.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    // 주문 등록
    public void save(Order order){
        em.persist(order);
    }

    // 단건 조회
    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    // 검색
    // 1. 문자열 조립으로 쿼리문을 만드는 방식 - 코드가 너무 길어짐
    public List<Order> findAllByString(OrderSearch orderSearch) {
        StringBuffer query = new StringBuffer("select o from Order o join o.member m");
        boolean isFirstCondition = true;    // query 문자열 뒤에 붙일 단어가 where 일지 and 일지 판단해주는 논리값

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null){
            if(isFirstCondition){
//                query += " where";
                query.append(" where");
                isFirstCondition = false;
            } else {
//                query += " and";
                query.append(" and");
            }
//            query += " o.orderStatus = :orderStatus";
            query.append(" o.orderStatus = :orderStatus");
        }

        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())){
            if(isFirstCondition){
//                query += " where";
                query.append(" where");
                isFirstCondition = false;
            } else {
//                query += " and";
                query.append(" and");
            }
//            query += " m.name like :name";
            query.append(" m.name like :name");
        }

        TypedQuery<Order> orderTypedQuery = em.createQuery(query.toString(), Order.class)        // 최대 1000건
                                            .setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null){
            orderTypedQuery = orderTypedQuery.setParameter("orderStatus", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            orderTypedQuery = orderTypedQuery.setParameter("name", orderSearch.getMemberName());
        }

        return orderTypedQuery.getResultList();
    }


    // 2. JPA Criteria 활용  - 흐름 변화가 눈에 잘 보이지 않음. 유지 보수에 좋지 못하다.
    public List<Order> findAllByCriteria(OrderSearch orderSearch){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null){
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        // 주문 상태 검색
        if (StringUtils.hasText(orderSearch.getMemberName())){
            Predicate name = cb.like(m.get("status"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));

        return em.createQuery(cq).setMaxResults(1000).getResultList();
    }


    public List<Order> findAllMemberWithDelivery() {
        return em.createQuery(
                "select o from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d", Order.class)
                .getResultList();

    }

}