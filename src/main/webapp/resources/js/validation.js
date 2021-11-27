function checkSpace(str) {
    if (str.search(/\s/) != -1) {
        return true;
    } else {
        return false;
    }
}

function isValidationPwd(str) {
    var cnt = 0; // 중복된 숫자 걸러내기
    var isPW = /^[A-Za-z0-9`\-=\\\[\];',\./~!@#\$%\^&\*\(\)_\+|\{\}:"<>\?]{8,16}$/;

    if (str == "") {
        return false;
    }

    var retVal = checkSpace(str);

    if (retVal) {
        return false;
    }

    if (str.length < 8) {
        return false;
    }

    // 중복된 숫자 걸러 내기
    for (var i = 0; i < str.length; ++i) {
        if (str.charAt(0) == str.substring(i, i + 1))
            ++cnt;
    }
    if (cnt == str.length) {
        return false;
    }

    if (!isPW.test(str)) {
        return false;
    }

    return true;
}