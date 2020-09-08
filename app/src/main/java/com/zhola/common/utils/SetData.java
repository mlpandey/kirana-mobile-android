package com.zhola.common.utils;

public class SetData {

    public static SetData data = new SetData();
    SendProfileData sendProfileData;

    public void setListener(SendProfileData sendProfileData) {
        this.sendProfileData = sendProfileData;
    }

    public void sendData(int pos) {
        if (sendProfileData != null) {
            sendProfileData.getProfileData(pos);
        }
    }

    public interface SendProfileData {
         void getProfileData(int pos);
    }
}
