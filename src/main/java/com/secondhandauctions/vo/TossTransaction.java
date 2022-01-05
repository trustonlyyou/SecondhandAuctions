package com.secondhandauctions.vo;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TossTransaction {

    private String transactionKey;
    private String paymentKey;
    private String orderId;
    private String currency;
    private String customerKey;
    private String method;
    private boolean useEscrow;
    private Integer amount;

}
