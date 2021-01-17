package co.aurasphere.aura.nebula.modules.dashboard.model;

import java.util.List;

/**
 * Created by Donato on 30/05/2016.
 */
public interface DashboardCardListListener {

    public void onCardListChanged(List<DashboardCard> cardList);
}
