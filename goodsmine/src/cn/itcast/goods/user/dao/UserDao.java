package cn.itcast.goods.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.goods.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

public class UserDao {
	private TxQueryRunner qr = new TxQueryRunner();
	
	//1.添加用户到数据库
	public void add(User user) throws SQLException{
		String sql ="insert into t_user value(?,?,?,?,?,?)";
		Object[] params ={user.getUid(),user.getLoginname(),user.getLoginpass(),user.getEmail(), user.isStatus(), user.getActivationCode()
				};
		qr.update(sql, params);
		
	}
	/**
	 * 校验用户名是否注册
	 * @param loginname
	 * @return
	 * @throws SQLException 
	 */
	
	public boolean ajaxValidateLoginname(String loginname) throws SQLException{
		String sql ="select count(1) from t_user" +
				" where loginname=?";
		Number number =(Number) qr.query(sql, new ScalarHandler(), loginname);
		return number.intValue()==0;
	}
	/**
	 * email校验
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateEmail(String email) throws SQLException{
		String sql ="select count(1) from t_user " +
				"where email=?";
		Number number =(Number) qr.query(sql, new ScalarHandler(), email);
		return number.intValue()==0;
	}
	/**
	 * 通过激活码查询User
	 * @param code
	 * @return
	 * @throws SQLException
	 */
	public User findByCode (String code) throws SQLException{
		String sql = "select * from t_user where activationCode=?";
		return qr.query(sql, new BeanHandler<User>(User.class), code);
		
	}
	/**
	 * 设置用户状态。
	 * @param uid
	 * @param status
	 * @throws SQLException 
	 */
	public void updateStatus(String uid,boolean status) throws SQLException{
		String sql  = "update t_user set status = ? where uid= ?";
		qr.update(sql, status,uid);
	}
	/**
	 * 通过用户名和密码进行查询
	 * @param loginname
	 * @param loginpass
	 * @return User user
	 * @throws SQLException
	 */
	public User findByLoginnameAndPassword(String loginname,String loginpass) throws SQLException{
			String sql =" select * from t_user where loginname = ? and loginpass = ? ";
			User user =qr.query(sql, new BeanHandler<User>(User.class), loginname,loginpass);
		return user;
	}
	
	/**
	 * 通过用户名进行查询
	 * @param loginname
	 * @param loginpass
	 * @return User user
	 * @throws SQLException
	 */
	public User findByLoginname(String loginname) throws SQLException{
			String sql =" select * from t_user where loginname = ?  ";
			User user =qr.query(sql, new BeanHandler<User>(User.class), loginname);
		return user;
	}
	
}
