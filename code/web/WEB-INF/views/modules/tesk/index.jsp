<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码集任务管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" src="${ctxStatic}/artTemplate/dist/template-debug.js"></script>
	<script type="text/javascript" src="${ctxStatic}/echarts/echarts.js"></script>
<%--<jsp:include page="../../../../securityJsp/nfrrinc.jsp" />--%>
	<%--<%@include file="/securityJsp/nfrrinc.jsp"%>--%>
	<script type="text/javascript">
		$(document).ready(function() {
            //重置
            $("#btnReset").click(function() {
                $("#codeSetName").val("");
                $("#taskStartDateStart").val("");
                $("#taskStartDateEnd").val("");
                $("#taskCompleteDateStart").val("");
                $("#taskCompleteDateEnd").val("");
                var sysNameFirstOpt = $("#systemName").find("option:first");
                $(".select2-container").find(".select2-chosen").text(sysNameFirstOpt.text());	//所有下拉框的文本内容置为业务系统下拉框第一个选项的文本内容
                sysNameFirstOpt.attr("selected","selected");	//选中下拉框的第一个选项
                $("#taskTypeId").find("option:first").attr("selected","selected");
            });

            //高级查询按钮
            $("#btnAdvanced").click(function() {
                var seaDivElem = $("#searchDiv");
                if (seaDivElem.is(":hidden")) {
                    seaDivElem.show();    //如果元素为隐藏,则将它显现
                } else {
                    seaDivElem.hide();     //如果元素为显现,则将其隐藏
                }
            });

            // 为模块加载器配置echarts的路径，从当前页面链接到echarts.js，定义所需图表路径
            require.config({
                paths : {
                    echarts : '${ctxStatic}/echarts/'
                }
            });

            // 动态加载echarts然后在回调函数中开始使用，注意保持按需加载结构定义图表路径
            require([ 'echarts',
                'echarts/chart/bar'// 柱形图（纵向），堆积柱形图，条形图（横向），堆积条形图
                ,'echarts/chart/pie'// 饼图
            ], function(ec) {
                drawBar(ec);
                echartPie(ec);
            });
		});

		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		/**
		 * 任务处理
		 * @param {String} 任务链接
		 */
		function handle(taskLink){
			
		}
		
		// 指派任务
		function assign(id,taskHandlerId,taskHandlerName){
//		    alertx("11");
			$("#assignUserId").val(id);
			$("#taskHandlerId").val(taskHandlerId);
			$("#taskHandlerName").val(taskHandlerName);
			$("#taskHandlerName").click();
		}
		
		// 选择用户回调函数
		function taskHandlerTreeselectCallBack(v, h, f){
			// 指派用户ID
			var id = $("#assignUserId").val();
			var taskHandlerId = $("#taskHandlerId").val();
			var url = ctx + "/tesk/task/assign";
			var obj = {"id": id,"taskHandlerId": taskHandlerId};
			$.post(url, obj,function(data) {
				if (data) {
                    var admin = "${fns:getUser().admin}";
					top.$.jBox.tip("操作成功");
					//当前不是超级管理员，指派成功要刷新
					// if (admin == "false") {
                        setTimeout("refreshUnhandledList()", 500);
                    // }
				} else {
					top.$.jBox.tip("指派失败,请联系管理员！");
				}	
			});
		}

        /**
		 * 刷新任务列表
         */
		function refreshUnhandledList() {
            <%--var seaFormElem = $("#searchForm");--%>
            <%--seaFormElem.attr("action", "${ctx}/tesk/task/?taskStatus=0");--%>
            <%--seaFormElem.submit();--%>
			window.location.reload();
        }

        var json;
        /**
         * 功能：创建标准柱状图
         * @param {Object}
         */
        function drawBar(ec){
            // var json;
            $.ajax({
                type: "POST",
                url: ctx + "/codesetstatistics/statistics/getData",
                async: false,
                success: function(data){

                    if (data) {
                        json = data;
                        template.onerror({"错误提示": "数据出错,请联系管理员！"});
                        // var html = template("contentTable_t",{list: json });
                        // $("#contentTable").html(html);
                    } else {
                        // $("#contentTable").html("数据出错,请联系管理员！");
                        alertx("抱歉，统计分析数据加载错误，请稍后再试或联系管理员")
                    }
                }
            });

            var myChart = ec.init(document.getElementById('barContent'));
            var xData = [];
            var yData = [];
            for (var i in json) {
                xData.push(json[i].systemName);
                yData.push(json[i].total);
            }

            var option = {
                title : {
                    text: '代码集统计柱状图'
                },
                tooltip : {
                    trigger: 'axis'
                },
                /* legend: {
                    data:[]
                }, */
                toolbox: {
                    show : true,
                    feature : {
                        // mark : {show: true},
                        // dataView : {show: true, readOnly: false},
                        // magicType : {show: true, type: ['line', 'bar']},
                        // restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : xData
                        //设置字体倾斜
                        /* axisLabel:{
                            interval:0,
                            rotate:45,//倾斜度 -90 至 90 默认为0
                            margin:2,
                            textStyle:{
                                fontWeight:"bolder",
                                color:"#000000"
                            }
                        }, */
                    }
                ],
                yAxis : [
                    {
                        type : 'value'
                    }
                ],
                series : [
                    {
                        name:'数量',
                        type:'bar',
                        data:yData,
                        markPoint : {
                            data : [
                                {type : 'max', name: '最大值'},
                                {type : 'min', name: '最小值'}
                            ]
                        }
                    }
                ]
            };

            myChart.setOption(option);
        }

        function echartPie(ec) {
            var myChart = ec.init(document.getElementById('pieDiv'));
            var sysNameArr = [];
            var sysNameCountArr = [];
            for (var i in json) {
                sysNameArr.push(json[i].systemName);
                sysNameCountArr.push({'value':json[i].total,'name':json[i].systemName});
            }
            var sysNameCountJson = JSON.stringify(sysNameCountArr);

            var option = {
                title : {
                    text: '代码集分析饼图',
                    // subtext: '纯属虚构',
                    x:'center'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    orient : 'vertical',
                    x : 'left',
                    data:sysNameArr
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                calculable : true,
                series : [
                    {
                        name:'数量',
                        type:'pie',
                        radius : '55%',
                        center: ['50%', '60%'],
                        data:sysNameCountArr
                    }
                ]
            };
            myChart.setOption(option);
        }
		
	</script>
	<style type="text/css">
		.form-search .ul-form li label{ width:120px;}
	</style>
</head>
<body>
<%--<ul class="nav nav-tabs">--%>
	<%--<li class="active"><a href="${ctx}/tesk/task/test">我的首页</a></li>--%>
<%--</ul>--%>
<sys:treeselect id="taskHandler" name="taskHandlerId" value="" labelName="taskHandlerName" labelValue=""
				title="用户" url="/sys/office/treeData?type=3" cssStyle="display:none;" allowClear="true" notAllowSelectParent="true" hideBtn="true"
/>
<input id="assignUserId" type="hidden" value=""/>
	<!-- 新建页面 -->
	<div class="n1112_nbox" id="n1112_nbox">
			<!-- 我的任务 -->
			<div class="layui-card">
				<div class="layui-card-header">
					<div class="n1112_cardicon"></div>
					我的任务
					<a class="amore" jurl="${ctx}/tesk/task/?taskStatus=0" id="amore1" tabName="我的任务" href="javascript:void(0)" >更多>></a>
				</div>
				<div class="layui-card-body">
						<sys:message content="${message}"/>
						<table id="contentTable" class="table table-striped table-bordered table-condensed">
							<thead>
								<tr>
									<th>
										任务送达时间
									</th>
									<th>
										业务系统名称
									</th>
									<th>
										所属区域
									</th>
									<th>
										代码集目录名称
									</th>
									<th>
										任务类型
									</th>
									<%--<th>--%>
										<%--taskHandlerId--%>
									<%--</th>--%>
									<%--<c:if test="${task.taskStatus == 0}">--%>
										<%--<shiro:hasPermission name="tesk:task:edit">--%>
											<th>操作</th>
										<%--</shiro:hasPermission>--%>
									<%--</c:if>--%>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${pageToDoTask.list}" var="task">
								<tr>
									<td>
										<div class="n1112_cardTable_icon"></div>
										<fmt:formatDate value="${task.taskStartDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
									</td>
									<td>
										${task.systemName }
										<%--${task.id}--%>
									</td>
									<td>
										${task.areaName }
									</td>
									<td>
										${task.codeSetName }
									</td>
									<td>
										${task.taskType }
									</td>
									<%--<td>--%>
										<%--${task.taskHandlerId }--%>
									<%--</td>--%>
									<td>
										<!-- 处理 指派 流程跟踪 -->
										<%--<c:if test="${task.taskStatus == 0}">--%>
											<shiro:hasPermission name="tesk:task:edit">
												<a class="amore n1112_aColor" id="amore${task.id}" tabName="待办任务处理-${task.codeSetName }"
												   href="javascript:void(0)" style="float:none;"
                                                   jurl="${ctx }${task.taskLink }&taskHandlerId=${task.taskHandlerId}&taskStatus=${task.taskStatus}&systemId=${task.systemId}&origin=taskList&handleStatus=${task.taskStatus}">处理</a>
												<%--<shiro:hasPermission name="tesk:task:assign">--%>
													<a class="n1112_aColor" href="javascript:;"  onclick="assign('${task.id}','${task.taskHandlerId }','${task.taskHandlerName }');">指派</a>
												<%--</shiro:hasPermission>--%>
											</shiro:hasPermission>
											<shiro:hasPermission name="tesk:task:view">
												<shiro:lacksPermission name="tesk:task:edit">
													<%--<c:if test="${task.taskLink != null}">--%>
													<a class="amore n1112_aColor" id="amore${task.id}" tabName="待办任务查看-${task.codeSetName }"
													   href="javascript:void(0)" style="float:none;"
													   jurl="${ctx }${task.taskLink }&taskHandlerId=${task.taskHandlerId}&taskStatus=${task.taskStatus}" >查看</a>
													<%--</c:if>--%>
												</shiro:lacksPermission>
											</shiro:hasPermission>
											<%--<c:if test="${task.procInstId != null && task.procInstId != ''}">--%>
												<%--<a  class="n1112_aColor" href="${pageContext.request.contextPath}/workflow/wfcomponent/web/showIframe.jsp?processInstID=${task.procInstId}">流程跟踪</a>--%>
											<%--</c:if>--%>
										<%--</c:if>--%>
										<a href="javascript:;" onclick="layerPage2('${ctx}/tesk/task/taskHisList?codeSetName=${task.codeSetName}&systemName=${task.systemName}',
												'“${task.codeSetName }”流程跟踪', '1000px', '400px');" style="cursor:pointer;color: #0070c1!important;">流程跟踪</a>
									</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
				</div>
			</div>

			<!-- 已办任务 -->
			<div class="layui-card">
				<div class="layui-card-header">
					<div class="n1112_cardicon"></div>
					已办任务
					<!-- <a class="amore" href="${ctx}/tesk/task/?taskStatus=1" target="_blank">更多>></a> -->
					<a class="amore" jurl="${ctx}/tesk/task/?taskStatus=1" id="amore2" tabName="已办任务" href="javascript:void(0)"   >更多>></a>
				</div>
				<div class="layui-card-body">
						<sys:message content="${message}"/>
						<table id="contentTable" class="table table-striped table-bordered table-condensed">
							<thead>
								<tr>
									<th>
										任务送达时间
									</th>
									<th>
										任务处理时间
									</th>
									<th>
										业务系统名称
									</th>
									<th>
										所属区域
									</th>
									<th>
										代码集目录名称
									</th>
									<th>
										任务类型
									</th>
									<%--<th>--%>
										<%--taskHandlerId--%>
									<%--</th>--%>
									<%--<c:if test="${task.taskStatus == 0}">--%>
										<%--<shiro:hasPermission name="tesk:task:edit">--%>
											<th>操作</th>
										<%--</shiro:hasPermission>--%>
									<%--</c:if>--%>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${pageDoneTask.list}" var="task">
								<tr>
									<td>
										<div class="n1112_cardTable_icon"></div>
										<fmt:formatDate value="${task.taskStartDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
									</td>
									<td>	
										<div class="n1112_cardTable_icon"></div>
										<fmt:formatDate value="${task.taskCompleteDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
									</td>
									<td>
										${task.systemName }
										<%--${task.id}--%>
									</td>
									<td>
										${task.areaName }
									</td>
									<td>
										${task.codeSetName }
									</td>
									<td>
										${task.taskType }
									</td>
									<%--<td>--%>
										<%--${task.taskHandlerId }--%>
									<%--</td>--%>
									<td>
											<a class="amore n1112_aColor" id="amore${task.id}done" tabName="已办任务查看-${task.codeSetName }"
											   href="javascript:void(0)" style="float: none;"
											   jurl="${ctx }${task.taskLink }&taskHandlerId=${task.taskHandlerId}&taskStatus=${task.taskStatus}" >查看</a>
											<%--<a class="n1112_aColor" href="${pageContext.request.contextPath}/workflow/wfcomponent/web/showIframe.jsp?processInstID=${task.procInstId}">流程跟踪</a>--%>
										<a class="" href="javascript:;" onclick="layerPage2('${ctx}/tesk/task/taskHisList?codeSetName=${task.codeSetName}&systemName=${task.systemName}',
												'“${task.codeSetName }”流程跟踪', '1000px', '400px');" style="cursor:pointer;color: #0070c1!important">流程跟踪</a>
									</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
				</div>
			</div>

			<!-- 代码集分析 -->
			<div class="layui-card n1112_5card n1112_5card1">
				<div class="layui-card-header">
					<div class="n1112_cardicon"></div>
					代码集分析
					<%--<a class="n1112_amore" jurl="${ctx}/codesetstatistics/statistics" id="amore3" tabName="代码集分析" href="javascript:void(0)">更多>></a>--%>
					<a class="amore" jurl="${ctx}/codesetstatistics/statistics" id="amore3" tabName="代码集分析" href="javascript:void(0)">更多>></a>
					<div class="ct_clear"></div>
				</div>
				<div class="layui-card-body n1112_cardBody">
					<%--<img class="n1112_img" src="${ctxStatic}/new_login/img/n1112/n1112_img13.png" alt="">--%>
					<div id="pieDiv" style="margin:0 auto;height: 239px; width: 580px; border: 1px solid #ccc; padding: 10px;"></div>
				</div>
			</div>

			<!-- 代码集统计 -->
			<div class="layui-card n1112_5card">
					<div class="layui-card-header">
						<div class="n1112_cardicon"></div>
						代码集统计
						<a class="amore" jurl="${ctx}/codesetstatistics/statistics" id="amore4" tabName="代码集统计" href="javascript:void(0)">更多>></a>
						<div class="ct_clear"></div>
					</div>
					<div class="layui-card-body n1112_cardBody">
						<%--<img class="n1112_img" src="${ctxStatic}/new_login/img/n1112/n1112_img14.png" alt="">--%>
						<div id="barContent" style="margin:0 auto;height: 239px; width: 580px; border: 1px solid #ccc; padding: 10px;"></div>
						<%--<div id="barContent" style="width: auto;height: auto;"></div>--%>
					</div>
			</div>
			<div class="ct_clear"></div>

			<!-- 系统日志 -->
			<div class="layui-card">
					<div class="layui-card-header">
						<div class="n1112_cardicon"></div>
						系统日志
						<a class="amore" jurl="${ctx}/inoutlog/logInout/" id="amore5" tabName="更多系统日志" href="javascript:void(0)">更多>></a>
					</div>
					<div class="layui-card-body">
							<sys:message content="${message}"/>
							<table id="contentTable" class="table table-striped table-bordered table-condensed">
								<thead>
									<tr>
										<th>
											源业务系统名称
										</th>
										<th>
											目的业务系统名称
										</th>
										<th>
											所属区域
										</th>
										<th>
											代码集目录名称
										</th>
										<th>
												记录数量
										</th>
										<th>
												记录类型
										</th>
										<th>
												状态
										</th>
										<th>
												开始操作时间
										</th>
										<th>
												结束操作时间
										</th>
										<th>
											失败原因
										</th>
										<th>
											操作
										</th>
									</tr>
								</thead>
								<tbody>
								<c:forEach items="${pageLogInout.list}" var="logInout">
									<tr>
										<%--<td class="n1112_aColor">--%>
										<td >
												${logInout.sourceSysName }
										</td>
										<td>
											${logInout.destSysName }
										</td>		
										<td>
											${logInout.areaName }
										</td>
										<td>
											${logInout.codeSetName }
										</td>
										<td>
											${logInout.recordAmount }
										</td>										
										<td>
												${logInout.recordType == 0 ? '数据核查' : '数据分发'}
										</td>
										<td>
												${logInout.status == 1 ? '成功' : '失败'}
										</td>
										<td>
											<fmt:formatDate value="${logInout.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
										</td>
										<td>
											<fmt:formatDate value="${logInout.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
										</td>
										<td>
												${logInout.failReason}
										</td>
										<td>
											<a class="amore" jurl="${ctx}/inoutlog/logInout/form?id=${logInout.id}" id="amore${logInout.id}"
											   tabName="系统日志查看-${logInout.codeSetName}" href="javascript:void(0)"
											   style="float: none;color: #0070c1;">查看</a>
										</td>
									</tr>
								</c:forEach>
								</tbody>
							</table>
					</div>
				</div>
	</div>




	<script>
		$(document).ready(function(){
			// 设置父级高度
			// var height =$('body').height();
			// parent.$(".n1127_indexif").css({height:height+'px'})+100;

			// 引用layui模块
			layui.use('element', function(){
				var element = layui.element;
				
				// 绑定按钮 触发layui添加tabs事件
				$("#n1112_nbox").on('click','.amore',function(){
					var t=$(this);
					var jurl =t.attr("jurl"); // 获取跳转路径
					var id=t.attr("id"); // 获取id
					var tabName=t.attr("tabName"); // 获取选项卡名称

					// 判断是否已经添加过该选项卡
					var layId_listLen=parent.$("#n1127_demo li[lay-id="+id+"]").length;
					if(layId_listLen>0){// 若已经添加过该选项卡 则不再添加 根据id判断
						return false;
					}

					parent.n1127_addIframe({
						jurl:jurl,
						id:id,
						tabName:tabName
					})


				})
			});

		});
	</script>


</body>
</html>