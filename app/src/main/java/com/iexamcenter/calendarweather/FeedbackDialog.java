package com.iexamcenter.calendarweather;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.iexamcenter.calendarweather.request.HttpRequestObject;
import com.iexamcenter.calendarweather.response.FeedbackResponse;
import com.iexamcenter.calendarweather.retro.ApiUtil;
import com.iexamcenter.calendarweather.utility.Connectivity;
import com.iexamcenter.calendarweather.utility.Constant;
import com.iexamcenter.calendarweather.utility.PrefManager;
import com.iexamcenter.calendarweather.utility.Utility;

import org.json.JSONObject;

import androidx.fragment.app.DialogFragment;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


public class FeedbackDialog extends DialogFragment implements RadioButton.OnClickListener {
    TextView txt1, txt2;
    static MainActivity mContext;
    PrefManager mPref;
    String lang;
    Resources res;
    String feedbackType = "Help";
    String contactType = "Whatsapp";
    static int mCurrPage = 1;
    View rootView;
    EditText msg;
    EditText senderAddress;
    RadioButton radio_bug, radio_help, radio_whatsapp, radio_suggestion, radio_email, radio_phone;

    public static FeedbackDialog newInstance(MainActivity ctx, int currPage) {
        FeedbackDialog f = new FeedbackDialog();
        mContext = ctx;
        mCurrPage = currPage;
        return f;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mContext == null)
            return null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        rootView = inflater.inflate(R.layout.frag_feedback, null);
        mPref = PrefManager.getInstance(mContext);
        lang = mPref.getMyLanguage();
        setlogocntr();
        msg =  rootView.findViewById(R.id.pick_my_city);
        MaterialButton close = rootView.findViewById(R.id.close);
        close.setOnClickListener(v -> FeedbackDialog.this.dismiss());
        senderAddress =  rootView.findViewById(R.id.senderAddress);
        radio_bug =  rootView.findViewById(R.id.radio_bug);
        radio_help = rootView.findViewById(R.id.radio_help);
        radio_suggestion =  rootView.findViewById(R.id.radio_suggestion);
        radio_whatsapp =  rootView.findViewById(R.id.radio_whatsapp);
        radio_email =  rootView.findViewById(R.id.radio_email);
        radio_phone =  rootView.findViewById(R.id.radio_phone);

        radio_bug.setOnClickListener(this);
        radio_help.setOnClickListener(this);
        radio_suggestion.setOnClickListener(this);
        radio_whatsapp.setOnClickListener(this);
        radio_email.setOnClickListener(this);
        radio_phone.setOnClickListener(this);
        LinearLayout send =  rootView.findViewById(R.id.sendCntr);
        MaterialButton sendBtn2 =  rootView.findViewById(R.id.sendBtn2);
        res = mContext.getResources();
        sendBtn2.setOnClickListener(v -> sendFeedBack());
        send.setOnClickListener(view -> sendFeedBack());

        Dialog dialog = new Dialog(mContext, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;

    }

    private void loadData(String profileJson, int api) {
        try {


            HttpRequestObject mReqobject = new HttpRequestObject(mContext);

            JSONObject jsonHeader = mReqobject.getRequestBody(api, profileJson);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (jsonHeader).toString());
            ApiUtil.getIExamCenterBaseURLClass().sendFeedBack(body).enqueue(new Callback<FeedbackResponse>() {
                @Override
                public void onResponse
                        (Call<FeedbackResponse> call, retrofit2.Response<FeedbackResponse> response) {

                }

                @Override
                public void onFailure(Call<FeedbackResponse> call, Throwable t) {
                    //showErrorMessage();
                    Log.d("xxx", "error loading from API");
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_help:
                if (checked)
                    feedbackType = ((RadioButton) view).getText().toString();
                break;
            case R.id.radio_bug:
                if (checked)
                    feedbackType = ((RadioButton) view).getText().toString();
                break;
            case R.id.radio_suggestion:
                if (checked)
                    feedbackType = ((RadioButton) view).getText().toString();
                break;

            case R.id.radio_whatsapp:
                if (checked)
                    contactType = ((RadioButton) view).getText().toString();
                break;
            case R.id.radio_email:
                if (checked)
                    contactType = ((RadioButton) view).getText().toString();
                break;
            case R.id.radio_phone:
                if (checked)
                    contactType = ((RadioButton) view).getText().toString();
                break;
        }
    }

    public void setlogocntr() {


        // int colr=Utility.getInstance(mContext).getPageThemeColors()[mCurrPage];
        // rootView.setBackgroundResource(colr);
        /*
        switch (mCurrPage) {
            case 0:

                rootView.setBackgroundResource(R.drawable.rounded_bg_page1);
                break;
            case 1:
                //setNavItemColor(R.color.page2);
                rootView.setBackgroundResource(R.drawable.rounded_bg_page2);
                break;
            case 2:
                //setNavItemColor(R.color.page3);
                rootView.setBackgroundResource(R.drawable.rounded_bg_page3);
                break;
            case 3:
                //setNavItemColor(R.color.page4);
                rootView.setBackgroundResource(R.drawable.rounded_bg_page4);
                break;
            case 4:
                //setNavItemColor(R.color.page5);
                rootView.setBackgroundResource(R.drawable.rounded_bg_page5);
                break;
            case 5:
                //setNavItemColor(R.color.page6);
                rootView.setBackgroundResource(R.drawable.rounded_bg_page6);
                break;
            case 6:
                //setNavItemColor(R.color.page7);
                rootView.setBackgroundResource(R.drawable.rounded_bg_page7);
                break;
            case 7:
                // setNavItemColor(R.color.page8);
                rootView.setBackgroundResource(R.drawable.rounded_bg_page8);
                break;
            case 8:
                //setNavItemColor(R.color.page9);
                rootView.setBackgroundResource(R.drawable.rounded_bg_page9);
                break;
            default:
                //setNavItemColor(R.color.page1);
                rootView.setBackgroundResource(R.drawable.rounded_bg_page1);
                break;
        }
        */
    }


    private void sendFeedBack() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
        }
        if (!Connectivity.isConnected(mContext)) {
            Toast.makeText(mContext, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!msg.getText().toString().isEmpty()) {
            String feedback = msg.getText().toString() + "\n" + feedbackType + "\n" + contactType + "\n sender contact:" + senderAddress.getText().toString();
            feedback = feedback.replaceAll("'", " ");
            feedback = feedback.replaceAll("\"", " ");
            String json1 = "{'FEEDBACK':'" + feedback + "'}";
            loadData(json1, Constant.FEEDBACK_API);
            msg.setText("");
            Utility.getInstance(mContext).newToast("Thank you for your feedback.");

            FeedbackDialog.this.dismiss();

        } else {
            Utility.getInstance(mContext).newToast("Please add your feedback.");
        }


    }
}



