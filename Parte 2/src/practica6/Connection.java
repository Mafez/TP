package practica6;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
	
	private Socket s;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public Connection(Socket s) throws Exception {
		this.s = s;
		this.out = new ObjectOutputStream(s.getOutputStream());
		this.in = new ObjectInputStream(s.getInputStream());
	}
	
	public void sendObject(Object r) throws Exception {
		out.writeObject(r);
		out.flush();
		out.reset();
	}
	
	public Object getObject() throws Exception {
		return in.readObject();
	}
	
	public void stop() throws Exception {
		s.close();
	}
}
