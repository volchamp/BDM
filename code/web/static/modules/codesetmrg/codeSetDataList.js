$(document).ready(function() {
	//新增代码集数据
	
	var id = $("#id").val();
	if(id.length>0){
		var processStatus = $("#processStatus").val();//2送审
		if(processStatus!=2){
			$("#btnImports").attr("disabled", false);//导入
			if(size!=0){
				var operation = $("#operation").val();//0新增，1修改,2删除，10正式数据
				//当有业务id时，disabled业务系统编码输入框
				if(operation!=0){
					$("#codeGroupId").attr({ readonly: true });//只读代码集分组
					$("#codeSetNo").attr({ readonly: true });
				}
				if(operation==10){
					$("#btnImportHistory").attr("disabled", false);//禁用导入历史版本
					$("#btnExport").attr("disabled", false);//导出
					$("#btnBatchDel").attr("disabled", false);
					$("#carryOver").attr("disabled", false);
					$("#btmCodesetMapping").attr("disabled", false);
					$("#btnSysSending").attr("disabled", false);
				}
				if(operation==0){
					//$("#btnPreview").attr("disabled", true);//取消禁用预览按钮
					$("#btnAudit").attr("disabled", false);//取消禁用导入历史版本
					$("#btnReview").attr("disabled", false);//取消禁用
					$("#btnBatchDel").attr("disabled", false);
					$("#carryOver").attr("disabled", false);
                    // $("#btnExport").attr("disabled", false);//导出
                    $("#btmCodesetMapping").attr("disabled", false);
					//$("#btnSysSending").attr("disabled", false);
					$("#btnImportHistory").attr("disabled",true);
				}
				if(operation==1){
					$("#btnPreview").attr("disabled", false);//取消禁用预览按钮
					$("#btnAudit").attr("disabled", false);//取消禁用审核通过按钮
					$("#btnReview").attr("disabled", false);//取消禁用送审按钮
					$("#btnImportHistory").attr("disabled", false);//取消禁用导入历史版本按钮
					$("#btnBatchDel").attr("disabled", false);
					$("#carryOver").attr("disabled", false);
                    // $("#btnExport").attr("disabled", false);//导出
                    $("#btmCodesetMapping").attr("disabled", false);
					//$("#btnSysSending").attr("disabled", false);
				}
			}
		}
		$("#btnAdvanced").attr("disabled", false);
	}
	
	//导出代码集
	$("#btnExport").click(function(){
		top.$.jBox.confirm("确认要导出代码集数据吗？","系统提示",function(v,h,f){
			if(v=="ok"){
				$("#searchForm").attr("action",ctx+"/codesetmrg/codeSetData/export");
				$("#searchForm").submit();
				$("#searchForm").attr("action",ctx+"/codesetmrg/pedningCodeItem/list");
			}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');
	});
	//导入代码集
	$("#btnImport").click(function(){
		var codeSetNo = $("#codeSetNo").val();
		var codeSetId = $("#id").val();
		var operation = $("#operation").val();
		var processStatus = $("#processStatus").val();
		$("#import_codeSetId").val(codeSetId);
		$("#import_codeSetNo").val(codeSetNo);
		$("#import_operation").val(operation);
		$("#import_processStatus").val(processStatus);
		$.jBox($("#importBox").html(), {
			title:"导入数据", 
			buttons:{"关闭":true}, 
			bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"
		});
	});
	
	//送审
	$("#btnReview").click(function(){
		top.$.jBox.confirm("确认要送审吗？","系统提示",function(v,h,f){
			if(v=="ok"){
					var $url = ctx+"/codesetmrg/pendingCodeSet/review";//送审代码集目录url
					var $inputFrom = $("#searchForm");
					var isOK = formSubmit($url,$inputFrom,false);
					// alert("isOK = " + isOK);
					if(isOK){
						alertx("送审成功！");
						// location.reload();
						// parent.refreshTree();
//						window.setTimeout(function () { 
							//window.location=ctx+"/codesetmrg/codeSetData/index"
							//parent.location.reload();
							//top.location.href = ctx+"/codesetmrg/codeSetData/index"
//						}, 100);
					} else {
                        alertx("抱歉，送审失败！");
					}
                location.reload();
                parent.refreshTree();
			}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');
	});
	
	//审核通过
	$("#btnAudit").click(function(){
        var $url = ctx+"/codesetmrg/pendingCodeSet/audit";//审核通过url
        layer.confirm('是否立即下发业务系统？', {
                // time: 20000, //20s后自动关闭
                btn: ['是', '否', '取消'],
                icon: 3,
                title:'请选择'
            }
            , function(){
                $("#changeForm").val("Y");
                var $inputFrom = $("#searchForm");
                //var data = formSubmit($url,$inputFrom,false);
                $.ajax({
                    url: $url,
                    type: "POST",
                    data: $inputFrom.serialize(),
                    dataType: "json",
                    cache: false,
                    async:false,
                    success: function(data) {
                        if(data!=null){
                            alertx("审核成功！");
                            location.replace(ctx+"/codesetmrg/pedningCodeItem/list?id="+data.id+"&operation=10&processStatus=10");
                            parent.refreshTree();
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        // jBox.tip('保存失败，请联系管理员', 'error');
                        layer.msg("抱歉，异步请求出错，原因：" + textStatus + ", " + errorThrown);
                        return false;
                    }
                });
                // var changeForm = $("#changeForm").val();
                // layer.msg("changeForm = " + changeForm);
                // alertx("审核成功");
                // location.replace(ctx+"/codesetmrg/pedningCodeItem/list?id="+data.id+"&operation=10&processStatus=10");
                // parent.refreshTree();
            }, function(){
                $("#changeForm").val("N");
                var $inputFrom = $("#searchForm");
                $.ajax({
                    url: $url,
                    type: "POST",
                    data: $inputFrom.serialize(),
                    dataType: "json",
                    cache: false,
                    async:false,
                    success: function(data) {
                        if(data!=null){
                            alertx("审核成功！");
                            location.replace(ctx+"/codesetmrg/pedningCodeItem/list?id="+data.id+"&operation=10&processStatus=10");
                            parent.refreshTree();
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        layer.msg("抱歉，异步请求出错，原因：" + textStatus + ", " + errorThrown);
                        return false;
                    }
                });
            }
        );

        // var changeForm = $("#changeForm").val();
        // alertx("changeForm = " + changeForm);
        // return;
			// var html = "<div style='padding:10px;'>请输入是否立即下发业务系统(Y/N)：<input type='text' id='changeForm' name='changeForm' /></div>";
			// var submit = function (v, h, f) {
			// 	var r = f.changeForm;
			//     if (r == '') {
			//     	jBox.tip("请输入是否立即下发业务系统。", 'error', { focusId: "changeForm" }); // 关闭设置 changeForm 为焦点
			//         return false;
			//     }
			//     var reg= /^[A-Za-z]+$/;
			// 	if (reg.test(r)){ //判断是否符合正则表达式
			// 		r = r.toUpperCase();
			// 		if(r == "Y" || r == "N"){
			// 			$("#changeForm").val(r);
			//
			// 			var $url = ctx+"/codesetmrg/pendingCodeSet/audit";//审核通过url
			// 			var $inputFrom = $("#searchForm");
			// 			//var data = formSubmit($url,$inputFrom,false);
			// 			$.ajax({
			// 				url: $url,
			// 				type: "POST",
			// 				data: $inputFrom.serialize(),
			// 				dataType: "json",
			// 				cache: false,
			// 				async:false,
			// 				success: function(data) {
			// 					if(data!=null){
			// 						alertx("审核成功！");
			// 						location.replace(ctx+"/codesetmrg/pedningCodeItem/list?id="+data.id+"&operation=10&processStatus=10");
			// 						parent.refreshTree();
			// 					}
			// 				},
			// 				error: function(XMLHttpRequest, textStatus, errorThrown) {
			// 					jBox.tip('保存失败，请联系管理员', 'error');
			// 					return false;
			// 				}
			// 			});
			//
			// 		}else{
			// 			jBox.tip("请输入Y/N");
			// 			return false;
			// 		}
			// 	}else{
			// 		jBox.tip("请输入Y/N");
			// 		return false;
			// 	}
			//     return true;
			// };
			//
			// jBox(html, { title: "提示信息", submit: submit });
			
	});
	
	//历史版本导入
	$("#btnImportHistory").click(function(){
		//var id = $("#id").val();//
		var codeSetId = $("#id").val();
		var codeSetNo = $("#codeSetNo").val();
		var op = operation;
		if (typeof(op) == "undefined" ) {
			op = $("#operation").val();
		}
        // alert("op = " + op);
        window.location=ctx+"/codesetmrg/codeSetData/codeSetHistoryImport?codeSetId="+codeSetId+"&status=0&codeSetNo="+codeSetNo+"&operation="+op+"&processStatus="+processStatus
		//window.open(ctx+"/codesetmrg/codeSetData/codeSetHistoryImport?codeSetId="+codeSetId+"&status=0&codeSetNo="+codeSetNo+"&operation="+operation+"&processStatus="+processStatus+"");
	});
	//代码集预览
	$("#btnPreview").click(function(){
		var id = $("#id").val();
		window.location=ctx+"/codesetmrg/pendingCodeSet/preview?id="+id
		//window.open(ctx+"/codesetmrg/codeSetData/codeSetHistoryImport?codeSetId="+codeSetId+"&status=0&codeSetNo="+codeSetNo+"&operation="+operation+"&processStatus="+processStatus+"");
	});
	//批量删除代码集数据
	$("#btnBatchDel").click(function(){
		var str=false;
		$("[name='selectedRow']").each(function() {
			if ($(this).attr("checked")) {
				str=true;
			}
		});
		if (str) {
			top.$.jBox.confirm("确认要批量删除代码集数据吗？","系统提示",function(v,h,f){
				if(v=="ok"){
					//$("#inputForm").submit();
					$.ajax({
						url: ctx+'/codesetmrg/pedningCodeItem/batchDelete',
						type: "POST",
						data: $("#inputForm").serialize(),
						dataType: "json",
						cache: false,
						success: function(data) {
							if(data!=null){
								alertx("代码集数据删除成功!");
								//parent.location.reload(); 
								location.reload();
								parent.refreshTree();
								//window.location=ctx+"/codesetmrg/codeSet/form?id="+codeSetId+"&operation=1&processStatus=0"; 
							}
						},
						error: function(XMLHttpRequest, textStatus, errorThrown) {
							jBox.tip('删除失败，请联系管理员', 'error');
						}
					});
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		}else{
			alertx("请选择删除的代码集数据");
		}
	});
	
	function deleteById(id,operation){
		var codeSetProcessStatus = $("#processStatus").val();
		if(codeSetProcessStatus!=2){
			top.$.jBox.confirm("确认要删除代码集数据吗？","系统提示",function(v,h,f){
				if(v=="ok"){
					var codeSetOperation = $("#operation").val();
					deleteSubmit(id,operation,$("#id").val(),codeSetOperation);
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		}else{
			alertx("代码集数据正在审核中，暂时无法删除!");
		}
		
	};
	
});

function formSubmit ($url,$inputFrom,isTip){
	var result;
	$.ajax({
		url: $url,
		type: "POST",
		data: $inputFrom.serialize(),
		dataType: "json",
		cache: false,
		async:isTip,
		success: function(data) {
            // alert("77data = " + data.id);
			// if (data == null || data == "null") {
             //    jBox.tip('抱歉，保存失败！', 'error');
             //    return false;
			// }
            // if(data!=null){
			// 	$("#id").val(data.id);
			// 	$("#operation").val(data.operation);
			// 	if(isTip){
			// 		jBox.tip('保存成功！', 'success');
			// 	}
			// 	return true;
			// }
            if (data.id.length == 0) {
                jBox.tip('抱歉，保存失败！', 'error');
                // alert("data.id.length == 0, " + data.id);
                result = false;
                return false;
            } else if (data.id.length > 0) {
                $("#id").val(data.id);
                $("#operation").val(data.operation);
                if(isTip){
                    jBox.tip('保存成功！', 'success');
                }
                // alert("data.id.length > 0, " + data.id);
                result = true;
                return true;
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
    // alert("888 = " + result);
    // return true;
    return result;
}

//修改代码集数据
function editCodeItem(id,operation){
		var codeSetProcessStatus = $("#processStatus").val();//2送审
		var codeSetOperation = $("#operation").val();
		var codeSetNo = $("#codeSetNo").val();
		var codeSetId = $("#id").val();
		if(codeSetProcessStatus!=2){
			window.location=ctx+"/codesetmrg/codeSetData/form?id="+id+"&operation="+operation+"&codeSetOperation="+codeSetOperation+"&codeSetProcessStatus="+codeSetProcessStatus+"&codeSetNo="+codeSetNo+"&codeSetId="+codeSetId; 
		}else{
			alertx("代码集数据正在审核中，暂时无法修改!");
		}
};

//删除代码集数据
function deleteById(id,operation){
	var codeSetProcessStatus = $("#processStatus").val();
	if(codeSetProcessStatus!=2){
		top.$.jBox.confirm("确认要删除代码集数据吗？","系统提示",function(v,h,f){
			if(v=="ok"){
				var codeSetOperation = $("#operation").val();
				deleteSubmit(id,operation,$("#id").val(),codeSetOperation);
			}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');
	}else{
		alertx("代码集数据正在审核中，暂时无法删除!");
	}
	
};
//删除代码集数据
function deleteSubmit(id,operation,codeSetId,codeSetOperation){
	$.ajax({
		url: ctx+'/codesetmrg/pedningCodeItem/delete',
		type: "POST",
		data: {'id':id,'operation':operation,'codeSetId':codeSetId,'codeSetOperation':codeSetOperation},
		dataType: "json",
		cache: false,
		success: function(data) {
			if(data!=null){
				alertx("代码集数据删除成功!");
				//parent.location.reload(); 
				location.reload();
				parent.refreshTree();
				//window.location=ctx+"/codesetmrg/codeSet/form?id="+codeSetId+"&operation=1&processStatus=0"; 
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			jBox.tip('删除失败，请联系管理员', 'error');
		}
	});
};

/**
 * 设置代码集分发关系
 * @returns {Boolean}
 */
function doSetSystem(){
	
	//$("#setSystemForm").submit();//保存代码集目录表单
	var checked = [];
	$("input:checkbox[name='systemIds']:checked").each(function() {
        checked.push($(this).val());
    });
    $("#systemIds").val(checked);
    var str=false;
	$("[name='selectedRow']").each(function() {
		if ($(this).attr("checked")) {
			str=true;
		}
	});
	var msg = '';
	if (str) {
		msg = '确定要为选择的代码集数据设置该分发关系吗？';
	}else{
		msg = '确定全部代码集数据设置该分发关系吗？';
	}
	
	top.$.jBox.confirm(msg,"系统提示",function(v,h,f){
		if(v=="ok"){
			$.ajax({
				url: ctx+'/codesetmrg/pedningCodeItem/setSystem',
				type: "POST",
				data: $("#inputForm").serialize(),
				dataType: "json",
				cache: false,
				success: function(data) {
					if(data!=null){
						alertx('分发关系设置成功！');
						$("#sendSystem").modal('hide');
						location.reload();
						parent.refreshTree();
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					alertx('分发关系设置失败，请联系管理员');
				}
			});
		}
	},{buttonsFocus:1});
	top.$('.jbox-body .jbox-icon').css('top','55px');
	
	return false;
}

/**
 * 下发代码集
 * @returns {Boolean}
 */
function doSendSystem(){
	//$("#sendSystemForm").submit();
	var checked = [];
	var send=true;
	$("input:checkbox[name='SendsystemIds']:checked").each(function() {
        checked.push($(this).val());
        send=false;
    });
    $("#systemIds").val(checked);
    
	if(send){
		alertx("请选择下发的业务系统");
		return;
	}
    var str=false;
	$("[name='selectedRow']").each(function() {
		if ($(this).attr("checked")) {
			str=true;
		}
	});
	var msg = '';
	if (str) {
		msg = '确定下发选择的代码集数据吗？';
	}else{
		msg = '确定下发全部代码集数据吗？';
	}
	
	top.$.jBox.confirm(msg,"系统提示",function(v,h,f){
		if(v=="ok"){
			$.ajax({
				url: ctx+'/codesetmrg/codeSetData/sendingSystemCodeSet',
				type: "POST",
				data: $("#inputForm").serialize(),
				dataType: "json",
				cache: false,
				success: function(data) {
					if(data!=null){
						alertx('正在后台下发！');
						$("#sendSystem").modal('hide');
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					alertx('下发失败，请联系管理员');
				}
			});
		}
	},{buttonsFocus:1});
	top.$('.jbox-body .jbox-icon').css('top','55px');
	
    return false;
}
