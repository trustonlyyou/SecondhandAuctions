function numberFormatter(startPrice) {
    startPrice = startPrice.replace(/[^0-9]/g,'');   // 입력값이 숫자가 아니면 공백
    startPrice = startPrice.replace(/,/g,'');          // ,값 공백처리

    return startPrice;
}