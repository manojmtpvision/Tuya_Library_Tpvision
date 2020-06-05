package org.droidtv.aiot.dev.personal;


import com.tuya.smart.android.mvp.model.IModel;

/**
 * Created by letian on 15/6/22.
 */
public interface IPersonalInfoModel extends IModel {
    String getNickName();

    String getMobile();

    void reNickName(String nickName);

    void logout();
}
