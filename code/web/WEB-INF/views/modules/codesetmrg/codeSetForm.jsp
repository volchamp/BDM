<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码集管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/codesetmrg.jsp" %>  
	<script type="text/JavaScript">
		
		$(document).ready(function() {
            jQuery.validator.addMethod("checkCodeSetNo",function(value,element){
                var msg = "抱歉，您填写的格式不正确!";
                var result = true;
                var codeNum = value.substring(2,value.length);
                if (!/[a-zA-Z]/ig.test(value.substring(0,2)) || !/^[0-9]*$/ig.test(codeNum)) {
                    msg = "抱歉，正确格式应该为：以1~2位英文字母开头，其余部分只能是数字或不输入";
                    $.validator.messages.checkCodeSetNo = msg;
                    return false;
                }
                var id = $("#id").val();
                var url = ctx + "/codesetmrg/codeSet/isCodeSetNoExist";
                // var url = ctx + "/connector/simple/isCodeSetNoExist";
                var obj = {};
                obj.id = id;
                // obj.code = value;
                obj.codeSetNo = value;
                $.ajaxSetup({
                    async : false
                });
                $.post(url, obj,function(data) {
                    if (data) {
                        // msg = "抱歉，您填写的内容已存在，请更改!";
                        msg = "抱歉，该代码集目录编码已存在。如果您查看不到该代码集目录，是因为其不属于您的机构!";
                        result = false;
                    }
                });
                $.validator.messages.checkCodeSetNo = msg;
                return result;
            });

            $("#inputForm").validate({
                rules: {
                    codeSetNo: {
                        checkCodeSetNo: true
                    }
                },
                messages: {
                },
                submitHandler: function(form){
                    if(validateForm()) {
                        var officeId = $("#officeId").val();
                        if (officeId.length == 0) {
                            layer.msg("抱歉，请选择所属机构");
                            return;
						}
                        loading('正在提交，请稍等...');
                        form.submit();
                    }
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

            //地市及县区不能对核心基础数据进行修改
			var cityCountyDistAndCore = "${cityCountyDistAndCore}";
			if (cityCountyDistAndCore == "true") {
			    //禁用所有单行文本框
			    $("input[type=text]").attr("disabled", true);
			    //禁用所有下拉框
                // $("select").each(function () {
					// $("#" + this.id).attr("disabled", true);
                // });
				$("form[id='inputForm'] select").attr("disabled", true);
				$("form[id='inputForm'] textarea").attr("disabled", true);
				// $(".btn btn-primary").hide();
				$(".btn.btn-primary").hide();
				$("#tips").show();
            }
        });
		
        // function page(n,s){
			// $("#pageNo").val(n);
			// $("#pageSize").val(s);
			// $("#searchForm").submit();
        // 	return false;
        // }
		
	</script>
</head>
<body>
	<%--<ul class="nav nav-tabs">--%>
		<%--<li><a href="${ctx}/codesetmrg/codeSet/">代码集目录列表</a></li>--%>
		<%--<li class="active">--%>
		<%--<a href="${ctx}/codesetmrg/codeSet/form?id=${codeSet.id}&operation=${codeSet.operation}&processStatus=${codeSet.processStatus}">代码集目录--%>
			<%--<shiro:hasPermission name="codesetmrg:codeSet:edit">--%>
				<%--&lt;%&ndash;${not empty codeSet.id?'修改':'添加'}&ndash;%&gt;--%>
				<%--<c:if test="${not empty codeSet.id}">--%>
					<%--${codeSet.processStatus == 2 ? '查看' : '修改'}--%>
				<%--</c:if>--%>
				<%--<c:if test="${empty codeSet.id}">--%>
					<%--添加--%>
				<%--</c:if>--%>
			<%--</shiro:hasPermission>--%>
			<%--<shiro:lacksPermission name="codesetmrg:codeSet:edit">--%>
				<%--查看--%>
			<%--</shiro:lacksPermission>--%>
		<%--</a>--%>
		<%--</li>--%>
	<%--</ul><br/>--%>

	<form:form id="inputForm" modelAttribute="codeSet" action="${ctx}/codesetmrg/pendingCodeSet/savePendingCodeSet" method="post" class="form-horizontal">
		
		<form:hidden path="id" id="id"/>
		<!--该字段的值代表，0：新增，1：修改 ，2：撤消-->
		<form:hidden path="operation" id="operation" name="operation"/>
		<!--该字段的值代表，1存档数据，0正式数据,2送审数据-->
		<form:hidden path="processStatus" id="processStatus" name="processStatus"/>
		<%-- <form:hidden path="changeForm" id="changeForm" name="changeForm"/> --%>
		<input type="hidden" id="changeForm" name="changeForm"/>
		<sys:message content="${message}"/>
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
				<form:input path="codeSetNo" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
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
		
		<%-- <div class="control-group">
			<label class="control-label">表示格式说明：</label>
			<div class="controls">
				<form:textarea path="formatDesc" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div> --%>
		<div class="control-group">
			<label class="control-label">排序：</label>
			<div class="controls">
				<form:input path="codeSetSort" htmlEscape="false" maxlength="9" class="input-xlarge  digits"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">代码规则说明：</label>
			<div class="controls">
				<form:textarea path="ruleDesc" htmlEscape="false" rows="2" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="2" maxlength="100" class="input-xlarge"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">所属机构:</label>
			<div class="controls">
                <%-- <c:choose>
                     <c:when test="${fns:getUser().admin}"> --%>
                        <sys:treeselect id="office" name="officeIds" value="${codeSet.officeIds}" labelName="office.name" labelValue="${codeSet.officeName}"
                                        title="机构" url="/sys/office/treeData?type=1" cssClass="" allowClear="true"/>
                        <span class="help-inline"><font color="red">*</font> </span>
                    <%-- </c:when>
                    <c:otherwise>
                        <form:hidden path="officeIds" htmlEscape="false" />
						${fn:length(codeSet.officeName) == 0 ? '未设置' :  codeSet.officeName}
					</c:otherwise> 
                </c:choose>--%>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">核心代码集：</label>
			<div class="controls">
					<%--<form:select path="coreFlag" class="input-mini required">--%>
					<%--<form:option value="0" label="是"/>--%>
					<%--<form:option value="1" label="否"/>--%>
					<%--<form:options items="${categorys}" itemValue="id" itemLabel="codeGroupName" />--%>
					<%--</form:select>--%>
                 <%--<c:choose>
                      <c:when test="${fns:getUser().admin}"> --%>
                         <select name="coreFlag" id="coreFlag" class="input-mini ">
                             <option value="" ${(codeSet.coreFlag != 0 && codeSet.coreFlag != 1) ? 'selected' : ''}>请选择</option>
                             <option value="0" ${codeSet.coreFlag == 0 ? 'selected' : ''}>是</option>
                             <option value="1" ${codeSet.coreFlag == 1 ? 'selected' : ''}>否</option>
                         </select>
                         <%--<span class="help-inline"><font color="red">*</font> </span>--%>
                     <%-- </c:when>
                     <c:otherwise>
                         <form:hidden path="coreFlag" htmlEscape="false" />
                         <c:choose>
                             <c:when test="${codeSet.coreFlag == 0}">是</c:when>
                             <c:when test="${codeSet.coreFlag == 1}">否</c:when>
							 <c:otherwise>未设置</c:otherwise>
                         </c:choose>
                     </c:otherwise> 
                 </c:choose>--%>
			</div>
		</div>

		<div class="control-group" style="display: none;" id="tips">
			<label class="control-label">温馨提示：</label>
			<div class="controls">
				<span class="help-inline"><font color="red">地市及县区不能对核心基础数据进行修改。</font> </span>
			</div>
		</div>

		<%--<div class="control-group">--%>
			<%--<label class="control-label">参与审核的业务系统：</label>--%>
			<%--<div class="controls">--%>
				<%--<select data-placeholder="请选择参与审核的业务系统" name="systemIds" class="chosen-select required" multiple="multiple" style="min-width: 280px">--%>
					<%--<c:forEach items="${systems}" var="system">--%>
						<%--<option value="${system.id}"--%>
								<%--<c:forEach items="${systemIds}" var="systemId">--%>
									<%--<c:if test="${system.id eq systemId}"> selected="selected"</c:if>--%>
								<%--</c:forEach>--%>
						<%-->${system.systemShort}</option>--%>
					<%--</c:forEach>--%>
				<%--</select>--%>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			<%--</div>--%>
		<%--</div>--%>
		
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
		<div class="form-actions">
			<shiro:hasPermission name="codesetmrg:codeSet:edit">
				<%--<input id="btnSave" class="btn btn-primary" type="button" value="保 存"/>&nbsp;--%>
				<c:if test="${codeSet.processStatus != 2}">
					<input class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
				</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
	
</body>
</html>