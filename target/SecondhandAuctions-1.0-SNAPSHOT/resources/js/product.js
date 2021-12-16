function numberWithCommas(startPrice) {
    startPrice = startPrice.replace(/[^0-9]/g,'');   // 입력값이 숫자가 아니면 공백
    startPrice = startPrice.replace(/,/g,'');          // ,값 공백처리

    $("#startPrice").val(startPrice.replace(/\B(?=(\d{3})+(?!\d))/g, ",")); // 정규식을 이용해서 3자리 마다 , 추가
}

function numberComparison(bidPrice, startPrice) {
    var chk = false;

    bidPrice = Number(bidPrice.replaceAll(",", ""));
    startPrice = Number(startPrice.replaceAll(",", ""));

    console.log("bidPrice :: " + bidPrice);
    console.log("startPrice :: " + startPrice);

    if (bidPrice <= startPrice) {
        return chk;
    }

    chk = true

    return chk;
}