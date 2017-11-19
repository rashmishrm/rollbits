package com.sjsu.rollbits.intercluster.sync;

import java.util.Date;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RollBitsTimerTask extends TimerTask {
	protected Logger logger = LoggerFactory.getLogger("RollBitsTimerTask");

	private long waitTime;

	RollBitsTimerTask(long waitTime) {
		this.waitTime = waitTime;
	}

	@Override
	public void run() {
		System.out.println("Timer task started at:" + new Date());
		completeTask();
		System.out.println("Timer task finished at:" + new Date());
	}

	private void completeTask() {
		logger.info("Waiting.. for some time for getting all clusters.");
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			logger.error("Error while waiting");

		}
	}
}
