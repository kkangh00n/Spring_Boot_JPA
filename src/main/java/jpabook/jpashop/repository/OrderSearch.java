package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//검색
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;
}
