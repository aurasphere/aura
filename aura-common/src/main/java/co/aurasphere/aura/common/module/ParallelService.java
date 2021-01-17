package co.aurasphere.aura.common.module;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ParallelService implements AuraService, Runnable {

	protected static final Logger logger = LoggerFactory
			.getLogger(ParallelService.class);

	protected Thread serviceThread;

	protected ServiceBus serviceBus;

	protected volatile boolean running;

	protected static final int MAX_RESTART_ATTEMPTS = 3;

	private static final int MAX_SECONDS_FAIL_THRESHOLD = 300;

	private long firstFailureTimestamp;

	private int restartAttempt = 0;

	public ServiceBus getServiceBus() {
		return serviceBus;
	}

	public void setServiceBus(ServiceBus serviceBus) {
		this.serviceBus = serviceBus;
	}

	public Thread getServiceThread() {
		return serviceThread;
	}

	public void setServiceThread(Thread serviceThread) {
		this.serviceThread = serviceThread;
	}

	public String getThreadName() {
		return getServiceName() + " Parallel Service";
	}

	public void start() {
		String threadName = getThreadName();
		if (this.serviceThread == null) {
			logger.debug("Thread {} doesn't exist, creating a new one.",
					threadName);
			this.serviceThread = new Thread(this, threadName);
		} else {
			logger.debug("Thread {} exists, restarting.", threadName);
			try {
				this.serviceThread.join();
			} catch (InterruptedException e) {
				logger.error("Exception during Parallel Service start: ", e);
			}
		}
		this.running = true;
		this.serviceThread.start();
		logger.info("Succesfully started ", threadName);
	}

	public void stop() {
		this.running = false;
	}

	public void run() {
		while (running) {
			try {
				process();
			} catch (Exception e) {
				logger.error("Exception during processing of [{}]",
						getThreadName(), e);
				tryRestart();
			}
		}
	}

	/**
	 * Tryes to restart the service after an Exception for
	 * {@link #MAX_RESTART_ATTEMPTS} times before giving up.
	 */
	private void tryRestart() {
		this.restartAttempt++;

		if (this.firstFailureTimestamp == 0) {
			this.firstFailureTimestamp = new Date().getTime();
		} else if (this.firstFailureTimestamp
				+ (MAX_SECONDS_FAIL_THRESHOLD * 1000) < new Date().getTime()) {
			logger.debug(
					"Out of restarting threshold for [{}]. Resetting counters...",
					getThreadName());
			// Out of the threshold, reset the counters.
			this.firstFailureTimestamp = new Date().getTime();
			this.restartAttempt = 1;
		}

		if (this.restartAttempt <= MAX_RESTART_ATTEMPTS) {
			logger.warn(
					"[{}] has failed. Trying to restart it. Attempt No [{}]...",
					getThreadName(), restartAttempt);
			start();
		} else {
			logger.warn(
					"[{}] has reached the failures threshold of [{}] in [{}] seconds. Giving up restarting.",
					getThreadName(), MAX_RESTART_ATTEMPTS,
					MAX_SECONDS_FAIL_THRESHOLD);
			giveUp();
		}

	}

	/**
	 * Aggressively releases the memory.
	 */
	private void giveUp() {
		stop();
		cleanUp();
		this.serviceBus = null;
		this.serviceThread = null;
	}

	/**
	 * This method does nothing in the parent implementation. It's designed to
	 * be overrided by the childs in order to let them clean up their state.
	 */
	protected void cleanUp() {
	}

	protected abstract void process() throws Exception;

}
