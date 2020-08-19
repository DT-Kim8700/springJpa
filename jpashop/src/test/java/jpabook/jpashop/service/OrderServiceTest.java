package jpabook.jpashop.service;

import jpabook.jpashop.domain.embedded.Address;
import jpabook.jpashop.domain.entity.Member;
import jpabook.jpashop.domain.entity.Order;
import jpabook.jpashop.domain.entity.itemSub.Book;
import jpabook.jpashop.domain.enumC.OrderStatus;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)        // junit 실행할 떄 스프링과 같이 엮어서 실행할 것인가를 설정.
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 상품주문() throws Exception{
        // given
        Member member = createMember();

        Book book = createBook("의룡", 4500, 25);

        // when
        int orderCount = 20;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order findOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, findOrder.getOrderStatus());    // 상태가 맞는지 확인
        assertEquals("주문 상품 종류 수가 정확해야 함", 1, findOrder.getOrderItemList().size());    // 주문한 상품 종류 수 확인
        assertEquals("주문 가격", 4500 * orderCount, findOrder.getTotalPrice());    // 주문 총 가격 확인
        assertEquals("주문 수량만큼 재고 감소", 5, book.getStockQuantity());    // 주문 재고 변동 확인

    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{
        // given
        Member member = createMember();
        Book book = createBook("의룡", 4500, 25);

        int orderCount = 30;    // 재고가 25권인데 30권 주문하려고 한다.

        // when
        orderService.order(member.getId(), book.getId(), orderCount);

        // then
        fail("재고 수량 부족 예외가 발생해야 합니다.");
    }

    @Test
    public void 주문취소() throws Exception{
        // given
        Member member = createMember();
        Book book = createBook("의룡", 4500, 25);

        int orderCount = 20;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order findOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소시 상태는 CANCEL", OrderStatus.CANCLE, findOrder.getOrderStatus());  // 주문 취소시 상태가 CANCLE 바뀌었는지 확인
        assertEquals("주문이 취소된 상품은 그만큼 재고가 복구", 25, book.getStockQuantity());  // 주문 취소시 재고가 복구되었는지 확인

    }



    private Book createBook(String title, int price, int stockQuantity) {
        Book book = new Book();
        book.setTitle(title);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("Ruby");
        member.setAddress(new Address("광주", "두암동", "19457"));
        em.persist(member);
        return member;
    }
}