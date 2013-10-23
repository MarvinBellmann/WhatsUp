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

	public void SQLBefehl(SQLData sqldata) {
		try {

			if (sqldata.sqlBefehl.length() < 11) {
				sqldata.sqlBefehl = "Select * from user";
			}
			if (sqldata.sqlBefehl.substring(0, 10).contains("CREATE")
					| sqldata.sqlBefehl.substring(0, 10).contains("INSERT")
					| sqldata.sqlBefehl.substring(0, 10).contains("UPDATE")
					| sqldata.sqlBefehl.substring(0, 10).contains("DELETE")) {

				if (sqldata.sqlBefehl.substring(0, 10).contains("CREATE")) {

					System.out.println("create!");
					SQLCreate(sqldata.sqlBefehl);

				} else {
					SQLManipulation(sqldata.sqlBefehl);
				}

			}

			else {

				rs = stmt.executeQuery(sqldata.sqlBefehl);// .executeQuery(sql);
				System.out.println("*DB<-* '" + sqldata.sqlBefehl + "'");

				ResultSetMetaData rsmd = rs.getMetaData();

				int spalten = rsmd.getColumnCount();
				antwort = "\n";
				antwort = antwort + "*DB<-* " + sqldata.sqlBefehl;
				antwort = antwort + "\n";

				antwort = antwort + "*DB>>* /";
				System.out.print("*DB>>* /");
				int j = 1;
				while (j < spalten + 1) {
					System.out.print(rsmd.getColumnName(j) + "/");
					antwort = antwort + rsmd.getColumnName(j) + "/";
					j++;
				}
				System.out.println("");
				antwort = antwort + "\n";
				if (sqldata.typ == 'k') {
					antwort = "";
					System.out.print("*DB->* ");
				}
				while (rs.next()) {
					int i = 1;

					if (sqldata.typ != 'k') {

						antwort = antwort + "*DB->* | ";
						System.out.print("*DB->* | ");
						while (i < spalten + 1) {

							antwort = antwort + rs.getString(i) + " | ";
							System.out.print(rs.getString(i) + " | ");
							i++;
						}

					} else {

						while (i < spalten + 1) {

							antwort = antwort + rs.getString(i) + ",";
							System.out.print(rs.getString(i) + ",");
							i++;
						}
					}
					if (sqldata.typ == 'n') {
						antwort = antwort + "\n";
						System.out.print("\n");
					}

				}

				if (sqldata.typ == 'n') {
					antwort = antwort.substring(0, antwort.length() - 2);
					MultiThreadedServer.messageList.add(new Message("ServerDB",
							"Admin", antwort));

				}
				if (sqldata.typ == 'a') {
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
					System.out.println("message müsste geaddet sein");

				}
				if (sqldata.typ == 'k') {
					System.out.print("\n");
					if (sqldata.sqlBefehl.contains("status")) {
						MultiThreadedServer.messageList
								.add(new Message("KontaktDBAntwort",
										sqldata.user, antwort, 's'));
					}
				}
				rs.close();
			}
		} catch (SQLException e) {
			System.out.println("SQL PROBLEM!");
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
			System.out.println("*DB<-* '" + sql + "'");
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
					MultiThreadedServer.sqlBefehlsListe.add(new SQLData(
							"SELECT username,status from user where username not like '"
									+ w.user + "' order by username", 'k',
							w.user));

				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void SQLCreate(String sql) {
		try {
			stmt.execute(sql);// .executeQuery(sql);
			System.out.println("*DB<-* '" + sql + "'");
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
			System.out.println("*DB* jdbc mysql Drivers geladen.");

			// Specify the Database URL where the DNS will be and the user and
			// password
			con = DriverManager.getConnection(
					"jdbc:mysql://46.163.119.64:3306/whatsup", "whatsup", pw);
			System.out
					.println("*DB* MySql verbindung erfolgreich mit: //46.163.119.64:3306/whatsup");

			// Initialize the statement to be used, specify if rows are
			// scrollable
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

		} catch (Exception e) {
			System.err.println(e);
		}
	}
}