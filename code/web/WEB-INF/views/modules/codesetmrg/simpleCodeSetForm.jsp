<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码集管理</title>
	<meta name="decorator" content="default"/>
	<%-- <%@include file="/WEB-INF/views/include/simplecodesetmrg.jsp" %>  --%>
	<script type="text/JavaScript">
		var $url = "${ctx}/codesetmrg/codeSet/save";//保存代码集目录url
		function validateForm() {     
			return $("#inputForm").validate({     
			    rules: {     
			    	codeGroupId: {  
			             required : true
			        },
			        codeSetNo:{
			        	required:true,
			        	checkCodeSetNo: true
			       }
			    },     
			    messages:{
			    	codeSetNo:{
			    		required:"代码集编码不能为空！"
			    	}
			    }
			}).form();     
		}
		$(document).ready(function() {
			// 验证编码是否存在 
		    jQuery.validator.addMethod("checkCodeSetNo",function(value,element){  
		    	var id = $("#id").val();
                var result = false;
                var obj = {};
                obj.id = id;
                obj.codeSetNo = value;
//		    	if(id) {
//		    		return true;
//		    	}
		    	<%--var url = "${ctx}/codesetmrg/pendingCodeSet/checkCodeSetNo?codeSetNo=" + codeSetNo;--%>
                var url = ctx+"/connector/simple/isCodeSetNoExist";
                $.ajaxSetup({
		            async : false  
		        }); 
				$.post(url, obj,function(data) {
                    if (data) {
                        result = true;
                    }
				});
                return result;
		    },"抱歉，代码集编码已经存在，请更改!");
			
			<%-- var mdm_mode= <%=request.getAttribute("mdm_mode")%>; --%> //当前运行模式
			<%--var $url = "${ctx}/codesetmrg/codeSet/save";//保存代码集目录url --%>
			var $inputFrom = $("#inputForm");//保存代码集目录表单
			var id = $("#id").val();
			if(id.length>0){
				//当有业务id时，disabled业务系统编码输入框
				$("#codeGroupId").attr({ readonly: true });//只读代码集分组
				$("#codeSetNo").attr({ readonly: true });
			}
			
			//保存代码集目录
			$("#btnSave").click(function() {
                if (!valQsSqlAndCodeTab()) {	//验证不通过，返回，不往下执行
                    return;
                }
				if(validateForm()){
//				    if (valQsSqlAndCodeTab()) {
                        formSubmit($url, $inputFrom, true);
                        if ($("#schema").val() == 1) {
                            window.location = "${ctx}/codesetmrg/codeSetData/index";
						}
//                    }
				}
			});
			//新增代码集数据
			$("#btnAdd").click(function() {
//                if (!valQsSqlAndCodeTab()) {	//验证不通过，返回，不往下执行
//                    return;
//                }
				var isFreshFlag="1";//关闭修改窗口时是否刷新列表页面标识，1：不刷新。2：刷新
				var codeSetNo = $("#codeSetNo").val();
				if(validateForm()){
					formSubmit($url,$inputFrom,false);
					var codeSetId = $("#id").val();
					// 正常打开	
					top.$.jBox.open("iframe:${ctx}/codesetmrg/codeSetData/form?codeSetId="+codeSetId+"&codeSetNo="+codeSetNo, "代码集数据录入", 900, 540, {
						buttons:{
							"确定":"ok", 
							"关闭":true
						}, 
						submit:function(v, h, f){
							if (v=="ok"){
	                            $(window.parent.document).contents().find("#jbox-iframe")[0].contentWindow.saveOrUpdate();
	                            return false;
							}
						}, loaded:function(h){
							 $(".jbox-content", document).css("overflow-y", "hidden");
						},closed:function (){
			                //在弹出窗口页面，如果我们保存了数据，就将父页面里的变量isFreshFlag 值设置为2
							<%--window.location="${ctx}/codesetmrg/codeSet/form?id="+codeSetId; --%>
							location.reload();
			            }
					}); 
				}
			}); 
			//导出代码集
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出代码集数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/codesetmrg/codeSetData/export");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			//导入代码集
			$("#btnImport").click(function(){
//                if (!valQsSqlAndCodeTab()) {	//验证不通过，返回，不往下执行
//                    return;
//                }
				var codeSetNo = $("#codeSetNo").val();
				if(validateForm()){
					formSubmit($url,$inputFrom,false);
					var codeSetId = $("#id").val();
					var codeSetNo = $("#codeSetNo").val();
					$("#import_codeSetId").val(codeSetId);
					$("#import_codeSetNo").val(codeSetNo);
					$.jBox($("#importBox").html(), {
						title:"导入数据", 
						buttons:{"关闭":true}, 
						bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"
					});
				}
				 
			});
			
			//关闭
			$("#btnCancel").click(function(){
				window.location="${ctx}/codesetmrg/codeSet"; 
				return false;
			});
			
			$("#btnReset").click(function(){
				$("#itemCode").val("");
				$("#itemName").val("");
				$("#year").val("");
				$("#qsSql").val("");
				return false;
			});
			
		});
		
		function formSubmit ($url,$inputFrom,isTip){
			$.ajax({
				url: $url,
				type: "POST",
				data: $inputFrom.serialize(),
				dataType: "json",
				cache: false,
				async:isTip,
				success: function(data) {
					if(data!=null){
						$("#id").val(data.id);
						if(isTip){
							jBox.tip('保存成功！', 'success');
						}
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					jBox.tip('保存失败，请联系管理员', 'error');
					return false;
				}
			});
		    return true;
		}
		//删除代码集数据
		function deleteById(id){
			top.$.jBox.confirm("确认要删除代码集数据吗？","系统提示",function(v,h,f){
				if(v=="ok"){
					var $inputFrom = $("#inputForm");//保存代码集目录表单
					formSubmit($url,$inputFrom,false);
					deleteSubmit(id);
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		}
		//删除代码集数据
		function deleteSubmit(id){
			$.ajax({
				url: '${ctx}/codesetmrg/codeSetData/delete',
				type: "POST",
				data: {'id':id},
				dataType: "json",
				cache: false,
				success: function(data) {
					if(data!=null){
						location.reload();
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					jBox.tip('删除失败，请联系管理员', 'error');
				}
			});
		}
		//修改代码集数据
		function editCodeItem(id,operation){
			var isFreshFlag="1";<%--关闭修改窗口时是否刷新列表页面标识，1：不刷新。2：刷新--%>
			var $inputFrom = $("#inputForm");//保存代码集目录表单
			if(validateForm()){
				formSubmit($url,$inputFrom,false);
				//alert("原代码集目录ID："+id);
				var codeSetId = $("#id").val();
				var codeSetNo = $("#codeSetNo").val();
				//alert("新代码集目录ID："+codeSetId);
				// 正常打开	
				top.$.jBox.open("iframe:${ctx}/codesetmrg/codeSetData/form?id="+id+"&codeSetNo="+codeSetNo+"&codeSetId="+codeSetId, "代码集数据编辑", 900, 540, {
					buttons:{
						"确定":"ok", 
						"关闭":true
					}, 
					submit:function(v, h, f){
						if (v=="ok"){
	                        $(window.parent.document).contents().find("#jbox-iframe")[0].contentWindow.saveOrUpdate();
	                        
	                        return false;
						}
					}, loaded:function(h){
						 $(".jbox-content", document).css("overflow-y", "hidden");
					},closed:function (){
						location.reload();
		            }
				}); 
			}
		}

		//验证取数表
//		function valiCodeTable() {
//			var T_MDM_CODE_SET_DATA = "T_MDM_CODE_SET_DATA";
//			var T_MDM_PENDING_CODE_ITEM = "T_MDM_PENDING_CODE_ITEM";
//			var T_MDM_SENDING_CODE_ITEM = "T_MDM_SENDING_CODE_ITEM";
//			var T_MDM_VALIDATION_DETAIL = "T_MDM_VALIDATION_DETAIL";
//			var T_MDM_VALIDATION_DETAIL_TMP = "T_MDM_VALIDATION_DETAIL_TMP";
//            var codeTabElem = $("#codeTable");
//			var codeTab = $.trim(codeTabElem.val());
//			if (codeTab.length > 0) {	//有值了就要验证
//			    if (codeTab == T_MDM_CODE_SET_DATA || codeTab == T_MDM_PENDING_CODE_ITEM || codeTab == T_MDM_SENDING_CODE_ITEM ||
//					codeTab == T_MDM_VALIDATION_DETAIL || codeTab == T_MDM_VALIDATION_DETAIL_TMP) {
//			        return true;
//				}
//			}
//			alertx("抱歉，您输入的表名不符合要求，请重输！");
//            codeTabElem.focus();
//			return false;
//        }

		//验证取数表和取数条件
		function valQsSqlAndCodeTab() {
//			var codeTabElem = $("#codeTable");
			var schemaElem = $("#schema");
			var qsSqlElem = $("#qsSql");
            var qsSqlDivElem = $("#qsSqlDiv");
//            var codeTab = codeTabElem.val();
            var schemaVal = schemaElem.val();
            var qsSql = $.trim(qsSqlElem.val());
            if (schemaVal == 0) {
                qsSqlDivElem.show();    //显现
            } else {
                qsSqlDivElem.hide();     //隐藏
            }
//			if (codeTab.length > 0 && qsSql.length > 0) {
			if (schemaVal == 0 && qsSql.length > 0) {
                <%--$.ajax({--%>
                    <%--url:'${ctx}/connector/simple/valQsSqlAndCodeTab',--%>
                    <%--type:'post',--%>
                    <%--data:'qsSql=' + qsSql + "&codeTab=" + codeTab,--%>
                    <%--async : false, //默认为true 异步--%>
					<%--dataType:"codeTab",--%>
                    <%--error:function() {--%>
                        <%--alertx("抱歉，取数表和取数过滤条件验证失败");--%>
                    <%--},--%>
                    <%--success:function(data) {--%>
                        <%--alertx("验证成功 = " + data);--%>
                        <%--if (data == true) {--%>
                            <%--alertx("返回true");--%>
                        <%--} else if (data == false) {--%>
                            <%--alertx("返回false");--%>
                        <%--}--%>
                    <%--}--%>
                <%--});--%>
                $.ajax({
                    url: '${ctx}/connector/simple/valQsSqlAndCodeTab',
                    <%--url: '${ctx}/connector/simple/valQsSqlAndCodeTab?qsSql=' + qsSql + "&codeTab=" + codeTab,--%>
                    type: "POST",
//                    type: "GET",
                    data: {'qsSql':qsSql, 'schemaVal':schemaVal},
//                    data:'qsSql=' + qsSql + "&codeTab=" + codeTab,
                    dataType: "json",
                    cache: false,
                    success: function(data) {
//                        alertx("ajax返回值data = " + data);
                        if (data.errMsg.length == 0) {
                            return true;
                        } else if (data.errMsg.length > 0) {
                            alertx("抱歉，“取数SQL”验证失败，请检查！错误信息：" + data.errMsg);
//                            jBox.tip('抱歉，取数表和取数过滤条件验证失败，请检查取数过滤条件', 'error');
//                            qsSqlElem.focus();
                            return false;
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
//                        alertx("抱歉，“取数表”和“取数过滤条件”验证失败，请检查“取数过滤条件”");
                        alertx("抱歉，异步请求出错，原因：" + textStatus + ", " + errorThrown);
//                        qsSqlElem.focus();
                        return false;
                    }
                });
			}
			return true;
        }
		
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
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<a href="${ctx}/codesetmrg/codeSetData/import/template">下载模板</a>
		</form>
	</div>
	
	
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

	<form:form id="inputForm" modelAttribute="codeSet" action="" method="post" class="form-horizontal">
		
		<form:hidden path="id" id="id"/>
		<!--该字段的值代表，0：新增，1：修改 ，2：撤消-->
		<form:hidden path="operation" id="operation" name="operation"/>
		<!--该字段的值代表，1存档数据，0正式数据,2送审数据-->
		<form:hidden path="processStatus" id="processStatus" name="processStatus"/>
		<input type="hidden" id="codeSetType" name="codeSetType" value="1"/>
		<%-- <form:hidden path="changeForm" id="changeForm" name="changeForm"/> --%>
		<input type="hidden" id="changeForm" name="changeForm"/>
		<sys:message content="${message}"/>
		<%--<fieldset>--%>
        <%--<legend>代码集目录</legend>--%>
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
		<%--<div class="control-group">--%>
			<%--<label class="control-label">CodeSetId：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="id" id="id" htmlEscape="false" maxlength="50" class="input-xlarge"/>--%>
			<%--</div>--%>
		<%--</div>--%>
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
				<form:input path="codeSetSort" htmlEscape="false" maxlength="9" class="input-xlarge  digits"/>
			</div>
		</div>
		
		<%-- <div class="control-group">
			<label class="control-label">代码集类型：</label>
			<div class="controls">
				<form:radiobutton path="codeSetType" value="0"/>复杂类型
        		<form:radiobutton path="codeSetType" value="1"/>简单类型
			</div>
		</div>--%>
			<div class="control-group">
				<label class="control-label">模式：</label>
				<div class="controls">
						<%--<form:select path="isFetchFromSys" class="input-mini " onchange="valQsSqlAndCodeTab();">--%>
						<%--<form:option value="1" label="是"/>--%>
						<%--<form:option value="0" label="否"/>--%>
						<%--</form:select>--%>
					<select name="schema" id="schema" class="input-mini" onchange="valQsSqlAndCodeTab();">
						<option value="0">复杂模式</option>
						<option value="1">简单模式</option>
					</select>
				</div>
			</div>
		<div class="control-group" id="qsSqlDiv">
			<label class="control-label">取数SQL：</label>
			<div class="controls">
				<%--<form:textarea path="qsSql" htmlEscape="false" rows="3" class="input-xxlarge" onblur="valQsSqlAndCodeTab();"/>--%>
				<form:textarea path="qsSql" htmlEscape="true" rows="3" class="input-xxlarge required" onblur="valQsSqlAndCodeTab();"/>
					<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">所属机构:</label>
			<div class="controls">
				<sys:treeselect id="office" name="officeIds" value="${codeSet.officeIds}" labelName="office.name" labelValue="${codeSet.officeName}"
								title="机构" url="/sys/office/treeData?type=1" cssClass="" allowClear="true"/>
			</div>
		</div>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">取数表：</label>--%>
			<%--<div class="controls">--%>
				<%--&lt;%&ndash;<form:input path="codeTable" htmlEscape="false" maxlength="50" class="input-xlarge "/>&ndash;%&gt;--%>
				<%--<form:select path="codeTable" class="input-xlarge " onchange="valQsSqlAndCodeTab();">--%>
					<%--<form:option value="" label="--请选择--"/>--%>
					<%--<form:option value="T_MDM_CODE_SET_DATA" label="T_MDM_CODE_SET_DATA"/>--%>
					<%--<form:option value="T_MDM_PENDING_CODE_ITEM" label="T_MDM_PENDING_CODE_ITEM"/>--%>
					<%--<form:option value="T_MDM_SENDING_CODE_ITEM" label="T_MDM_SENDING_CODE_ITEM"/>--%>
					<%--<form:option value="T_MDM_VALIDATION_DETAIL" label="T_MDM_VALIDATION_DETAIL"/>--%>
					<%--<form:option value="T_MDM_VALIDATION_DETAIL_TMP" label="T_MDM_VALIDATION_DETAIL_TMP"/>--%>
				<%--</form:select>--%>
			<%--</div>--%>
		<%--</div> --%>

		<%--div class="form-actions">
			<shiro:hasPermission name="codesetmrg:codeSet:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>--%>
		<%--</fieldset>--%>
		<div class="form-actions">
			<shiro:hasPermission name="codesetmrg:codeSet:edit">
				<input id="btnSave" class="btn btn-primary" type="button" value="保存"/>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返回"/>
		</div>
	</form:form>
	<%--<br/>--%>
	<%--<fieldset>--%>
        <%--<legend>代码集数据</legend>--%>
	<%--<form:form id="searchForm" action="${ctx}/codesetmrg/codeSet/form" method="post" class="breadcrumb form-search">--%>
		<%--<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>--%>
		<%--<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>--%>
		<%--<input id="codeSetId" name="codeSetId" type="hidden" value="${codeSet.id}"/>--%>
		<%--<input id="status" name="status" type="hidden" value="2"/>--%>
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
			<%--<input id="btnReset" class="btn btn-primary" type="button" value="重置"/></li>--%>
			<%--<li class="clearfix"></li>--%>
		<%--</ul>--%>
		<%----%>
	<%--</form:form>--%>

	<%--<table id="contentTable" class="table table-striped table-bordered table-condensed">--%>
		<%--<thead>--%>
			<%--<tr>--%>
				<%--<th>代码集名称</th>--%>
				<%--<th>代码集编码</th>--%>
				<%--<th>上级代码集编码</th>--%>
				<%--<th>年度</th>--%>
				<%--<th>是否启用</th>--%>
				<%--<th>版本</th>--%>
				<%--<th>有效开始时间</th>--%>
				<%--<th>有效结束时间</th>--%>
				<%--<!-- <th style="min-width: 180px">分发关系</th> -->--%>
				<%--<th>排序</th>--%>
				<%--<th>备注</th>--%>
				<%--<shiro:hasPermission name="codesetmrg:codeset:edit"> --%>
				<%--<th>操作</th>--%>
				<%--</shiro:hasPermission> --%>
			<%--</tr>--%>
		<%--</thead>--%>
		<%--<tbody>--%>
		<%--<c:forEach items="${page.list}" var="pendingCodeItem">--%>
			<%--<tr>--%>
				<%--<td>--%>
					<%--<a href="javascript:void(0);" onclick="editCodeItem('${pendingCodeItem.id}')">--%>
						<%--${pendingCodeItem.itemName}--%>
					<%--</a>--%>
				<%--</td>--%>
				<%--<td>${pendingCodeItem.itemCode }</td>--%>
				<%--<td>${pendingCodeItem.parentItemCode }</td>--%>
				<%--<td>${pendingCodeItem.year }</td>--%>
				<%--<td>--%>
					<%--<!-- 是否可用0：不可用 1：未启用；2：已启用 -->--%>
					<%--<c:if test="${pendingCodeItem.status==0 }">--%>
						<%--不可用--%>
					<%--</c:if>--%>
					<%--<c:if test="${pendingCodeItem.status==1 }">--%>
						<%--未启用--%>
					<%--</c:if>--%>
					<%--<c:if test="${pendingCodeItem.status==2 }">--%>
						<%--已启用--%>
					<%--</c:if>--%>
				<%--</td>--%>
				<%--<td>${pendingCodeItem.version }</td>--%>
				<%--<td>--%>
					<%--<fmt:formatDate value="${pendingCodeItem.validStartDate}" pattern="yyyy-MM-dd"/>--%>
				<%--</td>--%>
				<%--<td>--%>
					<%--<fmt:formatDate value="${pendingCodeItem.validEndDate}" pattern="yyyy-MM-dd"/>--%>
				<%--</td>--%>
				<%--&lt;%&ndash; <td>${pendingCodeItem.distribution }</td> &ndash;%&gt;--%>
				<%--<td>${pendingCodeItem.itemCodeSort }</td>--%>
				<%--<td>${pendingCodeItem.id }</td>--%>
				<%--&lt;%&ndash; <td>${pendingCodeItem.processStatus },${pendingCodeItem.codeSetId },${pendingCodeItem.operation }</td> &ndash;%&gt;--%>
				<%--<shiro:hasPermission name="codesetmrg:codeset:edit">--%>
				<%--<td>--%>
    				<%--<a href="javascript:void(0);" onclick="editCodeItem('${pendingCodeItem.id}')">修改</a>--%>
					<%--<a href="javascript:void(0);" onclick="deleteById('${pendingCodeItem.id}')">删除</a>--%>
				<%--</td>--%>
				<%--</shiro:hasPermission>--%>
			<%--</tr>--%>
		<%--</c:forEach>--%>
		<%--</tbody>--%>
	<%--</table>--%>
	<%--<div class="pagination">${page}</div>--%>
	<%--</fieldset>--%>
	<%--<br/><br/>--%>
	
</body>
</html>