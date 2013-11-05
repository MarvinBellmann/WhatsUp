package SendData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class StartData implements Serializable {
	private static final long serialVersionUID = 7526472295622776148L;
	public String user;

	public StartData(String user) {
		this.user = user;
		// System.out.println(this.user);
	}

	private void readObject(ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		// always perform the default de-serialization first
		try {
			aInputStream.defaultReadObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println(e);
		}

	}

	private void writeObject(ObjectOutputStream aOutputStream)
			throws IOException {
		// perform the default serialization for all non-transient, non-static
		// fields
		try {
			aOutputStream.defaultWriteObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println(e);
		}
		// new ClearThread(10).start();

	}

}
