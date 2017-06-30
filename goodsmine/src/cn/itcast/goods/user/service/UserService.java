package cn.itcast.goods.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.dao.UserDao;
import cn.itcast.goods.user.domain.User;
import cn.itcast.goods.user.service.exception.UserException;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

public class UserService {
	private UserDao userDao = new UserDao();
	/*
	 * 1.业务层注册方法
	 */
	public void regist(User user){
		/**
		 * 增加额外的属性
		 */
		user.setUid(CommonUtils.uuid());
		user.setActivationCode(CommonUtils.uuid()+CommonUtils.uuid());
		user.setStatus(false);
		
		//向数据库中插入。
		try {
			userDao.add(user);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		/**
		 * 3.发邮件
		 * 
		 */
		//把配置文件内容加载到prop对象中.
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		/**
		 * 登陆邮件服务器,得到session
		 */
		String host =prop.getProperty("host");
		String name =prop.getProperty("username");
		String pass = prop.getProperty("password");
		Session session = MailUtils.createSession(host, name, pass);
		//创建mail对象
		String from = prop.getProperty("from");
		String to = user.getEmail();
		String subject = prop.getProperty("subject");
		String content = MessageFormat.format(prop.getProperty("content"), user.getActivationCode());
		Mail mail = new Mail(from, to, subject, content);
		//发送邮件
		try {
//			for(int i=0;i<30;i++){
			MailUtils.send(session, mail);
//			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("出错了吧！！！！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	
	}
	/**
	 * 激活功能
	 * @param code
	 * @throws SQLException 
	 * @throws UserException 
	 */
	public void activation(String code) throws UserException {
		/*
		 * 1.通过激活码查询用户
		 * 2.通过User是否为空，说明是无效激活码，抛出异常：无效激活码
		 * 3.通过查看状态，如果是true，则抛出异常 异常信息：请不要二次激活
		 * 4.如果是false ，则设置为true
		 */
		try{
		User user = userDao.findByCode(code);
		if(user==null){
		throw new UserException("无效激活码！");
		}
		Boolean status = user.isStatus();
		if(status) throw new UserException("请不要二次激活") ;
		userDao.updateStatus(user.getUid(), true);
		}catch(SQLException e){
//			e.printStackTrace();
			throw new RuntimeException(e);
		} 
		
	}
	
	public  User login(User formUser) throws SQLException{
		String loginname = formUser.getLoginname();
		String loginpass = formUser.getLoginpass();
		return userDao.findByLoginnameAndPassword(loginname, loginpass);
	}
	
/**
 * 用户名校验
 * @param loginname
 * @return
 */
	
	public boolean ajaxValidateLoginname(String loginname) {
		try {
			return userDao.ajaxValidateLoginname(loginname);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
	}
	/**
	 * email校验
	 * @param email
	 * @return
	 */
	public boolean ajaxValidateEmail(String email) {
		try {
			return userDao.ajaxValidateEmail(email);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * 发送邮件
	 */
	public void sendEmail (User user){
		/**
		 * 3.发邮件
		 * 
		 */
		//把配置文件内容加载到prop对象中.
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		/**
		 * 登陆邮件服务器,得到session
		 */
		String host =prop.getProperty("host");
		String name =prop.getProperty("username");
		String pass = prop.getProperty("password");
		Session session = MailUtils.createSession(host, name, pass);
		//创建mail对象
		String from = prop.getProperty("from");
		String to = user.getEmail();
		String subject = prop.getProperty("subject");
		String content = MessageFormat.format(prop.getProperty("content"), user.getActivationCode());
		Mail mail = new Mail(from, to, subject, content);
		//发送邮件
		try {
//			for(int i=0;i<30;i++){
			MailUtils.send(session, mail);
//			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("出错了吧！！！！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
    /*
     * 
     */
	public User findByLoginname(String loginname) throws SQLException {
		
	return userDao.findByLoginname(loginname);
}

}
