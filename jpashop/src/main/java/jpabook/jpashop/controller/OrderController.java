package jpabook.jpashop.controller;

import jpabook.jpashop.domain.entity.Item;
import jpabook.jpashop.domain.entity.Member;
import jpabook.jpashop.domain.entity.Order;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model){

        List<Member> memberList = memberService.findMemberList();
        List<Item> itemList = itemService.findItemList();

        model.addAttribute("memberList", memberList);
        model.addAttribute("itemList", itemList);

        return "orders/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count){

        orderService.order(memberId, itemId, count);

        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch")OrderSearch orderSearch, Model model){
        List<Order> orderList = orderService.findOrders(orderSearch);
        model.addAttribute("orderList", orderList);

        return "orders/orderList";
    }

    @PostMapping("orders/{orderId}/cancel")
    public String cancleOrder(@PathVariable("orderId") Long orderId){
        orderService.cancelOrder(orderId);

        return "redirect:/orders";
    }


}
