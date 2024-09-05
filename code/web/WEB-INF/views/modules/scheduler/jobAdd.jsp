<%--
 * 添加任务-表单
 * 
 * @author Xwt
 * @date 2017-06-22
 * @description
 * 
 --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>添加任务</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" src="${ctxStatic}/modules/scheduler/JobParam.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			
			// 验证类是否存在 
		    jQuery.validator.addMethod("validClass",function(value,element){  
		  		var result = true;
		    	var url = ctx + "/sys/scheduler/validClass";
				var obj = {};
				obj.className = value;
				$.ajaxSetup({   
		            async : false  
		        }); 
				$.post(url, obj,function(data) {
					if (!data) {
						result = false;
					} 
				}); 
		        return result;     
		    },"您填写的类不存在!"); 
			
			//$("#name").focus();
			$("#inputForm").validate({
				
				rules: {
					jobName: {
						maxlength: 100
					},
					className: {
						maxlength: 100,
						validClass: true
					}
				},
				messages: {
					jobName: {required: "必填",maxlength: "不能超过100个字符"},
					className: {required: "必填",maxlength: "不能超过100个字符"}
				},
				submitHandler: function(form){
					var str = setParameterXml();
					$("#parameterJson").val(str);
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/scheduler/">定时任务列表</a></li>
		<li class="active"><a href="${ctx}/sys/scheduler/addJob${isAdd}">定时任务${isAdd eq 1 ? '添加':'修改'}</a></li>
	</ul><br/>
	<form id="inputForm" action="${ctx}/sys/scheduler/addJob${isAdd eq 1 ? '2':'4'}" method="post" class="form-horizontal">
		<div class="control-group">
			<label class="control-label">任务名称：</label>
			<div class="controls">
				<input type="text" id="jobName" name="jobName" ${isAdd eq 1 ? '':'readOnly'} class="input-xlarge required" value="${jobDetail.name}"/>
				<input id="planJson" name="planJson" type="hidden" />	
				<input id="parameterJson" name="parameterJson" type="hidden" />
 				<span class="help-inline"><font color="red">*</font> </span>			
			</div>
		</div>	
		<div class="control-group">
			<label class="control-label">任务类：</label>
			<div class="controls">
				<input type="text" id="className" name="className" ${isAdd eq 1 ? '':'readOnly'} value="${jobDetail.jobClass.name }" style="width:50%;" class="input-xlarge required" />		
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>	
		<div class="control-group">
			<label class="control-label">任务描述：</label>
			<div class="controls">
				<textarea id="description" ${isAdd eq 1 ? '':'readOnly'} value="${jobDetail.description }" name="description" class="input-xxlarge"></textarea>	
			</div>
		</div>
		<div class="control-group">
			<h4 style="margin-left:40%;">任务参数</h4>
			<input id="btnAddParameter" class="btn btn-primary" onclick="addRow()" type="button" value="添加"/>&nbsp;
		</div>
		<table id="paramterTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
				    <th>参数名</th>
				    <th>参数类型</th>
				    <th>参数值</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody id="trContainer">
				<c:forEach items="${list }" var="parameterObj">
					<tr>
						<td style="text-align: center;">
							<input type="text" id="paraName" name="paraName" value="${parameterObj.name }"/>
						</td>
						<td style="text-align: center;">
							<select id="paraType" name="paraType" style="width:220px;">
								<option value="int" ${parameterObj.type eq 'int' ? 'selected' : '' }>int</option>
								<option value="long" ${parameterObj.type eq 'long' ? 'selected' : '' }>long</option>
								<option value="float" ${parameterObj.type eq 'float' ? 'selected' : '' }>float</option>
								<option value="string" ${parameterObj.type eq 'string' ? 'selected' : '' }>string</option>
								<option value="blooean" ${parameterObj.type eq 'blooean' ? 'selected' : '' }>blooean</option>
							</select>
						</td>
						<td style="text-align: center;">
							<input type="text" id="paraValue" name="paraValue" value="${parameterObj.value }"/>
						</td>
						<td><a href="javascript:;" onclick="delRow(this);">删除</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="form-actions">
			<shiro:hasPermission name="sys:scheduler:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
</body>
</html>