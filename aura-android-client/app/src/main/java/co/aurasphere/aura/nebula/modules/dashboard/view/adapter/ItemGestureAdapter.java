package co.aurasphere.aura.nebula.modules.dashboard.view.adapter;

/**
 * Created by Donato on 29/05/2016.
 */
public interface ItemGestureAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
