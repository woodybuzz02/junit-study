package com.example.junitproject.web;

import com.example.junitproject.service.BookService;
import com.example.junitproject.web.dto.response.BookListRespDto;
import com.example.junitproject.web.dto.response.BookRespDto;
import com.example.junitproject.web.dto.request.BookSaveReqDto;
import com.example.junitproject.web.dto.response.CMRespDto;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
public class BookApiController { // 컴포지션 = has 관계, 어떤 객체가 다른 객체의 일부분인 경우

    private final BookService bookService;
    // BookApiController가 new될 때 무조건 같이 new 되어야 한다.
    // new 키워드를 통해 생성하고, 소멸시키는 과정 필요 없이 IoC에 있는 것 의존성을 주입(DI)
    // final 키워드로 선언된 변수는, 초기에 클래스 생성 시 반드시 초기값이 세팅되어야 하고, 변하지 않는 변수
    // 생성자 부분에서 HelloRepository에 대한 초기화 코드를 누락한 상태로 애플리케이션을 실행하면, 컴파일 단계에서 에러가 발생

    // 1. 책등록
    @PostMapping("/api/v1/book")
    public ResponseEntity<?> saveBook(@RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult){
        BookRespDto bookRespDto = bookService.책등록하기(bookSaveReqDto);

        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            for(FieldError fe : bindingResult.getFieldErrors()){
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            throw new RuntimeException(errorMap.toString());
        }

        CMRespDto<?> cmRespDto = CMRespDto.builder().code(1).msg("글 저장 성공").body(bookRespDto).build();
        return new ResponseEntity<>(cmRespDto, HttpStatus.CREATED);
    }

    // 2. 책목록보기
    @GetMapping("/api/v1/book")
    public ResponseEntity<?> getBookList(){
        BookListRespDto bookListRespDto = bookService.책목록보기();
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 목록보기 성공").body(bookListRespDto).build(), HttpStatus.OK);
    }

    // 데이터를 응답을 해줄 때 리스트로 응답하는 것은 추천하지 않는다. 오브젝트로 하는 것을 추천.
    // 리스트로 할 때 페이징하면 불편

    // 3. 책한건보기
    @GetMapping("/api/v1/book/{id}")
    public ResponseEntity<?> getBookOne(@PathVariable Long id){
        BookRespDto bookRespDto = bookService.책한건보기(id);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 한건 보기 성공").body(bookRespDto).build(), HttpStatus.OK);
    }

    // 4. 책삭제하기

    @DeleteMapping("/api/v1/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id){
        bookService.책삭제하기(id);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 삭제하기 성공").body(null).build(), HttpStatus.OK);
    }

    // 5. 책수정하기
    @PutMapping("/api/v1/book/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult){
        BookRespDto bookRespDto = bookService.책수정하기(id,bookSaveReqDto);

        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            for(FieldError fe : bindingResult.getFieldErrors()){
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            throw new RuntimeException(errorMap.toString());
        }

        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 수정하기 성공").body(bookRespDto).build(), HttpStatus.OK);
    }

}
