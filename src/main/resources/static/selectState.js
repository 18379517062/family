$(function selectState() {
    // 获取选中的值
    var objS = document.getElementById("status");
    var status = objS.options[objS.selectedIndex].value;
    $.ajax({
        type : "post",
        url :"selectState",
        data : {"status":status},
        dataType : 'json',
        success : function(data) {
            // alert(data.returnMsg);
            var list = data.categoryList;
            $("#categoryId").empty();         //清空所有option
            $("#categoryId").append("<option value= ''>"+"--请选择--"+"</option>")
            for (var i = 0; i < list.length; i++) {
                $("#categoryId").append("<option value= "+list[i].categoryId+">"+list[i].categoryName+"</option>")
            }


        },error:function(){
            alert("error");
        }
    });

})