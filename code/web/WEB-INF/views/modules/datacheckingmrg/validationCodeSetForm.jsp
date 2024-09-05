<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<%--<title>核查代码集目录管理</title>--%>
	<title>核查结果管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
				    var action = $("#inputForm").attr("action");
				    // alert("action = " + action);
				    if (action.indexOf("export") < 0) {	//如果是导出excel则不显示提示
                        loading('正在提交，请稍等...');
					}
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

			//处理异常
			$("#btnHandle").click(function () {
                top.$.jBox.confirm("确认要进行异常处理吗？","系统提示",function(v,h,f){
                    if(v =="ok"){
                        $("#inputForm").attr("action","${ctx}/datacheckingmrg/validationCodeSet/handle");
                        $("#inputForm").submit();
                    }
                },{buttonsFocus:1});
                top.$('.jbox-body .jbox-icon').css('top','55px');
            });

            // 导出
            $("#btnExportExcel").click(function(){
                top.$.jBox.confirm("确认要导出异常代码集数据吗？","系统提示",function(v,h,f){
                    if(v == "ok"){
//                        $("#searchForm").submit();
						var recordId = $("#id").val();
						var taskHandlerId = $("#taskHandlerId").val();
                        $("#inputForm").attr("action","${ctx}/datacheckingmrg/validationCodeSet/export?recordId=" + recordId + "&taskHandlerId2=" + taskHandlerId);
                        $("#inputForm").submit();
                    }
                },{buttonsFocus:1});
                top.$('.jbox-body .jbox-icon').css('top','55px');
            });

            //关闭
            <%--$("#btnCancel").click(function(){--%>
                <%--&lt;%&ndash;window.location="${ctx}/datacheckingmrg/validationCodeSet";&ndash;%&gt;--%>
                <%--history.go(-1);--%>
                <%--return false;--%>
            <%--});--%>

		});
	</script>
</head>
<body>
	<%--<ul class="nav nav-tabs">--%>
		<%--<li><a href="${ctx}/datacheckingmrg/validationCodeSet/">核查代码集目录列表</a></li>--%>
		<%--<li class="active">--%>
			<%--<a href="${ctx}/datacheckingmrg/validationCodeSet/form?id=${validationCodeSet.id}">异常代码集--%>
				<%--<shiro:hasPermission name="datacheckingmrg:validationCodeSet:edit">${not empty validationCodeSet.id?'详情':'添加'}</shiro:hasPermission>--%>
				<%--<shiro:lacksPermission name="datacheckingmrg:validationCodeSet:edit">详情</shiro:lacksPermission>--%>
			<%--</a>--%>
		<%--</li>--%>
	<%--</ul>--%>
	<%--<br/>--%>

	<form:form id="inputForm" modelAttribute="validationCodeSet" action="${ctx}/datacheckingmrg/validationCodeSet/save" method="post" class="form-horizontal">
	<%--<form:form id="inputForm" modelAttribute="validationCodeSet" action="${ctx}/datacheckingmrg/validationCodeSet/save" method="post" class="breadcrumb form-search">--%>
		<form:hidden path="id" id="id"/>
		<form:hidden path="systemCode" id="systemCode"/>
		<%--<form:hidden path="exceptionDesc" id="exceptionDesc"/>--%>
		<form:hidden path="systemId" id="systemId"/>
		<form:hidden path="codeSetId" id="codeSetId"/>
		<input type="hidden" id="taskHandlerId" name="taskHandlerId" value="${validationCodeSet.taskHandlerId}">
		<input type="hidden" name="origin" value="${origin}"/>
		<sys:message content="${message}"/>

		<ul class="breadcrumb ul-form">
			<shiro:hasPermission name="datacheckingmrg:validationCodeSet:check">
				<c:if test="${validationCodeSet.handleStatus == 0}">
					<li class="btns">
							<%-- <c:if test="${isShowHandle eq 'true'}"> --%>
						<input id="btnHandle" class="btn btn-primary" type="button" value="异常处理"/>&nbsp;
					</li>
					<li class="btns">
						<input id="btnExportExcel" class="btn btn-primary" type="button" value="导出EXCEL"/>&nbsp;
					</li>
				</c:if>
			</shiro:hasPermission>
			<shiro:hasPermission name="datacheckingmrg:validationCodeSet:view">
				<c:if test="${validationCodeSet.handleStatus==0}">
					<c:if test="${validationCodeSet.taskHandlerId==fns:getUser()}">
						<li class="btns">
								<%-- <c:if test="${isShowHandle eq 'true'}"> --%>
							<input id="btnHandle" class="btn btn-primary" type="button" value="异常处理"/>&nbsp;
						</li>
						<li class="btns">
							<input id="btnExportExcel" class="btn btn-primary" type="button" value="导出EXCEL"/>&nbsp;
						</li>
					</c:if>
				</c:if>
			</shiro:hasPermission>
			<li class="btns">
				<input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
				<%--<a href="${ctx}/datacheckingmrg/validationCodeSet/" class="btn">返回</a>--%>
			</li>
		</ul>
		<legend>
			代码集目录
				<%--${validationCodeSet.handleStatus}--%>
		</legend>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">代码集目录编码：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="codeSetNo" htmlEscape="false" maxlength="50" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>

		<%--<div class="control-group">--%>
			<%--<label class="control-label">代码集目录名称：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="codeSetName" htmlEscape="false" maxlength="50" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>

		<%--<div class="control-group">--%>
			<%--<label class="control-label">业务系统名称：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="systemName" htmlEscape="false" maxlength="50" class="input-xlarge " readonly="true"/>--%>
			<%--</div>--%>
		<%--</div>--%>

		<%--<div class="control-group">--%>
			<%--<label class="control-label">核查日期：</label>--%>
			<%--<div class="controls">--%>
				<%--<input name="checkDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "--%>
					   <%--value="<fmt:formatDate value="${validationCodeSet.checkDate}" pattern="yyyy-MM-dd"/>"--%>
					   <%--onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>--%>
			<%--</div>--%>
		<%--</div>--%>

		<%--<div class="control-group">--%>
			<%--<label class="control-label">核查结果：</label>--%>
			<%--<div class="controls">--%>
					<%--&lt;%&ndash;<form:input path="checkResult" htmlEscape="false" maxlength="18" class="input-xlarge  digits" />&ndash;%&gt;--%>
				<%--<form:select path="checkResult" class="input-xlarge required">--%>
					<%--<c:if test="${validationCodeSet.checkResult == 0}">--%>
						<%--<form:option value="0" label="匹配" />--%>
						<%--<form:option value="1" label="不匹配" ${validationCodeSet.checkResult == 1 ? 'selected' : ''}/>--%>
					<%--</c:if>--%>
					<%--<c:if test="${validationCodeSet.checkResult == 1}">--%>
						<%--<form:option value="1" label="不匹配"/>--%>
						<%--<form:option value="0" label="匹配"/>--%>
					<%--</c:if>--%>
				<%--</form:select>--%>
			<%--</div>--%>
		<%--</div>--%>

		<%--<div class="control-group">--%>
			<%--<label class="control-label">表示格式说明：</label>--%>
			<%--<div class="controls">--%>
					<%--&lt;%&ndash;<form:input path="formatDesc" htmlEscape="false" maxlength="64" class="input-xlarge "/>&ndash;%&gt;--%>
					<%--&lt;%&ndash;<form:textarea path="formatDesc" htmlEscape="false" cols="20" rows="2"/>&ndash;%&gt;--%>
				<%--<textarea name="formatDesc" cols="20" rows="2" class="input-xlarge"></textarea>--%>
			<%--</div>--%>
		<%--</div>--%>

		<%--<div class="control-group">--%>
			<%--<label class="control-label">代码规则说明：</label>--%>
			<%--<div class="controls">--%>
					<%--&lt;%&ndash;<form:textarea path="ruleDesc" htmlEscape="false" cols="20" rows="2"/>&ndash;%&gt;--%>
				<%--<textarea name="ruleDesc" cols="20" rows="2" class="input-xlarge"></textarea>--%>
			<%--</div>--%>
		<%--</div>--%>

		<%--<div class="control-group">--%>
			<%--<label class="control-label">核查结果说明：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="exceptionDesc" htmlEscape="false" maxlength="100" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">处理时间：</label>--%>
			<%--<div class="controls">--%>
				<%--<input name="handleDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "--%>
					<%--value="<fmt:formatDate value="${validationCodeSet.handleDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"--%>
					<%--onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">处理结果。0：未处理，1、已处理：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="handleStatus" htmlEscape="false" maxlength="18" class="input-xlarge  digits"/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">处理人：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="handleBy" htmlEscape="false" maxlength="64" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">system_id：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="systemId" htmlEscape="false" maxlength="64" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="form-actions">--%>
			<%--<shiro:hasPermission name="datacheckingmrg:validationCodeSet:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>--%>
			<%--<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>--%>
		<%--</div>--%>
		<div class="container-fluid breadcrumb">
			<div class="row-fluid span12">
				<span class="span4">代码集目录编码:
					<form:input path="codeSetNo" htmlEscape="false" maxlength="50" />
					<%--<input type="text" name="codeSetNo" value="${validationCodeSet.codeSetNo}" maxlength="50" />--%>
				</span>
				<span class="span4">代码集目录名称: <form:input path="codeSetName" htmlEscape="false" maxlength="50" />
				</span>
				<span class="span4">业务系统名称:
					<form:input path="systemName" htmlEscape="false" maxlength="50" readonly="true" />
				</span>
			</div>
			<hr/>
			<div class="row-fluid span8">
				<span class="span4">核查日期:
					<input name="checkDate" id="checkDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
						   value="<fmt:formatDate value="${validationCodeSet.checkDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					<%--<a onclick='javascript:$("#checkDate").val("");' style="cursor: pointer">清空</a>--%>
				</span>
				<span class="span4">核查结果：
					<form:select path="checkResult" class="input-small required">
							<form:option value="0" label="匹配"/>
							<form:option value="1" label="不匹配" />
					</form:select>
				</span>
				<span class="span4">
					<%--代码规则说明：--%>
					核查结果说明：
					<%--<textarea name="ruleDesc" cols="20" rows="2" ></textarea>--%>
					<form:textarea path="exceptionDesc" htmlEscape="false" cols="20" rows="2"/>
				</span>
			</div>
			<%--<hr/>--%>
			<%--<div class="row-fluid span12">--%>
				<%--<span class="span4">所属机构:--%>
					<%--<sys:treeselect id="office" name="officeIds" value="${validationCodeSet.officeIds}" labelName="office.name" labelValue="${validationCodeSet.officeName}"--%>
									<%--title="机构" url="/sys/office/treeData?type=1" cssClass="" allowClear="true"/>--%>
				<%--</span>--%>
			<%--</div>--%>
		</div>
	</form:form>

	<fieldset>
		<legend>异常代码集数据</legend>
		<%--<form:form id="searchForm" action="${ctx}/datacheckingmrg/validationCodeSet/form" method="post" class="breadcrumb form-search">--%>
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<%--<input id="operation" name="operation" type="hidden" value="${codeSet.operation}"/>--%>
			<%--<input id="processStatus" name="processStatus" type="hidden" value="${codeSet.processStatus}"/>--%>
			<%--<input id="id" name="id" type="hidden" value="${validationCodeSet.id}"/>--%>
			<%--<ul class="ul-form">--%>
				<%--<li><label>编码：</label>--%>
					<%--<input id="itemCode" name="itemCode" type="text" class="input-medium" value="${itemCode}"/>--%>
				<%--</li>--%>
				<%--<li><label>名称：</label>--%>
					<%--<input id="itemName" name="itemName" type="text" class="input-medium" value="${itemName}"/>--%>
				<%--</li>--%>
				<%--<li><label>年度：</label>--%>
					<%--<input id="year" name="year" type="text" value="${year}" class="input-medium input-xlarge  digits"/>--%>
				<%--</li>--%>
				<%--<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>--%>
					<%--<input id="btnReset" class="btn btn-primary" type="reset" value="重置"/></li>--%>
				<%--<li class="clearfix"></li>--%>
			<%--</ul>--%>
		<%--</form:form>--%>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
			<tr>
				<th>异常代码编码</th>
				<th>异常代码名称</th>
				<th>异常父代码编码</th>
				<%--<th>异常年度</th>--%>
				<th>有效开始时间</th>
				<th>有效结束时间</th>
				<th>异常类型</th>
				<th>标准代码编码</th>
				<th>标准代码名称</th>
				<th>标准父代码编码</th>
				<%--<th>标准年度</th>--%>
				<th>有效开始时间(标准)</th>
				<th>有效结束时间(标准)</th>
				<%--<shiro:hasPermission name="datacheckingmrg:validationCodeSet:edit">--%>
					<%--<th>操作</th>--%>
				<%--</shiro:hasPermission>--%>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="codeItemLost">
				<tr>
					<td>
						<%--<a href="${ctx}/datacheckingmrg/validationCodeSet/form?id=${codeItemLost.id}">--%>
							${codeItemLost.itemCode}
						<%--</a>--%>
					</td>
					<td>${codeItemLost.itemName }</td>
					<td>${codeItemLost.parentItemCode }</td>
					<%--<td>${codeItemLost.year }</td>--%>
					<td>
						<fmt:formatDate value="${codeItemLost.validStartDate}" pattern="yyyy-MM-dd"/>
					</td>
					<td>
						<fmt:formatDate value="${codeItemLost.validEndDate}" pattern="yyyy-MM-dd"/>
					</td>
					<td>
						<c:choose>
							<c:when test="${codeItemLost.exceptionType == 0}">缺失</c:when>
							<c:when test="${codeItemLost.exceptionType == 1}">冗余</c:when>
							<c:when test="${codeItemLost.exceptionType == 2}">修改</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
					</td>
						<td>
						${codeItemLost.itemCodeS}
						</td>
						<td>${codeItemLost.itemNameS }</td>
						<td>${codeItemLost.parentItemCodeS }</td>
						<%--<td>${codeItemLost.yearS }</td>--%>
						<td>
						<fmt:formatDate value="${codeItemLost.validStartDateS}" pattern="yyyy-MM-dd"/>
						</td>
						<td>
						<fmt:formatDate value="${codeItemLost.validEndDateS}" pattern="yyyy-MM-dd"/>
						</td>
					<%--<td>${pendingCodeItem.itemId }</td>--%>
					<%--<td>${pendingCodeItem.processStatus },${pendingCodeItem.codeSetId },${pendingCodeItem.opertion }</td> --%>
					<%--<shiro:hasPermission name="datacheckingmrg:validationCodeSet:edit">--%>
						<%--<td>--%>
							<%--<a href="javascript:void(0);" onclick="editCodeItem('${pendingCodeItem.id}',${pendingCodeItem.opertion })">修改</a>--%>
							<%--<a href="javascript:void(0);" onclick="deleteById('${pendingCodeItem.id}',${pendingCodeItem.opertion })">删除</a>--%>
						<%--</td>--%>
					<%--</shiro:hasPermission>--%>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div class="pagination">${page}</div>
	</fieldset>

	<%--<fieldset>--%>
		<%--<legend>业务系统冗余代码</legend>--%>
		<%--&lt;%&ndash;<form:form id="searchForm2" action="${ctx}/datacheckingmrg/validationCodeSet/form" method="post" class="breadcrumb form-search">&ndash;%&gt;--%>
			<%--<input id="pageNo2" name="pageNo2" type="hidden" value="${page2.pageNo}"/>--%>
			<%--<input id="pageSize2" name="pageSize2" type="hidden" value="${page2.pageSize}"/>--%>
			<%--&lt;%&ndash;<input id="operation" name="operation" type="hidden" value="${codeSet.operation}"/>&ndash;%&gt;--%>
			<%--&lt;%&ndash;<input id="processStatus" name="processStatus" type="hidden" value="${codeSet.processStatus}"/>&ndash;%&gt;--%>
			<%--&lt;%&ndash;<input id="id" name="id" type="hidden" value="${validationCodeSet.id}"/>&ndash;%&gt;--%>
		<%--&lt;%&ndash;</form:form>&ndash;%&gt;--%>
		<%--<table id="contentTable2" class="table table-striped table-bordered table-condensed">--%>
			<%--<thead>--%>
			<%--<tr>--%>
				<%--<th>代码集编码</th>--%>
				<%--<th>代码集名称</th>--%>
				<%--<th>年度</th>--%>
				<%--<th>有效开始时间</th>--%>
				<%--<th>有效结束时间</th>--%>
			<%--</tr>--%>
			<%--</thead>--%>
			<%--<tbody>--%>
			<%--<c:forEach items="${page2.list}" var="codeItemRedun">--%>
				<%--<tr>--%>
						<%--<td>--%>
						<%--${codeItemRedun.itemCode}--%>
						<%--</td>--%>
						<%--<td>${codeItemRedun.itemName }</td>--%>
						<%--<td>${codeItemRedun.year }</td>--%>
						<%--<td>--%>
						<%--<fmt:formatDate value="${codeItemRedun.validStartDate}" pattern="yyyy-MM-dd"/>--%>
						<%--</td>--%>
						<%--<td>--%>
						<%--<fmt:formatDate value="${codeItemRedun.validEndDate}" pattern="yyyy-MM-dd"/>--%>
						<%--</td>--%>
					<%--&lt;%&ndash;<td>&ndash;%&gt;--%>
						<%--&lt;%&ndash;${codeItemRedun.itemCodeS}&ndash;%&gt;--%>
					<%--&lt;%&ndash;</td>&ndash;%&gt;--%>
					<%--&lt;%&ndash;<td>${codeItemRedun.itemNameS }</td>&ndash;%&gt;--%>
					<%--&lt;%&ndash;<td>${codeItemRedun.yearS }</td>&ndash;%&gt;--%>
					<%--&lt;%&ndash;<td>&ndash;%&gt;--%>
						<%--&lt;%&ndash;<fmt:formatDate value="${codeItemRedun.validStartDateS}" pattern="yyyy-MM-dd"/>&ndash;%&gt;--%>
					<%--&lt;%&ndash;</td>&ndash;%&gt;--%>
					<%--&lt;%&ndash;<td>&ndash;%&gt;--%>
						<%--&lt;%&ndash;<fmt:formatDate value="${codeItemRedun.validEndDateS}" pattern="yyyy-MM-dd"/>&ndash;%&gt;--%>
					<%--&lt;%&ndash;</td>&ndash;%&gt;--%>
				<%--</tr>--%>
			<%--</c:forEach>--%>
			<%--</tbody>--%>
		<%--</table>--%>
		<%--<div class="pagination">${page2}</div>--%>
	<%--</fieldset>--%>

</body>
</html>