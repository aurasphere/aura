package co.aurasphere.aura.social.facebook;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import co.aurasphere.aura.common.module.ParallelService;

@Service
public class FacebookParallelService extends ParallelService {

	private int pollingDelay = 30000;
	
	@Inject
	private FacebookController controller;

	public String getServiceName() {
		return "Facebook Dashboard Poller";
	}

	/**
	 * Scans the timeline in order to find new posts.
	 * @throws InterruptedException 
	 */
	public void process() throws Exception {
		controller.scanTimeline();
		Thread.sleep(pollingDelay);
	}

	public int getPollingDelay() {
		return pollingDelay;
	}

	public void setPollingDelay(int pollingDelay) {
		this.pollingDelay = pollingDelay;
	}
}
