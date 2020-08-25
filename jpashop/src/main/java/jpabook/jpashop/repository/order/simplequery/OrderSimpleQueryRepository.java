package jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtoList() {

        return em.createQuery(
                "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.orderStatus, d.address) " +
                        "from Order o " +
                        "join o.member m " +
                        "join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
    // new 명령어를 통해서 JPQL의 결과를 DTO로 즉시 변환
    // SELECT 절에서 원하는 데이터를 직접 선택하므로 DB -> 애플리케이션 네트웍 용량 최적화(생각보다 미비하지만....)
    // 해당 구간에서만 원하는 데이터의 DTO를 만들어서 값을 받기 때문에 재사용성이 떨어진다.
    // 리포지토리의 스펙이 API스펙에 맞춰져버린 아이러니....API 스펙이 바뀌면 이 코드 역시 바뀌어야 한다.
}
