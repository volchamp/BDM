<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>日志查看</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
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
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="log" action="${ctx}/mdmlog/log/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">操作：</label>
			<div class="controls">
					${log.title == '系统登录' ? '系统登录成功' : log.title}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">登录名：</label>
			<div class="controls">
					${log.createBy.loginName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">操作用户：</label>
			<div class="controls">
					${log.createBy.name}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">URI：</label>
			<div class="controls">
				${log.requestUri}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">提交方式：</label>
			<div class="controls">
					${log.method}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">操作者IP：</label>
			<div class="controls">
					${log.remoteAddr}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">操作时间：</label>
			<div class="controls">
				<fmt:formatDate value="${log.createDate}" type="both"/>
			</div>
		</div>
		<c:if test="${not empty log.exception}">
			<div class="control-group">
				<label class="control-label">异常信息：</label>
				<div class="controls">
						<%--${fn:replace(fn:replace(fns:escapeHtml(log.exception), "\n", '<br/>'), "\t", '&nbsp; &nbsp; ')}--%>
						${fns:escapeHtml(log.exception)}
				</div>
			</div>
		</c:if>
		<%--<div class="form-actions">--%>
			<%--<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>--%>
		<%--</div>--%>
	</form:form>
</body>
</html>