package com.example.junitproject.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CMRespDto<T> { // 공통의 dto
    private Integer code; // 1 성공, -1 실패
    private String msg; // 에러메시지, 성공에 대한 메시지
    private T body;

    @Builder
    public CMRespDto(Integer code, String msg, T body) {
        this.code = code;
        this.msg = msg;
        this.body = body;
    }
}
