package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import SendData.Message;
import SendData.SQLData;

public class DBConnectorThread extends Thread {

	boolean DBAnwesend = true;
	String pw = "admin123";
	Connection con;
	Statement stmt;
	ResultSet rs;
	String antwort;
	public static ArrayList<SQLData> sqlBefehlsListeChecken = new ArrayList<SQLData>();

	public void run() {

		ConnectToDB();
		try {
			stmt.executeUpdate("UPDATE user set status='Offline'");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		while (true) {
			try {
				sqlBefehlsListeChecken.clear();
				sqlBefehlsListeChecken
						.addAll(MultiThreadedServer.sqlBefehlsListe);
				if (sqlBefehlsListeChecken.size() > 0) {
					SQLBefehl(sqlBefehlsListeChecken.get(0));
					MultiThreadedServer.sqlBefehlsListe.remove(0);
				}

				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	DBConnectorThread() {

	}

	public void SQLBefehl(SQLData sqldata) { //'p'=Picture; 'h'=hinzufügen eines Kontaktes;'l'=löschen aus Kontaktliste; 'k'=Kontaktliste zurückgeben; 'a'=anmelden
		try {

			if (sqldata.sqlBefehl.length() < 11) {
				sqldata.sqlBefehl = "Select * from user";
			}
			if (sqldata.sqlBefehl.substring(0, 10).contains("CREATE")
					| sqldata.sqlBefehl.substring(0, 10).contains("INSERT")
					| sqldata.sqlBefehl.substring(0, 10).contains("UPDATE")
					| sqldata.sqlBefehl.substring(0, 10).contains("IF NOT EX")
					| sqldata.sqlBefehl.substring(0, 10).contains("DELETE")) {

				if (sqldata.sqlBefehl.substring(0, 10).contains("CREATE")) {

					ServerStart.SystemWriteLogln("create!");
					SQLCreate(sqldata.sqlBefehl);

				} else {
					SQLManipulation(sqldata.sqlBefehl);
				}

			}
				// ALSO SELECT!
			else {

				rs = stmt.executeQuery(sqldata.sqlBefehl);// .executeQuery(sql);
				ServerStart.SystemWriteLogln("*DB<-* '" + sqldata.sqlBefehl + "'");

				ResultSetMetaData rsmd = rs.getMetaData();

				int spalten = rsmd.getColumnCount();
				antwort = "\n";
				antwort = antwort + "*DB<-* " + sqldata.sqlBefehl;
				antwort = antwort + "\n";

				antwort = antwort + "*DB>>* /";
				ServerStart.SystemWriteLog("*DB>>* /");
				int j = 1;
				while (j < spalten + 1) {
					ServerStart.SystemWriteLog(rsmd.getColumnName(j) + "/");
					antwort = antwort + rsmd.getColumnName(j) + "/";
					j++;
				}
				ServerStart.SystemWriteLogln("");
				antwort = antwort + "\n";
				if (sqldata.typ == 'k' | sqldata.typ == 'p'| sqldata.typ == 'h'| sqldata.typ == 'l') {
					antwort = "";
					ServerStart.SystemWriteLog("*DB->* ");
				}
				while (rs.next()) {
					int i = 1;

					if (sqldata.typ != 'k' && sqldata.typ != 'p'| sqldata.typ == 'h'| sqldata.typ == 'l') {

						antwort = antwort + "*DB->* | ";
						ServerStart.SystemWriteLog("*DB->* | ");
						while (i < spalten + 1) {

							antwort = antwort + rs.getString(i) + " | ";
							ServerStart.SystemWriteLog(rs.getString(i) + " | ");
							i++;
						}

					} else {

						while (i < spalten + 1) {

							antwort = antwort + rs.getString(i) + ",";
							ServerStart.SystemWriteLog(rs.getString(i) + ",");
							i++;
						}
					}
					if (sqldata.typ == 'n') {
						antwort = antwort + "\n";
						ServerStart.SystemWriteLog("\n");
					}

				}

				if (sqldata.typ == 'n') {
					antwort = antwort.substring(0, antwort.length() - 2);
					MultiThreadedServer.messageList.add(new Message("ServerDB",
							"Admin", antwort));

				}
				if (sqldata.typ == 'a' | sqldata.typ == 'h' | sqldata.typ == 'l') {
					rs.last();
					if (rs.getRow() == 0) {
						antwort = "\n";
						antwort = antwort + "*DB<-* " + sqldata.sqlBefehl;
						antwort = antwort + "\n";
						antwort = antwort + "*DB->* ";
						antwort = antwort + "Keine Einträge";
					}
					rs.beforeFirst();

					if (antwort.contains("DB->* Keine Einträge") == false) {
						antwort = antwort.substring(0, antwort.length() - 2);
					}
					MultiThreadedServer.messageList.add(new Message("ServerDB",
							"Anmelder", antwort));
					//ServerStart.SystemWriteLogln("message müsste geaddet sein");

				}
				if (sqldata.typ == 'k') {
					ServerStart.SystemWriteLog("\n");
					if (sqldata.sqlBefehl.contains("status")) {
						MultiThreadedServer.messageList
								.add(new Message("KontaktDBAntwort",
										sqldata.user, antwort, 's'));
					}
				}
				if (sqldata.typ == 'p') {
				   // ServerStart.SystemWriteLogln(sqldata.sqlBefehl);
					ServerStart.SystemWriteLog("\n");
					if (sqldata.sqlBefehl.contains("Select picture from")) {
						MultiThreadedServer.messageList
								.add(new Message("PictureDBAntwort",
										sqldata.user, antwort));
					}
				}
				if (sqldata.typ == 'h') {
					   // ServerStart.SystemWriteLogln(sqldata.sqlBefehl);
						ServerStart.SystemWriteLog("\n");
						//if (sqldata.sqlBefehl.contains("Select * from user")) {
							MultiThreadedServer.messageList
									.add(new Message("HinzufuegenDBAntwort",
											sqldata.user, antwort));
						//}
					}
				
				if (sqldata.typ == 'l') {
					   // ServerStart.SystemWriteLogln(sqldata.sqlBefehl);
						ServerStart.SystemWriteLog("\n");
						//if (sqldata.sqlBefehl.contains("Select * from user")) {
							MultiThreadedServer.messageList
									.add(new Message("LoeschenDBAntwort",
											sqldata.user, antwort));
						//}
					}
				rs.close();
			}
		} catch (SQLException e) {
			ServerStart.SystemWriteLogln("SQL PROBLEM!");
			MultiThreadedServer.messageList.add(new Message("ServerDB",
					"Admin", " SQL FEHLER: " + e.getMessage()));
			if (sqldata.typ == 'a') {
				MultiThreadedServer.messageList.add(new Message("ServerDB",
						"Anmelder", " SQL FEHLER: " + e.getMessage()));
			}

			e.printStackTrace();

		}
	}

	public void SQLManipulation(String sql) {
		try {
			stmt.executeUpdate(sql);// .executeQuery(sql);
			ServerStart.SystemWriteLogln("*DB<-* '" + sql + "'");
			String antwort = "\n*DB<-* '" + sql + "'";
			MultiThreadedServer.messageList.add(new Message("ServerDB",
					"Admin", antwort));

		} catch (SQLException e) {
			MultiThreadedServer.messageList.add(new Message("ServerDB",
					"Admin", " SQL FEHLER: " + e.getMessage()));
			e.printStackTrace();
		}

		try {
			if (sql.contains("UPDATE user set status='")) {
				for (WorkerRunnableRead w : MultiThreadedServer.AngemeldeteWorkerRunnableRead) {
				    if(w.user.equalsIgnoreCase("Anmelder")==false){
					
					
					if(w.user.equalsIgnoreCase("Admin")==false){
					MultiThreadedServer.sqlBefehlsListe.add(new SQLData(
							/*"SELECT username,status from user where username not like '"
									+ w.user + "' order by username", 'k',
							w.user));*/
						"SELECT c.contact,u.status, u.picture from user u, contacts c where c.contact=u.username and c.username like '"
						+ w.user + "' order by c.contact", 'k',
				w.user));
					
					}else{
					MultiThreadedServer.sqlBefehlsListe.add(new SQLData(
						"SELECT username,status,picture from user where username not like '"
								+ w.user + "' order by username", 'k',
						w.user));
					}
					
				    }

				}
			}
			
			/*if (sql.contains("INSERT INTO user (username,")){
			    MultiThreadedServer.sqlBefehlsListe.add(new SQLData(
					"INSERT INTO contacts (username,contact) values ('"+ w.user + "' order by username", 'k',
					w.user));
				}
			}*/

		} catch (Exception e) {
			ServerStart.SystemWriteLogln(e.getMessage());
		}

	}

	public void SQLCreate(String sql) {
		try {
			stmt.execute(sql);// .executeQuery(sql);
			ServerStart.SystemWriteLogln("*DB<-* '" + sql + "'");
			String antwort = "\n*DB<-* '" + sql + "'";
			MultiThreadedServer.messageList.add(new Message("ServerDB",
					"Admin", antwort));

		} catch (SQLException e) {
			MultiThreadedServer.messageList.add(new Message("ServerDB",
					"Admin", " SQL FEHLER: " + e.getMessage()));
			e.printStackTrace();

		}
	}

	public void ConnectToDB() {
		try {
			// laden der Treiberklasse
			Class.forName("com.mysql.jdbc.Driver");
			ServerStart.SystemWriteLogln("*DB* jdbc mysql Drivers geladen.");

			// Specify the Database URL where the DNS will be and the user and
			// password
			con = DriverManager.getConnection(
					"jdbc:mysql://46.163.119.64:3306/whatsup", "whatsup", pw);
			ServerStart.SystemWriteLogln("*DB* MySql verbindung erfolgreich mit: //46.163.119.64:3306/whatsup");

			// Initialize the statement to be used, specify if rows are
			// scrollable
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

		} catch (Exception e) {
		    ServerStart.SystemWriteErrorLogln(e);
		}
	}
}