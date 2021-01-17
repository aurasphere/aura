package co.aurasphere.aura.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AuraUtils {
	
	public static List makeList(Object object){
		List list = new ArrayList();
		list.add(object);
		return list;
	}

}
