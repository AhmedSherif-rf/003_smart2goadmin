package com.ntg.sadmin.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;

/**
 * <p>
 * Title: Network TeleCome System
 * </p>
 * <p>
 * Description: NTS
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004, NTG-Clarity Network All rights reserved
 * </p>
 * <p>
 * Company: NTG Clarity Egypt Office Co.
 * </p>
 * 
 * @author NTG
 * @version 3.0
 */

public class NTGObjectStreamReader extends ByteArrayInputStream {

	public NTGObjectStreamReader(byte[] SerializationStr) {
		super((SerializationStr == null) ? new byte[] {} : SerializationStr);
		NoRecivedObject = (SerializationStr == null || SerializationStr.length == 0);
	}

	boolean NoRecivedObject;

	public Object CreateObject() throws ClassNotFoundException, IOException {
		return DCompressData();
	}

	public Object CreateObjectFromNonCompressData()
			throws ClassNotFoundException, IOException {
		Object obj = null;
		if (!NoRecivedObject) {

			ObjectInputStream ois = new ObjectInputStream(this);
			obj = ois.readObject();

			ois.close();
			this.close();
		}
		return obj;
	}

	public Object DCompressData() throws IOException, ClassNotFoundException {
		Object obj = null;
		if (!NoRecivedObject) {
			GZIPInputStream gs = new GZIPInputStream(this);
			ObjectInputStream ois = new ObjectInputStream(gs);
			obj = ois.readObject();

			ois.close();
			this.close();
		}
		return obj;
	}

}