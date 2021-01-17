package co.aurasphere.aura.common.dto.base;

import java.io.Serializable;

/**
 * Aura base request used for web-services calls.
 * @author Donato Rimenti
 * @date 04/mag/2016
 */
public class AuraBaseRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Channel channel;

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

}
