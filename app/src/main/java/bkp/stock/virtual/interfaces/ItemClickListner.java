package bkp.stock.virtual.interfaces;

import android.view.View;

/*
 * its interface which is used as click listner bitween adapter and activities
 * */
public interface ItemClickListner {
    public void onItemClick(int position);
    public void onItemClick1(int position);
    public void onItemClick( int position, View v );
    public void onItemClick( int position, int position1);
}
