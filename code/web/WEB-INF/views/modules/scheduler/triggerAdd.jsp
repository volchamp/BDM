<%--
 * 添加计划-表单
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
	<title>添加计划</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			var planTypeVal = "${planTypeVal}";
			
			loadData(planTypeVal);
			
			$("#inputForm").validate({
				submitHandler: function(form){
					var str = getPlan();
					$("#planJson").val(str);
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
		
		function getPlan() {
			var planType = $("input[name=rdoTimeType]:checked").val();
			var str = "";
			switch(planType) {
				case "1":
						var date = $("#txtOnceDate").val();
						var h = $("#txtOnceHour").val();
						var m = $("#txtOnceMinute").val();
						var s = $("#txtOnceSecond").val();
						str = date + " " + h + ":" + m + ":" + s ;
						str = "{\"type\":1,\"timeInterval\":\"" +str+"\"}"; 
					break; 
				case "2":
						str = $("#selEveryDay").val();
						str = "{\"type\":2,\"timeInterval\":\"" +str+"\"}"; 
					break; 
				case "3":
					    var h = $("#txtDayHour").val();
					    var m = $("#txtDayMinute").val();
			            str =  h +":" + m ;
			            str ="{\"type\":3,\"timeInterval\":\"" +str+"\"}"; 
					break; 
				case "4":
						str = getChkVal("chkWeek");
						var h = $("#txtWeekHour").val();
						var m = $("#txtWeekMinute").val();
						str += "|" + h +":" + m ;
						str = "{\"type\":4,\"timeInterval\":\"" +str+"\"}"; 
					break; 
				case "5":
						str = getChkVal("chkMon");
						var h = $("#txtMonHour").val();
						var m = $("#txtMonMinute").val();
				        str += "|" + h +":" + m ;
				        str = "{\"type\":5,\"timeInterval\":\"" +str+"\"}"; 
					break; 
				// cron表达式
				case "6":
						str += $("#txtCronExpression").val();
						str = "{\"type\":6,\"timeInterval\":\"" +str+"\"}"; 
					break; 
			}
			return str;
		}
		
		/**
		 * 根据复选框的名称获取选中值，使用逗号分隔。
		 * 
		 * @param name
		 * @returns {String}
		 */
		function getChkVal(name) { 
			var str = "";
			$('input[type="checkbox"][name=' + name + ']').each(function() {
				if ($(this).attr('checked')) {
					str += $(this).val() + ",";
				}
			});
			if (str != "")
				str = str.substring(0, str.length - 1);
			return str;
		}
		
		/**
		 * 加载组件的数据
		 * @param {String} 计划类型
		 */
		function loadData(planType) {
			$("input[name=rdoTimeType][value=" + planType + "]").attr("checked","checked");
			var chkWeek = "${chkWeek}";
			var chkArr = chkWeek.split(",");
			var days = "${days}";
			var daysArr = days.split(",");
			// 每周
			if (planType == 4) {
				for (var i in chkArr) {
					$('input[name="chkWeek"][value=' + chkArr[i] + ']').attr("checked","checked");
				}
			}
			
			// 每月
			if (planType == 5) {
				for (var i in daysArr) {
					$('input[name="chkMon"][value=' + daysArr[i] + ']').attr("checked","checked");
				}
			}
			
		}
	</script>
	<style type="text/css">
		.select2-container{ width:80px;}
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/scheduler/getTriggersByJob?jobName=${jobName}">计划列表</a></li>
		<li class="active"><a href="${ctx}/sys/scheduler/addTrigger${isAdd eq 1 ? '1' : '3' }?jobName=${jobName}">${isAdd eq 1 ? '添加' : '修改' }定时计划:${jobName}</a></li>
	</ul><br/>
	<form id="inputForm" action="${ctx}/sys/scheduler/addTrigger${isAdd eq 1 ? '2' : '4' }" method="post" class="form-horizontal">
		<div class="control-group">
			<label class="control-label">计划名称：</label>
			<div class="controls">
				<input type="text" id="trigName" name="trigName" ${isAdd eq 1 ? '' : 'readOnly' } class="input-xlarge required" size="40" value="${trigger.name }"/>
				<input id="planJson" name="planJson" type="hidden" />	
 				<input id="jobName" name="jobName" type="hidden" value="${jobName}" />				
			</div>
		</div>	
		<div class="control-group">
			<label class="control-label"><input type="radio" value="1"  name="rdoTimeType" />一次：</label>
			<div class="controls">
			开始：<input type="text" id="txtOnceDate" class="Wdate" value="${dateStr }" size="10" onclick="WdatePicker({minDate:'%y-%M-{%d}'})" />
				<select id="txtOnceHour" >
					<c:forEach begin="0" end="23" step="1" var="tmp">
						<option value="${tmp }" ${date.hours eq tmp ? 'selected':'' }>${tmp }时</option>
					</c:forEach>
				</select>
				<select id="txtOnceMinute">
					<c:forEach begin="0" end="55" step="5" var="tmp">
						<option value="${tmp }" ${date.minutes eq tmp ? 'selected':'' }>${tmp }分</option>
					</c:forEach>
					<option value="59" ${date.minutes eq 59 ? 'selected':'' }>59分</option>
				</select>
				<select id="txtOnceSecond">
					<c:forEach begin="0" end="55" step="5" var="tmp">
						<option value="${tmp }" ${date.seconds eq tmp ? 'selected':'' }>${tmp }秒</option>
					</c:forEach>
					<option value="59" ${date.seconds eq 59 ? 'selected':'' }>59秒</option>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><input type="radio" value="2" name="rdoTimeType" ${isAdd eq 1 ? 'checked=checked' : '' } />每天：</label>
			<div class="controls">
				<select id="selEveryDay">
					<option value="1" ${repeatInterval eq 1 ? 'selected' : '' }>1分钟</option>
           			<option value="5" ${repeatInterval eq 5 ? 'selected' : '' }>5分钟</option>
           			<option value="10" ${repeatInterval eq 10 ? 'selected' : '' }>10分钟</option>
           			<option value="15" ${repeatInterval eq 15 ? 'selected' : '' }>15分钟</option>
           			<option value="30" ${repeatInterval eq 30 ? 'selected' : '' }>30分钟</option>
           			<option value="60" ${repeatInterval eq 60 ? 'selected' : '' }>1小时</option>
           		</select>			
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><input type="radio"  value="3" name="rdoTimeType" />每天：</label>
			<div class="controls">
				<select id="txtDayHour">
					<c:forEach begin="0" end="23" step="1" var="tmp">
						<option value="${tmp }" ${date.hours eq tmp ? 'selected':'' }>${tmp }时</option>
					</c:forEach>
				</select>
				<select id="txtDayMinute">
					<c:forEach begin="0" end="55" step="5" var="tmp">
						<option value="${tmp }" ${date.minutes eq tmp ? 'selected':'' }>${tmp }分</option>
					</c:forEach>
					<option value="59" ${date.minutes eq 59 ? 'selected':'' }>59分</option>
				</select>			
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><input type="radio" value="4" name="rdoTimeType" />每周：</label>
			<div class="controls">
				<input type="checkbox" name="chkWeek" value="MON"/>星期一
          		<input type="checkbox" name="chkWeek" value="TUE"/>星期二
          		<input type="checkbox" name="chkWeek" value="WED"/>星期三
          		<input type="checkbox" name="chkWeek" value="THU"/>星期四
          		<input type="checkbox" name="chkWeek" value="FRI"/>星期五
          		<input type="checkbox" name="chkWeek" value="SAT"/>星期六
          		<input type="checkbox" name="chkWeek" value="SUN"/>星期日	<br/>
            	<select id="txtWeekHour">
              		<c:forEach begin="0" end="23" step="1" var="tmp">
						<option value="${tmp }" ${hours eq tmp ? 'selected':'' }>${tmp }时</option>
					</c:forEach>
				</select>
				<select id="txtWeekMinute">
					<c:forEach begin="0" end="55" step="5" var="tmp">
						<option value="${tmp }" ${minutes eq tmp ? 'selected':'' }>${tmp }分</option>
					</c:forEach>
					<option value="59" ${minutes eq 59 ? 'selected':'' }>59分</option>
				</select>			
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><input type="radio" value="5" name="rdoTimeType" />每月：</label>
			<div class="controls">
				<c:forEach begin="1" end="31" var="mon">
					<input type="checkbox" name="chkMon" value="${mon}"/>${mon}
				</c:forEach>
             		<input type="checkbox" name="chkMon" value="L"/>最后一天<br/>
                <select id="txtMonHour">
              		<c:forEach begin="0" end="23" step="1" var="tmp">
						<option value="${tmp }" ${hours eq tmp ? 'selected':'' }>${tmp }时</option>
					</c:forEach>
				</select>
				<select id="txtMonMinute">
					<c:forEach begin="0" end="55" step="5" var="tmp">
						<option value="${tmp }" ${minutes eq tmp ? 'selected':'' }>${tmp }分</option>
					</c:forEach>
					<option value="59" ${minutes eq 59 ? 'selected':'' }>59分</option>
				</select>		
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><input type="radio" value="6" name="rdoTimeType" />Cron表达式：</label>
			<div class="controls">
				<input type="text" id="txtCronExpression" name="txtCronExpression" value="${cron }" class="input-xlarge" />		
			</div>
		</div>	
		<div class="form-actions">
			<shiro:hasPermission name="sys:scheduler:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
</body>
</html>