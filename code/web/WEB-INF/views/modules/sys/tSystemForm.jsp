<%--
 * 业务系统管理-表单
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
	<script type="text/javascript" src="${ctxStatic}/modules/sys/StringUtils.js"></script>
	<script type="text/javascript" src="${ctxStatic}/modules/sys/Share.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
            //校验编码是否存在
            // jQuery.validator.addMethod("isSystemCodeExist", function(value,element) {
            //     if (value.length == 0) {
            //         return true;
            //     }
            //     var result = true;
            //     var id = $("#id").val();
            //     var url = ctx + "/sys/tSystem/isSystemCodeExist";
            //     var obj = {};
            //     obj.id = id;
            //     obj.systemCode = value;
            //     $.ajaxSetup({
            //         async : false
            //     });
            //     $.post(url, obj,function(data) {
            //         if (data) {
            //             result = false;
            //         }
            //     });
            //     return result;
            // }, "该业务系统编码已存在!");

            //校验名称是否存在
            // jQuery.validator.addMethod("isSystemNameExist", function(value,element) {
            //     if (value.length == 0) {
            //         return true;
            //     }
            //     var result = true;
            //     var id = $("#id").val();
            //     var url = ctx + "/sys/tSystem/isSystemNameExist";
            //     var obj = {};
            //     obj.id = id;
            //     obj.systemName = value;
            //     $.ajaxSetup({
            //         async : false
            //     });
            //     $.post(url, obj,function(data) {
            //         if (data) {
            //             result = false;
            //         }
            //     });
            //     return result;
            // }, "该业务系统名称已存在!");

            //校验简称是否存在
            // jQuery.validator.addMethod("isSystemShortExist", function(value,element) {
            //     if (value.length == 0) {
            //         return true;
            //     }
            //     var result = true;
            //     var id = $("#id").val();
            //     var url = ctx + "/sys/tSystem/isSystemShortExist";
            //     var obj = {};
            //     obj.id = id;
            //     obj.systemShort = value;
            //     $.ajaxSetup({
            //         async : false
            //     });
            //     $.post(url, obj,function(data) {
            //         if (data) {
            //             result = false;
            //         }
            //     });
            //     return result;
            // }, "该业务系统简称已存在!");

			//$("#name").focus();
			$("#inputForm").validate({
                // rules: {
                //     systemCode: {
                //         isSystemCodeExist: true
                //     },
                //     systemName: {
                //         isSystemNameExist : true
                //     }
                //     , systemShort: {
                //         isSystemShortExist : true
                //     }
                // },
                // messages: {
                // },
				submitHandler: function(form){
                   var receiversId = $("#receiversId").val();
                   if (receiversId.length == 0) {
                       layer.msg("抱歉，请选择系统管理员");
                       return;
					}
					// alert("777");
					if (isSystemShortExist()) {
					    layer.msg("抱歉，该业务系统简称已存在");
					    $("#systemShort").focus();
					    return;
					}
					if (isSystemCodeExist()) {
					    layer.msg("抱歉，该业务系统编码已存在");
                        $("#systemCode").focus();
					    return;
					}
					if (isSystemNameExist()) {
					    layer.msg("抱歉，该业务系统名称已存在");
                        $("#systemName").focus();
                        return;
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
			
			$("#systemName").blur(function () {
				var arr = new Array($("#systemCode"),$("#systemShort"));
				Share.setPingyin($(this),arr);
			});
		});

		function isSystemShortExist() {
		    var value = $("#systemShort").val();
            // alert("value = " + value);
            if (value.length == 0) {
                return false;
            }
            var result = false;
            var id = $("#id").val();
            var url = ctx + "/sys/tSystem/isSystemShortExist";
            var obj = {};
            obj.id = id;
            obj.systemShort = value;
            $.ajaxSetup({
                async : false
            });
            $.post(url, obj,function(data) {
                if (data) {
                    result = true;
                }
            });
            return result;
        }

		function isSystemCodeExist() {
		    var value = $("#systemCode").val();
		    // alert("value = " + value);
            if (value.length == 0) {
                return false;
            }
            var result = false;
            var id = $("#id").val();
            var url = ctx + "/sys/tSystem/isSystemCodeExist";
            var obj = {};
            obj.id = id;
            obj.systemCode = value;
            $.ajaxSetup({
                async : false
            });
            $.post(url, obj,function(data) {
                if (data) {
                    result = true;
                }
            });
            return result;
        }

		function isSystemNameExist() {
		    var value = $("#systemName").val();
            if (value.length == 0) {
                return false;
            }
            var result = false;
            var id = $("#id").val();
            var url = ctx + "/sys/tSystem/isSystemNameExist";
            var obj = {};
            obj.id = id;
            obj.systemName = value;
            $.ajaxSetup({
                async : false
            });
            $.post(url, obj,function(data) {
                if (data) {
                    result = true;
                }
            });
            return result;
        }

        // 选择用户
        function selUser(id,receivers,adminName){
//		    alertx("11");
            $("#assignUserId").val(id);
            $("#receiversId").val(receivers);
            $("#receiversName").val(adminName);
            $("#receiversName").click();
        }

        // 选择用户回调函数
        function receiversTreeselectCallBack_deleteMe(v, h, f){
            // 指派用户ID
            var id = $("#assignUserId").val();
            var receiversId = $("#receiversId").val();
            var receiversName = $("#receiversName").val();
            var url = ctx + "/tesk/task/assign";
            var obj = {"id": id,"taskHandlerId": taskHandlerId};
//            alert("taskHandlerId = " + taskHandlerId);
//            var selectIds = document.getElementsByName("selectIds")[0].value;
            $("#receivers").val(receiversId);
            $("#receiversTextarea").val(receiversName);
            <%--$.post(url, obj,function(data) {--%>
                <%--if (data) {--%>
                    <%--var admin = "${fns:getUser().admin}";--%>
                    <%--top.$.jBox.tip("指派成功");--%>
                    <%--//当前不是超级管理员，指派成功要刷新--%>
                    <%--if (admin == "false") {--%>
<%--//                        setTimeout("refreshUnhandledList()", 500);--%>

                    <%--}--%>
                <%--} else {--%>
                    <%--top.$.jBox.tip("指派失败,请联系管理员！");--%>
                <%--}--%>
            <%--});--%>
        }
	</script>
</head>
<body>
	<%--<ul class="nav nav-tabs">--%>
		<%--<li><a href="${ctx}/sys/tSystem/">业务系统列表</a></li>--%>
		<%--<li class="active"><a href="${ctx}/sys/tSystem/form?id=${tSystem.id}">业务系统<shiro:hasPermission name="sys:tSystem:edit">${not empty tSystem.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:tSystem:edit">查看</shiro:lacksPermission></a></li>--%>
	<%--</ul><br/>--%>
	<form:form id="inputForm" modelAttribute="tSystem" action="${ctx}/sys/tSystem/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<div class="control-group">
			<label class="control-label">业务系统名称：</label>
			<div class="controls">
				<form:input path="systemName" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>	
		<div class="control-group">
			<label class="control-label">业务系统编码：</label>
			<div class="controls">
				<form:input path="systemCode" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">业务系统简称：</label>
			<div class="controls">
				<form:input path="systemShort" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">服务地址：</label>
			<div class="controls">
				<form:input path="serviceAddr" htmlEscape="false" maxlength="500" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序：</label>
			<div class="controls">
				<form:input path="systemSoprt" htmlEscape="false" maxlength="9" class="input-xlarge  digits "/>
				<%--<span class="help-inline"><font color="red">*</font></span>--%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">系统管理员：</label>
			<div class="controls">
				<%--<form:input path="receivers" htmlEscape="false"  class="input-xlarge required"/>--%>
				<%--<textarea id="receiversTextarea" class="input-xxlarge"></textarea>--%>
				<%--<a href="javascript:;" onclick="selUser('${tSystem.id}','${tSystem.receivers }','${tSystem.receivers }');">选择</a>--%>
				<sys:treeselect id="receivers" name="receivers" value="${tSystem.receivers }" labelName="adminName" labelValue="${tSystem.adminName }"
								title="用户" url="/sys/office/treeData?type=3" checked="true" cssClass="input-xxlarge " allowClear="true" notAllowSelectParent="true"
								 />
				<span class="help-inline">
					<font color="red">*</font>
					<%--<font color="#a9a9a9">多个用逗号隔开，首位须是逗号</font>--%>
				</span>
			</div>
		</div>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">腾讯通中业务系统编码：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="txtSysName" htmlEscape="false" maxlength="50" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="control-group">
			<label class="control-label">归属区域:</label>
			<div class="controls">
				<sys:treeselect id="area" name="area.id" value="${tSystem.area.id}" labelName="area.name" labelValue="${tSystem.area.name}"
								title="区域" url="/sys/area/treeData" allowClear="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">描述：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="100" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="sys:tSystem:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>