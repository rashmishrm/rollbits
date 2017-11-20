/**
 * 
 */
package com.sjsu.rollbits.intercluster.sync;

import java.util.concurrent.Callable;

/**
 * @author nishantrathi
 * @param <T>
 *
 */
public class Timer<T> implements Runnable {

	private static long TIMEOUT = 8 * 1000L;
	ResultCollectable<T> resultCollectable;
	/**
	 * 
	 */
	public Timer(ResultCollectable<T> resultCollectable) {
		this.resultCollectable = resultCollectable;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(TIMEOUT);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resultCollectable.timeout();
	}

}
