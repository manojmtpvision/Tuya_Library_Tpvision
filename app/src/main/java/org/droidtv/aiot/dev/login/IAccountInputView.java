package org.droidtv.aiot.dev.login;

/**
 * Created by lee on 16/6/3.
 */
public interface IAccountInputView {
    void setCountryInfo(String countryName, String countryNum);

    String getAccount();

    int getMode();
}
