<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接口管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            //高级查询按钮
            $("#btnAdvanced").click(function() {
                var seaDivElem = $("#searchDiv");
                if (seaDivElem.is(":hidden")) {
                    seaDivElem.show();    //如果元素为隐藏,则将它显现
                } else {
                    seaDivElem.hide();     //如果元素为显现,则将其隐藏
                }
            });
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
		<li class="active"><a href="${ctx}/sys/dataInterface/">接口列表</a></li>
		<shiro:hasPermission name="sys:dataInterface:edit"><li><a href="${ctx}/sys/dataInterface/form">接口添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="dataInterface" action="${ctx}/sys/dataInterface/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	<div style="text-align:right">
		<input id="btnAdvanced" class="btn btn-primary" type="button" value="高级查询"/>
	</div>
	<div id="searchDiv" class="searchDiv2" style="display: none">
		<ul class="ul-form">
			<li><label>接口名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="128" class="input-medium"/>
			</li>
			<li><label>接口编码：</label>
				<form:input path="code" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>接口名称</th>
				<th>接口编码</th>
				<th>接口类型</th>
				<th>接口授权码</th>
				<th>更新时间</th>
				<th>创建时间</th>
				<shiro:hasPermission name="sys:dataInterface:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="dataInterface">
			<tr>
				<td><a href="${ctx}/sys/dataInterface/form?id=${dataInterface.id}">${dataInterface.name}</a></td>
				<td>${dataInterface.code}</td>
				<td>${fns:getDictLabel(dataInterface.type, 'sys_data_interface_type', '')}</td>
				<td>${dataInterface.ticket}</td>
				<td><fmt:formatDate value="${dataInterface.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td><fmt:formatDate value="${dataInterface.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<shiro:hasPermission name="sys:dataInterface:edit"><td>
					<a href="${ctx}/sys/dataInterfaceDetail/list?dataInterface.id=${dataInterface.id}">接口授权</a>
    				<a href="${ctx}/sys/dataInterface/form?id=${dataInterface.id}">修改</a>
					<a href="${ctx}/sys/dataInterface/delete?id=${dataInterface.id}" onclick="return confirmx('确认要删除该接口管理吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>