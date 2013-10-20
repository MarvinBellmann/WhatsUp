package Client;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileReceiverThread {
    static String desktopPath=null;
    static String ServerIP ="192.168.178.43";
    static InetAddress Inet;
    
    
    public String toTextString(String kurztext)
    {
	//html to string umformung der nachricht
	//String kurztext=text;
	Pattern p = Pattern.compile("#(.*?)#");
        Matcher m = p.matcher(kurztext);        
        while (m.find()) {             
           kurztext=m.group(1);
        }            
        kurztext = kurztext.replaceAll(System.getProperty("line.separator"), ""); //("\\\n", "");//Replace(Nz(meinString, ""), vbCrLf, "")//string= string.replaceAll("\\\n", "<br />");
        kurztext = kurztext.replaceAll("    ", " ");
        kurztext = kurztext.replaceAll("   ", " ");
        kurztext = kurztext.replaceAll("  ", " ");
        kurztext = kurztext.replaceAll("#", "");
      //  kurztext = kurztext.substring(1);
        
       
	return (kurztext);//+ System.getProperty("line.separator")
    }
    /*
    public String toTextString(String kurztext)
    {
	//html to string umformung der nachricht
	//String kurztext=text;
	Pattern p = Pattern.compile("<(.*?)>");
        Matcher m = p.matcher(kurztext);        
        while (m.find()) {             
           kurztext= kurztext.replaceAll(m.group(), "");
        }            
        kurztext = kurztext.replaceAll(System.getProperty("line.separator"), ""); //("\\\n", "");//Replace(Nz(meinString, ""), vbCrLf, "")//string= string.replaceAll("\\\n", "<br />");
        kurztext = kurztext.replaceAll("    ", " ");
        kurztext = kurztext.replaceAll("   ", " ");
        kurztext = kurztext.replaceAll("  ", " ");
        kurztext = kurztext.replaceAll("  ", " ");
        kurztext = kurztext.substring(1);
        
       
	return (kurztext);//+ System.getProperty("line.separator")
    }*/
    
    public FileReceiverThread(String ServerIP) {
	this.ServerIP = toTextString(ServerIP);
	System.out.println(this.ServerIP);
	/*try {
	    Inet= InetAddress.getByName(this.ServerIP);
	} catch (UnknownHostException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	   // Inet.
	    
	}
	System.out.println(Inet.getAddress());*/
    }

    public void domain() throws IOException {
	
	//if(Inet.getAddress() == null){
	//    Inet= InetAddress.getByName("192.168.178.43");
	//}
	

        try{
           desktopPath = System.getProperty("user.home") + "/Desktop";
            //System.out.print(desktopPath.replace("\\", "/"));
            desktopPath=desktopPath.replace("\\", "/");
           // System.out.println( desktopPath);
            }catch (Exception e){
            System.out.println("Exception caught ="+e.getMessage());
            }
        Socket sock=null;
        try{
       sock = new Socket(this.ServerIP, 7867);
       System.out.println("%%% mit FileServer verbunden");
        }catch(Exception e7){
            e7.printStackTrace();
        }
       
        InputStream is = null;
        FileOutputStream fos = null;
        File ff = null;
        
        
        
        
        
        byte[] mybytearray = new byte[1024];
        try {
           
            is = sock.getInputStream();
            ff = new File( desktopPath+"/receivedData.png");
            System.out.println("Write Received Data to: "+ff.getAbsolutePath());
            fos = new FileOutputStream(ff);
            FileChannel fileChannel = fos.getChannel();
           
            DecimalFormat df = new DecimalFormat("0.0000");
            int count;
            while ((count = is.read(mybytearray)) >= 0) {
        	System.out.println("Data Transferred: " + fileChannel.size()/1024+" KBytes = " + df.format(fileChannel.size()/1024.00/1024.00) + " MByte");
                fos.write(mybytearray, 0, count);
            }
            System.out.println("Wrote Received Data to: "+ff.getAbsolutePath());
        } catch(Exception e){
            System.out.println(e.getMessage());
            fos.close();
            is.close();
            sock.close();
           
        }finally {
            //File f = new File(fos);
           
            System.out.println("Done.");
            
            fos.close();
            is.close();
            sock.close();
            try {
		Thread.sleep(500);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
            Desktop dt = Desktop.getDesktop();
            dt.open(ff);
        }
        
    }

}
