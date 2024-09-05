<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>修改密码</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#oldPassword").focus();
			$("#inputForm").validate({
				rules: {
				},
				messages: {
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
				submitHandler: function(form){
                    var psw = $("#confirmNewPassword").val();
                    if (psw.length > 0) {
                        if (!isPswComplex(psw)) {
                            layer.alert("密码需至少包含大小写字母、数字、特殊字符，且长度至少为8");
                            return;
                        }
                    }
                    var oldPassword = $("#oldPassword").val();
                    if (psw == oldPassword) {
                        layer.msg("抱歉，密码不能重复使用");
                        $("#newPassword").select();
                        return;
					}
                    if (!isPswTimeEnough()) {
                        layer.alert("抱歉，您的密码使用时间少于1天，不能修改");
                        return;
                    }
                    loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});

		function isPswTimeEnough() {
		    var res = true;
            $.ajax({
                type: "POST",
                url: ctx + "/common/comCon/isPswUseTimeEnough",
                async: false,
                success: function(data){
                    // alert("data = " + data);
                    if (!data) {
                        // alert("false");
                        res = false;
                    }
                    // if (data) {
                    //     alert("true");
                    //     return true;
                    // }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    jBox.tip('密码使用时间检测失败', 'error');
                    res = false;
                    return false;
                }
            });
            return res;
        }
	</script>
</head>
<body>
	<%--<ul class="nav nav-tabs">--%>
		<%--<li><a href="${ctx}/sys/user/info">个人信息</a></li>--%>
		<%--<li class="active"><a href="${ctx}/sys/user/modifyPwd">修改密码</a></li>--%>
	<%--</ul><br/>--%>
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/modifyPwd" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">旧密码:</label>
			<div class="controls">
				<input id="oldPassword" name="oldPassword" type="password" value="" maxlength="50" minlength="3" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">新密码:</label>
			<div class="controls">
				<input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="8" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">确认新密码:</label>
			<div class="controls">
				<input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="8" class="required" equalTo="#newPassword"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
		</div>
	</form:form>
</body>
</html>