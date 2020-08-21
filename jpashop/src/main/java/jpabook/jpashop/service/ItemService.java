package jpabook.jpashop.service;

import jpabook.jpashop.domain.entity.Item;
import jpabook.jpashop.domain.entity.Member;
import jpabook.jpashop.domain.entity.itemSub.Book;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional      // 단순 읽는 용도가 아니기때문에 @Transactional(readOnly = true) 영향을 받지 않아야함으로 별도로 다시 설정
    public Long saveItem(Item item){
        validateDuplicateItem(item);
        itemRepository.save(item);

        return item.getId();
    }

    @Transactional
    public void updateItem(Long itemId, String title, int price, int stockQuantity, String author, String isbn){
        Book fineItem = (Book) itemRepository.findOne(itemId);
        // 여기서 찾아온 fineItem는 영속성 컨텍스트에 관리되는 엔티티이다. 즉, 이 엔티티 내부의 변경은 변경감지가 되어 DB에 반영이 된다.

        fineItem.setTitle(title);
        fineItem.setPrice(price);
        fineItem.setStockQuantity(stockQuantity);
        fineItem.setAuthor(author);
        fineItem.setIsbn(isbn);
    }

    // 기존에 아이템이 있을 경우 해당 아이템을 갱신하는 메세지
    private void validateDuplicateItem(Item item){
        List<Item> findItem = itemRepository.findByTitle(item.getTitle());

        if(!findItem.isEmpty()) {     // 등록하려는 아이템이 이미 있다면
            System.out.println("상품을 갱신합니다.");
        }
    }

    public Item findItem(Long itemId){
        return itemRepository.findOne(itemId);
    }

    public List<Item> findItemList() {
        return itemRepository.findAll();
    }

}
