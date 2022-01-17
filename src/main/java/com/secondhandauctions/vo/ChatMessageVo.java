package com.secondhandauctions.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ChatMessageVo {

    private int roomNo;
    private String memberId;
    private Date regdate;
    private String msg;
}
