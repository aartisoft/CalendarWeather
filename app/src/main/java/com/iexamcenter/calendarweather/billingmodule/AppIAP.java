package com.iexamcenter.calendarweather.billingmodule;

import android.util.Log;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClient.BillingResponseCode;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.iexamcenter.calendarweather.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class AppIAP implements PurchasesUpdatedListener {
    private BillingClient billingClient;
    public static final String SKU_ID = "com.iexamcenter.calendarweather.removeads";
    public static final String SKU_ID1 = "com.iexamcenter.calendarweather.removeadsforcoffee";
    public static final String SKU_ID2 = "com.iexamcenter.calendarweather.removeadsforlunch";
    public static final String SKU_ID4 = "com.iexamcenter.calendarweather.removeadsfordinner";
    public static final String SKU_ID3 = "com.iexamcenter.calendarweather.removeadsfordayoff";
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;

    SkuDetails SKU_ID_Details;

    MainActivity activity;

    public void doIAP(MainActivity mactivity) {
        Log.e("BillingClient", "MyBillingClient:1:mactivity");
        activity = mactivity;
        acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                Log.e("BillingClient", "MyBillingClient:1:Purchase acknowledged");

            }

        };

        billingClient = BillingClient.newBuilder(activity).enablePendingPurchases().setListener(this).build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                Log.e("BillingClient", "MyBillingClient:11:OK");
                if (billingResult.getResponseCode() == BillingResponseCode.OK) {
                    Log.e("BillingClient", "MyBillingClient:1:OK");
                    loadSku();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Log.e("BillingClient", "MyBillingClient:1:FAILED");
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingResponseCode.OK
                && purchases != null) {
            Log.e("BillingClient", "MyBillingClient:1:Updat");
            for (Purchase purchase : purchases) {
                Log.e("BillingClient", "MyBillingClient:1:purchases");

                handlePurchase(purchase);
            }

        } else if (billingResult.getResponseCode() == BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Log.e("BillingClient", "MyBillingClient:1:can");
        } else {
            // Handle any other error codes.
            Log.e("BillingClient", "MyBillingClient:1:err"+billingResult.getResponseCode()+"::"+billingResult.getDebugMessage());
        }
    }

    public void loadSku() {
        List<String> skuList = new ArrayList<>();
      //  skuList.add(SKU_ID1);
       // skuList.add(SKU_ID2);
        //skuList.add(SKU_ID3);
        skuList.add(SKU_ID4);



        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);

        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        // Process the result.

                        Log.e("BillingClient", "MyBillingClient:1:purchases::1:"+billingResult.getResponseCode()+":"+ billingResult.getDebugMessage());
                        Log.e("BillingClient", "MyBillingClient:1:purchases::3:"+skuDetailsList.size());


                        if (billingResult.getResponseCode() == BillingResponseCode.OK && skuDetailsList != null) {
                            for (SkuDetails skuDetails : skuDetailsList) {

                                String sku = skuDetails.getSku();
                                String price = skuDetails.getPrice();
                                Log.e("BillingClient", "MyBillingClient:1:purchases:" + sku + ":" + price);
                                SKU_ID_Details = skuDetails;
                                if (SKU_ID4.equals(sku)) {
                                    buy();
                                    //  premiumUpgradePrice = price;
                                } else if (SKU_ID1.equals(sku)) {
                                    buy();
                                    // gasPrice = price;
                                }


                            }
                            Log.e("BillingClient", "MyBillingClient:1:purchases::2:");

                        }


                    }
                });
    }

    void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            // Grant entitlement to the user.
            Log.e("BillingClient", "MyBillingClient::" + purchase.getOriginalJson());
            Log.e("BillingClient", "MyBillingClient::" + purchase.getSignature());
            // Acknowledge the purchase if it hasn't already been acknowledged.
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
            }
        }
    }

    public void buy() {
        Log.e("BillingClient", "MyBillingClient:1:buy:1");
        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(SKU_ID_Details)
                .build();
        BillingResult responseCode = billingClient.launchBillingFlow(activity, flowParams);


    }
}
