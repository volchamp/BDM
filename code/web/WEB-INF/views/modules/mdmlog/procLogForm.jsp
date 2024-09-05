<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>存储过程日志管理</title>
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
	<%--<ul class="nav nav-tabs">--%>
		<%--<li><a href="${ctx}/mdmlog/procLog/">存储过程日志列表</a></li>--%>
		<%--&lt;%&ndash;<li class="active"><a href="${ctx}/mdmlog/procLog/form?id=${procLog.id}">存储过程日志<shiro:hasPermission name="mdmlog:procLog:edit">${not empty procLog.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="mdmlog:procLog:edit">查看</shiro:lacksPermission></a></li>&ndash;%&gt;--%>
		<%--<li class="active"><a href="${ctx}/mdmlog/procLog/form?id=${procLog.id}">存储过程日志查看</a></li>--%>
	<%--</ul><br/>--%>
	<form:form id="inputForm" modelAttribute="procLog" action="${ctx}/mdmlog/procLog/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">存储过程名：</label>
			<div class="controls">
				<form:input path="procName" htmlEscape="false" maxlength="50" class="input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">原因：</label>
			<div class="controls">
				<form:textarea path="procReason" htmlEscape="false" rows="4" maxlength="1000" class="input-xxlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">SQL语句：</label>
			<div class="controls">
				<form:textarea path="procSql" htmlEscape="false" rows="4" maxlength="2000" class="input-xxlarge " readonly="true" cssStyle="height: 400px;"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">执行时间：</label>
			<div class="controls">
				<input name="procTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${procLog.procTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" disabled/>
			</div>
		</div>
		<div class="form-actions">
			<%--<shiro:hasPermission name="mdmlog:procLog:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>--%>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>