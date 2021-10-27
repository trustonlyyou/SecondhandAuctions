<%--
  Created by IntelliJ IDEA.
  User: junghwan
  Date: 2021/10/16
  Time: 11:48 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">

</head>
<body>
    <div class="form_section">
        <div class="form_select_title">
            <label>상품 이미지</label>
        </div>
        <div class="form_section_content">
            <input type="file" multiple id="uploadFile" name="uploadFile" style="height: 30px;">
        </div>
    </div>



</body>
<script>
    $("input[type='file']").on("change", function (e) {

        let formData = new FormData();
        let fileInput = $('input[name="uploadFile"]');
        let fileList = fileInput[0].files;
        let fileObj = fileList[0];

        console.log("fileList : " + fileList);
        console.log("fileObj : " + fileObj);
        console.log("fileType : " + fileObj.type);
        console.log("fileName : " + fileObj.name);
        console.log("fileSize : " + fileObj.size);
        console.log("fileType(MimeType) : " + fileObj.type);

        if(!fileCheck(fileObj.name, fileObj.size)){
            return false;
        }

        formData.append("uploadFile", fileObj);

        $.ajax({
            url: '/uploadAjaxAction',
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            data: formData,
            type: 'POST',
            dataType: 'json',

            success: function (result) {
                alert(result.message)
            },
            error: function (request,status,error) {
                alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            }
        });

    });

    let regex = new RegExp("(.*?)\.(jpg|png)$");
    let maxSize = 1048576; //1MB

    function fileCheck(fileName, fileSize){

        if(fileSize >= maxSize){
            alert("파일 사이즈 초과");
            return false;
        }

        if(!regex.test(fileName)){
            alert("해당 종류의 파일은 업로드할 수 없습니다.");
            return false;
        }

        return true;
    }
</script>
</html>
