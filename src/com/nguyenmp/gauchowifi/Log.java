package com.nguyenmp.gauchowifi;

import android.content.Context;

import java.io.*;
import java.util.List;

public class Log {
	private static final String LOG_FILE = "com_nguyenmp_gauchowifi_log.json";
	
	public static void save(List<Entry> log, Context context) throws IOException {
		FileOutputStream fileOut = context.openFileOutput(LOG_FILE, Context.MODE_PRIVATE);
		ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
		objectOut.writeObject(log);
		objectOut.close();
	}
	
	public static List<Entry> load(Context context) throws IOException, ClassNotFoundException {
		FileInputStream fileIn = context.openFileInput(LOG_FILE);
		ObjectInputStream objectIn = new ObjectInputStream(fileIn);
		List<Entry> log = (List<Entry>) objectIn.readObject();
		objectIn.close();
		return log;
	}
	
	public static class Entry implements Serializable, Comparable<Entry> {
		public static final int TYPE_VERBOSE = 0, TYPE_ERROR = 1, TYPE_WARNING = 2;
		
		public final long timestamp;
		public final int type;
		public final String message;
		public final String exception;
		
		public Entry(int type, String message, Exception exception, long timestamp) {
			this.type = type;
			this.message = message;
			this.exception = toString(exception);
			this.timestamp = timestamp;
		}
		
		public Entry(int type, String message, String exception, long timestamp) {
			this.type = type;
			this.message = message;
			this.exception = exception;
			this.timestamp = timestamp;
		}
		
		public String toString(Exception exception) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			exception.printStackTrace(pw);
			return sw.toString();
		}

		@Override
		public int compareTo(Entry another) {
			return (int) (another.timestamp - this.timestamp);
		}
	}
	
}
