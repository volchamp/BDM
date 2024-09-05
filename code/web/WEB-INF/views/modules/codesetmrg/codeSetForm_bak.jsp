<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码集管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/codesetmrg.jsp" %>  
	<script type="text/JavaScript">
		
		$(document).ready(function() {
			$("#btnReset").click(function(){
				$("#itemCode").val("");
				$("#itemName").val("");
				$("#year").val("");
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
	<!-- 导入HTML S -->
	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/codesetmrg/codeSetData/import" method="post" enctype="multipart/form-data"
			class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>
			<input type="hidden" name="codeSetId" id="import_codeSetId"/>
			<input type="hidden" name="codeSetNo" id="import_codeSetNo"/>
			<!--该字段的值代表，0：新增，1：修改 ，2：撤消-->
			<input type="hidden" name="operation" id="import_operation"/>
			<!--该字段的值代表，1存档数据，0正式数据,2送审数据-->
			<input type="hidden" name="processStatus" id="import_processStatus"/>
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<a href="${ctx}/codesetmrg/codeSetData/import/template">下载模板</a>
		</form>
	</div>
	<!-- 导入HTML E -->
	<!-- 设置业务系统HTML S -->
	<!--设置分发关系 -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">选择业务系统</h4>
            </div>
            <form id="setSystemForm" action="${ctx}/codesetmrg/pedningCodeItem/setSystem" method="post" class="form-horizontal">
            <div class="modal-body">
            	<div class="control-group">
					<label class="control-label">业务系统：</label>
						<div class="controls">
							<c:forEach items="${systems}" var="system">
								<input type="checkbox" name="systemIds" value="${system.id}"/>${system.systemShort}
							</c:forEach>
							<input type="hidden" name="codeSetId" id="setSystem_codeSetId"/>
							<input type="hidden" name="codeSetNo" id="setSystem_codeSetNo"/>
							<!--该字段的值代表，0：新增，1：修改 ，2：撤消-->
							<input type="hidden" name="operation" id="setSystem_operation"/>
							<!--该字段的值代表，1存档数据，0正式数据,2送审数据-->
							<input type="hidden" name="processStatus" id="setSystem_processStatus"/>
						</div>
				</div>
            </div>
            </form>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onClick="doSetSystem()">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
	</div>
	<!-- 下发代码集 -->
	<div class="modal fade" id="sendSystem" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">选择业务系统</h4>
            </div>
            <form id="sendSystemForm" action="${ctx}/codesetmrg/codeSetData/sendingSystemCodeSet" method="post" class="form-horizontal">
            <div class="modal-body">
            	<div class="control-group">
					<label class="control-label">业务系统：</label>
						<div class="controls">
							<c:forEach items="${systems}" var="system">
								<input type="checkbox" name="systemIds" value="${system.id}"/>${system.systemShort}
							</c:forEach>
							<input type="hidden" name="codeSetNo" id="setSendSystem_codeSetNo"/>
						</div>
				</div>
            </div>
            </form>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onClick="doSendSystem()">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
	</div>
	<!-- 设置业务系统HTML E -->
	
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/codesetmrg/codeSet/">代码集列表</a></li>
		<li class="active">
		<a href="${ctx}/codesetmrg/codeSet/form?id=${codeSet.id}">代码集
			<shiro:hasPermission name="codesetmrg:codeSet:edit">
				${not empty codeSet.id?'修改':'添加'}
			</shiro:hasPermission>
			<shiro:lacksPermission name="codesetmrg:codeSet:edit">
				查看
			</shiro:lacksPermission>
		</a>
		</li>
	</ul>

	<ul class="breadcrumb ul-form">
		<li class="btns">
		<shiro:hasPermission name="codesetmrg:codeSet:edit">
		<li class="btns"><input id="btnSave" class="btn btn-primary" type="button" value="保 存"/></li>
		<li class="btns"><input id="btnAdd" class="btn btn-primary" type="button" value="新增"/></li>
		<li class="btns"><input id="btnReview" class="btn btn-primary" type="button" value="送审" disabled="disabled"/></li>
		<li class="btns"><input id="btnPreview" class="btn btn-primary" type="button" value="预览" disabled="disabled"/></li>
		<li class="btns"><input id="btnAudit" class="btn btn-primary" type="button" value="审核通过" disabled="disabled"/></li>
		<!-- <li class="btns"><input id="btnSysMapping" class="btn btn-primary" type="button" value="维护分发关系"/></li> -->
		<li class="btns"><input id="btmCodesetMapping" class="btn btn-primary" type="button" value="通用分发关系"  data-toggle="modal" data-target="#myModal"/></li>
		<li class="btns"><input id="btnSysSending" class="btn btn-primary" type="button" value="下发业务系统" disabled="disabled" data-toggle="modal" data-target="#sendSystem"/></li>
		<li class="btns"><input id="btnImport" class="btn btn-primary" type="button" value="导入"/></li>
		<li class="btns"><input id="btnImportHistory" class="btn btn-primary" type="button" value="历史版本导入" disabled="disabled"/></li>
		<li class="btns"><input id="btnExport" class="btn btn-primary" type="button" value="导出" disabled="disabled"/></li>
		</shiro:hasPermission></li>
		<li class="btns"><input id="btnCancel" class="btn" type="button" value="返 回"/></li>
		<li class="clearfix"></li>
	</ul>
	
	<form:form id="inputForm" modelAttribute="codeSet" action="" method="post" class="form-horizontal">
		
		<form:hidden path="id" id="id"/>
		<!--该字段的值代表，0：新增，1：修改 ，2：撤消-->
		<form:hidden path="operation" id="operation" name="operation"/>
		<!--该字段的值代表，1存档数据，0正式数据,2送审数据-->
		<form:hidden path="processStatus" id="processStatus" name="processStatus"/>
		<%-- <form:hidden path="changeForm" id="changeForm" name="changeForm"/> --%>
		<input type="hidden" id="changeForm" name="changeForm"/>
		<sys:message content="${message}"/>
		<fieldset>
        <legend>代码集目录</legend>
		<div class="control-group">
			<label class="control-label">代码集分组：</label>
			<div class="controls">
				<%-- <form:input path="codeGroupId" name="codeGroupId" id="codeGroupId"  htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				 --%>
				<form:select path="codeGroupId" class="input-xlarge required">  
		            <form:option value="" label="--请选择--"/>  
		            <form:options items="${categorys}" itemValue="id" itemLabel="codeGroupName" />  
	        	</form:select>    
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">代码集目录编码：</label>
			<div class="controls">
				<form:input path="codeSetNo" id="codeSetNo" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">代码集目录名称：</label>
			<div class="controls">
				<form:input path="codeSetName" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">代码规则说明：</label>
			<div class="controls">
				<form:textarea path="ruleDesc" htmlEscape="false" rows="2" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label">表示格式说明：</label>
			<div class="controls">
				<form:textarea path="formatDesc" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div> --%>
		<div class="control-group">
			<label class="control-label">排序：</label>
			<div class="controls">
				<form:input path="codeSetSort" htmlEscape="false" maxlength="18" class="input-xlarge  digits"/>
			</div>
		</div>
		
		<%-- <div class="control-group">
			<label class="control-label">代码集类型：</label>
			<div class="controls">
				<form:radiobutton path="codeSetType" value="0"/>复杂类型
        		<form:radiobutton path="codeSetType" value="1"/>简单类型
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">取数过滤条件：</label>
			<div class="controls">
				<form:textarea path="qsSql" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">取数表：</label>
			<div class="controls">
				<form:input path="codeTable" htmlEscape="false" maxlength="50" class="input-xlarge "/>
			</div>
		</div> --%>
		<%--div class="form-actions">
			<shiro:hasPermission name="codesetmrg:codeSet:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>--%>
		</fieldset>
	</form:form>
	<br/>
	<fieldset>
        <legend>代码集数据</legend>
	<form:form id="searchForm" action="${ctx}/codesetmrg/codeSet/form" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="operation" name="operation" type="hidden" value="${codeSet.operation}"/>
		<input id="processStatus" name="processStatus" type="hidden" value="${codeSet.processStatus}"/>
		<input id="id" name="id" type="hidden" value="${codeSet.id}"/>
		<ul class="ul-form">
			<li><label>编码：</label>
				<input id="itemCode" name="itemCode" type="text" class="input-medium" value="${itemCode}"/>
			</li>
			<li><label>名称：</label>
				<input id="itemName" name="itemName" type="text" class="input-medium" value="${itemName}"/>
			</li>
			<li><label>年度：</label>
				<input id="year" name="year" type="text" value="${year}" class="input-medium input-xlarge  digits"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			<input id="btnReset" class="btn btn-primary" type="button" value="重置"/></li>
			<li class="clearfix"></li>
		</ul>
		
	</form:form>

	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th align="center"><input type="checkbox" id="selAll" onclick="doSelectAll()" /></th>
				<th>代码集编码</th>
				<th>上级代码集编码</th>
				<th>代码集名称</th>
				<th>年度</th>
				<th>是否启用</th>
				<th>版本</th>
				<th>有效开始时间</th>
				<th>有效结束时间</th>
				<th style="min-width: 180px">分发关系</th>
				<th>排序</th>
				<th>备注</th>
				<shiro:hasPermission name="codesetmrg:codeset:edit"> 
				<th>操作</th>
				</shiro:hasPermission> 
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="pendingCodeItem">
			<tr>
				<td align="center"><input type="checkbox" name="selectedRow" value="" /></td>
				<td>${pendingCodeItem.itemCode }</td>
				<td>${pendingCodeItem.parentItemCode }</td>
				<td>${pendingCodeItem.itemName}</td>
				<td>${pendingCodeItem.year }</td>
				<td>
					<!-- 是否可用0：不可用 1：未启用；2：已启用 -->
					<c:if test="${pendingCodeItem.status==0 }">
						不可用
					</c:if>
					<c:if test="${pendingCodeItem.status==1 }">
						未启用
					</c:if>
					<c:if test="${pendingCodeItem.status==2 }">
						已启用
					</c:if>
				</td>
				<td>${pendingCodeItem.version }</td>
				<td>
					<fmt:formatDate value="${pendingCodeItem.validStartDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${pendingCodeItem.validEndDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>${pendingCodeItem.distribution }</td>
				<td>${pendingCodeItem.itemCodeSort }</td>
				<td>${pendingCodeItem.remarks }</td>
				<%-- <td>${pendingCodeItem.processStatus },${pendingCodeItem.codeSetId },${pendingCodeItem.opertion }</td> --%>
				<shiro:hasPermission name="codesetmrg:codeset:edit">
				<td>
    				<a href="javascript:void(0);" onclick="editCodeItem('${pendingCodeItem.id}',${pendingCodeItem.operation })">修改</a>
					<a href="javascript:void(0);" onclick="deleteById('${pendingCodeItem.id}',${pendingCodeItem.operation })">删除</a>
				</td>
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	</fieldset>
	<br/><br/>
	
</body>
</html>