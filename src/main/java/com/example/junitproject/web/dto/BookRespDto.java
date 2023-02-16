package com.example.junitproject.web.dto;

import com.example.junitproject.domain.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BookRespDto {
    private Long id;
    private String title;
    private String author;

    public BookRespDto toDto(Book bookps){
        this.id = bookps.getId();
        this.title = bookps.getTitle();
        this.author = bookps.getAuthor();
        return this;
    }
}
