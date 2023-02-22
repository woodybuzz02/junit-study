package com.example.junitproject.service;

import com.example.junitproject.domain.Book;
import com.example.junitproject.domain.BookRepository;
import com.example.junitproject.util.MailSender;
import com.example.junitproject.web.dto.response.BookListRespDto;
import com.example.junitproject.web.dto.response.BookRespDto;
import com.example.junitproject.web.dto.request.BookSaveReqDto;
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
    private final MailSender mailSender;

    // 1. 책등록
    @Transactional(rollbackFor = RuntimeException.class) // RuntimeException발생하면 롤백 발생
    public BookRespDto 책등록하기(BookSaveReqDto dto){
        Book bookPs = bookRepository.save(dto.toEntity()); // 영속화된 객체
        if (bookPs != null){
            if(!mailSender.send()){
                throw new RuntimeException("메일이 전송되지 않았습니다");
            }
        }
        return bookPs.toDto();
    }
    
    // 2. 책목록보기
    public BookListRespDto 책목록보기(){
        List<Book> bookPsList = bookRepository.findAll(); //영속화된 북을 리턴함
        List<BookRespDto> bookRespDtoList = new ArrayList<>();
        for(Book b : bookPsList){
            bookRespDtoList.add(b.toDto());
        }

        BookListRespDto bookListRespDto = BookListRespDto.builder().items(bookRespDtoList).build();
        return bookListRespDto;
    }

    // 3. 책한건보기
    public BookRespDto 책한건보기(Long id){
        Optional<Book> bookOP = bookRepository.findById(id);
        if(bookOP.isPresent()){
            return bookOP.get().toDto();
        }else{
            throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
        }

    }
    // 4. 책삭제
    @Transactional(rollbackFor = RuntimeException.class)
    public void 책삭제하기(Long id){
        bookRepository.deleteById(id);
    }

    // 5. 책수정
    @Transactional(rollbackFor = RuntimeException.class)
    public BookRespDto 책수정하기(Long id, BookSaveReqDto dto){
        Optional<Book> bookOP = bookRepository.findById(id);
        if(bookOP.isPresent()){
            Book bookPs = bookOP.get();
            bookPs.update(dto.getTitle(), dto.getAuthor());
            return bookPs.toDto();
        }else{
            throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
        }
    }// 메서드 종료시에 더티체킹(flush)으로 update 된다.
}
