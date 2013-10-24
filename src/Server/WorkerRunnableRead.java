package Server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;

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

    public void run() {
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
		   // System.out.println(e2.getMessage());
		    System.out.println("Lösche Workerrunnable aus liste problem!!!! " +e2.getMessage());
		}*/
		
		clientAnwesend=false;
		// System.out.println("Server read Problem.");
		System.out.println("!!! Abmeldung Client: [" + clientIP + " |Port:"+clientPort + "] - schließe Thread; Grund: "+e.getMessage());
		
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
		    System.out.println("ZZZZZZZZZZZZZZZZZZZZZZ size der workerrunnableliste reduziert: "+MultiThreadedServer.AngemeldeteWorkerRunnableRead.size());
			
			
		    }
		}
		}catch(Exception e4){
		    System.out.println("set status problem: " +e.getMessage());
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
	//  System.out.println("X<< Warte auf Message");
	Object obj =  ois.readObject();
	//  System.out.println("<<< Message erhalten");

	if (obj instanceof Message)
	{
	    System.out.print("<<< Nachricht von Cl: ["+clientSocket.getInetAddress() + " |Port:" + clientSocket.getPort()+"] "  );
	    messageIGot = (Message) obj;	
	    
	    if(messageIGot.from.equalsIgnoreCase("Admin") && messageIGot.to.equalsIgnoreCase("ServerDB")){
		System.out.println("|"+messageIGot.toText()+"");
		MultiThreadedServer.sqlBefehlsListe.add(new SQLData(messageIGot.toTextString(),'n'));
		   
	    }else{
		    MultiThreadedServer.messageList.add(messageIGot);		
	    }
	    
	    // System.out.println(messageIGot.toString());
	    //Thread.sleep(15);
	}
	if (obj instanceof StartData)
	{
	    startdata = (StartData) obj;
	    this.user=startdata.user;
	    w.user=startdata.user;
	    System.out.println("*** Client nennt sich: " + this.user);
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
		System.out.println("ZZZZZZZZZZZZZZZZZZZZZZ size der workerrunnableliste: "+MultiThreadedServer.AngemeldeteWorkerRunnableRead.size());
	
		
		
		
	    
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
		    System.out.println(sqldata.sqlBefehl);

		    MultiThreadedServer.sqlBefehlsListe.add(new SQLData(sqldata.sqlBefehl,'n'));

		}
	    }else{
		MultiThreadedServer.sqlBefehlsListe.add(sqldata);
	    }
	}

	// IN PROGRESS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

}
