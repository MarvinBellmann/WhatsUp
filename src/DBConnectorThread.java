import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBConnectorThread extends Thread {

    boolean DBAnwesend=true;
    String pw = "admin123";
    Connection con;	    
    Statement stmt;	    
    ResultSet rs;
    public static ArrayList<String> dieSQLBefehlsListe= new ArrayList<String>(); 

    public void run() {

	ConnectToDB();
	//SQLBefehl("INSERT INTO user (username, email, password, create_time)  VALUES ('F','F@gmx.com',1234,now());");
	SQLBefehl("SELECT * FROM user"); 
	SQLBefehl("SELECT username FROM user"); 
	SQLBefehl("SELECT password FROM user where username like 'A'");
	
    }
    DBConnectorThread() {

    }

    public void SQLBefehl(String sql){
	try {
	    
	    if(sql.substring(0,10).contains("INSERT") | sql.substring(0,10).contains("UPDATE") | sql.substring(0,10).contains("DELETE")){
		SQLManipulation(sql);
		//sql.su
	    }
	    else{   
	    
        	    rs = stmt.executeQuery(sql);//.executeQuery(sql);
        	    System.out.println("*DB<-* '"+sql+"'");
        
        	    ResultSetMetaData rsmd = rs.getMetaData();
        
        	    int spalten = rsmd.getColumnCount();
        
        	    while (rs.next()){
        		int i = 1;
        		System.out.print("*DB->* | ");
        		while(i<spalten+1){
        		    System.out.print(rs.getString(i)+" | ");
        		    i++;
        		}
        		System.out.print("\n");
        		
        	    }
        	    rs.close();
	    }
	    
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public void SQLManipulation(String sql){
   	try {
   	    
   	    //Executes the given SQL statement, which may be an 
   	    //INSERT, UPDATE, or DELETE statement or an SQL statement that returns nothing, such as an SQL DDL statement.
   	    stmt.executeUpdate(sql);//.executeQuery(sql);
   	    System.out.println("*DB<-* '"+sql+"'");
   	    
   	} catch (SQLException e) {
   	    // TODO Auto-generated catch block
   	    e.printStackTrace();
   	}
       }

    public void ConnectToDB() {
	try{	   
	    //laden der Treiberklasse
	    Class.forName("com.mysql.jdbc.Driver");	   
	    System.out.println("*DB* jdbc mysql Drivers geladen.");

	    //Specify the Database URL where the DNS will be and the user and password
	    con = DriverManager.getConnection("jdbc:mysql://46.163.119.64:3306/whatsup","whatsup",pw);
	    System.out.println("*DB* MySql verbindung erfolgreich mit: //46.163.119.64:3306/whatsup");

	    //Initialize the statement to be used, specify if rows are scrollable
	    stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);

	    //ResultSet will hold the data retrieved
	    // rs = stmt.executeQuery("SELECT * FROM user");
	    // System.out.println("*DB* SELECT * FROM user");	
	    //Display the results	    
	  	/*try {
	  	    while(rs.next()){	    
	  		System.out.println("*DB-Return* " + rs.getString("username") + " " + rs.getString("password"));	    
	  	    }
	  	} catch (SQLException e) {	   
	  	    e.printStackTrace();
	  	}*/

	}catch(Exception e){	       
	    System.err.println(e);	       
	}


    }


 

}
