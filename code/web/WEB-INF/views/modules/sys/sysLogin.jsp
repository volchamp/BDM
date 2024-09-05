<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="decorator" content="blank"/>
		<title>基础数据管控平台</title>
		<link rel="stylesheet" type="text/css" href="${ctxStatic}/new_login/css/style.css"/>
		<link rel="stylesheet" type="text/css" href="${ctxStatic}/new_login/css/gx_mdm_login.css"/>
		<!-- <link rel="stylesheet" href="${ctxStatic}/new_login/css/ie8-gx_mdm_login.css"> -->
		<!--[if IE 8]>
		    
		<![endif]-->
		<style type="text/css">
			html{
				font-size: 16px;
			}
			body{
				background-color:#4c5ee8;
				font-size: 100%;
				}
		    .header{height:80px;padding-top:20px;position:relative;z-index:9999;zoom:1;} .alert{position:relative;width:300px;margin:0 auto;*padding-bottom:0px;}
				label.error{background:none;width:270px;font-weight:normal;color:inherit;margin:0;}
				
				.n1112.gx_mdm_login_main{
					/* background: url(${ctxStatic}/new_login/img/n1112/n1112_img4.png) repeat-y left top; */
					/* background-size: 100% 100%; */
					background: none;
				}
				.n1112 .n1112_bg{
					width: 100%;
					height: 100%;
					position: absolute;
					left: 0;
					top: 0;	
					/* z-index: -1; */
				}
				.n1112 .gx_mdm_login_center{
					width: 28%;
					height: 45%;
					left: 24%;
    			top: 36%;
					/* background: url(${ctxStatic}/new_login/img/n1112/n1112_img1.png) no-repeat center center; */
					/* background-size: 100%; */
					background: none;					
				}
				.n1112 .gx_mdm_login_center img{
					display: block;
					width: 100%;
				}
				.n1112 .gx_mdm_login_contents{
					width: 25%;
					height: 45%;
					padding: 0;
					border-radius: 0;
					border: 0;
					background: url(/mdm/static/new_login/img/n1112/n1112_img3.png) no-repeat left top;
					background-size: 100% 100%;
					top: 36%;
    			right: 21%;
				}
				.n1112 .n1112_p{
					height: 41px;
					line-height: 41px;
					margin: 0;
					font-size: 16px;
					color: #333;
				}
				.n1112 #loginForm{
					/* padding-top: 75px; */
					padding-top: 15%;
					width: 75%;
					height: auto;
				}
				.n1112 .gx_login_name input, .n1112 .gx_login_password input{
					padding-left: 3%;
					width: 100%;
					height: 100%;
					border-radius: 3px;
				}
				.n1112 .gx_login_password{
					margin-top: 0;
				}
				.n1112 .n1112_sub{
					width: 100%;
					height: 48px;
					font-size: 16px;
					border-radius: 3px;
				}
				.n1112 .gx_login_btn{
					margin-top: 6%;
				}
				.n1112 .n1112_headerTitle{
					position: absolute;
					left: 0;
					top: 14%;
					width: 100%;
					font-size: 40px;
					color: #fff;
					text-align: center;
					font-weight: bold;
					/* top: 0; */
					letter-spacing: 5px;
				}
		</style>
		<script type="text/javascript">
		window.onload=function() {
			gx_mdm();
			
			$("#loginForm").validate({
				rules: {
					validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
				},
				messages: {
					username: {required: "请填写用户名."},password: {required: "请填写密码."},
					validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
				},
				errorLabelContainer: "#messageBox",
				errorPlacement: function(error, element) {
					error.appendTo($("#loginError").parent());
				} 
			});
			
		}
		window.onresize=function() {
			gx_mdm();
		}

		function gx_mdm () {
			var w = document.documentElement.clientWidth;
			if (w>1920) {
				w=1920;
			}
			document.querySelector('html').style.fontSize=w/120+'px';
		}
		// 如果在框架或在对话框中，则弹出提示并跳转到首页
		if(self.frameElement && self.frameElement.tagName == "IFRAME" || $('#left').length > 0 || $('.jbox').length > 0){
			alert('未登录或登录超时。请重新登录，谢谢！');
			top.location = "${ctx}";
		}
	</script>
	</head>
	<body class="gx_mdn_ie8">
		<div class="gx_top_hied"></div>
		<div class="gx_mdm_login_main n1112">
			<img src="${ctxStatic}/new_login/img/n1112/n1112_img2.png" class="n1112_bg" />
			<div class="n1112_headerTitle">基础数据管理系统</div>
			<!-- <div class="gx_mdm_login_bottom"></div> -->
			<div class="gx_mdm_login_center">
					<!-- <img src="${ctxStatic}/new_login/img/n1112/gx_zsjglpt_login_center11.png"/> -->
				<img src="${ctxStatic}/new_login/img/n1112/n1112_img1.png"/>
				
			</div>
			<div class="gx_mdm_login_contents">
				<!-- <h3>主数据管理平台</h3> -->
				<form id="loginForm" class="gx_mdm_login_input" action="${ctx}/login" method="post">
					<div class="gx_login_name">
						<p class="n1112_p">登录名</p>
						<input type="text" placeholder="请输入登录名" id="username" name="username" class="input-block-level required" value="${username}">
					</div>
					<div class="gx_login_password">
						<p class="n1112_p">密码</p>
						<input type="password" id="password" name="password" placeholder="请输入密码" class="input-block-level required">
					</div>
					<c:if test="${isValidateCodeLogin}">
						<div class="validateCode">
							<label class="input-label mid" for="validateCode">验证码</label>
							<sys:validateCode name="validateCode" inputCssStyle="margin-bottom:0;"/>
						</div>
					</c:if>
					<div class="gx_login_btn">
						<input type="submit" value="立即登录" class="n1112_sub" />
						<%--<input type="button" value="手机登录" class="n1112_sub" />--%>
					</div>
					<%--<div class="gx_login_pwdgs">--%>
						<%--<input class="gx_btngg" type="checkbox" id="rememberMe" name="rememberMe" ${rememberMe ? 'checked' : ''}/><span>记住密码</span>--%>
						<%--<a href="javaScript:;">忘记密码?</a>--%>
					<%--</div>--%>
					
				</form>
			</div>
			<div class="header">
				<div id="messageBox" class="alert alert-error ${empty message ? 'hide' : ''}"><button data-dismiss="alert" class="close">×</button>
					<label id="loginError" class="error">${message}</label>
				</div>
			</div>
	</div>
		</div>
	</body>
	
</html>
