<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>存储过程日志管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<%--<ul class="nav nav-tabs">--%>
		<%--<li class="active"><a href="${ctx}/mdmlog/procLog/">存储过程日志列表</a></li>--%>
		<%--&lt;%&ndash;<shiro:hasPermission name="mdmlog:procLog:edit"><li><a href="${ctx}/mdmlog/procLog/form">存储过程日志添加</a></li></shiro:hasPermission>&ndash;%&gt;--%>
	<%--</ul>--%>
	<form:form id="searchForm" modelAttribute="procLog" action="${ctx}/mdmlog/procLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>id：</label>
				<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>存储过程名：</label>
				<form:input path="procName" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>原因：</label>
				<form:input path="procReason" htmlEscape="false" maxlength="1000" class="input-medium"/>
			</li>
			<li><label>SQL语句：</label>
				<form:input path="procSql" htmlEscape="false" maxlength="2000" class="input-medium"/>
			</li>
			<li><label>执行时间：</label>
				<input name="beginProcTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${procLog.beginProcTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endProcTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${procLog.endProcTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>id</th>
				<th>存储过程名</th>
				<th>SQL语句</th>
				<th>原因</th>
				<th>执行时间</th>
				<shiro:hasPermission name="mdmlog:procLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="procLog">
			<tr>
				<td><a href="${ctx}/mdmlog/procLog/form?id=${procLog.id}">
					${procLog.id}
				</a></td>
				<td>
					${procLog.procName}
				</td>
				<td title="${procLog.procSql}">
						${fns:abbr(procLog.procSql,30)}
				</td>
				<td>
					${procLog.procReason}
				</td>
				<td>
					<fmt:formatDate value="${procLog.procTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="mdmlog:procLog:edit"><td>
    				<a href="${ctx}/mdmlog/procLog/form?id=${procLog.id}">查看</a>
					<a href="${ctx}/mdmlog/procLog/delete?id=${procLog.id}" onclick="return confirmx('确认要删除该存储过程日志吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>