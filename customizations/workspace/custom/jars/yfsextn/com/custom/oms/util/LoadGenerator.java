package com.kroger.oms.util;

import org.w3c.dom.Document;

import com.yantra.yfs.japi.YFSEnvironment;

public class LoadGenerator {
	/**
	 * Starts the Load Generation
	 * 
	 * @param args Command line arguments, ignored
	 */
	public static void main(String[] args) {
		int numCore = 1;
		int numThreadsPerCore = 1;
		double load = 0.8;
		final long duration = 100000;
		for (int thread = 0; thread < numCore * numThreadsPerCore; thread++) {
			new BusyThread("Thread" + thread, load, duration).start();
		}
	}
	
	public Document processInput(YFSEnvironment env, Document inDoc) {
		
		int numCore = 1;
		String strNumCore = inDoc.getDocumentElement().getAttribute("NumCore");
		numCore = DataTypeConverter.stringToInt(strNumCore);
		
		
		int numThreadsPerCore = 1;
		String strNumThreadsPerCore = inDoc.getDocumentElement().getAttribute("NumThreadsPerCore");
		numThreadsPerCore = DataTypeConverter.stringToInt(strNumThreadsPerCore);
		
		double load = 0.5;
		String strLoad = inDoc.getDocumentElement().getAttribute("Load");
		load = DataTypeConverter.stringToDouble(strLoad);
		
		long duration = 100000;
		String strDuration = inDoc.getDocumentElement().getAttribute("Duration");
		duration = DataTypeConverter.stringToLong(strDuration);
		
		for (int thread = 0; thread < numCore * numThreadsPerCore; thread++) {
			new BusyThread("Thread" + thread, load, duration).start();
		}
		
		return inDoc;
	}

	/**
	 * Thread that actually generates the given load
	 * 
	 * @author Sriram
	 */
	private static class BusyThread extends Thread {
		private double load;
		private long duration;

		/**
		 * Constructor which creates the thread
		 * 
		 * @param name     Name of this thread
		 * @param load     Load % that this thread should generate
		 * @param duration Duration that this thread should generate the load for
		 */
		public BusyThread(String name, double load, long duration) {
			super(name);
			this.load = load;
			this.duration = duration;
		}

		/**
		 * Generates the load when run
		 */
		@Override
		public void run() {
			long startTime = System.currentTimeMillis();
			try {
				// Loop for the given duration
				while (System.currentTimeMillis() - startTime < duration) {
					// Every 100ms, sleep for the percentage of unladen time
					if (System.currentTimeMillis() % 100 == 0) {
						Thread.sleep((long) Math.floor((1 - load) * 100));
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
