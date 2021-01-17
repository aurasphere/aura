package co.aurasphere.aura.common.dto.base;

import java.io.Serializable;

/**
 * Aura base response wrapper used for web-services calls.
 * @author Donato Rimenti
 * @date 05/mag/2016
 */
public class AuraBaseResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * The operation outcome.
	 */
	protected boolean outcome;
	
	/**
	 * Default outcome is true.
	 */
	public AuraBaseResponse(){
		this.outcome = true;
	}
	
	public boolean isOutcome() {
		return outcome;
	}

	public void setOutcome(boolean outcome) {
		this.outcome = outcome;
	}
	
}
