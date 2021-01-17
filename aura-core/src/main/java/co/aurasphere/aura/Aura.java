package co.aurasphere.aura;


public class Aura {

	private static Aura instance;

	private Aura() {}

	public static Aura getInstance() {
		if (instance == null) {
			instance = new Aura();
		}
		return instance;
	}

}
