package com.ywvision.wyvisionhelper.paypal;

import com.google.gson.annotations.SerializedName;

public class PaypalClient {

    @SerializedName("environment")
    private String environment;
    @SerializedName("paypal_sdk_version")
    private String paypalSdkVersion;
    @SerializedName("platform")
    private String platform;
    @SerializedName("product_name")
    private String productName;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getPaypalSdkVersion() {
        return paypalSdkVersion;
    }

    public void setPaypalSdkVersion(String paypalSdkVersion) {
        this.paypalSdkVersion = paypalSdkVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}
