function priceStrFormatNumber(priceStr) {
    var price;

    price = Number(priceStr.replaceAll(",", ""));

    return price;
}

function numberWithCommas(startPrice) {
    startPrice = startPrice.replace(/[^0-9]/g,'');   // 입력값이 숫자가 아니면 공백
    startPrice = startPrice.replace(/,/g,'');          // ,값 공백처리

    return startPrice.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}


function comparisonPoint(getPoint, requestPoint) {
    var point = priceStrFormatNumber(getPoint);
    var reqPoint = priceStrFormatNumber(requestPoint);

    if (point < reqPoint) {
        return false;
    } else {
        return true;
    }
}