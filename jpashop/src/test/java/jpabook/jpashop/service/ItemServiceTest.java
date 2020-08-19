package jpabook.jpashop.service;

import jpabook.jpashop.domain.entity.Item;
import jpabook.jpashop.domain.entity.itemSub.Album;
import jpabook.jpashop.domain.entity.itemSub.Book;
import jpabook.jpashop.domain.entity.itemSub.Movie;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
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
public class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 엘범등록() throws Exception{
        // given
        Album album = new Album();
//        item.setTitle("너를 태우고");

        // when
        Long savedId = itemService.saveItem(album);

        // then
        assertEquals(album, itemRepository.findOne(savedId));
    }

    @Test
    public void 책등록() throws Exception{
        // given
        Book book = new Book();
//        item.setTitle("너를 태우고");

        // when
        Long savedId = itemService.saveItem(book);

        // then
        assertEquals(book, itemRepository.findOne(savedId));
    }

    @Test
    public void 영화등록() throws Exception{
        // given
        Movie movie = new Movie();
//        item.setTitle("너를 태우고");

        // when
        Long savedId = itemService.saveItem(movie);

        // then
        assertEquals(movie, itemRepository.findOne(savedId));
    }

    @Test
    public void 아이템중복등록확인() {
        // given
        Album album1 = new Album();
        album1.setTitle("너를 태우고");

        Album album2 = new Album();
        album2.setTitle("너를 태우고");

        // when
        itemService.saveItem(album1);
        itemService.saveItem(album2);

        // then
    }

}