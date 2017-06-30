$(function(){
	/*
	 * 1.得到所有的错误信息，旬环比那里之，调用一个方法来确定是否显示错误信息
	 */
	$(".errorClass").each(function(){
		showError($(this));
	});
	
	/*
	 * 2.切换注册按钮的图片
	 */
	$("#submitBtn").hover(
			function(){
				$("#submitBtn").attr("src","/goodsmine/images/regist2.jpg");
			},
			function(){
				$("#submitBtn").attr("src","/goodsmine/images/regist1.jpg");

			}
	);
	
	/**
	 * 3.得到焦点隐藏错误信息
	 */
	$(".inputClass").focus(function(){
		var labelId = $(this).attr("id");
//		alert(labelId);
		$("#"+labelId+"Error").text("");
		showError($("#"+labelId+"Error"));
		
	});
	/**
	 * 4.失去焦点进行校验
	 */
	$(".inputClass").blur(function(){
//		alert(1+1);
		var id = $(this).attr("id");//获取对应输入框的id
//		alert(id);
		var funName ="validate"+id.substring(0,1).toUpperCase()+id.substring(1)+"()";//因为方法的命名规范由validate+id首字母大写作为方法名
//		alert(funName);
		eval(funName);//执行函数调用
		
	});
	
	/*
	 * 5.表单提交校验
	 */
 $("#registForm").submit(function(){
//alert(validateLoginname()&
//		validateLoginpass()&
//		validateReloginpass()&
//		validateEmail()&
//		validateVerifyCode());
alert(validateLoginname()) ;
alert(validateVerifyCode());
	if(	validateLoginname()&
			validateLoginpass()&
			validateReloginpass()&
			validateEmail()&
			validateVerifyCode())
		
		return true;
	else{
		return false;
	}
		
	});

	
});

/**
 * 判断当前元素是否存在内容，存在 显示 不存在 不显示
 * @param ele
 */
function showError(ele){
	var text = ele.text();
	if(!text){
		ele.css("display","none");
	}else {
		ele.css("display","");
	}
}

function _changeImage(){
	$("#imgVerifyCode").attr("src","/goodsmine/VerifyCodeServlet?"+new Date().getTime());
}

/**
 * 登录用户名校验
 */
function validateLoginname(){
	/*
	 * 1.非空验证
	 */
	var id ="loginname";
	var value =$("#"+id).val();
	if(!value){$("#"+id+"Error").text("用户名不能为空！");
				showError($("#"+id+"Error"));
				return false;//阻断程序向下运行
	}
	/*
	 * 2.用户名长度在3~20之间！
	 */
	if(value.length<3||value.length>20){
		$("#"+id+"Error").text("用户名长度在3~20之间！");
		showError($("#"+id+"Error"));
		return false;
	}
	/*
	 * 3.校验用户名是否已经存在
	 */
	$.ajax({
		url:"/goodsmine/UserServlet",
		data:{method:"ajaxValidateLoginname",loginname:value},
		type:"POST",
		dataType:"json",
		asyn:false,
		cache:false,
		success:function(result){
//alert(result);
			if(!result){
				$("#"+id+"Error").text("用户名已经存在！");
				showError($("#"+id+"Error"));
				return false;
			}else{
			$("#"+id+"Error").text("正确！");
			showRight($("#"+id+"Error"));
			return true;}
		}
		
	});
	return true;
}
/**
 * 密码校验
 */
function validateLoginpass(){
	/*
	 * 1.非空验证
	 */
	var id ="loginpass";
	var value =$("#"+id).val();
	if(!value){$("#"+id+"Error").text("密码不能为空！");
				showError($("#"+id+"Error"));
				return false;//阻断程序向下运行
	}
	/*
	 * 2.密码长度在3~20之间！
	 */
	if(value.length<3||value.length>20){
		$("#"+id+"Error").text("密码长度在3~20之间！");
		showError($("#"+id+"Error"));
		return false;
	}else{
		$("#"+id+"Error").text("正确！");
		showRight($("#"+id+"Error"));
		return true;
	}
return true;
}
/**
 * 确认密码校验
 */
function validateReloginpass(){
	/*
	 * 1.非空验证
	 */
	var id ="reloginpass";
	var value =$("#"+id).val();
	if(!value){$("#"+id+"Error").text("确认密码不能为空！");
				showError($("#"+id+"Error"));
				return false;//阻断程序向下运行
	}
	/*
	 * 2.是否一致
	 */
	
	if(value!=$("#loginpass").val()){
		$("#"+id+"Error").text("两次密码不一致！");
		showError($("#"+id+"Error"));
		return false;//阻断程序向下运行
	}else{
		$("#"+id+"Error").text("正确！");
		showRight($("#"+id+"Error"));
		return true;
	}
	return true;
}
/**
 * Email校验
 */
function validateEmail(){
	/*
	 * 1.非空验证
	 */
	var id ="email";
	var value =$("#"+id).val();
	if(!value){$("#"+id+"Error").text("Email不能为空！");
				showError($("#"+id+"Error"));
				return false;//阻断程序向下运行
	}
	/*
	 * 2.Email格式校验！
	 */
	if(!/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(value)){
		$("#"+id+"Error").text("Email格式不正确！");
		showError($("#"+id+"Error"));
		return false;
	}
	
	/*
	 * 3.email校验是否注册 ajax方法
	 */
	$.ajax({
		url:"/goodsmine/UserServlet",
		data:{method:"ajaxValidateEmail",email:value},
		type:"POST",
		dataType:"json",
		asyn:false,
		cache:false,
		success:function(result){
//			alert(result);
			if(!result){
				$("#"+id+"Error").text("Email已经被注册了！");
				showError($("#"+id+"Error"));
				return false;//阻断程序向下运行
			}
			else{$("#"+id+"Error").text("正确！");
			showRight($("#"+id+"Error"));
			return true;
			}
		}
		
	});
	return true;
}
/**
 * 验证码校验
 */
function validateVerifyCode(){
	/*
	 * 1.非空验证
	 */
	var id ="verifyCode";
	var value =$("#"+id).val();
	
	if(!value){$("#"+id+"Error").text("验证码不能为空！");
				showError($("#"+id+"Error"));
				return false;//阻断程序向下运行
	}
	/*
	 * 2.验证码格式校验！
	 */
	if(value.length!=4){
		$("#"+id+"Error").text("验证码格式不正确！");
		showError($("#"+id+"Error"));
		return false;
	}
	/*
	 * 3.输入验证码与真实验证码进行比对。
	 */
	$.ajax({
		url:"/goodsmine/UserServlet",
		data:{method:"ajaxValidateVerifyCode",verifyCode:value},
		type:"POST",
		dataType:"json",
		asyn:false,
		cache:false,
		success:function(result){
//	alert(result);
		if(!result){
			$("#"+id+"Error").text("验证码不正确！");
			showError($("#"+id+"Error"));
			return false;//阻断程序向下运行
		}
		
		else{$("#"+id+"Error").text("正确！");
		showRight($("#"+id+"Error"));
		return true;
		}
		
		}
	});
	
	return true;
}

function showRight(ele){
//alert(ele);
	var text = ele.text();
	if(!text){
		ele.css("display","none");
	}else{
		  ele.css("display","");
	      ele.css("background-image","url(/goodsmine/images/right.png)");
	      ele.css("background-size","15%");
//	      ele.css("background-color","green");
	      ele.css("background-repeat","no-repeat");
//          ele.css({background-image:url(/goodsmine/images/right.png),background-repeat:no-repeat});
		}
	

}
