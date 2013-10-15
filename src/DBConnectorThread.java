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
    String antwort;
    public static ArrayList<String> sqlBefehlsListeChecken= new ArrayList<String>(); 
    public static ArrayList<String> sqlBefehlsListeAnmeldungChecken= new ArrayList<String>();
    
    public void run() {

	ConnectToDB();
	SQLBefehl("SELECT * FROM user"); 

	while(true){
	    try {
		sqlBefehlsListeChecken.clear();
		sqlBefehlsListeChecken.addAll(MultiThreadedServer.sqlBefehlsListe);				
		if(sqlBefehlsListeChecken.size()>0){
		    System.out.println("<>< SQL Befehl weitergeleitet!");
		    System.out.println("|"+sqlBefehlsListeChecken.get(0)+"|");
		    SQLBefehl(sqlBefehlsListeChecken.get(0)); 
		    MultiThreadedServer.sqlBefehlsListe.remove(0);
		}
		
		sqlBefehlsListeAnmeldungChecken.clear();
		sqlBefehlsListeAnmeldungChecken.addAll(MultiThreadedServer.sqlBefehlsListeAnmeldung);				
		if(sqlBefehlsListeAnmeldungChecken.size()>0){
		    System.out.println("<>< SQL Befehl weitergeleitet!");
		    System.out.println("|"+sqlBefehlsListeAnmeldungChecken.get(0)+"|");
		    SQLBefehlAnmeldung(sqlBefehlsListeAnmeldungChecken.get(0)); 
		    MultiThreadedServer.sqlBefehlsListeAnmeldung.remove(0);
		}
		
		
		Thread.sleep(100);
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }
    
    
    
    DBConnectorThread() {

    }

    public void SQLBefehl(String sql){
	try {

	    if(sql.length()<11){sql="Select * from user";}
	    if(sql.substring(0,10).contains("INSERT") | sql.substring(0,10).contains("UPDATE") | sql.substring(0,10).contains("DELETE")){
		SQLManipulation(sql);
		//sql.su
	    }
	    else{   

		rs = stmt.executeQuery(sql);//.executeQuery(sql);
		System.out.println("*DB<-* '"+sql+"'");

		ResultSetMetaData rsmd = rs.getMetaData();

		int spalten = rsmd.getColumnCount();
		 antwort="\n\n";
		 antwort=antwort+"*DB<-* "+sql;
		 antwort=antwort+"\n";
		 
		 antwort=antwort+"*DB>>* /";
		 System.out.print("*DB>>* /");
		 int j = 1;
		 while(j<spalten+1){
		     System.out.print(rsmd.getColumnName(j)+"/");
		     antwort=antwort+rsmd.getColumnName(j)+"/";
		     j++;
		 }
		 System.out.println("");
		 antwort=antwort+"\n";
		while (rs.next()){
		    int i = 1;
		   
		    antwort=antwort+"*DB->* | ";
		    System.out.print("*DB->* | ");
		    while(i<spalten+1){
			
			antwort=antwort+rs.getString(i)+" | ";
			System.out.print(rs.getString(i)+" | ");
			i++;
		    }
		    antwort=antwort+"\n";
		    System.out.print("\n");
		    //System.out.println();

		}
		MultiThreadedServer.messageList.add(new Message("ServerDB","Admin",antwort));
		rs.close();
	    }

	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    System.out.println("SQL PROBLEM!");
	    MultiThreadedServer.messageList.add(new Message("ServerDB","Admin"," SQL FEHLER: "+e.getMessage()));
	    e.printStackTrace();
	}
    }
    
    
    
    public void SQLBefehlAnmeldung(String sql){
	try {

	    if(sql.length()<11){sql="Select * from user";}
	    if(sql.substring(0,10).contains("INSERT") | sql.substring(0,10).contains("UPDATE") | sql.substring(0,10).contains("DELETE")){
		SQLManipulation(sql);
		//sql.su
	    }
	    else{   

		rs = stmt.executeQuery(sql);//.executeQuery(sql);
		
		System.out.println("*DB<-* '"+sql+"'");

		ResultSetMetaData rsmd = rs.getMetaData();

		int spalten = rsmd.getColumnCount();
		
		//rsmd.get
		 antwort="\n\n";
		 antwort=antwort+"*DB<-* "+sql;
		 antwort=antwort+"\n";
		 
		 antwort=antwort+"*DB>>* /";
		 System.out.print("*DB>>* /");
		 int j = 1;
		 while(j<spalten+1){
		     System.out.print(rsmd.getColumnName(j)+"/");
		     antwort=antwort+rsmd.getColumnName(j)+"/";
		     j++;
		 }
		 System.out.println("");
		 antwort=antwort+"\n";
		 
		//System.out.println("huuuuuuuhuuuuuuuu"+rs.getRow());
		/*rs.last();
		 
		 System.out.println(rs.getRow());
		 if(rs.getRow()==0){antwort="Keine Einträge";}
		 rs.beforeFirst();*/
		 
		while (rs.next()){
		    int i = 1;
		   
		    antwort=antwort+"*DB->* | ";
		    System.out.print("*DB->* | ");
		    while(i<spalten+1){
			
			antwort=antwort+rs.getString(i)+" | ";
			System.out.print(rs.getString(i)+" | ");
			i++;
		    }
		    
		   
		    
		    
		    
		    antwort=antwort+"\n";
		    System.out.print("\n");
		    
		    
		    
		    //System.out.println();
		   
		}
		rs.last();
		System.out.println(rs.getRow());
		if(rs.getRow()==0){antwort="Keine Einträge";}
		rs.beforeFirst();
		MultiThreadedServer.messageList.add(new Message("ServerDB","Anmelder",antwort));
		rs.close();
	    }

	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    System.out.println("SQL PROBLEM!");
	    MultiThreadedServer.messageList.add(new Message("ServerDB","Admin"," SQL FEHLER: "+e.getMessage()));
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
	   
	}catch(Exception e){	       
	    System.err.println(e);	       
	}


    }




}


//SQLBefehl("INSERT INTO user (username, email, password, create_time)  VALUES ('F','F@gmx.com',1234,now());");
//SQLBefehl("SELECT username FROM user"); 
//SQLBefehl("SELECT password FROM user where username like 'A'");


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


/*int index=0;
	for (String sqlB : sqlBefehlsListeChecken){
	   // if (m.to.equalsIgnoreCase(this.user)){
		System.out.println("<>< Message weitergeleitet!");
		sqlBefehlsListe.remove(index);
	  //  }   
	  //  index++;
	  //  break;
	}*/