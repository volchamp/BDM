<%--
 * 代码集统计
 * 
 * @author Xwt
 * @date 2017-06-14
 * @description
 * 
 --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码集统计</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" src="${ctxStatic}/artTemplate/dist/template-debug.js"></script>
	<script type="text/javascript" src="${ctxStatic}/echarts/echarts.js"></script>
	<script type="text/javascript">
		
		$(document).ready(function() {
			
			// 导出
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出代码集统计数据吗？","系统提示",function(v,h,f){
					if(v == "ok"){
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			
			// 为模块加载器配置echarts的路径，从当前页面链接到echarts.js，定义所需图表路径
			require.config({
				paths : {
					echarts : ctxStatic + '/echarts/'
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
						var html = template("contentTable_t",{list: json });
						$("#contentTable").html(html);
					} else {
						$("#contentTable").html("数据出错,请联系管理员！");
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
            // $.ajax({
            //     type: "POST",
            //     url: ctx + "/codesetstatistics/statistics/getData",
            //     async: false,
            //     success: function(data){
            //
            //         if (data) {
            //             json = data;
            //             template.onerror({"错误提示": "数据出错,请联系管理员！"});
            //             // var html = template("contentTable_t",{list: json });
            //             // $("#contentTable").html(html);
            //         } else {
            //             // $("#contentTable").html("数据出错,请联系管理员！");
            //             alertx("抱歉，统计分析数据加载错误，请稍后再试或联系管理员")
            //         }
            //     }
            // });
            var myChart = ec.init(document.getElementById('pieDiv'));
            var sysNameArr = [];
            var sysNameCountArr = [];
            for (var i in json) {
                sysNameArr.push(json[i].systemName);
                // sysNameCountArr.push(json[i].total);
                sysNameCountArr.push({'value':json[i].total,'name':json[i].systemName});
            }
            var sysNameCountJson = JSON.stringify(sysNameCountArr);
            // console.log("sysNameCountArr = " + sysNameCountArr);
            // console.log("sysNameCountJson = " + sysNameCountJson);

            // var optionCustomized = {
            //     backgroundColor: 'white',
            //
            //     title: {
            //         text: '代码集统计饼图',
            //         left: 'center',
            //         top: 20,
            //         textStyle: {
            //             color: 'black'
            //         }
            //     },
            //
            //     tooltip : {
            //         trigger: 'item',
            //         formatter: "{a} <br/>{b} : {c} ({d}%)"
            //     },
            //
            //     visualMap: {
            //         show: false,
            //         min: 80,
            //         max: 600,
            //         inRange: {
            //             colorLightness: [0, 1]
            //         }
            //     },
            //     series : [
            //         {
            //             name:'数量',
            //             type:'pie',
            //             radius : '55%',
            //             center: ['50%', '50%'],
            //             data:[
            //                 {value:11, name:'政府采购系统'},
            //                 {value:31, name:'非税系统'},
            //                 {value:29, name:'预算执行系统'},
            //                 {value:25, name:'OA'},
            //                 {value:40, name:'部门预算系统'}
            //             ].sort(function (a, b) { return a.value - b.value; }),
            //             roseType: 'radius',
            //             label: {
            //                 normal: {
            //                     textStyle: {
            //                         color: 'rgba(255, 255, 255, 0.3)'
            //                     }
            //                 }
            //             },
            //             labelLine: {
            //                 normal: {
            //                     lineStyle: {
            //                         color: 'rgba(255, 255, 255, 0.3)'
            //                     },
            //                     smooth: 0.2,
            //                     length: 10,
            //                     length2: 20
            //                 }
            //             },
            //             itemStyle: {
            //                 normal: {
            //                     color: '#c23531',
            //                     shadowBlur: 200,
            //                     shadowColor: 'rgba(0, 0, 0, 0.5)'
            //                 }
            //             },
            //
            //             animationType: 'scale',
            //             animationEasing: 'elasticOut',
            //             animationDelay: function (idx) {
            //                 return Math.random() * 200;
            //             }
            //         }
            //     ]
            // };

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
                        // magicType : {
                        //     show: true,
                        //     type: ['pie', 'funnel'],
                        //     option: {
                        //         funnel: {
                        //             x: '25%',
                        //             width: '50%',
                        //             funnelAlign: 'left',
                        //             max: 1548
                        //         }
                        //     }
                        // },
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
                        // data:[
                        //     {"value":0, "name":"政府采购系统"},
                        //     {"value":0, "name":"非税系统"},
                        //     {"value":8, "name":"预算执行系统"},
                        //     {"value":0, "name":"OA"},
                        //     {"value":0, "name":"部门预算系统"}
                        // ]
						data:sysNameCountArr
                    }
                ]
            };
            myChart.setOption(option);
        }
		
	</script>
</head>
<body>
	<form id="searchForm" action="${ctx}/codesetstatistics/statistics/export" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<shiro:hasPermission name="codesetstatistics:statistics:edit">
				<li><input id="btnExport" class="btn btn-primary audit" type="button" value="导出Excel"/>&nbsp;</li>
			</shiro:hasPermission>
		</ul>
	</form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<script id="contentTable_t" type="text/html">
			<thead>
				<tr>
					<th></th>
					{{each list as data}}
					<th>{{data.systemName}}</th>
					{{/each}}
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>
						代码集统计
					</td>
					{{each list as data}}
					<td>{{data.total}}</td>
					{{/each}}
				</tr>
			</tbody>
		</script>
	</table>
	<div id="barContent" style="margin:0 auto;height: 600px; width: 1000px; border: 1px solid #ccc; padding: 10px;"></div>
	<div id="pieDiv" style="margin:0 auto;height: 600px; width: 1000px; border: 1px solid #ccc; padding: 10px;"></div>
</body>
</html>