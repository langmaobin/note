package com.ywvision.wyvisionhelper.paypal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lib.utils.LogUtil;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;

import static com.ywvision.wyvisionhelper.app.AppConstants.Args.ARG_PAYMENT_AMOUNT;
import static com.ywvision.wyvisionhelper.app.AppConstants.Args.ARG_PAYMENT_INVOICE;
import static com.ywvision.wyvisionhelper.app.AppConstants.Args.ARG_PAYPAL_INVOICE;
import static com.ywvision.wyvisionhelper.app.AppConstants.Args.ARG_PAYPAL_PAYID;
import static com.ywvision.wyvisionhelper.app.AppConstants.PAYPAL_CLIENT_ID;
import static com.ywvision.wyvisionhelper.app.AppConstants.PAYPAL_ENVIRONMENT;
import static com.ywvision.wyvisionhelper.app.AppConstants.ReqCode.REQ_CODE_PAYPAL_PAYMENT;

/**
 * 1. Pass ARG_PAYMENT_AMOUNT as intent
 * 2. Override onPaypalPaymentSuccess() and onPaypalPaymentFailed()
 */
public class PaypalPaymentActivity extends AppCompatActivity {

    protected final int PAYPAL_RESULT_EXCEPTION = 1;
    protected final int PAYPAL_RESULT_CANCELLED = 2;
    protected final int PAYPAL_RESULT_INVALID = 3;

    private String paymentAmount;
    private String paymentInvoice;
    private Gson gson;
    private PayPalConfiguration payPalConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().getExtras() != null) {
            paymentAmount = getIntent().getStringExtra(ARG_PAYMENT_AMOUNT);
            paymentInvoice = getIntent().getStringExtra(ARG_PAYMENT_INVOICE);
        }
        if (paymentAmount == null || PAYPAL_ENVIRONMENT == null || PAYPAL_CLIENT_ID == null) {
            finish();
            return;
        }
        gson = new Gson();
        payPalConfiguration = new PayPalConfiguration()
                .environment(PAYPAL_ENVIRONMENT)
                .clientId(PAYPAL_CLIENT_ID);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        startService(intent);
        makePayment(paymentAmount);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PAYPAL_PAYMENT) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                    if (confirm != null) {
                        String details = confirm.toJSONObject().toString();
                        if (details != null) {
                            PaypalPaymentDetails paymentDetails = gson.fromJson(details, PaypalPaymentDetails.class);
                            if (paymentDetails != null) {
                                onPaypalPaymentSuccess(paymentDetails);
                                return;
                            }
                        }
                    }
                    onPaypalPaymentFailed(PAYPAL_RESULT_EXCEPTION);
                    break;
                case Activity.RESULT_CANCELED:
                    onPaypalPaymentFailed(PAYPAL_RESULT_CANCELLED);
                    break;
                case PaymentActivity.RESULT_EXTRAS_INVALID:
                    onPaypalPaymentFailed(PAYPAL_RESULT_INVALID);
                    break;
            }
        }
    }

    private void makePayment(String amount) {
        makePayment(amount, "SGD", "Amount");
    }

    private void makePayment(String amount, String currency, String label) {
        amount = amount.replaceAll("[^\\d.]", "");
        PayPalPayment payment = new PayPalPayment(new BigDecimal(amount), currency, label, PayPalPayment.PAYMENT_INTENT_SALE);
        payment.invoiceNumber(paymentInvoice);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, REQ_CODE_PAYPAL_PAYMENT);
    }

    protected void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void onPaypalPaymentSuccess(PaypalPaymentDetails paymentDetails) {
        // Do something after payment success...
        LogUtil.logError("paypal", "pay detail-" + paymentDetails.getResponse().toString());
        Intent intent = getIntent();
        intent.putExtra(ARG_PAYPAL_PAYID, paymentDetails.getResponse().getId());
        intent.putExtra(ARG_PAYPAL_INVOICE, paymentInvoice);
        setResult(RESULT_OK, intent);
        finish();
    }

    protected void onPaypalPaymentFailed(int failedResult) {
        switch (failedResult) {
            case PAYPAL_RESULT_EXCEPTION:
//                displayToast("An extremely unlikely failure occurred.");
                break;
            case PAYPAL_RESULT_CANCELLED:
                // displayToast("The user canceled.");
                break;
            case PAYPAL_RESULT_INVALID:
//                displayToast("An invalid Payment or PayPalConfiguration was submitted.");
                break;
        }
        Intent intent = getIntent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

}
