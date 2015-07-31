package hackthon.com.hackathon.webservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserDetailsController {

    private final AtomicLong counter = new AtomicLong();
    private boolean initiateOnce = true;
    Connection con;

    @RequestMapping("/register/user")
    public Boolean registerUser(@RequestParam(value="userName") String username,@RequestParam(value="emailAdd") String email,
    		@RequestParam(value="imei") String imei,
    		@RequestParam(value="mobNo") String mobNo
    		) throws SQLException {
    	
    	if(initiateOnce)
    	{
        	init();	
        	initiateOnce = false;
    	}
    	
        return insertNewUser(counter,username,email,imei,mobNo);
    }

    @RequestMapping("/register/userCheck")
    public Boolean registerUserCheck(
    		@RequestParam(value="imei") String imei
    		) throws SQLException {
    	
    	if(initiateOnce)
    	{
        	init();	
        	initiateOnce = false;
    	}
    	
    	PreparedStatement ps = con.prepareStatement("SELECT 1 FROM USER WHERE IMEINUMBER = " +imei);
		ResultSet rs = ps.executeQuery();
        return rs.next();
    }

	private Boolean insertNewUser(AtomicLong counter, String username,String email, String imei, String mobNo) throws SQLException {
		
		PreparedStatement ps = con.prepareStatement("INSERT INTO USER VALUES (?,?,?,?,?)");
		ps.setLong(1, counter.get());
		ps.setString(2, username);
		ps.setString(3, mobNo);
		ps.setString(4, imei);
		ps.setString(5, email);
		int rs = ps.executeUpdate();
		
		return rs == 0?false:true;
	}

	private void init() {
	
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		try 
		{
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hackdb","root","H@ppy123");
			PreparedStatement ps = con.prepareStatement("select 1 from user");
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			{
				System.out.println("Connection Successful");
			}
		}
		catch (SQLException e) 
		{
		e.printStackTrace();
		System.out.println("Invalid Connection");
		}
		
		
		
		
	}
}

