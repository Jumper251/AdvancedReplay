package me.jumper251.replay.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Seralizer {

	  public static byte[] serialize(Object obj)
			    throws IOException
			  {
			    ByteArrayOutputStream out = new ByteArrayOutputStream();
			    ObjectOutputStream os = new ObjectOutputStream(out);
			    os.writeObject(obj);
			    return out.toByteArray();
			  }

			  public static Object deserialize(byte[] data)
			    throws IOException, ClassNotFoundException
			  {
			    ByteArrayInputStream in = new ByteArrayInputStream(data);
			    ObjectInputStream is = new ObjectInputStream(in);
			    return is.readObject();
			  }
}
