<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码集任务管理</title>
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
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/tesk/task/">代码集任务列表</a></li>
		<li class="active"><a href="${ctx}/tesk/task/form?id=${task.id}">代码集任务<shiro:hasPermission name="tesk:task:edit">${not empty task.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="tesk:task:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="task" action="${ctx}/tesk/task/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">任务处理者ID：</label>
			<div class="controls">
				<form:input path="taskHandlerId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">任务开始时间：</label>
			<div class="controls">
				<input name="taskStartDate" id="taskStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${task.taskStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<%--<a onclick='javascript:$("#taskStartDate").val("");' style="cursor: pointer">清空</a>--%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">任务处理时间：</label>
			<div class="controls">
				<input name="taskCompleteDate" id="taskCompleteDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${task.taskCompleteDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<%--<a onclick='javascript:$("#taskCompleteDate").val("");' style="cursor: pointer">清空</a>--%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">任务处理状态。0：待处理，1：已处理：</label>
			<div class="controls">
				<form:input path="taskStatus" htmlEscape="false" maxlength="18" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">业务系统名称：</label>
			<div class="controls">
				<form:input path="systemName" htmlEscape="false" maxlength="50" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">代码集目录名称：</label>
			<div class="controls">
				<form:input path="codeSetName" htmlEscape="false" maxlength="50" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">任务类型：</label>
			<div class="controls">
				<form:input path="taskTypeId" htmlEscape="false" maxlength="18" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">任务链接：</label>
			<div class="controls">
				<form:input path="taskLink" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">待处理记录ID：</label>
			<div class="controls">
				<form:input path="pendingRecordId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">数据核查记录ID：</label>
			<div class="controls">
				<form:input path="dataExceptionId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">待发送记录ID：</label>
			<div class="controls">
				<form:input path="sendingRecordId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="tesk:task:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>