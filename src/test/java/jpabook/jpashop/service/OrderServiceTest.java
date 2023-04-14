package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.fail;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        Member member = createMember();
        Book book = createBook();

        int orderCount=2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문 시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격 = 가격*수량",10000*2, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고 감소", 8, book.getStockQuantity());
    }

    @Test()
    public void 상품주문_재고수량초과() throws Exception{
        Member member = createMember();
        Book book = createBook();

        int orderCount=11;

        try{
            orderService.order(member.getId(), book.getId(), orderCount);
        }catch(NotEnoughStockException e){
            return ;
        }
        fail("재고 수량 부족 예외 발생 X");
    }

    @Test
    public void 주문취소() throws Exception{
        Member member = createMember();
        Book book = createBook();

        int orderCount=2;

        Long order = orderService.order(member.getId(), book.getId(), orderCount);

        orderService.cancelOrder(order);


        Order getOrder = orderRepository.findOne(order);
        assertEquals("주문 취소시 상태는 CANCEL", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 재고 원복", 10, book.getStockQuantity());
    }






    private Book createBook() {
        Book book = new Book();
        book.setName("JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }
}