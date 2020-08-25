package jpabook.jpashop;

import jpabook.jpashop.domain.embedded.Address;
import jpabook.jpashop.domain.entity.Delivery;
import jpabook.jpashop.domain.entity.Member;
import jpabook.jpashop.domain.entity.Order;
import jpabook.jpashop.domain.entity.OrderItem;
import jpabook.jpashop.domain.entity.itemSub.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;



// 데이터를 넣어 테스트하기 위해 작성한 클래스
@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init(){

        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;

        public void dbInit1() {
            Member member = createMember("Ruby","두암동", "삼정로", "11111" );
            em.persist(member);

            Book book1 = createBook("JPA", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("MVC", 12000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 12000, 2);

            Delivery delivery = createDelivery(member.getAddress());

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);

        }

        public void dbInit2() {
            Member member = createMember("Eun","일곡동", "허밍", "22222");
            em.persist(member);

            Book book1 = createBook("인생수업", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("의룡", 4500, 30);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 4500, 5);

            Delivery delivery = createDelivery(member.getAddress());

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);

        }

        public Member createMember(String name, String city, String street, String zipCode){
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipCode));

            return member;
        }

        public Book createBook(String title, int price, int stockQuantity){
            Book book = new Book();
            book.setTitle(title);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);

            return book;
        }

        public Delivery createDelivery(Address address){
            Delivery delivery = new Delivery();
            delivery.setAddress(address);

            return delivery;
        }
    }

}

