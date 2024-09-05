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
		});
		function saveOrUpdate() {
			var id = $("#id").val();
			if(validateForm()){
				$.ajax({
		            type:"post",
		            url:"${ctx}/codesetmrg/codeSetData/save",
		            data:$("#inputForm").serialize(),
		            dataType:"json",
		            success:function(data){
		                if(data !=null){
		                	jBox.tip('保存成功！', 'success');
		                    window.parent.window.isFreshFlag="2";//回写父页面的值
		                    if(id){
			                  	//仅关闭当前jBox 
		                    	window.parent.window.jBox.close();
		                    }else{
		                    	//刷新当前页
			                    window.setTimeout(function () { location.reload(); }, 200);
		                    }
		                    
		                }else{
		                    alert("保存失败，请联系管理员");
		                }
		            },
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						//alert(XMLHttpRequest.status);
						//alert(XMLHttpRequest.readyState);
						//alert(textStatus);
						alert("保存失败，请联系管理员");
					}
		        });
			}
	    };
	    function validateForm() {     
			return $("#inputForm").validate({     
			    rules: {     
			    	
			    },     
			    messages:{     
			    }
			}).form();     
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="codeSetData" action="${ctx}/codesetmrg/codeSetData/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="codeSetId"/>
		<form:hidden path="codeSetNo"/>
		<form:hidden path="status"/>
		<form:hidden path="version"/>
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
						title="上级代码集编码" url="/codesetmrg/pedningCodeItem/parentCodeTreeData?codeSetNo=${codeSetData.codeSetNo }" cssClass="input-small" allowClear="true" notAllowSelectParent="false" />
				<%-- <form:input path="parentItemCode" htmlEscape="false" maxlength="50" class="input-xlarge "/> --%>
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
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">有效开始日期：</label>
			<div class="controls">
				<input name="validStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${codeSetData.validStartDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">有效结束日期：</label>
			<div class="controls">
				<input name="validEndDate" id="validEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${codeSetData.validEndDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<%--<a onclick='javascript:$("#validEndDate").val("");' style="cursor: pointer">清空</a>--%>
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label">分发关系：</label>
			<div class="controls">
				<select data-placeholder="请选择下发业务系统" name="systemIds" class="chosen-select" multiple="multiple" style="min-width: 280px">
					<c:forEach items="${systems}" var="system">
						<option value="${system.id}" 
						
						<c:forEach items="${systemIds}" var="systemId">
							<c:if test="${system.id eq systemId}"> selected="selected"</c:if>
						</c:forEach>
						
						  >${system.systemShort}</option>
					</c:forEach>
				</select>
				
			</div>
		</div> --%>
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

		<div class="form-actions">
			<shiro:hasPermission name="codesetmrg:codeSetData:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>