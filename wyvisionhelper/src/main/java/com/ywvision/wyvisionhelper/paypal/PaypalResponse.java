package com.ywvision.wyvisionhelper.paypal;

import com.google.gson.annotations.SerializedName;

public class PaypalResponse {

    @SerializedName("id")
    private String id;
    @SerializedName("intent")
    private String intent;
    @SerializedName("state")
    private String state;
    @SerializedName("create_time")
    private String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
