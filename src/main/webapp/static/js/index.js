function login () {
    var userName=$("input[name='username']").val();
    var userPwd=$("input[name='password']").val();
    if(isEmpty(userName)){
        alert("请输入用户名!");
        return;
    }
    if(isEmpty(userPwd)){
        alert("请输入密码!");
        return;
    }
    $.ajax({
        type:"post",
        url:ctx+"/user/login",
        data:{
            userName:userName,
            userPwd:userPwd
        },
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                //console.log(data);
                // 用户信息写入cookie
                $.cookie("userIdStr", data.result.userIdStr);
                $.cookie("userName", data.result.userName);
                $.cookie("trueName", data.result.trueName);
                window.location.href = ctx + "/main";
            } else {
                alert(data.msg);
            }
        }

    })
}