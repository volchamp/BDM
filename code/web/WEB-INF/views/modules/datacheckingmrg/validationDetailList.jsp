<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>异常代码集数据管理</title>
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
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/datacheckingmrg/validationDetail/">异常代码集数据列表</a></li>
		<shiro:hasPermission name="datacheckingmrg:validationDetail:edit"><li><a href="${ctx}/datacheckingmrg/validationDetail/form">异常代码集数据添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="validationDetail" action="${ctx}/datacheckingmrg/validationDetail/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>异常代码号：</label>
				<form:input path="itemCode" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>异常代码名：</label>
				<form:input path="itemName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>异常类型：</label>
				<form:input path="exceptionType" htmlEscape="false" maxlength="18" class="input-small"/>
			</li>
			<li><label>异常年度：</label>
				<form:input path="year" htmlEscape="false" maxlength="18" class="input-small"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>ID</th>
				<th>核查结果ID</th>
				<th>异常代码集号</th>
				<th>异常代码集名</th>
				<th>异常年度</th>
				<th>异常类型</th>
				<th>标准代码集号</th>
				<th>标准代码集名</th>
				<th>标准年度</th>
				<shiro:hasPermission name="datacheckingmrg:validationDetail:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="validationDetail">
			<tr>
				<td>
					${validationDetail.id}
				</td>
				<td>
					${validationDetail.recordId}
				</td>
				<td>
					<a href="${ctx}/datacheckingmrg/validationDetail/form?id=${validationDetail.id}">
					${validationDetail.itemCode}
					</a>
				</td>
				<td>
					${validationDetail.itemName}
				</td>
				<td>
					${validationDetail.year}
				</td>
				<td>
					${validationDetail.exceptionType}
				</td>
				<td>
					${validationDetail.itemCodeS}
				</td>
				<td>
					${validationDetail.itemNameS}
				</td>
				<td>
					${validationDetail.yearS}
				</td>
				<shiro:hasPermission name="datacheckingmrg:validationDetail:edit"><td>
    				<a href="${ctx}/datacheckingmrg/validationDetail/form?id=${validationDetail.id}">修改</a>
					<a href="${ctx}/datacheckingmrg/validationDetail/delete?id=${validationDetail.id}" onclick="return confirmx('确认要删除该异常代码集数据吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>