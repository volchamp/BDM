<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接口授权管理</title>
	<meta name="decorator" content="default"/>
</head>
<body>
<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/dataInterface/">接口列表</a></li>
		<li class="active"><a href="${ctx}/sys/dataInterfaceDetail/?dataInterface.id=${dataInterface.id}"><shiro:hasPermission name="sys:dataInterfaceDetail:edit">接口授权</shiro:hasPermission><shiro:lacksPermission name="sys:dataInterfaceDetail:edit">接口授权列表</shiro:lacksPermission></a></li>
	</ul>
	<div class="container-fluid breadcrumb">
		<div class="row-fluid span12">
			<span class="span4">接口名称: <b>${dataInterface.name}</b></span>
			<span class="span4">接口编码: ${dataInterface.code}</span>
			<c:set var="type" value="${dataInterface.type}" scope="page"></c:set>
			<span class="span4">接口类型: ${fns:getDictLabel(type, 'sys_data_interface_type', '')}</span>
		</div>
		<div class="row-fluid span8">
			<span class="span4">接口授权码: ${dataInterface.ticket}</span>
			<c:set var="state" value="${dataInterface.state}" scope="page"></c:set>
			<span class="span4">接口状态: ${fns:getDictLabel(state, 'sys_data_interface_state', '')}</span>
		</div>
	</div>
	<sys:message content="${message}"/>
	<div class="breadcrumb">
		<form:form id="inputForm" modelAttribute="dataInterfaceDetail" action="${ctx}/sys/dataInterfaceDetail/save" method="post">
			<form:hidden path="dataInterface.id"/>
			<form:hidden id="tablename" path="tablename"/>
			<form:hidden id="fields" path="fields"/>
		</form:form>
		<input id="assignButton" class="btn btn-primary" type="submit" value="添加授权"/>
		<script type="text/javascript">
			$("#assignButton").click(function(){
				top.$.jBox.open("iframe:${ctx}/sys/dataInterfaceDetail/form", "选择接口授权",810,$(top.document).height()-240,{
					buttons:{"确定":"ok", "关闭":true},submit:function(v, h, f){
						if (v == "ok"){
							var rs = h.find("iframe")[0].contentWindow.getTableAndFiledFun();
							//alert("tablename="+rs.tablename+" fiedls="+rs.fields);
							$("#tablename").val(rs.tablename);
							$("#fields").val(rs.fields);
					    	$('#inputForm').submit();
					    	return true;
						}
					}, loaded:function(h){
						$(".jbox-content", top.document).css("overflow-y","hidden");
					}
				});
			});
		</script>
	</div>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>授权表</th>
				<th>授权字段</th>
				<th>更新时间</th>
				<th>创建时间</th>
				<shiro:hasPermission name="sys:dataInterfaceDetail:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="dataInterfaceDetail">
			<tr>
				<td>${dataInterfaceDetail.tablename}</td>
				<td>${dataInterfaceDetail.fields }</td>
				<td><fmt:formatDate value="${dataInterfaceDetail.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td><fmt:formatDate value="${dataInterfaceDetail.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<shiro:hasPermission name="sys:dataInterfaceDetail:edit"><td>
					<a href="${ctx}/sys/dataInterfaceDetail/delete?id=${dataInterfaceDetail.id}&dataInterface.id=${dataInterfaceDetail.dataInterface.id}" onclick="return confirmx('确认要删除该接口授权吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>