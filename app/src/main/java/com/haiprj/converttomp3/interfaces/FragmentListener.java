package com.haiprj.converttomp3.interfaces;

public interface FragmentListener {
    void onConvertDone(Object... object);
    void onConvertFailed(String mess);
}
