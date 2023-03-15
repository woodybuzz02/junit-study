package com.example.junitproject.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles("dev")
@DataJpaTest // DB와 관련된 컴포넌트만 메모리에 로딩
public class BookRepositoryTest {

    @Autowired // DI
    private BookRepository bookRepository;

    @BeforeEach
    public void 데이터준비(){

        String title = "junit5";
        String author = "메타코딩";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        bookRepository.save(book);

    } // 트랜잭션(데이터베이스의 상태를 변환시키는 하나의 논리적 기능을 수행하기 위한 작업의 단위)종료 됐다면 말이 안됨
    // 데이터준비()+책등록_test()하고 트랜잭션 종료

    // 1. 책 등록
    @Test
    public void 책등록_test() {

        // given (데이터 준비)

        String title = "junit5";
        String author = "겟인데어";
        Book book = Book.builder()
                    .title(title)
                    .author(author)
                    .build();

        // when (테스트 실행)

        Book bookPS = bookRepository.save(book);

        // DB에 저장(primary key 생성 = id 생성완료)
        // save 메서드가 DB에 저장된 Book을 return (DB데이터와 동기화된 데이터)
        // PS = 영속화

        // then (검증)

        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());

    } // 트랜잭션 종료 (저장된 데이터를 초기화함)

    // 2. 책 목록보기

    @Test
    public void 책목록보기_test(){

        // given (데이터 준비)

        String title = "junit5";
        String author = "메타코딩";

        // when (테스트 실행)

        List<Book> bookPSList = bookRepository.findAll();

        // then (검증)

        //assertEquals("junit4", bookPSList.get(0).getTitle()); // 일부러 오류 한번 내봄.
        assertEquals(title, bookPSList.get(0).getTitle());
        assertEquals(author, bookPSList.get(0).getAuthor());

    }

    // 3. 책 한건보기

    @Sql("classpath:db/tableInit.sql")
    // 굳이 ID로 검증할 거면 날려야된다. 실제 서버로 테스트하면 ID 검증을 하지마라. 다른 방식으로 해라.
    @Test
    public void 책한건보기_test(){

        // given (데이터 준비)

        String title = "junit5";
        String author = "메타코딩";

        // when (테스트 실행)

        Book bookPS = bookRepository.findById(1L).get();

        // then (검증)

        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());

    }

    // 4. 책 수정

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책수정_test(){

        // given (데이터 준비)

        Long id = 1L;
        String title = "junit5";
        String author = "겟인데어";
        Book book = new Book(id, title, author);

        // when (테스트 실행)

        Book bookPS = bookRepository.save(book);

//        bookRepository.findAll().stream()
//                .forEach((b)->{
//                    System.out.println(b.getId());
//                    System.out.println(b.getTitle());
//                    System.out.println(b.getAuthor());
//                    System.out.println("====================================");
//                });

        // then (검증)

        assertEquals(id, bookPS.getId());
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());

    }

    // 5. 책 삭제

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책삭제_test(){


        // 통합테스트를 할 때 오류 발생
        // 1. 테스트 메서드가 여러 개 있을 때 실행 순서가 보장이 되지 않는다.
        // Order()라는 어노테이션을 써서 순서를 보장해야된다.
        // 2. 테스트 메서드가 하나 실행 후 종료되면 데이터가 초기화된다. - Transactional() 어노테이션
        // primary key auto_increment값이 초기화가 안됨.


        // given (데이터 준비)

        Long id = 1L;

        // when (테스트 실행)

        bookRepository.deleteById(id);

        // then (검증)

        Optional<Book> bookPS = bookRepository.findById(1L);
        // Optional = null이 올 수도 있게 하는 것,  null이 올 수 있는 값을 감싸는 Wrapper 클래스로, 참조하더라도 NPE가 발생하지 않도록 도와준다.

        assertFalse(bookPS.isPresent()); // false일 때 성공하는 것
        
    }

}
