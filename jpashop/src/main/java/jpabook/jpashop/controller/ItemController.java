package jpabook.jpashop.controller;

import jpabook.jpashop.controller.form.BookForm;
import jpabook.jpashop.controller.form.MemberForm;
import jpabook.jpashop.domain.embedded.Address;
import jpabook.jpashop.domain.entity.Item;
import jpabook.jpashop.domain.entity.Member;
import jpabook.jpashop.domain.entity.itemSub.Book;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")       // 이전 페이지에서 a태그를 통한 get방식으로 접근할 때
    public String createForm(Model model){
        model.addAttribute("bookForm", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(@Valid BookForm bookForm, BindingResult result){

        if(result.hasErrors()){
            bookForm.setPrice(0);
            bookForm.setStockQuantity(0);
            return "items/createItemForm";
        }

        Book book = new Book();

        // setter를 활용하는 것보다는 엔티티 클래스에 생성할수 있는 메서드를 만들고 그것을 활용하는 측면이 더 바람직하다.
        book.setTitle(bookForm.getTitle());
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());
        book.setAuthor(bookForm.getAuthor());
        book.setIsbn(bookForm.getIsbn());

        itemService.saveItem(book);

        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model){
        List<Item> itemList = itemService.findItemList();
        model.addAttribute("itemList", itemList);

        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
        Book book = (Book)itemService.findItem(itemId);

        BookForm form = new BookForm();
        form.setId(book.getId());
        form.setTitle(book.getTitle());
        form.setPrice(book.getPrice());
        form.setStockQuantity(book.getStockQuantity());
        form.setAuthor(book.getAuthor());
        form.setIsbn(book.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form){
//        Book book = new Book();

        // 되도록 setter를 그대로 쓰지 않고 의미있는 이름의 메서드를 만들어 값을 변경시키는 것이 바람직하다. 무분별한 setter의 사용은 어떤 의도로 쓰이는지 알 수 없기 때문이다.
//        book.setId(form.getId());
//        book.setTitle(form.getTitle());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());

        // 여기까지의 book 객체는 기존 DB에서 찾아와서 생긴 영속성 컨텍스트에 의해 관리되는 엔티티가 아니다.
        // new 키워드로 새로 생성된 상태에서 아직 persist가 되지 않았으므로, 영속성 컨텍스트에 관리되지 않는 '준영속 엔티티' 이다.
        // 이 상태에서는 변경감지(dirty checking)가 적용되지 않기 때문에 값을 변동시키도 결국 DB에 반영되지 못한다.

//        itemService.saveItem(book);     merge를 활용한 변경. 되도록 쓰지 않는 것을 추천
        itemService.updateItem(itemId,
                form.getTitle(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());
        // 변경감지를 활용한 방법. 또한 @Transactional 이 붙은 서비스에서 변경을 처리하면 데이터가 변경되고 트랜젝션이 되므로 유용하다.
        return "redirect:/items";
    }
}