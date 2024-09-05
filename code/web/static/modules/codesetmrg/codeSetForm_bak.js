$(document).ready(function() {
	//新增代码集数据
	// 验证编码是否存在 
    jQuery.validator.addMethod("checkCodeSetNo",function(value,element){
    	var id = $("#id").val();
    	var operation = $("#operation").val();
    	var result = false;
    	var obj = {};
		obj.id = id;
		obj.codeSetNo = value;
		obj.operation = operation;
    	var url = ctx+"/codesetmrg/pendingCodeSet/checkCodeSetNo";
		$.ajaxSetup({  
            async : false  
        }); 
		$.post(url, obj,function(data) {
			if (data) {
				result = true;
			}
		});
		return result;     
    },"代码集编码已经被存在!"); 
	
	var $url = ctx+"/codesetmrg/pendingCodeSet/savePendingCodeSet";//保存代码集目录url
	var $inputFrom = $("#inputForm");//保存代码集目录表单
	var id = $("#id").val();
	if(id.length>0){
		var operation = $("#operation").val();//0新增，1修改,2删除，10正式数据
		//当有业务id时，disabled业务系统编码输入框
		if(operation!=0){
			$("#codeGroupId").attr({ readonly: true });//只读代码集分组
			$("#codeSetNo").attr({ readonly: true });
		}
		if(operation==10){
			$("#btnImportHistory").attr("disabled", false);//禁用导入历史版本
			$("#btnExport").attr("disabled", false);//禁用导出
			$("#btnSysSending").attr("disabled", false);//禁用下发业务系统按钮
		}
		if(operation==0){
			//$("#btnPreview").attr("disabled", true);//取消禁用预览按钮
			$("#btnAudit").attr("disabled", false);//取消禁用导入历史版本
			$("#btnReview").attr("disabled", false);//取消禁用
		}
		if(operation==1){
			$("#btnPreview").attr("disabled", false);//取消禁用预览按钮
			$("#btnAudit").attr("disabled", false);//取消禁用审核通过按钮
			$("#btnReview").attr("disabled", false);//取消禁用送审按钮
			$("#btnImportHistory").attr("disabled", false);//取消禁用导入历史版本按钮
		}
	}else{//新增代码集目录
		//$("#btnSysSending").attr("disabled", true);//禁用下发业务系统按钮
	}
	
	//保存代码集目录
	$("#btnSave").click(function() {
		if(validateForm()){
			formSubmit($url,$inputFrom,true);
		}
	});
	//新增代码集数据
	$("#btnAdd").click(function() {
		var isFreshFlag="1";//关闭修改窗口时是否刷新列表页面标识，1：不刷新。2：刷新
		var codeSetNo = $("#codeSetNo").val();
		if(validateForm()){
			formSubmit($url,$inputFrom,false);
			var codeSetId = $("#id").val();
			// 正常打开	
			top.$.jBox.open("iframe:"+ctx+"/codesetmrg/codeSetData/form?codeSetId="+codeSetId+"&codeSetNo="+codeSetNo, "代码集数据录入", 900, 540, {
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
	                //if(isFreshFlag==2){
//						var processStatus = $("#processStatus").val();//0新增，1修改,2删除
//						if(processStatus==10){
							window.location=ctx+"/codesetmrg/codeSet/form?id="+codeSetId+"&operation=1&processStatus=1"; 
//						}else{
//							location.reload();
//						}
	            }
			}); 
		}
		
	}); 
	//导出代码集
	$("#btnExport").click(function(){
		top.$.jBox.confirm("确认要导出代码集数据吗？","系统提示",function(v,h,f){
			if(v=="ok"){
				$("#searchForm").attr("action",ctx+"/codesetmrg/codeSetData/export");
				$("#searchForm").submit();
			}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');
	});
	//导入代码集
	$("#btnImport").click(function(){
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
	//送审
	$("#btnReview").click(function(){
		top.$.jBox.confirm("确认要送审吗？","系统提示",function(v,h,f){
			if(v=="ok"){
				if(validateForm()){
					$("#processStatus").val(2);//送审
					var $url = ctx+"/codesetmrg/pendingCodeSet/review";//送审代码集目录url
					var isOK = formSubmit($url,$inputFrom,false);
					if(isOK){
						jBox.tip('送审成功！', 'success');
						window.setTimeout(function () { 
							window.location=ctx+"/codesetmrg/codeSet/list"; 
						}, 100);
					}
					
				}
			}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');
	});
	
	//审核通过
	$("#btnAudit").click(function(){
		if(validateForm()){
			var html = "<div style='padding:10px;'>请输入是否立即下发业务系统(Y/N)：<input type='text' id='changeForm' name='changeForm' /></div>";
			var submit = function (v, h, f) {
				var r = f.changeForm;
			    if (r == '') {
			    	jBox.tip("请输入是否立即下发业务系统。", 'error', { focusId: "changeForm" }); // 关闭设置 changeForm 为焦点
			        return false;
			    }
			    var reg= /^[A-Za-z]+$/;
				if (reg.test(r)){ //判断是否符合正则表达式
					r = r.toUpperCase();
					if(r == "Y" || r == "N"){
						$("#changeForm").val(r);
						//alert($("#changeForm").val());
						
						var $url = ctx+"/codesetmrg/pendingCodeSet/audit";//审核通过url
						var isOK = formSubmit($url,$inputFrom,false);
						if(isOK){
							jBox.tip('审核成功！', 'success');
							window.setTimeout(function () { 
								window.location=ctx+"/codesetmrg/codeSet/list"; 
							}, 100);
						}
					}else{
						jBox.tip("请输入Y/N");
						return false;
					}
				}else{
					jBox.tip("请输入Y/N");
					return false;
				}
			    return true;
			};
			
			jBox(html, { title: "提示信息", submit: submit });
			
		}
	});
	
	//历史版本导入
	$("#btnImportHistory").click(function(){
		//var id = $("#id").val();//
		formSubmit($url,$inputFrom,false);
		var codeSetId = $("#id").val();
		var codeSetNo = $("#codeSetNo").val();
		window.location=ctx+"/codesetmrg/codeSetData/codeSetHistoryImport?codeSetId="+codeSetId+"&status=0&codeSetNo="+codeSetNo+"&operation="+operation+"&processStatus="+processStatus 
		//window.open(ctx+"/codesetmrg/codeSetData/codeSetHistoryImport?codeSetId="+codeSetId+"&status=0&codeSetNo="+codeSetNo+"&operation="+operation+"&processStatus="+processStatus+"");
	});
	//代码集预览
	$("#btnPreview").click(function(){
		var id = $("#id").val();
		window.location=ctx+"/codesetmrg/pendingCodeSet/preview?id="+id
		//window.open(ctx+"/codesetmrg/codeSetData/codeSetHistoryImport?codeSetId="+codeSetId+"&status=0&codeSetNo="+codeSetNo+"&operation="+operation+"&processStatus="+processStatus+"");
	});
	//关闭
	$("#btnCancel").click(function(){
		window.location=ctx+"/codesetmrg/codeSet"; 
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
				$("#operation").val(data.operation);
				if(isTip){
					jBox.tip('保存成功！', 'success');
				}
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			//alert(XMLHttpRequest.status);
			//alert(XMLHttpRequest.readyState);
			//alert(textStatus);
			jBox.tip('保存失败，请联系管理员', 'error');
			return false;
		}
	});
    return true;
};
//删除代码集数据
function deleteById(id,operation){
	top.$.jBox.confirm("确认要删除代码集数据吗？","系统提示",function(v,h,f){
		if(v=="ok"){
			var codeSetoperation = $("#operation").val();
			//代码集目录非正式数据
			if(codeSetoperation!=10){
				deleteSubmit(id,operation,$("#id").val());
			}else{
				var $url = ctx+"/codesetmrg/pendingCodeSet/savePendingCodeSet";//保存代码集目录url
				var $inputFrom = $("#inputForm");//保存代码集目录表单
				formSubmit($url,$inputFrom,false);
				var codeSetId = $("#id").val();
				deleteSubmit(id,operation,codeSetId);
			}
		}
	},{buttonsFocus:1});
	top.$('.jbox-body .jbox-icon').css('top','55px');
};
//删除代码集数据
function deleteSubmit(id,operation,codeSetId){
	$.ajax({
		url: ctx+'/codesetmrg/pedningCodeItem/delete',
		type: "POST",
		data: {'id':id,'operation':operation,'codeSetId':codeSetId},
		dataType: "json",
		cache: false,
		success: function(data) {
			if(data!=null){
				//location.reload();
				window.location=ctx+"/codesetmrg/codeSet/form?id="+codeSetId+"&operation=1&processStatus=0"; 
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			//alert(XMLHttpRequest.status);
			//alert(XMLHttpRequest.readyState);
			//alert(textStatus);
			jBox.tip('删除失败，请联系管理员', 'error');
		}
	});
};

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
	    		required:"代码集编码不能为空！",
	    	}
	    }
	}).form();     
};
//修改代码集数据
function editCodeItem(id,operation){
	var isFreshFlag="1";//关闭修改窗口时是否刷新列表页面标识，1：不刷新。2：刷新
	var $url = ctx+"/codesetmrg/pendingCodeSet/savePendingCodeSet";//保存代码集目录url
	var $inputFrom = $("#inputForm");//保存代码集目录表单
	if(validateForm()){
		formSubmit($url,$inputFrom,false);
		//alert("原代码集目录ID："+id);
		var codeSetId = $("#id").val();
		var codeSetNo = $("#codeSetNo").val();
		//alert("新代码集目录ID："+codeSetId);
		// 正常打开	
		top.$.jBox.open("iframe:"+ctx+"/codesetmrg/codeSetData/form?id="+id+"&operation="+operation+"&codeSetNo="+codeSetNo+"&codeSetId="+codeSetId, "代码集数据编辑", 900, 540, {
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
                //if(isFreshFlag==2){
                    //location.reload();
                //}
				var processStatus = $("#processStatus").val();//0新增，1修改,2删除
				if(processStatus==10){
					window.location=ctx+"/codesetmrg/codeSet/form?id="+codeSetId+"&operation=1&processStatus=1"; 
				}else{
					location.reload();
				}
            }
		}); 
	}
};

/**
 * 设置代码集分发关系
 * @returns {Boolean}
 */
function doSetSystem(){
	var $url = ctx+"/codesetmrg/pendingCodeSet/savePendingCodeSet";//保存代码集目录url
	var $inputFrom = $("#inputForm");//保存代码集目录表单
	formSubmit($url,$inputFrom,false);
	var codeSetId = $("#id").val();
	var codeSetNo = $("#codeSetNo").val();
	var operation = $("#operation").val();
	$("#setSystem_codeSetId").val(codeSetId);
	$("#setSystem_codeSetNo").val(codeSetNo);
	$("#setSystem_operation").val(operation);
	$("#setSystemForm").submit();//保存代码集目录表单
	return false;
}

/**
 * 下发代码集
 * @returns {Boolean}
 */
function doSendSystem(){
	var codeSetNo = $("#codeSetNo").val();
	$("#setSendSystem_codeSetNo").val(codeSetNo);
	//$("#sendSystemForm").submit();
	$.ajax({
		url: ctx+'/codesetmrg/codeSetData/sendingSystemCodeSet',
		type: "POST",
		data: $("#sendSystemForm").serialize(),
		dataType: "json",
		cache: false,
		success: function(data) {
			if(data!=null){
				jBox.tip('正在后台下发！', 'success');
				$("#sendSystem").modal('hide');
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			//alert(XMLHttpRequest.status);
			//alert(XMLHttpRequest.readyState);
			//alert(textStatus);
			jBox.tip('下发失败，请联系管理员', 'error');
		}
	});
    return false;
}