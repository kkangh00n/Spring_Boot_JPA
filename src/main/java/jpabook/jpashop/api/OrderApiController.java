package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.dto.orderDto.OrderDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){

        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName();        //Lazy 강제 초기화
            order.getDelivery().getAddress();       //Lazy 강제 초기화

            List<OrderItem> orderItems = order.getOrderItems();         //Lazy 강제 초기화
            orderItems.stream().forEach(o->o.getItem().getName());      //Lazy 강제 초기화
        }

        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> orderV3(){
        List<Order> orders = orderRepository.findAllWithItem();
        for (Order order : orders) {
            System.out.println("order.getId() = " + order.getId());
        }
        
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> orderV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                       @RequestParam(value = "limit", defaultValue = "100") int limit){
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());

        return result;
    }
}
