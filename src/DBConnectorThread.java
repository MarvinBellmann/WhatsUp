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
	//SQLBefehl("SELECT * FROM user"); 

	while(true){
	    try {
		sqlBefehlsListeChecken.clear();
		sqlBefehlsListeChecken.addAll(MultiThreadedServer.sqlBefehlsListe);				
		if(sqlBefehlsListeChecken.size()>0){
		    //System.out.println("<>< SQL Befehl weitergeleitet!");
		    System.out.println("|"+sqlBefehlsListeChecken.get(0)+"|");
		    SQLBefehl(sqlBefehlsListeChecken.get(0),"normal"); 
		    MultiThreadedServer.sqlBefehlsListe.remove(0);
		}

		sqlBefehlsListeAnmeldungChecken.clear();
		sqlBefehlsListeAnmeldungChecken.addAll(MultiThreadedServer.sqlBefehlsListeAnmeldung);				
		if(sqlBefehlsListeAnmeldungChecken.size()>0){
		    //System.out.println("<>< SQL Befehl weitergeleitet!");
		    System.out.println("|"+sqlBefehlsListeAnmeldungChecken.get(0)+"|");
		    SQLBefehl(sqlBefehlsListeAnmeldungChecken.get(0),"anmelden"); 
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

    public void SQLBefehl(String sql, String typ){
	try {

	    if(sql.length()<11){sql="Select * from user";}
	    if(sql.substring(0,10).contains("CREATE") | sql.substring(0,10).contains("INSERT") | sql.substring(0,10).contains("UPDATE") | sql.substring(0,10).contains("DELETE")){

		if(sql.substring(0,10).contains("CREATE")){

		    System.out.println("create!");
		    SQLCreate(sql);

		    //sql.su
		}else{
		    SQLManipulation(sql);
		}


		//sql.su
	    }

	    else{ 

		rs = stmt.executeQuery(sql);//.executeQuery(sql);
		System.out.println("*DB<-* '"+sql+"'");

		ResultSetMetaData rsmd = rs.getMetaData();

		int spalten = rsmd.getColumnCount();
		antwort="\n";
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
		    
		    if(typ.equals("normal")){
		    antwort=antwort+"\n";
		    System.out.print("\n");
		    //System.out.println();
		    }

		}
		
		
		
	
		
		if(typ.equals("normal")){
		 antwort= antwort.substring(0, antwort.length()-2);
		MultiThreadedServer.messageList.add(new Message("ServerDB","Admin",antwort));
		rs.close();
		}
		if(typ.equals("anmelden")){
		    rs.last();
			//System.out.println("Datensätze gefunden: "+rs.getRow());
			if(rs.getRow()==0){
			    antwort="\n";
			    antwort=antwort+"*DB<-* "+sql;
			    antwort=antwort+"\n";
			    antwort=antwort+"*DB->* ";
			    antwort=antwort+"Keine Einträge";
			    }
			rs.beforeFirst();
			
			//if(antwort.substring(antwort.length()-2, antwort.length()).equalsIgnoreCase("\n"))
			if(antwort.contains("DB->* Keine Einträge")==false){ antwort= antwort.substring(0, antwort.length()-2);}
			MultiThreadedServer.messageList.add(new Message("ServerDB","Anmelder",antwort));
			//MultiThreadedServer.messageList.add(new Message("ServerDB","Admin",antwort));
			
			rs.close();	
		}
		
	    }

	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    System.out.println("SQL PROBLEM!");
	    MultiThreadedServer.messageList.add(new Message("ServerDB","Admin"," SQL FEHLER: "+e.getMessage()));
	    if(typ.equals("anmelden")){
	    MultiThreadedServer.messageList.add(new Message("ServerDB","Anmelder"," SQL FEHLER: "+e.getMessage()));
	    }
	    
	    e.printStackTrace();
	    
	    
	 
	    
	    
	}
    }






    public void SQLManipulation(String sql){
	try {

	    //Executes the given SQL statement, which may be an 
	    //INSERT, UPDATE, or DELETE statement or an SQL statement that returns nothing, such as an SQL DDL statement.
	    stmt.executeUpdate(sql);//.executeQuery(sql);
	    System.out.println("*DB<-* '"+sql+"'");
	    String antwort = "\n*DB<-* '"+sql+"'";
	    MultiThreadedServer.messageList.add(new Message("ServerDB","Admin",antwort));
		
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    MultiThreadedServer.messageList.add(new Message("ServerDB","Admin"," SQL FEHLER: "+e.getMessage()));
	    e.printStackTrace();
	}
    }

    public void SQLCreate(String sql){
	try {

	    //Executes the given SQL statement, which may be an 
	    //INSERT, UPDATE, or DELETE statement or an SQL statement that returns nothing, such as an SQL DDL statement.
	    stmt.execute(sql);//.executeQuery(sql);
	    System.out.println("*DB<-* '"+sql+"'");
	    String antwort = "\n*DB<-* '"+sql+"'";
	    MultiThreadedServer.messageList.add(new Message("ServerDB","Admin",antwort));

	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    MultiThreadedServer.messageList.add(new Message("ServerDB","Admin"," SQL FEHLER: "+e.getMessage()));
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
	  // INSERT INTO messages (from,to) VALUES ('A','B');
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