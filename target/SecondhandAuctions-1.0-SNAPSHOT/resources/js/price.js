function priceStrFormatNumber(priceStr) {
    var price;

    price = Number(priceStr.replaceAll(",", ""));

    return price;
}