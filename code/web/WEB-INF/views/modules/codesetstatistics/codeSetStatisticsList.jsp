<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码集统计</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            //导出代码集统计数据
            $("#btnExportExcel").click(function(){
                top.$.jBox.confirm("确认要导出代码集统计数据吗？","系统提示",function(v,h,f){
                    if(v=="ok"){
                        $("#searchForm").attr("action","${ctx}/codesetstatistics/codeSetStatistics/export");
                        $("#searchForm").submit();
                    }
                },{buttonsFocus:1});
                top.$('.jbox-body .jbox-icon').css('top','55px');
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
		<li class="active"><a href="${ctx}/codesetstatistics/codeSetStatistics/">代码集统计</a></li>
		<%--<shiro:hasPermission name="inoutlog:logInout:edit"><li><a href="${ctx}/inoutlog/logInout/form">接口收发日志添加</a></li></shiro:hasPermission>--%>
	</ul>
	<%--<form:form id="searchForm" modelAttribute="codeSetStatistics" action="${ctx}/codesetstatistics/codeSetStatistics/" method="post" class="breadcrumb form-search">--%>
	<form:form id="searchForm" action="${ctx}/codesetstatistics/codeSetStatistics/" method="post" class="breadcrumb form-search">
		<%--<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>--%>
		<%--<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>--%>
		<ul class="ul-form">
			<%--<li><label>源业务系统名称：</label>--%>
				<%--<form:input path="sourceSysName" htmlEscape="false" maxlength="50" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>目的业务系统名称：</label>--%>
				<%--<form:input path="destSysName" htmlEscape="false" maxlength="50" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>代码集名称：</label>--%>
				<%--<form:input path="codeSetName" htmlEscape="false" maxlength="50" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>状态。0：失败：1：成功。：</label>--%>
				<%--<form:input path="status" htmlEscape="false" maxlength="18" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>记录类型0：接收，1：发送：</label>--%>
				<%--<form:input path="recordType" htmlEscape="false" maxlength="18" class="input-medium"/>--%>
			<%--</li>--%>
			<li class="btns"><input id="btnExportExcel" class="btn btn-primary" type="button" value="导出EXCEL"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th width="8%">业务系统名称：</th>
				<c:forEach items="${cssList}" var="codeSetStatistics">
					<th>${codeSetStatistics.systemName}</th>
					<%--<th>财务核算</th>--%>
					<%--<th>采购系统</th>--%>
					<%--<th>稽核系统</th>--%>
					<%--<th>绩效系统</th>--%>
					<%--<th>决算系统</th>--%>
					<%--<th>省投系统</th>--%>
					<%--<th>预算一体化</th>--%>
					<%--<th>资产管理系统</th>--%>
					<%--<th>综合平台</th>--%>
				</c:forEach>
				<%--<shiro:hasPermission name="inoutlog:logInout:edit"><th>操作</th></shiro:hasPermission>--%>
			</tr>
		</thead>
		<tbody>
			<tr>
				<th width="8%">代码集数量：</th>
				<c:forEach items="${cssList}" var="codeSetStatistics">
					<td>${codeSetStatistics.itemCount}</td>
				</c:forEach>
			</tr>
		</tbody>
	</table>
	<%--<div class="pagination">${page}</div>--%>
</body>
</html>