<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接口收发日志管理</title>
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
			//禁用和启用标签
			$("input").attr("disabled", true);	//禁用所有input标签
			$("textarea").attr("disabled", true);
			$("#btnCancel").attr("disabled", false);	//启用返回按钮
		});

		function clickBtn() {
			layer.alert("噶噶阿哥");
			// alertx("噶噶阿哥2");
			return;
        }
	</script>
</head>
<body>
	<%--<ul class="nav nav-tabs">--%>
		<%--<li><a href="${ctx}/inoutlog/logInout/">接口收发日志列表</a></li>--%>
		<%--&lt;%&ndash;<li class="active"><a href="${ctx}/inoutlog/logInout/form?id=${logInout.id}">接口收发日志<shiro:hasPermission name="inoutlog:logInout:edit">${not empty logInout.id?'修改/查看':'添加'}</shiro:hasPermission><shiro:lacksPermission name="inoutlog:logInout:edit">查看</shiro:lacksPermission></a></li>&ndash;%&gt;--%>
		<%--<li class="active"><a href="${ctx}/inoutlog/logInout/form?id=${logInout.id}">接口收发日志查看</a></li>--%>
	<%--</ul><br/>--%>
	<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
		<legend style="margin-left: 0px;">接口收发日志信息</legend>
	</fieldset>
	<form:form id="inputForm" modelAttribute="logInout" action="${ctx}/inoutlog/logInout/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">源业务系统名称：</label>
			<div class="controls">
				<%--<form:input path="sourceSysName" htmlEscape="false" maxlength="50" class="input-xlarge "/>--%>
					${logInout.sourceSysName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">目的业务系统名称：</label>
			<div class="controls">
				<%--<form:input path="destSysName" htmlEscape="false" maxlength="50" class="input-xlarge "/>--%>
					${logInout.destSysName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">代码集名称：</label>
			<div class="controls">
				<%--<form:input path="codeSetName" htmlEscape="false" maxlength="50" class="input-xlarge "/>--%>
					${logInout.codeSetName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">记录数量：</label>
			<div class="controls">
				<%--<form:input path="recordAmount" htmlEscape="false" maxlength="18" class="input-xlarge  digits"/>--%>
					${logInout.recordAmount}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始操作时间：</label>
			<div class="controls">
				<%--<input name="startDate" id="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "--%>
					<%--value="<fmt:formatDate value="${logInout.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"--%>
					<%--onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>--%>
				<%--<a onclick='javascript:$("#startDate").val("");' style="cursor: pointer">清空</a>--%>
				<fmt:formatDate value="${logInout.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束操作时间：</label>
			<div class="controls">
				<%--<input name="endDate" id="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "--%>
					<%--value="<fmt:formatDate value="${logInout.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"--%>
					<%--onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>--%>
				<!--<a onclick='javascript:$("#endDate").val("");' style="cursor: pointer">清空</a>-->
				<fmt:formatDate value="${logInout.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</div>
		</div>
		<div class="control-group">
		<!-- 0：失败：1：成功。 -->
			<label class="control-label">状态：</label>
			<div class="controls">
				<%-- <form:input path="status" htmlEscape="false" maxlength="18" class="input-xlarge  digits"/> --%>
				<%--<input name="status" value="${logInout.status == 1 ? '成功' : '失败'}" type="text" readonly="readonly" maxlength="20"/>--%>
						${logInout.status == 1 ? '成功' : '失败'}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">失败原因：</label>
			<div class="controls">
				<%--<form:input path="failReason" htmlEscape="false" maxlength="300" class="input-xlarge "/>--%>
					${logInout.failReason}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="100" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<!-- 0：接收，1：发送 -->
			<label class="control-label">记录类型：</label>
			<div class="controls">
				<%-- <form:input path="recordType" htmlEscape="false" maxlength="18" class="input-xlarge  digits"/> --%>
				<%--<input name="recordType" value="${logInout.recordType == 1 ? '发送' : '接收'}" type="text" readonly="readonly" maxlength="20"/>--%>
						${logInout.recordType == 1 ? '数据分发' : '数据核查'}
			</div>
		</div>
		<div class="control-group">
			<!-- 调用接口时产生唯一标识，如果有分页情况，消息id保持一致。 -->
			<label class="control-label">消息id：</label>
			<div class="controls">
				<%--<form:input path="messageId" htmlEscape="false" maxlength="50" class="input-xlarge "/>--%>
					${logInout.messageId}
			</div>
		</div>
		<div class="control-group">
			<!-- 如果有分页显示传输数据当前页数，如果不分页默认为第一页： -->
			<label class="control-label">当前页：</label>
			<div class="controls">
				<%--<form:input path="currentPage" htmlEscape="false" maxlength="18" class="input-xlarge  digits"/>--%>
					${logInout.currentPage}
			</div>
		</div>
		<div class="form-actions">
			<%--<shiro:hasPermission name="inoutlog:logInout:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>--%>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			<%--<input type="button" value="其实我是一个按钮" class="btn" onclick="clickBtn();">--%>
			<%--<button class="btn" onclick="clickBtn();">其实我是一个按钮</button>--%>
		</div>
	</form:form>
</body>
</html>