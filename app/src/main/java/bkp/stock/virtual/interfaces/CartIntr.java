package bkp.stock.virtual.interfaces;

import android.view.View;

public interface CartIntr {
    public void delItem(int position, View v);
    public void addItem(int position,int qty);
    public void subItem(int position,int qty);
    public void editItem(int position);
}
