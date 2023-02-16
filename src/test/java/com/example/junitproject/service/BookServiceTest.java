package com.example.junitproject.service;

import static org.assertj.core.api.Assertions.*;

import com.example.junitproject.domain.Book;
import com.example.junitproject.domain.BookRepository;
import com.example.junitproject.util.MailSender;
import com.example.junitproject.util.MailSenderStub;
import com.example.junitproject.web.dto.BookRespDto;
import com.example.junitproject.web.dto.BookSaveReqDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // 가짜 환경
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MailSender mailSender;

    @Test
    public void 책등록하기_테스트(){
        // given
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("junit강의");
        dto.setAuthor("메타코딩");

        // stub (행동정의)
        // 가짜여서 동작을 안하니까 이러이러할 것이라고 미리 행동을 만들어둔다. bookPs가 dto.toEntity()라고 정의하는 것 = 가설
        when(bookRepository.save(any())).thenReturn(dto.toEntity());
        when(mailSender.send()).thenReturn(true);

        // when
        BookRespDto bookRespDto = bookService.책등록하기(dto);

        // then
        // assertEquals(dto.getTitle(), bookRespDto.getTitle());
        // assertEquals(dto.getAuthor(), bookRespDto.getAuthor());
        assertThat(dto.getTitle()).isEqualTo(bookRespDto.getTitle());
        assertThat(dto.getAuthor()).isEqualTo(bookRespDto.getAuthor());
    }

    @Test
    public void 책목록보기_테스트(){
        // given // 들어가야되는 값이 없으니 given은 없다.

        // stub (행동정의)
        List<Book> books = new ArrayList<>();
        books.add(new Book(1l, "junit강의", "메타"));
        books.add(new Book(2l, "스프링강의", "겟인데어"));
        when(bookRepository.findAll()).thenReturn(books);

        // when
        List<BookRespDto> dtos = bookService.책목록보기();

        // then
        assertThat(dtos.get(0).getTitle()).isEqualTo("junit강의");
        assertThat(dtos.get(0).getAuthor()).isEqualTo("메타");
        assertThat(dtos.get(1).getTitle()).isEqualTo("스프링강의");
        assertThat(dtos.get(1).getAuthor()).isEqualTo("겟인데어");
    }

    @Test
    public void 책한건보기_테스트(){
        // given

        Long id = 1l;

        // stub (행동정의)

        Book book = new Book(1l,"junit강의","메타코딩");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        // when
        BookRespDto dto = bookService.책한건보기(id);

        // then
        assertThat(dto.getTitle()).isEqualTo("junit강의");
        assertThat(dto.getAuthor()).isEqualTo("메타코딩");

    }


    @Test
    public void 책수정_테스트(){
        // given

        Long id = 1l;
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("스프링강의");
        bookSaveReqDto.setAuthor("겟인데어");

        // stub (행동정의)
        Book book = new Book(1l,"junit강의","메타코딩");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        // when
        BookRespDto bookRespDto = bookService.책수정하기(id,bookSaveReqDto);

        // then

        assertThat(bookRespDto.getTitle()).isEqualTo("스프링강의");
        assertThat(bookRespDto.getAuthor()).isEqualTo("겟인데어");

    }
}
