]<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>待办任务管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/tesk/task/?taskStatus=0">代码集任务列表</a></li>
		<li class="active"><a href="${ctx}/bps/workFlow/toDoList?processType=basicDataAudit">数据审核任务列表</a></li>
		<%--<shiro:hasPermission name="bps:workflow:view"><li><a href="${ctx}/bps/workflow/showWFGraph?processInstID=681">流程跟踪</a></li></shiro:hasPermission>--%>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th>工作项id</th>
			<th>流程实例id</th>
			<th>流程显示名称</th>
			<th>流程实例名称</th>
			<th>活动名称</th>
			<th>默认参与人</th>
			<%--<th>默认参与人id</th>--%>
			<th>状态</th>
			<th>是否超时</th>
			<th>时间限制</th>
			<th>开始时间</th>
			<th>提醒时间</th>
			<%--<shiro:hasPermission name="bps:workflow:edit">--%>
				<th>操作</th>
			<%--</shiro:hasPermission>--%>
		</tr>
		</thead>
		<tbody>
		<c:if test="${page == null || page.list == null || fn:length(page.list) == 0}">
			<tr>
				<td colspan="17" align="center" style="text-align: center">暂无相关数据</td>
			</tr>
		</c:if>
		<c:forEach items="${page.list}" var="workItem">
			<tr>
				<td>${workItem.workItemID}</td>
				<td>${workItem.processInstID}</td>
				<td>${workItem.processChName}</td>
				<td>${workItem.processInstName}</td>
				<td>${workItem.activityInstName}</td>
				<td>${workItem.partiName}</td>
				<%--<td>${workItem.participant}</td>--%>
				<td>
					<c:choose>
						<c:when test="${'4' eq workItem.currentState}">待领取</c:when>
						<c:when test="${'8' eq workItem.currentState}">挂起</c:when>
						<c:when test="${'10' eq workItem.currentState}">运行 </c:when>
						<c:when test="${'12' eq workItem.currentState}">完成 </c:when>
						<c:when test="${'13' eq workItem.currentState}">终止</c:when>
						<c:otherwise>未知</c:otherwise>
					</c:choose>
				</td>
				<td>${'Y' eq workItem.isTimeOut?"是":"否"}</td>
				<td>${workItem.limitNum }</td>
				<td>${workItem.startTime  }</td>
				<td>${workItem.remindTime  }</td>
				<td>
					<%--<shiro:hasPermission name="bps:workflow:view">--%>
					<a href="${pageContext.request.contextPath}/workflow/wfcomponent/web/showIframe.jsp?processInstID=${workItem.processInstID}">流程跟踪</a>
						<c:choose>
							<c:when test="${workItem.activityInstName == '基础数据录入'}">
								<a href="${ctx}/codesetmrg/codeSetData/index?processInstID=${workItem.processInstID}&workItemID=${workItem.workItemID}&taskStatus=0">处理</a>
							</c:when>
							<c:otherwise>
								<a href="${ctx}/businessdatamrg/pendingAudit/form?processInstID=${workItem.processInstID}&workItemID=${workItem.workItemID}">处理</a>
							</c:otherwise>
						</c:choose>
					<%--<a href="#">指派</a>--%>
					<%--</shiro:hasPermission>--%>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>