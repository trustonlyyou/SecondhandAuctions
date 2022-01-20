package com.secondhandauctions.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OpenBankRequestToken {
    private String code;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String grant_type; //고정값: authorization_code}
}