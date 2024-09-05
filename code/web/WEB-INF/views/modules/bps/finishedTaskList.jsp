]<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>待办任务管理</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/bps/workflow/">已办任务</a></li>
		<shiro:hasPermission name="bps:workflow:view"><li><a href="${ctx}/bps/workflow/showWFGraph?processInstID=">流程跟踪</a></li></shiro:hasPermission>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tr><th>业务名称</th><th>业务说明</th><th>活动名称</th><th>办理人员</th><th>状态</th><th>是否超时</th><th>时间限制</th><th>开始时间</th><th>结束时间</th><shiro:hasPermission name="bps:workflow:edit"><th>操作</th></shiro:hasPermission></tr>
		<c:forEach items="${list}" var="workItem">
			<tr>
				<td>${workItem.processChName}</td>
				<td>${workItem.processInstName}</td>
				<td>${workItem.activityInstName}</td>
				<td>${workItem.participant}</td>
				<td>${workItem.participant}</td>
				<td>
					<c:choose>
						<c:when test="${'4' eq workItem.currentState}">待领取</c:when>
						<c:when test="${'8' eq workItem.currentState}">挂起</c:when>
						<c:when test="${'10' eq workItem.currentState}">运行 </c:when>
						<c:when test="${'12' eq workItem.currentState}">完成 </c:when>
						<c:when test="${'13' eq workItem.currentState}">终止</c:when>
					</c:choose>
				</td>
				<td>${'Y' eq workItem.isTimeOut?"是":"否"}</td>
				<td>${workItem.limitNum }</td>
				<td>${workItem.startTime  }</td>
				<td>${workItem.endTime  }</td>
				<shiro:hasPermission name="bps:workflow:view"><td>
					<a href="${ctx}/bps/workflow/showWFGraph?processInstID=${workItem.processInstID}">流程跟踪</a>
					<a href="${ctx}/sys/role/assign?id=${role.id}">办理</a>
				</td></shiro:hasPermission>	
			</tr>
		</c:forEach>
	</table>
</body>
</html>