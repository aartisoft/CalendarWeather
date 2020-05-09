package com.iexamcenter.calendarweather.billingmodule;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.fragment.app.DialogFragment;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.material.button.MaterialButton;
import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.PrefManager;

import java.util.ArrayList;
import java.util.List;
@Keep
public class IAPFragment extends DialogFragment implements PurchasesUpdatedListener {

    PrefManager mPref;
    private BillingClient billingClient;
    public static final String SKU_ID = "com.iexamcenter.calendarweather.removeads";
    public static final String SKU_ID1 = "com.iexamcenter.calendarweather.removeadsforcoffee";
    public static final String SKU_ID2 = "com.iexamcenter.calendarweather.removeadsforlunch";
    public static final String SKU_ID4 = "com.iexamcenter.calendarweather.removeadsfordinner";
    public static final String SKU_ID3 = "com.iexamcenter.calendarweather.removeadsfordayoff";
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;

    SkuDetails SKU_ID_Details;

    static MainActivity activity;
    LinearLayout cntr;

    public static IAPFragment newInstance(MainActivity ctx) {
        activity = ctx;
        return new IAPFragment();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.fragment_iap, null);
        mPref = PrefManager.getInstance(activity);
        MaterialButton sbmtCoupon = rootView.findViewById(R.id.sbmtCoupon);
        EditText coupon = rootView.findViewById(R.id.coupon);


        sbmtCoupon.setOnClickListener(v -> {
            if (coupon.getText().toString().equals("0101202020200101")) {
                mPref.setRemovedAds(true);
                mPref.load();
                Toast.makeText(activity, "Coupon Applied\nYou are an ads free user.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, "Invalid Coupon Code", Toast.LENGTH_LONG).show();
            }
        });
        rootView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        cntr = rootView.findViewById(R.id.cntr);
        acknowledgePurchaseResponseListener = billingResult -> Log.e("BillingClient", "MyBillingClient:1:Purchase acknowledged");

        handleRemoveAds(1,activity);


        Dialog dialog = new Dialog(activity, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    public void handleRemoveAds(int type,MainActivity ctx) {
        System.out.println("IAPFragment::3:");
        activity = ctx;
        mPref = PrefManager.getInstance(activity);
        //1 for show in ui, 0 for only check

        billingClient = BillingClient.newBuilder(activity).enablePendingPurchases().setListener(this).build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                System.out.println("IAPFragment::4:");
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                    loadSku(type);
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                System.out.println("IAPFragment::5:");
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }


    public void loadSku(int type) {
        List<String> skuList = new ArrayList<>();
        skuList.add(SKU_ID);
        skuList.add(SKU_ID1);
        skuList.add(SKU_ID2);
        skuList.add(SKU_ID3);
        skuList.add(SKU_ID4);


        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);



        if (type == 1) {
            System.out.println("IAPFragment::61:");
            queryDetails(params);
        }
        // dont use lamba here, sometime not working
        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP,
                new PurchaseHistoryResponseListener() {

                    @Override
                    public void onPurchaseHistoryResponse(BillingResult billingResult, List<PurchaseHistoryRecord> list) {
                        if (billingResult.getResponseCode() ==BillingClient.BillingResponseCode.OK
                                && list != null) {
                            System.out.println("IAPFragment::61:"+list.size());
                            if(list.size()>0) {
                                Toast.makeText(activity, "  You are a premium member. " + list.size(), Toast.LENGTH_LONG).show();
                                mPref.setRemovedAds(true);
                                mPref.load();
                            }else{
                                Toast.makeText(activity,  "  You are not a premium member. ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

    }

    void queryDetails(SkuDetailsParams.Builder params) {
        System.out.println("IAPFragment::612:");

        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        System.out.println("IAPFragment::6121:");
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                            cntr.removeAllViews();
                            for (SkuDetails skuDetails : skuDetailsList) {
                                System.out.println("IAPFragment::613:");
                                View child = getLayoutInflater().inflate(R.layout.sku_details_row, null);
                                TextView titleTxt = child.findViewById(R.id.title);
                                TextView priceTxt = child.findViewById(R.id.price);
                                TextView descriptionTxt = child.findViewById(R.id.description);
                                MaterialButton state_button = child.findViewById(R.id.state_button);
                                ImageView sku_icon = child.findViewById(R.id.sku_icon);
                                String sku = skuDetails.getSku();
                                String skuType = skuDetails.getType();
                                String priceStr = skuDetails.getPrice();
                                String titleStr = skuDetails.getTitle();
                                String descStr = skuDetails.getDescription();
                                Log.e("BillingClient", "MyBillingClient:1:purchases::--" + skuDetails.getOriginalJson());

                                titleTxt.setText(titleStr);
                                priceTxt.setText(priceStr);
                                descriptionTxt.setText(descStr);
                                state_button.setTag(skuDetails);
                                switch (sku) {
                                    case SKU_ID:
                                        sku_icon.setImageResource(R.drawable.ic_remov_ads);
                                        break;
                                    case SKU_ID1:
                                        sku_icon.setImageResource(R.drawable.ic_ads_coffee);
                                        break;
                                    case SKU_ID2:
                                        sku_icon.setImageResource(R.drawable.ic_ads_lunch);
                                        break;
                                    case SKU_ID3:
                                        sku_icon.setImageResource(R.drawable.ic_ads_dayoff);
                                        break;
                                    case SKU_ID4:
                                        sku_icon.setImageResource(R.drawable.ic_ads_dinner);
                                        break;
                                    default:
                                        sku_icon.setImageResource(R.drawable.ic_remov_ads);
                                        break;


                                }

                                state_button.setOnClickListener(v -> {
                                    SkuDetails skuDetails1 = (SkuDetails) v.getTag();
                                    String sku1 = skuDetails1.getSku();
                                    SKU_ID_Details = skuDetails1;
                                    buy();

                                });


                                cntr.addView(child);


                            }


                        }


                    }
                });



    }

    void handlePurchase(Purchase purchase) {
        Log.e("BillingClient", "MyBillingClient:xxxx:" + purchase.getOriginalJson());
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            // Grant entitlement to the user.
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

        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(SKU_ID_Details)
                .build();
        BillingResult responseCode = billingClient.launchBillingFlow(activity, flowParams);

    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        Log.e("BillingClient", "MyBillingClient:xxxx:y");
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {

                handlePurchase(purchase);
            }

        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase fl
        } else {
            // Handle any other error codes.
        }
    }


}

