package co.aurasphere.aura.nebula.dto;

import co.aurasphere.aura.common.dto.base.AuraBaseRequest;
import co.aurasphere.aura.common.dto.base.Channel;

/**
 * Created by Donato on 01/06/2016.
 */
public class AuraNebulaBaseRequest extends AuraBaseRequest {

    public AuraNebulaBaseRequest(){
        setChannel(Channel.NEBULA);
    }
}
