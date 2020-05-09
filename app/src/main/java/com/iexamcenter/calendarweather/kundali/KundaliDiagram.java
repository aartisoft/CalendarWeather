package com.iexamcenter.calendarweather.kundali;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;

import com.iexamcenter.calendarweather.CalendarWeatherApp;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.utility.Utility;

import java.util.HashMap;

public class KundaliDiagram extends View {

    private Paint paint;
    int width, height;
    String[] houseOwner;
    //String[] houseOwner = new String[]{"ମଙ୍ଗଳ", "ଶୁକ୍ର", "ବୁଧ", "ଚନ୍ଦ୍ର", "ରବି", "ବୁଧ", "ଶୁକ୍ର", "ମଙ୍ଗଳ", "ବୃହସ୍ପତି", "ଶନି", "ଶନି", "ବୃହସ୍ପତି"};

    //  String[] houseOwner = new String[]{"Mars", "Venus", "Mercury", "Moon", "Sun", "Mercury", "Venus", "Mars", "Jupiter", "Saturn", "Saturn", "Jupiter"};
    String[] zodiac;// = new String[]{"Moon", "Mercury", "Venus", "Mars", "Jupiter", "Saturn", "Saturn", "Jupiter", "Mars", "Venus", "Mercury", "Sun"};
    String[] house = new String[]{"10", "9", "8", "7", "6", "5", "4", "3", "2", "1", "12", "11"};
    Resources res;
    float rad3, rad2, rad1, rad11;
    HashMap<Integer, KundaliDiagramFrag.houseinfo> houseinfo;
    String nakshetraTithi, le_lagna;
    Context context;
    int textSizelagna,textSize, offset,mType;
    String le_planet_mangal,le_planet_shukra,le_planet_budha,le_planet_chandra,le_planet_surya,le_planet_gura,le_planet_sani;

    public KundaliDiagram(Context context, int width, int height, HashMap<Integer, KundaliDiagramFrag.houseinfo> houseinfo, String nakshetraTithi, String[] zodiac) {
        super(context);
        res = context.getResources();
        mType = CalendarWeatherApp.isPanchangEng ? 1 : 0;
        if(mType==0){
            le_lagna = res.getString(R.string.l_lagna);
            le_planet_mangal=res.getString(R.string.l_planet_mangal);
            le_planet_shukra=res.getString(R.string.l_planet_shukra);
            le_planet_budha=res.getString(R.string.l_planet_budha);
            le_planet_chandra=res.getString(R.string.l_planet_chandra);
            le_planet_surya=res.getString(R.string.l_planet_surya);
            le_planet_budha=res.getString(R.string.l_planet_budha);
            le_planet_shukra=res.getString(R.string.l_planet_shukra);
            le_planet_mangal=res.getString(R.string.l_planet_mangal);
            le_planet_gura=res.getString(R.string.l_planet_gura);
            le_planet_sani=res.getString(R.string.l_planet_sani);
            le_planet_sani=res.getString(R.string.l_planet_sani);
            le_planet_gura=res.getString(R.string.l_planet_gura);
        }else{
            le_lagna = res.getString(R.string.e_lagna);
            le_planet_mangal=res.getString(R.string.e_planet_mangal);
            le_planet_shukra=res.getString(R.string.e_planet_shukra);
            le_planet_budha=res.getString(R.string.e_planet_budha);
            le_planet_chandra=res.getString(R.string.e_planet_chandra);
            le_planet_surya=res.getString(R.string.e_planet_surya);
            le_planet_budha=res.getString(R.string.e_planet_budha);
            le_planet_shukra=res.getString(R.string.e_planet_shukra);
            le_planet_mangal=res.getString(R.string.e_planet_mangal);
            le_planet_gura=res.getString(R.string.e_planet_gura);
            le_planet_sani=res.getString(R.string.e_planet_sani);
            le_planet_sani=res.getString(R.string.e_planet_sani);
            le_planet_gura=res.getString(R.string.e_planet_gura);
        }

        houseOwner = new String[]{le_planet_mangal,le_planet_shukra,le_planet_budha,le_planet_chandra,le_planet_surya,le_planet_budha,le_planet_shukra,le_planet_mangal
        ,le_planet_gura,le_planet_sani,le_planet_sani,le_planet_gura};

        this.width = width;
        this.height = height;
        this.zodiac = zodiac;
        rad3 = width / 2.01f;
        rad2 = width / 2.3f;
        rad1 = width / 2.6f;
        rad11 = rad1 / 3 + (rad1 / 10);
        this.context = context;
        paint = new Paint();
        paint.setColor(Color.GRAY);
        this.houseinfo = houseinfo;
        this.nakshetraTithi = nakshetraTithi;
        textSize = Utility.getInstance(context).dpToPx(14);
        textSizelagna = Utility.getInstance(context).dpToPx(16);
        offset = Utility.getInstance(context).dpToPx(15);

    }

    @Override
    protected void onDraw(Canvas canvas) {


        canvas.drawColor(Color.WHITE);


        Paint paint = new Paint();
        paint.setStrokeWidth(5f);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(width / 2, height / 2, rad3, paint);
        canvas.drawCircle(width / 2, height / 2, rad2, paint);


        // Paint paint = new Paint();
        paint.setStrokeWidth(5f);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(width / 2, height / 2, rad1, paint);

        double baseDeg = ((Math.PI / 18) + ((Math.PI / 18) / 2));

        double x1, x2, y1, y2;

        Paint paintLine = new Paint();
        paintLine.setColor(Color.RED);
        paintLine.setStrokeWidth(5f);


        double thetha = Math.PI / 2;
        x1 = (width / 2) - rad1 * Math.cos(thetha);
        y1 = (height / 2) - rad1 * Math.sin(thetha);
        x2 = (width / 2) + rad1 * Math.cos(thetha);
        y2 = (height / 2) + rad1 * Math.sin(thetha);
        canvas.drawLine((float) x1, (float) y1, (float) x2, (float) y2, paintLine);


        thetha = -Math.PI / 2;
        x1 = (width / 2) - rad1 * Math.cos(thetha);
        y1 = (height / 2) - rad1 * Math.sin(thetha);
        x2 = (width / 2) + rad1 * Math.cos(thetha);
        y2 = (height / 2) + rad1 * Math.sin(thetha);
        canvas.drawLine((float) x1, (float) y1, (float) x2, (float) y2, paintLine);


        thetha = Math.PI / 3;

        x1 = (width / 2) - rad1 * Math.cos(thetha);
        y1 = (height / 2) - rad1 * Math.sin(thetha);
        x2 = (width / 2) + rad1 * Math.cos(thetha);
        y2 = (height / 2) + rad1 * Math.sin(thetha);
        canvas.drawLine((float) x1, (float) y1, (float) x2, (float) y2, paintLine);

        thetha = -Math.PI / 3;
        x1 = (width / 2) - rad1 * Math.cos(thetha);
        y1 = (height / 2) - rad1 * Math.sin(thetha);
        x2 = (width / 2) + rad1 * Math.cos(thetha);
        y2 = (height / 2) + rad1 * Math.sin(thetha);
        canvas.drawLine((float) x1, (float) y1, (float) x2, (float) y2, paintLine);

        thetha = Math.PI / 6;
        x1 = (width / 2) - rad1 * Math.cos(thetha);
        y1 = (height / 2) - rad1 * Math.sin(thetha);
        x2 = (width / 2) + rad1 * Math.cos(thetha);
        y2 = (height / 2) + rad1 * Math.sin(thetha);
        canvas.drawLine((float) x1, (float) y1, (float) x2, (float) y2, paintLine);


        thetha = -Math.PI / 6;
        x1 = (width / 2) - rad1 * Math.cos(thetha);
        y1 = (height / 2) - rad1 * Math.sin(thetha);
        x2 = (width / 2) + rad1 * Math.cos(thetha);
        y2 = (height / 2) + rad1 * Math.sin(thetha);
        canvas.drawLine((float) x1, (float) y1, (float) x2, (float) y2, paintLine);

        thetha = Math.PI;
        x1 = (width / 2) - rad1 * Math.cos(thetha);
        y1 = (height / 2) - rad1 * Math.sin(thetha);
        x2 = (width / 2) + rad1 * Math.cos(thetha);
        y2 = (height / 2) + rad1 * Math.sin(thetha);
        canvas.drawLine((float) x1, (float) y1, (float) x2, (float) y2, paintLine);


        thetha = -Math.PI;
        x1 = (width / 2) - rad1 * Math.cos(thetha);
        y1 = (height / 2) - rad1 * Math.sin(thetha);
        x2 = (width / 2) + rad1 * Math.cos(thetha);
        y2 = (height / 2) + rad1 * Math.sin(thetha);
        canvas.drawLine((float) x1, (float) y1, (float) x2, (float) y2, paintLine);


        Paint paintRect = new Paint();
        paintRect.setStrokeWidth(5f);
        paintRect.setColor(Color.RED);

        canvas.drawCircle(width / 2, height / 2, rad3 / 6, paintRect);
        houseNumberText(canvas, rad11, house);

        drawNakshetraRashi(canvas, nakshetraTithi.split(" ")[0], 1);
        drawNakshetraRashi(canvas, nakshetraTithi.split(" ")[1], 2);

    }

    public void drawNakshetraRashi(Canvas canvas, String val, int type) {
        Paint tPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        tPaint.setTypeface(Typeface.DEFAULT_BOLD);
        tPaint.setColor(Color.YELLOW);
        tPaint.setTextSize(textSize);
        String tmp = val;
        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(textSize);

        p.getTextBounds(tmp, 0, tmp.length(), bounds);
        int bw = bounds.width() / 2;
        int bh = bounds.height() / 2;


        if (type == 1) {
            canvas.drawText(val, width / 2 - bw, (height / 2) -  .5f * bh, tPaint);
        } else {
            canvas.drawText(val, width / 2 - bw, (height / 2) +  2.5f* bh, tPaint);
        }

        // canvas.drawTextOnPath(val, circle, (float)( dist-bw), 30, tPaint);

    }


    public void houseNumberText(Canvas canvas, float rad, String[] strArr) {
        Path circle = new Path();
        circle.addCircle((width / 2), (height / 2), rad, Path.Direction.CW);
        Paint tPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        tPaint.setTypeface(Typeface.DEFAULT_BOLD);
        tPaint.setColor(Color.RED);
        tPaint.setTextSize(textSize);
        double dist;
        for (int i = 0; i < 12; i++) {
            dist = (i * ((2 * Math.PI * rad) / 12.0)) + ((Math.PI * rad) / 12.0);
            String hno = Utility.getInstance(context).getDayNo(strArr[i]);
            Paint p = new Paint();
            Rect bounds = new Rect();
            p.setTextSize(textSize);

            p.getTextBounds(hno, 0, hno.length(), bounds);
            int bw = bounds.width() / 2;
            if (strArr[i].equalsIgnoreCase("1")) {
                Paint tPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
                tPaint1.setStyle(Paint.Style.FILL_AND_STROKE);
                tPaint1.setTypeface(Typeface.DEFAULT_BOLD);

                tPaint1.setColor(Color.RED);
                tPaint1.setTextSize(textSizelagna);
                Paint p1 = new Paint();
                Rect bounds1 = new Rect();
                p1.setTextSize(textSizelagna);

                p1.getTextBounds(le_lagna, 0, le_lagna.length(), bounds1);
                int bw1 = bounds1.width() / 2;
                int bh1 = bounds1.height();
                canvas.drawTextOnPath(le_lagna, circle, (float) (dist - bw1), offset - bh1, tPaint1);

            }
            canvas.drawTextOnPath(hno, circle, (float) (dist - bw), offset, tPaint);
            KundaliDiagramFrag.houseinfo obj = houseinfo.get((Integer.parseInt(strArr[i]) - 1));
            String planetVals = obj.planetList;
            String[] arrStr = planetVals.split(",");
            for (int k = 0; k < arrStr.length; k++) {
                if (!arrStr[k].isEmpty()) {
                    Paint p1 = new Paint();
                    Rect bounds1 = new Rect();
                    p1.setTextSize(textSize);
                    p1.getTextBounds(arrStr[k], 0, arrStr[k].length(), bounds1);
                    int bw1 = bounds1.width() / 2;
                    Path circle1 = new Path();
                    circle1.addCircle((width / 2), (height / 2), rad1, Path.Direction.CW);
                    double dist1 = (i * ((2 * Math.PI * rad1) / 12.0)) + ((Math.PI * rad1) / 12.0);
                    canvas.drawTextOnPath(arrStr[k], circle1, (float) (dist1 - bw1), (k + 1) * offset, tPaint);
                }
            }


            drawTextval(canvas, zodiac[obj.rashi], rad2, i);
            drawTextval(canvas, houseOwner[obj.houseno], rad3, i);


        }

    }

    public void drawTextval(Canvas canvas, String str, double rad, int i) {
        Paint tPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        tPaint.setTypeface(Typeface.DEFAULT_BOLD);
        tPaint.setColor(Color.RED);
        tPaint.setTextSize(textSize);
        Paint p1 = new Paint();
        Rect bounds1 = new Rect();
        p1.setTextSize(textSize);
        p1.getTextBounds(str, 0, str.length(), bounds1);
        int bw1 = bounds1.width() / 2;
        // int bh1 = bounds1.height() / 2;
        Path circle1 = new Path();
        circle1.addCircle((width / 2), (height / 2), (float) rad, Path.Direction.CW);
        double dist1 = (i * ((2 * Math.PI * rad) / 12.0)) + ((Math.PI * rad) / 12.0);
        canvas.drawTextOnPath(str, circle1, (float) (dist1 - bw1), offset, tPaint);

    }


}