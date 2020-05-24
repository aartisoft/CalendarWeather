package com.iexamcenter.calendarweather.kundali;

import android.content.res.Resources;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DashaListAdapter extends RecyclerView.Adapter<DashaListAdapter.ItemViewHolder> {
    Resources res;
    MainActivity mContext;
   static ArrayList<KundaliDashaFrag.dasha> mItems;
    int[] orderOfDasha;
    int[] orderOfDashaYear;
    String[] lplanetList;
   // static Calendar startdate, enddate;
    Calendar dob;
    String DATE_FORMAT_1 = "dd MMMM yyyy";

    public DashaListAdapter(MainActivity context, ArrayList<KundaliDashaFrag.dasha> pglist, String[] lplanetList, Calendar dob) {
        res = context.getResources();
        mContext = context;
        mItems = pglist;

        this.lplanetList = lplanetList;
        orderOfDasha = new int[]{11, 3, 0, 1, 4, 10, 5, 6, 2};
        orderOfDashaYear = new int[]{7, 20, 6, 10, 7, 18, 16, 19, 17};
       // startdate = Calendar.getInstance();
       // enddate = Calendar.getInstance();
        this.dob=dob;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_dashalist;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);


        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
         KundaliDashaFrag.dasha obj=mItems.get(position);
        Calendar startdate = Calendar.getInstance();
        Calendar enddate = Calendar.getInstance();

        String planetLord= lplanetList[obj.planet];
        Log.e("BindViewHolde", "BindViewHolde:POSITIOn:" + position + ":" +planetLord);
        String startDateStr, endDateStr;
        int days=0;

        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat(DATE_FORMAT_1, Locale.ENGLISH);
        String dateStr="";
        enddate.setTimeInMillis(dob.getTimeInMillis());
        days = (int) (mItems.get(0).remYear * (365.2425));
        enddate.add(Calendar.DAY_OF_MONTH, days);
        for(int i=0;i<=position;i++){
            KundaliDashaFrag.dasha obj1 = mItems.get(i);
            if(i==0) {
                startdate = Calendar.getInstance();
                startdate.setTimeInMillis(dob.getTimeInMillis());
                startDateStr = dateFormat.format(startdate.getTime());
                endDateStr = dateFormat.format(enddate.getTime());
                dateStr = startDateStr + "   -    " + endDateStr + "<BR/><font color='#AAAAAA'>"+ yearMonthDay(obj.remYear) + "</font>";

            }
            else{
                days = (int) (obj1.totalyear * (365.2425));

                enddate.add(Calendar.DAY_OF_MONTH, days);

                startdate.setTimeInMillis(enddate.getTimeInMillis() - days * 24 * 60 * 60 * 1000L);
                startDateStr = dateFormat.format(startdate.getTime());
                endDateStr = dateFormat.format(enddate.getTime());
                dateStr = startDateStr + "   -    " + endDateStr + "<BR/><font color='#AAAAAA'>" + obj.totalyear + " years period</font>";
            }


        }



        holder.container.removeAllViews();
        calculateSubDasha(obj, position,startdate, enddate, holder.container);
        // Calendar cal = Calendar.getInstance();


        holder.dasha.setText(planetLord);
        holder.dasha_duration.setText(Html.fromHtml(dateStr));
        if(((String)holder.dashacntr.getTag()).contains("0")){
            holder.container.setVisibility(View.GONE);
            holder.dasha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_right_black_24dp,0,0,0);

        }else{
            holder.container.setVisibility(View.VISIBLE);
            holder.dasha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_down_black_24dp,0,0,0);

        }
        holder.dashacntr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.container.setVisibility(View.VISIBLE);
                if(((String)holder.dashacntr.getTag()).contains("0")){
                    holder.dasha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_down_black_24dp,0,0,0);
                    holder.dashacntr.setTag("1");
                    holder.container.setVisibility(View.VISIBLE);

                }else{
                    holder.dashacntr.setTag("0");
                    holder.dasha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_right_black_24dp,0,0,0);

                    holder.container.setVisibility(View.GONE);
                }
            }
        });

        Log.e("BindViewHolde", "BindViewHolde:E:" + position + ":" + System.currentTimeMillis());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    private void calculateSubDasha(KundaliDashaFrag.dasha obj, int position, Calendar startdate, Calendar mainStartdate, LinearLayout container) {
        boolean start = false;
        // int j=9;
        int index = 0;
        double globalYear = 0;
        for (int i = 0; i < 9; i++) {

            int dashaIndex = orderOfDasha[i];
            int dashaYear = orderOfDashaYear[i];

            if (dashaIndex == obj.planet) {
                index = i;
                break;

            }

        }
        Calendar subDashaStart, subDashaEnd;
        subDashaStart = Calendar.getInstance();
        subDashaStart.setTimeInMillis(mainStartdate.getTimeInMillis() - (long) (obj.totalyear * 365.2425 * 24 * 60 * 60 * 1000));

        subDashaEnd = Calendar.getInstance();
        subDashaEnd.setTimeInMillis(subDashaStart.getTimeInMillis());


        String startDateStr, endDateStr;
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat(DATE_FORMAT_1, Locale.ENGLISH);


        Calendar sStartdate=Calendar.getInstance();
        sStartdate.setTimeInMillis(startdate.getTimeInMillis());
        double elapse=0;// = obj.totalyear - obj.remYear;

        if(position==0){
            sStartdate.setTimeInMillis(mainStartdate.getTimeInMillis() - (long) (obj.totalyear * 365.2425 * 24 * 60 * 60 * 1000));
            elapse = obj.totalyear - obj.remYear;
        }
        boolean isbalance=false;
        for (int j = 0; j < 9; j++) {

            int dashaIndex = orderOfDasha[index];
            int dashaYear = orderOfDashaYear[index];
            double subDasha = (obj.totalyear * dashaYear) / 120.0;
            if (index < 8) {
                index++;
            } else
                index = 0;
            String mainPlanet = lplanetList[obj.planet];
            String subPlanet = lplanetList[dashaIndex];
            globalYear = globalYear + subDasha;

            subDashaStart.setTimeInMillis(sStartdate.getTimeInMillis());
            subDashaEnd.setTimeInMillis(sStartdate.getTimeInMillis() + (long) (subDasha * 365.2425 * 24 * 60 * 60 * 1000));



/*
            subDashaEnd.add(Calendar.DAY_OF_MONTH, (int) (subDasha * 365.2425));
            subDashaStart.setTimeInMillis(subDashaEnd.getTimeInMillis() - (long) (subDasha * 365.2425 * 24 * 60 * 60 * 1000));

*/
            sStartdate.setTimeInMillis(subDashaEnd.getTimeInMillis());

            if ( subDashaEnd.getTimeInMillis()<startdate.getTimeInMillis() && position==0) {
                System.out.println("continue::"+subDashaStart.getTimeInMillis()+"-:"+startdate.getTimeInMillis()+"="+(subDashaStart.getTimeInMillis()-startdate.getTimeInMillis()));

                  continue;
            }else if(position==0 && !isbalance){
                isbalance=true;
              //  sStartdate.setTimeInMillis(startdate.getTimeInMillis());
                subDashaStart.setTimeInMillis(startdate.getTimeInMillis());
                subDasha=(subDashaEnd.getTimeInMillis()-subDashaStart.getTimeInMillis())/(1000.0*60*60*24*365.2425);

            }
     /*       if (subDashaStart.getTimeInMillis() < dob.getTimeInMillis()) {
                double elapseYr = (dob.getTimeInMillis() - subDashaStart.getTimeInMillis()) / (1000 * 60 * 60 * 24 * 365.2425);
                subDashaStart.setTimeInMillis(dob.getTimeInMillis());
                subDasha = subDasha - elapseYr;
            }*/



          // if(position!=0){

           // }


            startDateStr = dateFormat.format(subDashaStart.getTime());
            endDateStr = dateFormat.format(subDashaEnd.getTime());

            View child = mContext.getLayoutInflater().inflate(R.layout.sub_dasha, null);
            TextView subdasha = child.findViewById(R.id.subdasha);
            TextView subdasha_duration = child.findViewById(R.id.subdasha_duration);
            subdasha.setText(mainPlanet + "/" + subPlanet);
            subdasha_duration.setText(startDateStr + "   -    " + endDateStr + "\n" + yearMonthDay(subDasha));
            container.addView(child);
            Log.e("globalYear", mainPlanet + "-" + subPlanet + ":TOTALYEAR:");
        }
    }


    private void calculateSubDasha1(KundaliDashaFrag.dasha obj, int position, Calendar startdate, Calendar mainStartdate, LinearLayout container) {
        boolean start = false;
        // int j=9;
        int index = 0;
        double globalYear = 0;
        for (int i = 0; i < 9; i++) {

            int dashaIndex = orderOfDasha[i];
            int dashaYear = orderOfDashaYear[i];

            if (dashaIndex == obj.planet) {
                index = i;
                break;

            }

        }
        Calendar subDashaStart, subDashaEnd;
        subDashaStart = Calendar.getInstance();
        subDashaStart.setTimeInMillis(mainStartdate.getTimeInMillis() - (long) (obj.totalyear * 365.2425 * 24 * 60 * 60 * 1000));

        subDashaEnd = Calendar.getInstance();
        subDashaEnd.setTimeInMillis(subDashaStart.getTimeInMillis());


        String startDateStr, endDateStr;
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat(DATE_FORMAT_1, Locale.ENGLISH);
        for (int j = 0; j < 9; j++) {

            int dashaIndex = orderOfDasha[index];
            int dashaYear = orderOfDashaYear[index];
            double subDasha = (obj.totalyear * dashaYear) / 120.0;
            if (index < 8) {
                index++;
            } else
                index = 0;
            String mainPlanet = lplanetList[obj.planet];
            String subPlanet = lplanetList[dashaIndex];
            globalYear = globalYear + subDasha;
            double elapse = obj.totalyear - obj.remYear;

            subDashaEnd.add(Calendar.DAY_OF_MONTH, (int) (subDasha * 365.2425));
            subDashaStart.setTimeInMillis(subDashaEnd.getTimeInMillis() - (long) (subDasha * 365.2425 * 24 * 60 * 60 * 1000));

            if (elapse != 0 && globalYear < elapse) {
                continue;
            }
            if (subDashaStart.getTimeInMillis() < dob.getTimeInMillis()) {
                double elapseYr = (dob.getTimeInMillis() - subDashaStart.getTimeInMillis()) / (1000 * 60 * 60 * 24 * 365.2425);
                subDashaStart.setTimeInMillis(dob.getTimeInMillis());
                subDasha = subDasha - elapseYr;
            }

            startDateStr = dateFormat.format(subDashaStart.getTime());
            endDateStr = dateFormat.format(subDashaEnd.getTime());

            View child = mContext.getLayoutInflater().inflate(R.layout.sub_dasha, null);
            TextView subdasha = child.findViewById(R.id.subdasha);
            TextView subdasha_duration = child.findViewById(R.id.subdasha_duration);
            subdasha.setText(mainPlanet + "/" + subPlanet);
            subdasha_duration.setText(startDateStr + "   -    " + endDateStr + "\n" + yearMonthDay(subDasha));
            container.addView(child);
            Log.e("globalYear", mainPlanet + "-" + subPlanet + ":TOTALYEAR:");
        }
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView dasha, dasha_duration;

        public LinearLayout container,dashacntr;

        public ItemViewHolder(View itemView) {
            super(itemView);
            dasha = itemView.findViewById(R.id.dasha);
            dasha_duration = itemView.findViewById(R.id.dasha_duration);
            container = itemView.findViewById(R.id.subdasha);
            dashacntr= itemView.findViewById(R.id.dashacntr);

        }


    }

    public String yearMonthDay(double year) {
        int compYear = (int) year;
        double fracYear = year - compYear;
        double days = fracYear * 365.2425;
        int compMonth = (int) days / 30;
        int remDay = (int) days % 30;
        return compYear + " year(s) " + compMonth + "month(s) " + remDay + " days";

    }
}
