import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Message implements Serializable{
    private static final long serialVersionUID = 7526472295622776147L; 
    private String from;
    private String to;
    private String text;

  
    public Message(String from, String to, String text)
    {
        this.from = from;
        this.to = to;
        this.text = text;
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
