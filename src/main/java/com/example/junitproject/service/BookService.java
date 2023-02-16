package com.example.junitproject.service;

import com.example.junitproject.domain.Book;
import com.example.junitproject.domain.BookRepository;
import com.example.junitproject.web.dto.BookRespDto;
import com.example.junitproject.web.dto.BookSaveReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    // 1. 책등록
    @Transactional(rollbackFor = RuntimeException.class) // RuntimeException발생하면 롤백 발생
    public BookRespDto 책등록하기(BookSaveReqDto dto){
        Book bookps = bookRepository.save(dto.toEntity()); // 영속화된 객체
        return new BookRespDto().toDto(bookps);
    }
    
    // 2. 책목록보기

    // 3. 책한건보기

    // 4. 책삭제

    // 5. 책수정
}
