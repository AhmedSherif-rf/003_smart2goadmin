package com.ntg.sadmin.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

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

// This class used to convert object to serialized String to store in databasis

public class NTGObjectStream extends ByteArrayOutputStream {
	public NTGObjectStream() {

	}

	public NTGObjectStream(Object obj) throws IOException {
		TargetData = obj;
	}

	public Object TargetData;

	public String getData(Object obj) throws Exception {
		TargetData = obj;
		return getData();
	}

	public static String sp = "_";// new String(new byte[] {0});

	public String getData() throws Exception {
		this.reset();
		byte[] data = getCompressData(TargetData);
		return Base64.getEncoder().encodeToString(data);
	}

	public byte[] getCompressData(Object TargetData) throws Exception {
		this.reset();
		GZIPOutputStream gz = new GZIPOutputStream(this);

		ObjectOutputStream oos = new ObjectOutputStream(gz);
		oos.writeObject(TargetData);
		oos.flush();
		oos.close();
		this.close();

		return this.toByteArray();
	}

}