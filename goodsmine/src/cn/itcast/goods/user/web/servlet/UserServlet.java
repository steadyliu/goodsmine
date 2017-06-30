package cn.itcast.goods.user.web.servlet;



import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.domain.User;
import cn.itcast.goods.user.service.UserService;
import cn.itcast.goods.user.service.exception.UserException;
import cn.itcast.servlet.BaseServlet;

public class UserServlet extends BaseServlet {
	private UserService userService = new UserService();
	//注册功能
	public String regist(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		//封装表单数据到User对象
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		//把formUser对象放到request域中。----以便回显输入内容。
		req.setAttribute("form", formUser);
		//验证注册表单数据
		HttpSession session = req.getSession();
		Map<String ,String> errors =validateRegist(formUser, session);
		req.setAttribute("errors", errors);
		//转发到（不能重定向不然参数要丢失)
		if(errors.size()!=0){
			return "f:/jsps/user/regist.jsp";
		}
		//使用service完成业务层操作
		
		userService.regist(formUser);
		//保存成功信息，转发到msg.jsp
		req.setAttribute("code", "success");
		req.setAttribute("msg", "注册成功！");
		return "f:/jsps/msg.jsp";
		
		
	}
	/**
	 * 校验注册方法
	 * @param user
	 * @param session
	 */
private Map<String,String> validateRegist(User formUser,HttpSession session){
		
		Map<String ,String> errors = new HashMap<String,String>();
		/*
		 * 1.校验登陆用户名
		 * 2.验证用户名是否为空
		 * 3.验证用户名长度是否在3~20之间
		 * 4.校验是否已存在。
		 */
		String loginname = formUser.getLoginname();
		//是否为空
		if(loginname==null||loginname.trim().isEmpty()){
			errors.put("loginname", "用户名不能为空！");
		}else if(loginname.length()<3||loginname.length()>20){
			errors.put("loginname", "用户名长度要在3~20之间");
		}if(!userService.ajaxValidateLoginname(loginname)){
			errors.put("loginname", "用户名已存在");
		}
		
		//校验密码
		String loginpass = formUser.getLoginpass();
		//是否为空
		if(loginpass==null||loginpass.trim().isEmpty()){
			errors.put("loginpass", "密码不能为空！");
		}else if(loginpass.length()<3||loginpass.length()>20){
			errors.put("loginpass", "密码要在3~20之间");
		}
		
		//校验确认密码
		String reloginpass = formUser.getReloginpass();
		//是否为空
		if(reloginpass==null||reloginpass.trim().isEmpty()){
			errors.put("reloginpass", "确认密码不能为空！");
		}else if(! reloginpass.equals(loginpass) ){

			errors.put("reloginpass", "两次密码不一致");
		}
		
//		校验邮件
		String  email = formUser.getEmail();
		if(email == null ||email.trim().isEmpty()){
			errors.put("email", "邮件不能为空！");
		}else if(!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
			errors.put("email", "邮件格式不正确");
		}else if(!userService.ajaxValidateEmail(email)){
			errors.put("email", "邮件已被注册！");
		}
//		校验验证码
		String verifyCode = formUser.getVerifyCode();
		if(verifyCode == null||verifyCode.trim().isEmpty()){
			errors.put("verifyCode", "验证码不能为空！");
		}else if (!verifyCode.equalsIgnoreCase((String) session.getAttribute("vCode")) ){
			errors.put("verifyCode", "验证码不正确！");
		}
		
		
		return errors;
		
	}
/*
 * 登陆
 */
public String login(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException, SQLException{
	//封装表单数据到User formUser
	User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
	//校验表单数据
	Map<String,String> errors = avlidateLogin(formUser);
	if(errors.size()!=0){
		req.setAttribute("errors", errors);
		return "f:/jsps/user/login.jsp";
	}
	//校验通过，需要判断根据用户名和密码查询的用户可存在
	/**
	 * 1.如果存在，且状态为未激活 false，则保存错误信息，转发到login.jsp页面。
	 */
	User user = userService.login(formUser);
	if(user !=null){
		if(!user.isStatus()){
			req.setAttribute("msg", "用户尚未激活请登录注册邮箱进行激活！<a " +
					"href='/goodsmine/UserServlet?method=sendMail' >发送激活邮件 </a>");
			req.getSession().setAttribute("username", user.getLoginname());
			return "f:/jsps/user/login.jsp";
		}else{
			req.getSession().setAttribute("loginname", user.getLoginname());
			Cookie cookie = new Cookie("loginname", user.getLoginname());
			return "r:/index.jsp";
		}
	}
		req.setAttribute("msg", "用户名或密码不正确！");
		req.setAttribute("user", formUser);
	return "f:/jsps/user/login.jsp";
}

private Map<String,String> avlidateLogin(User formUser) throws SQLException{
	/**
	 * 1.用户名为空校验
	 * 定义一个空的map对象用来装错误信息。
	 * 
	 */
	Map<String ,String> errors = new HashMap<String, String>();
	String loginname1 = formUser.getLoginname();
	
	if(loginname1==null || loginname1.trim().isEmpty()||"".equals(loginname1)){
		errors.put("loginname", "用户名不能为空！");
		return errors;
	}
	/**
	 * 2.用户名的长度校验 3~20
	 */
	if(formUser.getLoginname().length()<3||formUser.getLoginname().length()>20){
		errors.put("loginname", "用户名长度必须为3~20！");
		return errors;
	}
	/**
	 * 3.用户名已存在校验
	 */
	boolean loginname = userService.ajaxValidateLoginname(formUser.getLoginname());
	if(loginname){
		errors.put("loginname", "后台校验用户名不存在！");
		return errors;
	}
	
	
	return errors;
}

	
	
	/**
	 * 用户名是否注册
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	
	public String ajaxValidateLoginname(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//获得表单参数 
		String loginname =req.getParameter("loginname");
		//请service来校验用户名是否存在
		boolean b =  userService.ajaxValidateLoginname(loginname);
		resp.getWriter().print(b);
		 //发送到客户端  
		 return null;//表示既不转发也不重定向。
	}
	/**
	 * email是否注册
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateEmail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//获得表单参数 
		String email =req.getParameter("email");
		//请service来校验用户名是否存在
		boolean b =  userService.ajaxValidateEmail(email);
		resp.getWriter().print(b);
		 //发送到客户端  
		 return null;//表示既不转发也不重定向。
	}
	
	/**
	 * 校验验证码
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateVerifyCode (HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//得到传递的验证码参数
		String verifyCode = req.getParameter("verifyCode");
		//得到真实图片上的验证码参数
		String  vCode = (String) req.getSession().getAttribute("vCode");
		//进行比对，如果不一致发送false
		Boolean b =(verifyCode.equalsIgnoreCase(vCode));
		resp.getWriter().print(b);
		return null;
	}
	/**
	 * 激活用户状态
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	
	    public String activation(HttpServletRequest req, HttpServletResponse resp)
	    		throws ServletException, IOException {
	    	// TODO Auto-generated method stub
	    	/**
	    	 * 1.获取激活码 
	    	 * 2.用UserService中的activation方法完成激活
	    	 * 》有可能产生异常，得到异常信息，保存异常信息msg.jsp
	    	 * 激活完成，保存成功信息到request域中转发到msg.jsp
	    	 */
	    	String activationCode = req.getParameter("activationCode");
	    	try{
	    	userService.activation(activationCode);
	    	req.setAttribute("code", "success");
	    	req.setAttribute("msg", "恭喜，激活成功，请享受商城吧！！");
	    	}catch(UserException e){
	    		String msg =e.getMessage();
	    		req.setAttribute("msg", msg);
	    		req.setAttribute("code", "error");//通知msg显示错号图片
	    		
	    	}finally{
	    		return "f:/jsps/msg.jsp";
	    	}
	    	
	    	
	    	
	    }
	    
	    public String sendMail(HttpServletRequest req, HttpServletResponse resp)
	    		throws ServletException, IOException, SQLException {
	    	HttpSession session = req.getSession();
	    	String username =  (String) session.getAttribute("username") ;
	    	User user = userService.findByLoginname(username);
	    	userService.sendEmail(user);
	    	req.setAttribute("msg", "邮件已发送请登录激活啊");
	    			return "f:/jsps/msg.jsp";
	    }
	    

	
	
}
