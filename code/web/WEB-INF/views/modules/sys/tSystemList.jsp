<%--
 * 业务系统管理-列表
 * 
 * @author Xwt
 * @date 2017-06-07
 * @description
 * 
 --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>业务系统管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#btnExport").click(function(){
			top.$.jBox.confirm("确认要导出业务系统数据吗？","系统提示",function(v,h,f){
				if(v=="ok"){
					$("#searchForm").attr("action","${ctx}/sys/tSystem/export");
					$("#searchForm").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		});
		$("#btnImport").click(function(){
			$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
				bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
		});
		
		//$(document).ready(function() {
		//	$("#btnReset").click(function(){
		//		$("#systemName").val("");
		//		$("#serviceAddr").val("");
		//		return false;
		//	});

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
		
		
		//function wenjianname(){
			
		//	var filename=document.getElementById("hidden").value=document.getElementById("uploadFile").value;
		//	alert(filename);
		//}
		
	</script>
	<style type="text/css">
		.form-search .ul-form li label{ width:100px;}
	</style>
</head>
<body>

<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/sys/tSystem/import" method="post" enctype="multipart/form-data"
			class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/>
			<br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<input id="pageSize" name="username" type="hidden" value="${fns:getUser().name}"/>
			<a href="${ctx}/sys/tSystem/import/template">下载模板</a>
		</form>
	</div>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/tSystem/">业务系统列表</a></li>
		<shiro:hasPermission name="sys:tSystem:edit"><li><a href="${ctx}/sys/tSystem/form">业务系统添加</a></li></shiro:hasPermission>
	</ul>

	

	<form:form id="searchForm" modelAttribute="TSystem" action="${ctx}/sys/tSystem/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
		<input id="btnExport" class="btn btn-primary" type="button" value="导出"/>
		<input id="btnImport" class="btn btn-primary" type="button" value="导入"/>
		<!-- <div style="text-align:right">-->
			<input id="btnAdvanced" class="btn btn-primary" type="button" style="float: right" value="高级查询"/>
		</div>
		<div id="searchDiv" class="searchDiv2" style="display: none">
		<ul class="ul-form">
			<li><label>业务系统名称：</label><form:input path="systemName" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>服务地址：</label><form:input path="serviceAddr" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>归属区域：</label>
				<sys:treeselect id="area" name="area.id" value="${TSystem.area.id}" labelName="area.name" labelValue="${TSystem.area.name}"
								title="区域" url="/sys/area/treeData" cssClass="required" smallBtn="true" allowClear="true" />
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			<input id="btnReset" class="btn btn-primary" type="button" value="重置"/></li>
			<li class="clearfix"></li>
		</ul>
		</div>
	</form:form>

	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>业务系统名称</th>
				<th>业务系统编码</th>
				<th>服务地址</th>
				<%--<th>系统管理员id</th>--%>
				<th>系统管理员</th>
				<th>归属区域</th>
				<th>创建人</th>
				<th>排序</th>
				<th>上次更新时间</th>
				<th>状态</th>
				<shiro:hasPermission name="sys:tSystem:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tSystem">
			<tr>
				<td>
					<a href="${ctx}/sys/tSystem/form?id=${tSystem.id}"></a>
					${tSystem.systemName }
				</td>
				<td>
					${tSystem.systemCode }
				</td>
				<td>
					${tSystem.serviceAddr }
				</td>
				<%--<td>--%>
					<%--${tSystem.receivers }--%>
				<%--</td>--%>
				<td>
					${tSystem.adminName }
				</td>
				<td>
					${tSystem.area.name }
				</td>
				<td>
					${tSystem.createBy.name }
				</td>
				<td>
					${tSystem.systemSoprt }
				</td>
				<td>
					<fmt:formatDate value="${tSystem.updateDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>${tSystem.delFlag == "0" ? "启用" : "禁用"}</td>
				<shiro:hasPermission name="sys:tSystem:edit"><td>
					<c:if test="${tSystem.delFlag == '0'}">
						<a href="${ctx}/sys/tSystem/form?id=${tSystem.id}">修改</a>
						<a href="${ctx}/sys/tSystem/delete?id=${tSystem.id}" onclick="return confirmx('确认要禁用该业务系统吗？', this.href)">禁用</a>
					</c:if>
					<c:if test="${tSystem.delFlag == '1'}">
						<a href="${ctx}/sys/tSystem/enable?id=${tSystem.id}" onclick="return confirmx('确认要启用该业务系统吗？', this.href)">启用</a>
					</c:if>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>