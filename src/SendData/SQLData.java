package SendData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SQLData implements Serializable {
	private static final long serialVersionUID = 7526472295622776148L;
	public String user;
	public String sqlBefehl;
	public String antwort;
	public String to;
	public char typ;

	public SQLData(String sql, char typ) {
		this.sqlBefehl = sql;
		this.typ = typ;

	}

	public SQLData(String sql, char typ, String from) {
		this.sqlBefehl = sql;
		this.typ = typ;
		this.user = from;

	}

	public SQLData(String to, String sql) {
		this.to = to;
		this.sqlBefehl = sql;

	}

	public SQLData(String to, String sql, String antwort) {
		this.to = to;
		this.sqlBefehl = sql;
		this.antwort = antwort;

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
