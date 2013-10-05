import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message implements Serializable{
    private static final long serialVersionUID = 7526472295622776147L; 
    String from;
    String to;
    String text;
    Date date;
    

  
    public Message(String from, String to, String text)
    {
        this.from = from;
        this.to = to;
        this.text = text;
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        this.date =  new Date(stamp.getTime());
        
    }

    
    
    private void readObject(
	       ObjectInputStream aInputStream
	     ) throws ClassNotFoundException, IOException {
	       //always perform the default de-serialization first
	       try{
		aInputStream.defaultReadObject();
	       }catch (Exception e) {
		    // TODO Auto-generated catch block
		   // e.printStackTrace();
		   System.out.println(e);
		}
	    

	    }

	     
	      private void writeObject(
	        ObjectOutputStream aOutputStream
	      ) throws IOException {
	        //perform the default serialization for all non-transient, non-static fields
		  try{  
		      aOutputStream.defaultWriteObject();
		  }catch (Exception e) {
			    // TODO Auto-generated catch block
			  //  e.printStackTrace();
		      System.out.println(e);
			}
			  //  new ClearThread(10).start();
			
		   
	        
	      }

    
 
    public String getfrom()
    {
        return from;
    }
    
    public String toString()
    {
	//html to string umformung der nachricht
	String kurztext=text;
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
        
        //rückgabe
	return ("(From: "+from+" |To: " + to +" |Date: " + date + " |"+"Message: " + kurztext+")");//+ System.getProperty("line.separator")
    }

    public String getto()
    {
        return to;
    }

    public String getText()
    {
        return text;
    }

    public void ausgeben()
    {
        System.out.println("Von: " + from);
        System.out.println("An: " + to);
        System.out.println("Text: " + text);
    }
}
