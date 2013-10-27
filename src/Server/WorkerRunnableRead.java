package Server;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.FileChannel;
//import java.text.DecimalFormat;


import SendData.ByteData;
import SendData.Message;
import SendData.SQLData;
import SendData.StartData;

public class WorkerRunnableRead extends Thread {

    Socket clientSocket = null;
    ObjectInputStream ois = null;
    Message messageIGot;
    StartData startdata;
    SQLData sqldata;
    public String user;
    boolean clientAnwesend=true;
    WorkerRunnable w;
    InetAddress clientIP;
    int clientPort;
    InputStream is = null;
	FileOutputStream fos = null;
	File ff = null;
	static String desktopPath = null;

    public void run() {
    	try {
			desktopPath = System.getProperty("user.home") + "/Desktop";
			desktopPath = desktopPath.replace("\\", "/");
		} catch (Exception e) {
			ServerStart.SystemWriteLogln("Exception caught =" + e.getMessage());
		}
	while (clientAnwesend==true) {

	    try {
		Empfange();
		Thread.sleep(100);
	    } catch (Exception e) {
		//    e.printStackTrace();
		
		/*try{
		    int index=0;
		    boolean loescheindex=false;
		    for(int i=0;i<MultiThreadedServer.AngemeldeteWorkerRunnableRead.size();i++){
			if(MultiThreadedServer.AngemeldeteWorkerRunnableRead.get(i).user.equals(user)){
			    index=i;
			    loescheindex=true;
			   break;
			}
		    }
		    
		    if(loescheindex==true){
		    MultiThreadedServer.AngemeldeteWorkerRunnableRead.remove(index);
		    }
		    
		}catch(Exception e2){
		   // ServerStart.SystemWriteLogln(e2.getMessage());
		    ServerStart.SystemWriteLogln("Lösche Workerrunnable aus liste problem!!!! " +e2.getMessage());
		}*/
		
		clientAnwesend=false;
		// ServerStart.SystemWriteLogln("Server read Problem.");
		ServerStart.SystemWriteLogln("!!! Abmeldung Client: [" + clientIP + " |Port:"+clientPort + "] - schließe Thread; Grund: "+e.getMessage());
		
		try{
		if(this.user.contains("Anmelder")==false){		    
		MultiThreadedServer.sqlBefehlsListe.add(new SQLData("UPDATE user set status='Offline' where username like '"+this.user+"'",'a'));
		 int index=0;
		    boolean loescheindex=false;
		    for(int i=0;i<MultiThreadedServer.AngemeldeteWorkerRunnableRead.size();i++){
			if(MultiThreadedServer.AngemeldeteWorkerRunnableRead.get(i).user.equals(user)){
			    index=i;
			    loescheindex=true;
			   break;
			}
		    }
		    
		    if(loescheindex==true){
		    MultiThreadedServer.AngemeldeteWorkerRunnableRead.remove(index);
		    ServerStart.SystemWriteLogln("!!! Anzahl der bestehenden Verbindungen zu Usern um einen reduziert(size der workerrunnableliste): "+MultiThreadedServer.AngemeldeteWorkerRunnableRead.size());
			
			
		    }
		}
		}catch(Exception e4){
		    ServerStart.SystemWriteLogln("set status problem: " +e.getMessage());
		}
		
	    }

	}
    }

    WorkerRunnableRead(Socket socket, ObjectInputStream ois, WorkerRunnable w) {
	this.clientSocket = socket;
	this.ois = ois;
	this.w=w;
	//this.user=this.w.user;
	this.clientIP= clientSocket.getInetAddress() ;
	this.clientPort=clientSocket.getPort();

    }

    public void Empfange() throws ClassNotFoundException, IOException {
	// IN PROGRESS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	//  ServerStart.SystemWriteLogln("X<< Warte auf Message");
	Object obj =  ois.readObject();
	//  ServerStart.SystemWriteLogln("<<< Message erhalten");

	if (obj instanceof Message)
	{
	    ServerStart.SystemWriteLog("<<< Nachricht von Cl: ["+clientSocket.getInetAddress() + " |Port:" + clientSocket.getPort()+"] "  );
	    messageIGot = (Message) obj;	
	    
	    if(messageIGot.from.equalsIgnoreCase("Admin") && messageIGot.to.equalsIgnoreCase("ServerDB")){
		ServerStart.SystemWriteLogln("|"+messageIGot.toText()+"");
		MultiThreadedServer.sqlBefehlsListe.add(new SQLData(messageIGot.toTextString(),'n'));
		   
	    }else{
		    MultiThreadedServer.messageList.add(messageIGot);		
	    }
	    
	    // ServerStart.SystemWriteLogln(messageIGot.toString());
	    //Thread.sleep(15);
	}
	if (obj instanceof StartData)
	{
	    startdata = (StartData) obj;
	    this.user=startdata.user;
	    w.user=startdata.user;
	    ServerStart.SystemWriteLogln("*** Client nennt sich: " + this.user);
	    if(this.user.contains("Anmelder")==false){
		//MultiThreadedServer.AngemeldeteUser.add(this.user);
		MultiThreadedServer.sqlBefehlsListe.add(new SQLData("UPDATE user set status='Online' where username like '"+this.user+"'",'n'));
		
		MultiThreadedServer.sqlBefehlsListe.add(new SQLData("Select picture from user where username like '"+this.user+"'",'p',this.user));
		
		
		
		
		
		
		boolean nichtErneutEinfügen=false;
		for(WorkerRunnableRead wr: MultiThreadedServer.AngemeldeteWorkerRunnableRead){
		    if(wr.user.equalsIgnoreCase(user)){
			nichtErneutEinfügen=true;
		    }
		    
		
		}
		
		if(nichtErneutEinfügen==false){
		    MultiThreadedServer.AngemeldeteWorkerRunnableRead
			.add(this);
		}
		ServerStart.SystemWriteLogln("!!! Anzahl der bestehenden Verbindungen zu Usern(size der workerrunnableliste): "+MultiThreadedServer.AngemeldeteWorkerRunnableRead.size());
	
		
		
		
	    
	    }
	    if(this.user.equals("Admin")){MultiThreadedServer.sqlBefehlsListe.add(new SQLData("SELECT * FROM user",'n')); }

	    if(this.user.contains("Anmelder")==false){
		
		
		 if(this.user.contains("Admin")==false){
		
		MultiThreadedServer.sqlBefehlsListe.add(new SQLData(
			//"SELECT username,status from user where username not like '"+ this.user+ "' order by username"
			"SELECT c.contact,status, u.picture from user u, contacts c where c.contact=u.username and c.username like '"+ this.user + "' order by c.contact"
			,'k',this.user));
		
		 }else{
		     
		 
		MultiThreadedServer.sqlBefehlsListe.add(new SQLData(
			"SELECT username,status,picture from user where username not like '"+ this.user+ "' order by username"
			//"SELECT c.contact,u.status, u.picture from user u, contacts c where c.username=u.username and c.username like '"+ this.user + "' order by c.contact"
			,'k',this.user));
		 }
		//MultiThreadedServer.sqlBefehlsListe.add(new SQLData("SELECT status from user where username not like '"+ this.user+ "' order by username",'k',this.user));
	    }
	    
	}
	if (obj instanceof SQLData)
	{
	    sqldata = (SQLData) obj;
	    if(sqldata.typ=='\u0000'){
		if(sqldata.to.equals("AnmeldeDbChecker")){
		    MultiThreadedServer.sqlBefehlsListe.add(new SQLData(sqldata.sqlBefehl,'a'));
		}else{
		    ServerStart.SystemWriteLogln(sqldata.sqlBefehl);

		    MultiThreadedServer.sqlBefehlsListe.add(new SQLData(sqldata.sqlBefehl,'n'));

		}
	    }else{
		MultiThreadedServer.sqlBefehlsListe.add(sqldata);
	    }
	}
	
	if (obj instanceof ByteData){
		ByteData bytedata = (ByteData) obj;
		ServerStart.SystemWriteLogln("1");
		byte[] mybytearray = new byte[1024];
		try {

			ServerStart.SystemWriteLogln("2");
			//is = clientSocket.getInputStream();
			
			String fileName = bytedata.dateiname.substring( bytedata.dateiname.lastIndexOf('/')+1, bytedata.dateiname.length() );

			
			ff = new File(desktopPath + "/"+fileName);//receivedData.png");
			ff.createNewFile();
			ServerStart.SystemWriteLogln("3");
			ServerStart.SystemWriteLogln("Write Received Data to: "
					+ ff.getAbsolutePath());
			
			fos = new FileOutputStream(ff);
			FileChannel fileChannel = fos.getChannel();

		//	DecimalFormat df = new DecimalFormat("0.0000");
			int count;
			boolean uebertragen=false;
			//while ((count = ois.read(mybytearray)) >= 1023) {
			while(uebertragen==false){
				count = ois.read(mybytearray);
				System.out.println("Data Transferred: " + fileChannel.size()
						/ 1024 + " KBytes / "
						+ (bytedata.bytes/1024));
						//+ df.format(fileChannel.size() / 1024.00 / 1024.00)
						//+ " MByte");
				//ServerStart.SystemWriteLogln(ois.read(mybytearray));
				
				if(fileChannel.size()/1024 == bytedata.bytes/1024){
					uebertragen=true;
				}
				fos.write(mybytearray, 0, count);
			}
			ServerStart.SystemWriteLogln("Wrote Received Data to: "
					+ ff.getAbsolutePath());
		} catch (Exception e) {
			ServerStart.SystemWriteLogln(e.getMessage());
			fos.close();
			//is.close();
			//sock.close();

		} finally {
			ServerStart.SystemWriteLogln("Done.");

			fos.close();
			//is.close();
			//sock.close();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	//		Desktop dt = Desktop.getDesktop();
		//	dt.open(ff);
		}
		
		
		
		
		sendBytes(bytedata.from, bytedata.to, ff.getAbsolutePath());
		
		
		
		
		
	}

	// IN PROGRESS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }
    
    public static void sendBytes(String bfrom, String bto, String zuVerschickendeDatei) {
		// TODO Auto-generated method stub
		File myFile = new File(zuVerschickendeDatei.replace('\\', '/'));
		FileInputStream fis;
		long count=0;
		try {
			fis = new FileInputStream(myFile);
			FileChannel fileChannel = fis.getChannel();
			count = fileChannel.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//os = sock.getOutputStream();
		
		
		
		MultiThreadedServer.byteList.add(new ByteData(bfrom,bto,zuVerschickendeDatei.replace('\\', '/'),count));
			
			//oos.writeObject(new ByteData("zuVerschickendeDatei"));
		
		
	}
    

}
