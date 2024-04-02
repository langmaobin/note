package com.ywvision.wyvisionhelper.paypal;

import com.google.gson.annotations.SerializedName;

public class PaypalPaymentDetails {

    @SerializedName("client")
    private PaypalClient client;
    @SerializedName("response")
    private PaypalResponse response;
    @SerializedName("response_type")
    private String responseType;

    public PaypalClient getClient() {
        return client;
    }

    public void setClient(PaypalClient client) {
        this.client = client;
    }

    public PaypalResponse getResponse() {
        return response;
    }

    public void setResponse(PaypalResponse response) {
        this.response = response;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

}
