<%--
 * 代码集分组管理-表单
 * 
 * @author Xwt
 * @date 2017-06-06
 * @description
 * 
 --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码集分组管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			// 验证名称是否存在 
		    jQuery.validator.addMethod("checkName",function(value,element){
                if (value.length == 0) {
                    return true;
                }
		  		var result = true;
		  		var id = $("#id").val();
		    	var url = ctx + "/codesetmrg/dataSetCategory/checkName";
				var obj = {};
				obj.id = id;
				obj.codeGroupName = value;
				$.ajaxSetup({   
		            async : false  
		        }); 
				$.post(url, obj,function(data) {
					if (data) {
						result = false;
					} 
				}); 
		        return result;     
		    },"您填写的分组名称系统中已存在!"); 
			
			// 验证范围是否存在 
		    jQuery.validator.addMethod("validExistCode",function(value,element){  
		    	var msg = "你填写的格式不正确!";
		  		var result = true;
		  		var codeNum = value.substring(2,value.length);
		  		if (!/[a-zA-Z]/ig.test(value.substring(0,2)) || !/^[0-9]*$/ig.test(codeNum)) {
		  		    msg = "抱歉，正确格式应该为：以1~2位英文字母开头，其余部分只能是数字或不输入";
		  			$.validator.messages.validExistCode = msg;		
		  			return false;
		  		}
		  		
		  		var id = $("#id").val();
		    	var url = ctx + "/codesetmrg/dataSetCategory/validExistCode";
				var obj = {};
				obj.id = id;
				obj.code = value;
				$.ajaxSetup({   
		            async : false  
		        }); 
				$.post(url, obj,function(data) {
					if (data) {
						msg = "您填写的内容系统中已存在!";
						result = false;
					} 
				}); 
				$.validator.messages.validExistCode = msg;
		        return result;     
		    }); 
			
			$("#inputForm").validate({
				rules: {
					codeGroupName: {
						checkName: true
					},
					codeStartWith: {
						validExistCode : true
					},
					codeEndWith: {
						validExistCode : true
					}
				},
				messages: {
				},
				submitHandler: function(form){
					var code1 = $("#codeStartWith").val();
					var code2 = $("#codeEndWith").val();
                    var startNum = code1.substring(1);
                    var endNum = code2.substring(1);
					if (!/^[0-9]{1}$/ig.test(startNum.substring(0, 1))) {	//codeStartWith的第二个字符不是数字
						startNum = startNum.substring(1);
					}
					if (!/^[0-9]{1}$/ig.test(endNum.substring(0, 1))) {	//codeEndWith的第二个字符不是数字
						endNum = endNum.substring(1);
					}
					if (parseInt(endNum) > parseInt(startNum)) {
						loading('正在提交，请稍等...');
						form.submit();
                    } else {
						top.$.jBox.tip("结束范围必须大于开始范围!");
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
		});
		
		
		
		
	</script>
</head>
<body>
	<%--<ul class="nav nav-tabs">--%>
		<%--<li><a href="${ctx}/codesetmrg/dataSetCategory/">代码集分组列表</a></li>--%>
		<%--<li class="active"><a href="${ctx}/codesetmrg/dataSetCategory/form?id=${dataSetCategory.id}">代码集分组<shiro:hasPermission name="codesetmrg:dataSetCategory:edit">${not empty dataSetCategory.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="codesetmrg:dataSetCategory:edit">查看</shiro:lacksPermission></a></li>--%>
	<%--</ul><br/>--%>
	<form:form id="inputForm" modelAttribute="dataSetCategory" action="${ctx}/codesetmrg/dataSetCategory/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">代码集分组名称：</label>
			<div class="controls">
				<form:input path="codeGroupName" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始范围：</label>
			<div class="controls">
				<form:input path="codeStartWith" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束范围：</label>
			<div class="controls">
				<form:input path="codeEndWith" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>
<!-- 				<span class="help-inline"><font color="red">例如：CS0001</font> </span>
 -->			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序：</label>
			<div class="controls">
				<form:input path="codeGroupSort" htmlEscape="false" maxlength="9" class="input-xlarge  digits required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="100" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="codesetmrg:dataSetCategory:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>