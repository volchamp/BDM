<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:getConfig('productName')}</title>
	<meta name="decorator" content="blank"/><c:set var="tabmode" value="${empty cookie.tabmode.value ? '0' : cookie.tabmode.value}"/>
    <c:if test="${tabmode eq '1'}"><link rel="Stylesheet" href="${ctxStatic}/jerichotab/css/jquery.jerichotab.css" />
    <script type="text/javascript" src="${ctxStatic}/jerichotab/js/jquery.jerichotab.js"></script></c:if>
	<style type="text/css">
		#main {padding:0;margin:0;} #main .container-fluid{padding:0 4px 0 6px;}
		#header {margin:0 0 8px;position:static;} #header li {font-size:14px;_font-size:12px;}
		#header .brand {font-family:Helvetica, Georgia, Arial, sans-serif, 黑体;font-size:26px;padding-left:33px;}
		#footer {margin:8px 0 0 0;padding:3px 0 0 0;font-size:11px;text-align:center;border-top:2px solid #0663A2;}
		#footer, #footer a {color:#999;} #left{overflow-x:hidden;overflow-y:auto;} #left .collapse{position:static;}
		#userControl>li>a{/*color:#fff;*/text-shadow:none;} #userControl>li>a:hover, #user #userControl>li.open>a{background:transparent;}
		/*右键关闭选项卡*/
		.rightmenu { position: absolute; width: 80px; z-index: 9999; display: none; background-color: #fff; padding: 2px; color: #333; border: 1px solid #eee; border-radius: 2px; cursor: pointer; }
		.rightmenu li { text-align: center; display: block; height: 25px; line-height: 25px; }
		.rightmenu li:hover { background-color: #666; color: #fff; }
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			// <c:if test="${tabmode eq '1'}"> 初始化页签
			$.fn.initJerichoTab({
                renderTo: '#right', uniqueId: 'jerichotab',
                contentCss: { 'height': $('#right').height() - tabTitleHeight },
                tabs: [], loadOnce: true, tabWidth: 110, titleHeight: tabTitleHeight
            });//</c:if>
			// 绑定菜单单击事件
			$("#menu a.menu").click(function(){
				// 一级菜单焦点
				$("#menu li.menu").removeClass("active");
				$(this).parent().addClass("active");
				// 左侧区域隐藏
				if ($(this).attr("target") == "mainFrame"){
					$("#left,#openClose").hide();
					wSizeWidth();
					// <c:if test="${tabmode eq '1'}"> 隐藏页签
					$(".jericho_tab").hide();
					$("#mainFrame").show();//</c:if>
					return true;
				}
				// 左侧区域显示
				$("#left,#openClose").show();
				if(!$("#openClose").hasClass("close")){
					$("#openClose").click();
				}
				
				// 显示二级菜单
				var menuId = "#menu-" + $(this).attr("data-id");
				if ($(menuId).length > 0){
					$("#left .accordion").hide();
					$(menuId).show();
					// 初始化点击第一个二级菜单
					if (!$(menuId + " .accordion-body:first").hasClass('in')){
						$(menuId + " .accordion-heading:first a").click();
					}
					if (!$(menuId + " .accordion-body li:first ul:first").is(":visible")){
						$(menuId + " .accordion-body a:first i").click();
					}
					// 初始化点击第一个三级菜单
					$(menuId + " .accordion-body li:first li:first a:first i").click();
				}else{
					// 获取二级菜单数据
					$.get($(this).attr("data-href"), function(data){
						if (data.indexOf("id=\"loginForm\"") != -1){
							alert('未登录或登录超时。请重新登录，谢谢！');
							top.location = "${ctx}";
							return false;
						}
						$("#left .accordion").hide();
						// 左侧菜单 插入数据
						$("#left").append(data);
						// 链接去掉虚框
						$(menuId + " a").bind("focus",function() {
							if(this.blur) {this.blur()};
						});
						// 二级标题
						$(menuId + " .accordion-heading a").click(function(){
							$(menuId + " .accordion-toggle i").removeClass('icon-chevron-down').addClass('icon-chevron-right');
							if(!$($(this).attr('data-href')).hasClass('in')){
								$(this).children("i").removeClass('icon-chevron-right').addClass('icon-chevron-down');
							}
						});
						// 二级内容
						$(menuId + " .accordion-body a").click(function(){
							$(menuId + " li").removeClass("active");
							$(menuId + " li i").removeClass("icon-white");
							$(this).parent().addClass("active");
							$(this).children("i").addClass("icon-white");
						});
						// 展现三级
						$(menuId + " .accordion-inner a").click(function(){
							var href = $(this).attr("data-href");
							if($(href).length > 0){
								$(href).toggle().parent().toggle();
								return false;
							}
							// <c:if test="${tabmode eq '1'}"> 打开显示页签
							return addTab($(this)); // </c:if>
						});
						// 默认选中第一个菜单
						$(menuId + " .accordion-body a:first i").click();
						$(menuId + " .accordion-body li:first li:first a:first i").click();
					});
				}
				// 大小宽度调整
				wSizeWidth();
				return false;
			});
			// 初始化点击第一个一级菜单
			$("#menu a.menu:first span").click();
			// <c:if test="${tabmode eq '1'}"> 下拉菜单以选项卡方式打开
			$("#userInfo .dropdown-menu a").mouseup(function(){
				return addTab($(this), true);
			});// </c:if>
			// 鼠标移动到边界自动弹出左侧菜单
			$("#openClose").mouseover(function(){
				if($(this).hasClass("open")){
					$(this).click();
				}
			});
			// 获取通知数目  <c:set var="oaNotifyRemindInterval" value="${fns:getConfig('oa.notify.remind.interval')}"/>
			function getNotifyNum(){
				$.get("${ctx}/oa/oaNotify/self/count?updateSession=0&t="+new Date().getTime(),function(data){
					var num = parseFloat(data);
					if (num > 0){
						$("#notifyNum,#notifyNum2").show().html("("+num+")");
					}else{
						$("#notifyNum,#notifyNum2").hide()
					}
				});
			}
			getNotifyNum(); //<c:if test="${oaNotifyRemindInterval ne '' && oaNotifyRemindInterval ne '0'}">
			setInterval(getNotifyNum, ${oaNotifyRemindInterval}); //</c:if>
		});
		// <c:if test="${tabmode eq '1'}"> 添加一个页签
		function addTab($this, refresh){
			$.fn.jerichoTab.addTab({
                tabFirer: $this,
                title: $this.text(),
                closeable: true,
                data: {
                    dataType: 'iframe',
                    dataLink: $this.attr('href')
                }
            }).loadData(refresh);
			return false;
		}// </c:if>

		// 根据屏幕分辨率 缩放logo字体
		window.onload=font; 
		function font(){ 
			var n1112_width = $('body').width();
			var n1112_width2 = 1920;
			var n1112_width3 = (1920-n1112_width)/100*1.5;
			var n1112_width4 = 36-n1112_width3;
			var n1112_width5 = 15-n1112_width3;
			var n1112_width6 = 5+n1112_width3;
			$(".n1112_htitle").css({fontSize:n1112_width4+"px",paddingTop:n1112_width6+"px"});
			$(".n1112_htitle2").css({fontSize:n1112_width5+"px"});
		}

        // function onLoad() {
        //     //增加密码过期功能，要求定期更改密码功能
        //     alert("抱歉，您的密码已过期，请修改3");
        // }
	</script>
</head>
<body>
	<!-- n1112 修改class -->
	<div id="main" class="n1112">
		<div id="header" class="navbar navbar-fixed-top">
			<!-- 首页1 -->
			<div class="navbar-inner">
				<div class="brand">
					<!-- <span id="productName">${fns:getConfig('productName')}</span> -->
					<!-- 修改logo -->
					<div id="productName">
							<div class="n1112_htitle">基础数据管理系统</div>
							<div class="n1112_htitle2">Yunnan Provincial Department of Finance Basic Data Management System</div>
					</div>
					
				</div>
				<ul id="userControl" class="nav pull-right">
					<!-- 主题切换 去掉 -->

					<%--<li id="themeSwitch" class="dropdown">--%>
						<%--<a class="dropdown-toggle" data-toggle="dropdown" href="#" title="主题切换"><i class="icon-th-large"></i></a>--%>
						<%--<ul class="dropdown-menu">--%>
							<%--<c:forEach items="${fns:getDictList('theme')}" var="dict"><li><a href="#" onclick="location='${pageContext.request.contextPath}/theme/${dict.value}?url='+location.href">${dict.label}</a></li></c:forEach>--%>
							<%--<li><a href="javascript:cookie('tabmode','${tabmode eq '1' ? '0' : '1'}');location=location.href">${tabmode eq '1' ? '关闭' : '开启'}页签模式</a></li>--%>
						<%--</ul>--%>
						<%--<!--[if lte IE 6]><script type="text/javascript">$('#themeSwitch').hide();</script><![endif]-->--%>
					<%--</li>--%>


					<li id="userInfo" class="dropdown">
						<a class="dropdown-toggle" data-toggle="dropdown" href="#" title="个人信息">您好, ${fns:getUser().name}&nbsp;<span id="notifyNum" class="label label-info hide"></span></a>
						<ul class="dropdown-menu">
							<li><a class="n1203_a" tabName="个人信息" tabId="个人信息" jhref="${ctx}/sys/user/info" target="mainFrame"><i class="icon-user"></i>&nbsp; 个人信息</a></li>
							<li><a class="n1203_a" tabName="修改密码" tabId="修改密码" jhref="${ctx}/sys/user/modifyPwd" target="mainFrame"><i class="icon-lock"></i>&nbsp;  修改密码</a></li>
							<li><a class="n1203_a" tabName="我的通知" tabId="我的通知" jhref="${ctx}/oa/oaNotify/self" target="mainFrame"><i class="icon-bell"></i>&nbsp;  我的通知 <span id="notifyNum2" class="label label-info hide"></span></a></li>
						</ul>
					</li>
					<!-- 修改退出图标 -->
					<!-- <li><a href="${ctx}/logout" title="退出登录">退出</a></li> -->
					<li><a href="${ctx}/logout" class="n1112_aout" title="退出登录"></a></li>
					<li>&nbsp;</li>
				</ul>
				<%-- <c:if test="${cookie.theme.value eq 'cerulean'}">
					<div id="user" style="position:absolute;top:0;right:0;"></div>
					<div id="logo" style="background:url(${ctxStatic}/images/logo_bg.jpg) right repeat-x;width:100%;">
						<div style="background:url(${ctxStatic}/images/logo.jpg) left no-repeat;width:100%;height:70px;"></div>
					</div>
					<script type="text/javascript">
						$("#productName").hide();$("#user").html($("#userControl"));$("#header").prepend($("#user, #logo"));
					</script>
				</c:if> --%>
				<div class="nav-collapse">
					<ul id="menu" class="nav" style="*white-space:nowrap;float:none;">
						<!-- 修改菜单 -->
						<!-- <li class="menu active">
								<a class="menu" href="javascript:" data-href="/mdm/a/sys/menu/tree?parentId=395a2c80c1a74fd19cb6c9b9e2a83286" data-id="395a2c80c1a74fd19cb6c9b9e2a83286">
									<span class="n1112_icon n1112_icon1"></span>
									<span>基础数据管理</span>
								</a>
						</li>
						<li class="menu">
								<a class="menu" href="javascript:" data-href="/mdm/a/sys/menu/tree?parentId=395a2c80c1a74fd19cb6c9b9e2a83286" data-id="395a2c80c1a74fd19cb6c9b9e2a83286"><span class="n1112_icon n1112_icon2"></span><span>数据审核管理</span></a>
						</li>
						<li class="menu">
								<a class="menu" href="javascript:" data-href="/mdm/a/sys/menu/tree?parentId=395a2c80c1a74fd19cb6c9b9e2a83286" data-id="395a2c80c1a74fd19cb6c9b9e2a83286"><span class="n1112_icon n1112_icon3"></span><span>数据核查管理</span></a>
						</li>
						<li class="menu">
								<a class="menu" href="javascript:" data-href="/mdm/a/sys/menu/tree?parentId=395a2c80c1a74fd19cb6c9b9e2a83286" data-id="395a2c80c1a74fd19cb6c9b9e2a83286"><span class="n1112_icon n1112_icon4"></span><span>系统管理</span></a>
						</li>  -->

						<!-- n1112_icon1 --基础数据管理 n1112_icon2 --数据审核管理 n1112_icon3 --数据核查管理 n1112_icon4 --系统管理  -->
						<c:set var="firstMenu" value="true"/>
						<c:forEach items="${fn:length(fns:getMenuList()) > 0 ? fns:getMenuList() : menuList}" var="menu" varStatus="idxStatus">
							<c:if test="${menu.parent.id eq '1'&&menu.isShow eq '1'}">
								<li class="menu ${not empty firstMenu && firstMenu ? ' active' : ''}">
									<c:if test="${empty menu.href}">
										<a class="menu" href="javascript:" data-href="${ctx}/sys/menu/tree?parentId=${menu.id}" data-id="${menu.id}">
											<span class="n1112_icon n1112_icon1"></span>
											<span>${menu.name}</span>
										</a>
									</c:if>
									<c:if test="${not empty menu.href}">
										<!-- 顶部菜单跳转 -->
										<a class="menu" href="${fn:indexOf(menu.href, '://') eq -1 ? ctx : ''}${menu.href}" data-id="${menu.id}" target="mainFrame">
											<span class="n1112_icon n1112_icon1"></span>
											<span>${menu.name}</span>
										</a>
									</c:if>
								</li>
								<c:if test="${firstMenu}">
									<c:set var="firstMenuId" value="${menu.id}"/>
								</c:if>
								<c:set var="firstMenu" value="false"/>
							</c:if>
						</c:forEach>
					</ul>
				</div><!--/.nav-collapse -->
			</div>
	    </div>
	    <div class="container-fluid">
			<div id="content" class="row-fluid">
				<div id="left"><%-- 
					<iframe id="menuFrame" name="menuFrame" src="" style="overflow:visible;" scrolling="yes" frameborder="no" width="100%" height="650"></iframe> --%>
				</div>
				<div id="openClose" class="close">&nbsp;</div>
				<div id="right">
					<!-- 选项卡 -->
					<div class="layui-tab" lay-filter="demo" lay-allowclose="true" id="n1127_demo" style="margin:0;">
						<ul class="layui-tab-title">
							<!-- <li class="layui-this">我的首页</li> -->
						</ul>
						<ul class="rightmenu">
							<li data-type="closeOther">关闭其他</li>
							<li data-type="closeall">关闭所有</li>
						</ul>
						<div class="layui-tab-content">
							<!-- <div class="layui-tab-item layui-show">
									<iframe id="mainFrame" name="mainFrame" src="3123123123" style="overflow:visible;" scrolling="yes" frameborder="no" width="100%" height="650"></iframe>
							</div> -->
						</div>
					</div>
				
				</div>
			</div>
		    <div id="footer" class="row-fluid">
	            <%-- Copyright &copy; 2012-${fns:getConfig('copyrightYear')} ${fns:getConfig('productName')}  ${fns:getConfig('version')} --%>非涉密办公系统不得存储、处理、传输涉密文件、资料、信息
			</div>
		</div>
	</div>
	<script type="text/javascript"> 
		var leftWidth = 160; // 左侧窗口大小
		var tabTitleHeight = 33; // 页签的高度
		var htmlObj = $("html"), mainObj = $("#main");
		var headerObj = $("#header"), footerObj = $("#footer");
		var frameObj = $("#left, #openClose, #right, #right iframe");
		function wSize(){
			var minHeight = 500, minWidth = 980;
			var strs = getWindowSize().toString().split(",");
			htmlObj.css({"overflow-x":strs[1] < minWidth ? "auto" : "hidden", "overflow-y":strs[0] < minHeight ? "auto" : "hidden"});
			mainObj.css("width",strs[1] < minWidth ? minWidth - 10 : "auto");
			frameObj.height((strs[0] < minHeight ? minHeight : strs[0]) - headerObj.height() - (footerObj.height()-8) - (strs[1] < minWidth ? 42 : 28));
			$("#openClose").height($("#openClose").height() - 5);// <c:if test="${tabmode eq '1'}"> 
			$(".jericho_tab iframe").height($("#right").height() - tabTitleHeight); // </c:if>
			wSizeWidth();
		}
		function wSizeWidth(){
			if (!$("#openClose").is(":hidden")){
				var leftWidth = ($("#left").width() < 0 ? 0 : $("#left").width());
				$("#right").width($("#content").width()- leftWidth - $("#openClose").width() -5);
			}else{
				$("#right").width("100%");
			}
		}// <c:if test="${tabmode eq '1'}"> 
		function openCloseClickCallBack(b){
			$.fn.jerichoTab.resize();
		} // </c:if>


		// $(document).ready(function(){
		// 	// 绑定左侧跳转方法
		// 	$(".nav-list li").on('click',function(){
		// 		$.fn.jerichoTab.addTab({
		// 				tabFirer: $(this),
		// 				title: '测试1',
		// 				closeable: true,
		// 				data: {
		// 						dataType: 'iframe',
		// 						dataLink: '/mdm/a/sys/user/info'
		// 				}
		// 		}).loadData(true);
		// 		return false;
		// 	});
		// });
		var element ={};
		var height22=0;
		$(document).ready(function(){
			// 引用layui模块
			layui.use('element', function(){
				element = layui.element;
				height22 =($('#right').height())-45;

				var timestamp = Date.parse(new Date());// 当前时间戳
				// 绑定侧边栏跳转事件
				// var if1=$("#mainFrame");// 右侧iframe
				var idName='menu-'+'${param.parentId}';// 
				var idDom=$("#userInfo");
				idDom.on('click','.n1203_a',function(){
					var t=$(this);
					var jhref = t.attr('jhref');
					var tabName = t.attr('tabName');
					var id=t.attr('tabId');
					// if1.attr('src',jhref);
					var obj ={
						jurl:jhref,
						id:id,
						tabName:tabName
					};
					n1127_addIframe(obj);
				});

			});

            setTimeout(function () {
                //增加密码过期功能，要求定期更改密码功能
                // alert("抱歉，您的密码已过期，请修改2");
                $.ajax({
                    type: "POST",
                    url: ctx + "/common/comCon/isPswExpire",
                    async: false,
                    success: function(data){
                        // alert(data);
                        if (data) {
                            layerPage2('${ctx}/sys/user/modifyPwd', '您的密码已过期，请尽快修改密码', '600px', '300px');
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        jBox.tip('密码过期检测失败', 'error');
                        return false;
                    }
                });
            }, 4000);

            setTimeout(function () {
                //用户账号的上一次使用信息（成功或失败）应当在下一次成功登录时向用户报告
                $.ajax({
                    type: "POST",
                    url: ctx + "/common/comCon/isExistLastLgLog",
                    async: false,
                    success: function(data){
                        if (data) {
                            layerPage2('${ctx}/common/comCon/logForm', '您的上一次账号登录信息', '400px', '400px');
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        jBox.tip('上一次登录信息加载失败', 'error');
                        return false;
                    }
                });
            }, 7000);
		});

		// 添加iframe
		function n1127_addIframe(obj){
			var jurl =obj.jurl||''; // 获取跳转路径
			var id=obj.id||''; // 获取id
			var tabName=obj.tabName||''; // 获取选项卡名称

			// 获取当前页面高度
			//var height =parent.$('body').height()-80;
			// 820px 740px 
			// 用于新增一个Tab选项
			// 参数filter：tab元素的 lay-filter="value" 过滤器的值（value）
			// 参数options：设定可选值的对象，目前支持的选项如下述示例：
			// 判断是否已经添加过该选项卡
			var layId_listLen=parent.$("#n1127_demo li[lay-id="+id+"]").length;
			if(layId_listLen>0){// 若已经添加过该选项卡 则不再添加 根据id判断
				// 选项卡切换到当前新添加的选项卡
				element.tabChange('demo', id);
				return false;
			}

			// api https://www.layui.com/doc/modules/element.html
			element.tabAdd('demo', {
				title: tabName
				,content: '<iframe id="mainFrame'+id+'" class="n1127_indexif" name="mainFrame" src="'+jurl+'" style="overflow: visible;" scrolling="yes" frameborder="no" width="100%" height="'+height22+'"></iframe>' //支持传入html
				,id: id
			});

			// 选项卡切换到当前新添加的选项卡
			element.tabChange('demo', id);
		}

		//选项卡右键关闭功能
        //var $ = layui.$;
        var deleteIndex;//全局变量
        element.on('tab(demo)', function (data) {
            deleteIndex = $(this).attr("lay-id");
        });

        var active = {
            //在这里给active绑定几项事件，后面可通过active调用这些事件
            tabAdd: function (url, id, name) {
                //新增一个Tab项 传入三个参数，分别对应其标题，tab页面的地址，还有一个规定的id，是标签中data-id的属性值
                //关于tabAdd的方法所传入的参数可看layui的开发文档中基础方法部分
                element.tabAdd('demo', {
                    title: name,
                    content: '<iframe data-frameid="' + id + '" scrolling="auto" frameborder="0" src="' + url + '" style="width:100%;height:99%;"></iframe>',
                    id: id //规定好的id
                });
                //以下是新加的******************************************************
                CustomRightClick(id);//绑定右键菜单
                FrameWH();  //计算ifram层的大小
            },
            tabChange: function (id) {
                //切换到指定Tab项
                element.tabChange('demo', id); //根据传入的id传入到指定的tab项
                //以下是新加的*************************************************************
                $("iframe[data-frameid='" + id + "']").attr("src", $("iframe[data-frameid='" + id + "']").attr("src"));//切换后刷新框架
            },
            tabDelete: function (id) {
                element.tabDelete("demo", id);//删除
            },
            //以下是新加的******************************************************************
            tabDeleteAll: function (ids) {//删除所有
                $.each(ids, function (i, item) {
                    element.tabDelete("demo", item);
                })
            }
            //以上是新增的*********************************************************************
        };

        function FrameWH() {
            var h = $(window).height();
            $("iframe").css("height", h + "px");
        }

        //绑定右键菜单
        function CustomRightClick(id) {
            //取消右键
            $('.layui-tab-title li').on('contextmenu', function () {
                return false;
            });
            $('.layui-tab-title,.layui-tab-title li').click(function () {
                $('.rightmenu').hide();
            });
            //桌面点击右击
            $('.layui-tab-title li').on('contextmenu', function (e) {
                var popupmenu = $(".rightmenu");
                popupmenu.find("li").attr("data-id", id);
                l = ($(document).width() - e.clientX) < popupmenu.width() ? (e.clientX - popupmenu.width()) : e.clientX;
                t = ($(document).height() - e.clientY) < popupmenu.height() ? (e.clientY - popupmenu.height()) : e.clientY;
                popupmenu.css({left: l, top: t}).show();
                //alert("右键菜单")
                return false;
            });
        }

        //就需要给右键添加功能
        $(".rightmenu li").click(function () {
            if ($(this).attr("data-type") == "closeOther") {//关闭其他
                alert("333");
                $.each($(".layui-tab-title li[lay-id]"), function () {
                    //如果点击左侧菜单栏所传入的id 在右侧tab项中的lay-id属性可以找到，则说明该tab项已经打开
                    var layId = $(this).attr("lay-id");
                    if (layId != deleteIndex && layId.indexOf("我的") == -1) {
                        console.log("si即将删除lay-id" + $(this).attr("lay-id"));
                        active.tabDelete($(this).attr("lay-id"));
                    }
                });
            } else if ($(this).attr("data-type") == "closeall") {//关闭所有
                alert("444");
                var tabtitle = $(".layui-tab-title li");
                var ids = new Array();
                $.each(tabtitle, function (i) {
                    var layId = $(this).attr("lay-id");
                    // if (layId.indexOf("我的") == -1) {
                        ids[i] = layId;
                    // }
                });
                active.tabDeleteAll(ids);
            }
            $('.rightmenu').hide();
        });

        //增加密码过期功能，要求定期更改密码功能
        alert("抱歉，您的密码已过期，请修改");

	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>