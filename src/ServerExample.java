import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.*; 
import java.net.*;
import java.util.Date;
import java.text.*;
 
public class ServerExample { 
  /*
    - Server benutzt Port 3141,
      liefert zu einem Datum selbiges mit dem Wochentag an den Client
    - jede Client-Anforderung wird von einem Thread des erzeugten Thread-Pools
      behandelt
    - Server-Socket kann mit Strg+C geschlossen werden oder vom Client mit
      dem Wert 'Exit'.
  */
  public static void main(String[] args) throws IOException {
    final ExecutorService pool;
    final ServerSocket serverSocket;
    int port = 3141;
    String var = "C";
    String zusatz;
    if (args.length > 0 )
      var = args[0].toUpperCase();
    if (var == "C") {
      //Liefert einen Thread-Pool, dem bei Bedarf neue Threads hinzugefügt
      //werden. Vorrangig werden jedoch vorhandene freie Threads benutzt.
      pool = Executors.newCachedThreadPool();
      zusatz = "CachedThreadPool";
    } else {
      int poolSize = 4;
      //Liefert einen Thread-Pool für maximal poolSize Threads
      pool = Executors.newFixedThreadPool(poolSize);
      zusatz = "poolsize="+poolSize;
    }
    serverSocket = new ServerSocket(port);
    //Thread zur Behandlung der Client-Server-Kommunikation, der Thread-
    //Parameter liefert das Runnable-Interface (also die run-Methode für t1).
    Thread t1 = new Thread(new NetworkService(pool,serverSocket));
    System.out.println("Start NetworkService(Multiplikation), " + zusatz +
                       ", Thread: "+Thread.currentThread());
    //Start der run-Methode von NetworkService: warten auf Client-request
    t1.start();
//
    //reagiert auf Strg+C, der Thread(Parameter) darf nicht gestartet sein
    Runtime.getRuntime().addShutdownHook(
      new Thread() {
        public void run() {
          System.out.println("Strg+C, pool.shutdown");
          pool.shutdown();  //keine Annahme von neuen Anforderungen
          try {
            //warte maximal 4 Sekunden auf Beendigung aller Anforderungen
            pool.awaitTermination(4L, TimeUnit.SECONDS);
            if (!serverSocket.isClosed()) {
              System.out.println("ServerSocket close");
              serverSocket.close();
            }
          } catch ( IOException e ) { }
          catch ( InterruptedException ei ) { }
        }
      }
    );
//
  }
}
 
//Thread bzw. Runnable zur Entgegennahme der Client-Anforderungen
class NetworkService implements Runnable { //oder extends Thread
  private final ServerSocket serverSocket;
  private final ExecutorService pool;
  public NetworkService(ExecutorService pool,
                        ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
    this.pool = pool;
  }
  public void run() { // run the service
    try {
      //Endlos-Schleife: warte auf Client-Anforderungen
      //Abbruch durch Strg+C oder Client-Anforderung 'Exit',
      //dadurch wird der ServerSocket beendet, was hier zu einer IOException
      //führt und damit zum Ende der run-Methode mit vorheriger Abarbeitung der
      //finally-Klausel.
      while ( true ) {
        /*
         Zunächst wird eine Client-Anforderung entgegengenommen(accept-Methode).
         Der ExecutorService pool liefert einen Thread, dessen run-Methode
         durch die run-Methode der Handler-Instanz realisiert wird.
         Dem Handler werden als Parameter übergeben:
         der ServerSocket und der Socket des anfordernden Clients.
        */
        Socket cs = serverSocket.accept();  //warten auf Client-Anforderung
 
        //starte den Handler-Thread zur Realisierung der Client-Anforderung
        pool.execute(new Handler(serverSocket,cs));
      }
    } catch (IOException ex) {
      System.out.println("--- Interrupt NetworkService-run");
    }
    finally {
      System.out.println("--- Ende NetworkService(pool.shutdown)");
      pool.shutdown();  //keine Annahme von neuen Anforderungen
      try {
        //warte maximal 4 Sekunden auf Beendigung aller Anforderungen
        pool.awaitTermination(4L, TimeUnit.SECONDS);
        if ( !serverSocket.isClosed() ) {
          System.out.println("--- Ende NetworkService:ServerSocket close");
          serverSocket.close();
        }
      } catch ( IOException e ) { }
      catch ( InterruptedException ei ) { }
    }
  }
}
 
//Thread bzw. Runnable zur Realisierung der Client-Anforderungen
class Handler implements Runnable {  //oder 'extends Thread'
  private final Socket client;
  private final ServerSocket serverSocket;
  Handler(ServerSocket serverSocket,Socket client) { //Server/Client-Socket
    this.client = client;
    this.serverSocket = serverSocket;
  }
  public void run() {
    StringBuffer sb = new StringBuffer();
    PrintWriter out = null;
    try {
      // read and service request on client
      System.out.println( "running service, " + Thread.currentThread() );
      out = new PrintWriter( client.getOutputStream(), true );
      BufferedReader bufferedReader = 
        new BufferedReader(
          new InputStreamReader(
            client.getInputStream()));
      char[] buffer = new char[100];
      int anzahlZeichen = bufferedReader.read(buffer, 0, 100); // blockiert bis Nachricht empfangen
      String nachricht = new String(buffer, 0, anzahlZeichen);
      String[] werte = nachricht.split("\\s");  //Trennzeichen: whitespace
      if (werte[0].compareTo("Exit") == 0) {
        out.println("Server ended");
        if ( !serverSocket.isClosed() ) {
          System.out.println("--- Ende Handler:ServerSocket close");
          try {
            serverSocket.close();
          } catch ( IOException e ) { }
        }
      } else {  //normale Client-Anforderung
        for (int i = 0; i < werte.length; i++) {
          String rt = getWday(werte[i]);  //ermittle den Wochentag
          sb.append(rt + "\n");
        }
        sb.deleteCharAt(sb.length()-1);
      }
    } catch (IOException e) {System.out.println("IOException, Handler-run");}
    finally { 
      out.println(sb);  //Rückgabe Ergebnis an den Client
      if ( !client.isClosed() ) {
        System.out.println("****** Handler:Client close");
        try {
          client.close();
        } catch ( IOException e ) { }
      } 
    }
  }  //Ende run
 
  String getWday(String s) {  //Datum mit Wochentag
    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd.MM.yyyy");
    String res="";
    try {
      //Parameter ist vom Typ Date
      res=sdf.format(DateFormat.getDateInstance().parse(s));
    } catch (ParseException p) {}
    return res;
  }
}