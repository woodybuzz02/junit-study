package com.example.junitproject.service;

import com.example.junitproject.domain.Book;
import com.example.junitproject.domain.BookRepository;
import com.example.junitproject.web.dto.BookRespDto;
import com.example.junitproject.web.dto.BookSaveReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    // 1. 책등록
    @Transactional(rollbackFor = RuntimeException.class) // RuntimeException발생하면 롤백 발생
    public BookRespDto 책등록하기(BookSaveReqDto dto){
        Book bookPs = bookRepository.save(dto.toEntity()); // 영속화된 객체
        return new BookRespDto().toDto(bookPs);
    }
    
    // 2. 책목록보기
    public List<BookRespDto> 책목록보기(){
        List<Book> bookPsList = bookRepository.findAll(); //영속화된 북을 리턴함
        List<BookRespDto> bookRespDtoList = new ArrayList<>();
        for(Book b : bookPsList){
            bookRespDtoList.add(new BookRespDto().toDto(b));
        }
        return bookRespDtoList;
    }

    // 3. 책한건보기

    // 4. 책삭제

    // 5. 책수정
}
