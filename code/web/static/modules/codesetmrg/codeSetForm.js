// $(document).ready(function() {
// 	//新增代码集数据
// 	// 验证编码是否存在
//     jQuery.validator.addMethod("checkCodeSetNo",function(value,element){
//     	var id = $("#id").val();
//     	var operation = $("#operation").val();
//     	if(operation ==''){
//     		operation=0;
//     	}
//     	var result = false;
//     	var obj = {};
// 		obj.id = id;
// 		obj.codeSetNo = value;
// 		obj.operation = operation;
//     	var url = ctx+"/codesetmrg/pendingCodeSet/checkCodeSetNo";
// 		$.ajaxSetup({
//             async : false
//         });
// 		$.post(url, obj,function(data) {
// 			if (data) {
// 				result = true;
// 			}
// 		});
// 		return result;
//     },"抱歉，代码集编码已经存在，请更改!");
//
// 	var $url = ctx+"/codesetmrg/pendingCodeSet/savePendingCodeSet";//保存代码集目录url
// 	var $inputFrom = $("#inputForm");//保存代码集目录表单
// 	var id = $("#id").val();
// 	if(id.length>0){
// 		var operation = $("#operation").val();//0新增，1修改,2删除，10正式数据
// 		//当有业务id时，disabled业务系统编码输入框
// 		if(operation!=0){
// 			$("#codeGroupId").attr({ readonly: true });//只读代码集分组
// 			$("#codeSetNo").attr({ readonly: true });
// 		}
// 		if(operation==10){
// 			$("#btnImportHistory").attr("disabled", false);//禁用导入历史版本
// 			$("#btnExport").attr("disabled", false);//禁用导出
// 			$("#btnSysSending").attr("disabled", false);//禁用下发业务系统按钮
// 		}
// 		if(operation==0){
// 			//$("#btnPreview").attr("disabled", true);//取消禁用预览按钮
// 			$("#btnAudit").attr("disabled", false);//取消禁用导入历史版本
// 			$("#btnReview").attr("disabled", false);//取消禁用
// 		}
// 		if(operation==1){
// 			$("#btnPreview").attr("disabled", false);//取消禁用预览按钮
// 			$("#btnAudit").attr("disabled", false);//取消禁用审核通过按钮
// 			$("#btnReview").attr("disabled", false);//取消禁用送审按钮
// 			$("#btnImportHistory").attr("disabled", false);//取消禁用导入历史版本按钮
// 		}
// 	}else{//新增代码集目录
// 		//$("#btnSysSending").attr("disabled", true);//禁用下发业务系统按钮
// 	}
//
// 	//保存代码集目录
// 	$("#btnSave").click(function() {
// 		if(validateForm()){
//             loading('正在提交，请稍等...');
// 			$("#inputForm").submit();
// 		}
// 	});
//
// });

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