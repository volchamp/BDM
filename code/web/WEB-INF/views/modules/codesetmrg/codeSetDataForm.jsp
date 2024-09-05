<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码集数据管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
				    var smType = $("#smType").val();
				    if (smType != "none") {
				        if ($("#splitMergeId").val().indexOf(",") == -1) {
				            //没有包含逗号，说明没有选择多个代码集
                            layer.msg("抱歉，请选择多个代码集进行拆分/合并");
                            return;
                        }
                    }
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

			//根据状态判断是否为查看页面
            var status = "${codeSetData.status}";
            if (status == "0") {
                // $("form input").prop("disabled", true);
                // $("#systemIds").prop("readonly", true);
                $("form input").attr("disabled", "disabled");
                $("#systemIds").attr("disabled", "disabled");
                $("#remarks").attr("readonly", true);
                $("#parentItemCodeButton").hide();
                $("#splitMergeButton").hide();
                $("#btnSubmit").hide();
                $("#btnCancel").removeAttr("disabled");
            }
		});
		
	</script>
</head>
<body>
	<%--<ul class="nav nav-tabs">--%>
		<%--<li><a href="${ctx}/codesetmrg/pedningCodeItem/list?id=${codeSetData.codeSetId}&operation=${codeSetOperation}&processStatus=${codeSetProcessStatus}">代码集数据列表</a></li>--%>
		<%--<li class="active">--%>
			<%--<a href="${ctx}/codesetmrg/codeSetData/form?id=${codeSetData.id}">--%>
				<%--代码集数据--%>
				<%--<shiro:hasPermission name="codesetmrg:codesetdata:edit">${not empty codeSetData.id?'修改':'添加'}</shiro:hasPermission>--%>
				<%--<shiro:lacksPermission name="codesetmrg:codesetdata:edit">查看</shiro:lacksPermission>--%>
			<%--</a>--%>
		<%--</li>--%>
	<%--</ul><br/>--%>
	<form:form id="inputForm" modelAttribute="codeSetData" action="${ctx}/codesetmrg/pedningCodeItem/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="codeSetId"/>
		<form:hidden path="codeSetNo"/>
		<form:hidden path="status"/>
		<form:hidden path="version"/>
		<form:hidden path="operation" id="operation"/>
		<input id="codeSetOperation" name="codeSetOperation" type="hidden" value="${codeSetOperation}"/>
		<input id="codeSetProcessStatus" name="codeSetProcessStatus" type="hidden" value="${codeSetProcessStatus}"/>
		<sys:message content="${message}"/>		
		
		<div class="control-group">
			<label class="control-label">代码集编码：</label>
			<div class="controls">
				<form:input path="itemCode" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">上级代码集编码：</label>
			<div class="controls">
				<sys:treeselect id="parentItemCode" name="parentItemCode" value="${codeSetData.parentItemCode }" labelName="${id}" labelValue="${codeSetData.parentItemCode}"
						title="上级代码集编码" url="/codesetmrg/pedningCodeItem/parentCodeTreeData?codeSetNo=${codeSetData.codeSetNo }&itemId=${codeSetData.id}" cssClass="input-small" allowClear="true" notAllowSelectParent="false" />
				<%-- <input name="parentItemCode" value="${codeSetData.parentItemCode}" maxlength="50" class="input-xlarge "/> --%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">代码集名称：</label>
			<div class="controls">
				<form:input path="itemName" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label">版本：</label>
			<div class="controls">
				<form:input path="version" htmlEscape="false" maxlength="18" class="input-xlarge  digits" disabled="true"/>
			</div>
		</div> --%>
		<div class="control-group">
			<label class="control-label">年度：</label>
			<div class="controls">
				 <form:input path="year" htmlEscape="false" maxlength="18" class="input-xlarge  digits"/> 
				<%--<input name="validStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="" pattern="yyyy"/>"
					onclick="WdatePicker({dateFmt:'yyyy',isShowClear:false});"/>--%>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">有效开始日期：</label>
			<div class="controls">
				<input name="validStartDate" id="validStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${codeSetData.validStartDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,maxDate:'#F{$dp.$D(\'validEndDate\')}'});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">有效结束日期：</label>
			<div class="controls">
				<input name="validEndDate" id="validEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${codeSetData.validEndDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'validStartDate\')}'});"/>
				<%--<a onclick='javascript:$("#validEndDate").val("");' style="cursor: pointer">清空</a>--%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">分发关系：</label>
			<div class="controls">
				<select data-placeholder="请选择下发业务系统" name="systemIds" id="systemIds" class="chosen-select" multiple="multiple" style="min-width: 280px">
					<c:forEach items="${systems}" var="system">
						<option value="${system.id}" 
						
						<c:forEach items="${systemIds}" var="systemId">
							<c:if test="${system.id eq systemId}"> selected="selected"</c:if>
						</c:forEach>
						
						  >${system.systemShort}</option>
					</c:forEach>
				</select>
				
			</div>
		</div>
		<%-- <div class="control-group">
		<!-- 是否可用0：不可用 1：未启用；2：已启用： -->
			<label class="control-label">状态：</label>
			<div class="controls">
				<form:select path="status" items="${states}" disabled="true"/><br/>
			</div>
		</div> --%>
		
		<div class="control-group">
			<label class="control-label">排序：</label>
			<div class="controls">
				<form:input path="itemCodeSort" htmlEscape="false" maxlength="9" class="input-xlarge  digits"/>
			</div>
		</div>
		<c:if test="${codeSetData.col1 != null}">
			<div class="control-group">
				<label class="control-label">内编码：</label>
				<div class="controls">
					<form:input path="col1" htmlEscape="false" class="input-xxlarge " readonly="true"/>
				</div>
			</div>
		</c:if>
        <div class="control-group">
            <label class="control-label">拆分/合并关系：</label>
            <div class="controls">
                <c:choose>
                    <c:when test="${smSize == 0}">
                        <select name="smType" id="smType" class="small">
                            <option value="none">没有</option>
                            <option value="split">拆分</option>
                            <option value="merge">合并</option>
                        </select>
                <sys:treeselect id="splitMerge" name="splitMerge" value="${codeSetData.splitMerge }" labelName="" labelValue=""
                                title="拆分/合并关系" url="/codesetmrg/pedningCodeItem/findItemTree?codeSetNo=${codeSetData.codeSetNo }&itemId=${codeSetData.id}" cssClass="input-xxlarge" allowClear="true" checked="true" />
                <%--<font color="#808080">温馨提示：选择多个为拆分关系，选择一个为合并关系</font>--%>
                    </c:when>
                    <c:otherwise>
                        <a href="javascript:;" onclick="layerPage2('${ctx}/codesetmrg/splitMerge/changeRelationList?oldItemId=${codeSetData.id}&newItemId=${codeSetData.id}','“${codeSetData.itemName}”变动关系', '1200px', '600px');" class="btn btn-primary" style="cursor:pointer;">查看变动关系</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="2" maxlength="100" class="input-xlarge"/>
			</div>
		</div>

		<%--<div class="control-group">--%>
			<%--<label class="control-label">col1：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="col1" htmlEscape="false" maxlength="50" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">col2：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="col2" htmlEscape="false" maxlength="50" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">col3：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="col3" htmlEscape="false" maxlength="50" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">col4：</label>--%>
			<%--<div class="controls">--%>
				<%--<input name="col4" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "--%>
					<%--value="<fmt:formatDate value="${codeSetData.col4}" pattern="yyyy-MM-dd HH:mm:ss"/>"--%>
					<%--onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">col5：</label>--%>
			<%--<div class="controls">--%>
				<%--<input name="col5" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "--%>
					<%--value="<fmt:formatDate value="${codeSetData.col5}" pattern="yyyy-MM-dd HH:mm:ss"/>"--%>
					<%--onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="form-actions">--%>
			<%--<shiro:hasPermission name="codesetmrg:codeSetData:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>--%>
			<%--<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>--%>
		<%--</div> --%>

		<div class="form-actions">
			<shiro:hasPermission name="codesetmrg:codesetdata:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>