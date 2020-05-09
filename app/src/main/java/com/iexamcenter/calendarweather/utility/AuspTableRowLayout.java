package com.iexamcenter.calendarweather.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.iexamcenter.calendarweather.MainActivity;
import com.iexamcenter.calendarweather.R;
import com.iexamcenter.calendarweather.mydata.AuspData;
import com.iexamcenter.calendarweather.mydata.AuspWork;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ViewConstructor")
public class AuspTableRowLayout extends RelativeLayout {


    public TableLayout tableA, tableB, tableC, tableD;
    public HorizontalScrollView horizontalScrollViewB, horizontalScrollViewD;
    public ScrollView scrollViewC, scrollViewD;
    private Context context;
    String mColumnHeader;
    private ArrayList<String> columnList, mHeader;
    private List<TableDataObject> tableDataObjects;
    private int[] headerCellsWidth;
    String[] ausp_work_yes_no;

    public AuspTableRowLayout(Context context, ArrayList<String> columnList, ArrayList<String> header, String columnHeader, String[] ausp_work_yes_no, List<TableDataObject> tableDataObject) {

        super(context);
        this.ausp_work_yes_no = ausp_work_yes_no;

        this.context = context;
        this.mHeader = header;
        this.mColumnHeader = columnHeader;
        this.columnList = columnList;

        this.tableDataObjects = tableDataObject;
        headerCellsWidth = new int[mHeader.size() + 1];
        this.initComponents();
        this.setComponentsId();
        this.setScrollViewAndHorizontalScrollViewTag();
        this.horizontalScrollViewB.addView(this.tableB);

        this.scrollViewC.addView(this.tableC);

        this.scrollViewD.addView(this.horizontalScrollViewD);
        this.horizontalScrollViewD.addView(this.tableD);
        this.addComponentToMainLayout();
        this.addTableRowToTableA();
        this.addTableRowToTableB();
        this.resizeHeaderHeight();
        this.getTableRowHeaderCellWidth();
        this.generateTableC_AndTable_B();
        this.resizeBodyTableRowHeight();
    }



    private void initComponents() {
        this.tableA = new TableLayout(this.context);
        this.tableB = new TableLayout(this.context);
        this.tableC = new TableLayout(this.context);
        this.tableD = new TableLayout(this.context);

        this.horizontalScrollViewB = new MyHorizontalScrollView(this.context);
        this.horizontalScrollViewD = new MyHorizontalScrollView(this.context);
        horizontalScrollViewB.setVerticalScrollBarEnabled(false);
        horizontalScrollViewB.setHorizontalScrollBarEnabled(false);

        this.scrollViewC = new MyScrollView(this.context);
        this.scrollViewD = new MyScrollView(this.context);
        scrollViewC.setVerticalScrollBarEnabled(false);
        scrollViewC.setHorizontalScrollBarEnabled(false);
        scrollViewD.setVerticalScrollBarEnabled(false);
        scrollViewD.setHorizontalScrollBarEnabled(false);

    }

    private void setComponentsId() {
        this.tableA.setId(R.id.inquiry_id1);
        this.horizontalScrollViewB.setId(R.id.inquiry_id2);
        this.scrollViewC.setId(R.id.inquiry_id3);
        this.scrollViewD.setId(R.id.inquiry_id4);
    }

    private void setScrollViewAndHorizontalScrollViewTag() {

        this.horizontalScrollViewB.setTag("horizontal scroll view b");
        this.horizontalScrollViewD.setTag("horizontal scroll view d");

        this.scrollViewC.setTag("scroll view c");
        this.scrollViewD.setTag("scroll view d");
    }

    private void addComponentToMainLayout() {
        LayoutParams componentB_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableA.getId());

        LayoutParams componentC_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, this.tableA.getId());

        LayoutParams componentD_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, this.scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, this.horizontalScrollViewB.getId());
        this.addView(this.tableA);
        this.addView(this.horizontalScrollViewB, componentB_Params);
        this.addView(this.scrollViewC, componentC_Params);
        this.addView(this.scrollViewD, componentD_Params);

    }


    private void addTableRowToTableA() {
        this.tableA.addView(this.componentATableRow());
    }

    private void addTableRowToTableB() {
        this.tableB.addView(this.componentBTableRow());
    }

    TableRow componentATableRow() {

        TableRow componentATableRow = new TableRow(this.context);
        TextView textView = this.headerTextView(mColumnHeader);
          // int bgClr = context.getResources().getColor(R.color.dellBlue);
      //  int txtClr = context.getResources().getColor(R.color.orange);
        textView.setTypeface(Typeface.DEFAULT);
        textView.setBackgroundResource(R.drawable.border3);
        textView.setTextColor(Color.parseColor("#FFAB00"));
        int px = Helper.getInstance().dpToPx(10, context);
        textView.setPadding(0, px, 0, px);
        componentATableRow.addView(textView);
        return componentATableRow;
    }

    TableRow componentBTableRow() {

        TableRow componentBTableRow = new TableRow(this.context);
        int headerFieldCount = this.mHeader.size();

        TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 0);

        int txtClr = context.getResources().getColor(R.color.orange);
        for (int x = 0; x < headerFieldCount; x++) {

            TextView textView = this.headerTextView(mHeader.get(x));
            textView.setLayoutParams(params);
            textView.setBackgroundResource(R.drawable.border3);
            textView.setTextColor(txtClr);


            textView.setTypeface(Typeface.DEFAULT_BOLD);
            componentBTableRow.addView(textView);
        }

        return componentBTableRow;
    }

    private void generateTableC_AndTable_B() {
        int slNo = 1;
        Log.e("tableDataObject","::tableDataObject:2:"+ this.tableDataObjects.size());
        for (TableDataObject sampleObject : this.tableDataObjects) {

            TableRow tableRowForTableC = this.tableRowForTableC(sampleObject,slNo);
            TableRow taleRowForTableD = this.taleRowForTableD(sampleObject, slNo);

            //  tableRowForTableC.setBackgroundColor(Color.LTGRAY);
            // taleRowForTableD.setBackgroundColor(Color.LTGRAY);

            this.tableC.addView(tableRowForTableC);
            this.tableD.addView(taleRowForTableD);
            slNo++;
        }
    }

    TableRow tableRowForTableC(TableDataObject sampleObject,int slNo) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
        // params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
        int px = Helper.getInstance().dpToPx(10, context);
        TextView textView = this.bodyTextView(columnList.get(slNo - 1));


        if(slNo%2==0){
            textView.setBackgroundColor(Color.parseColor("#EEEEEE"));
        }else{
            textView.setBackgroundResource(R.drawable.border3);
        }

        textView.setOnClickListener(v -> {
            int slno1=(int)v.getTag();
            ArrayList<AuspData> aw = AuspWork.setAuspData();
            AuspData val = aw.get(slno1);

            BottomDialogFragment bottomSheetDialog = BottomDialogFragment.getInstance(val,2);

            bottomSheetDialog.show(((MainActivity) context).getSupportFragmentManager(), "Custom Bottom Sheet");


        });
        textView.setTag((slNo - 1));
        textView.setPadding(px, px, px, px);

      //  textView.setBackgroundResource(R.drawable.border3);


        textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        textView.setTextColor(context.getResources().getColor(R.color.orange));
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowForTableC.addView(textView, params);

        return tableRowForTableC;
    }

    TableRow taleRowForTableD(TableDataObject sampleObject, int slNo) {
        TableRow taleRowForTableD = new TableRow(this.context);
        int loopCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();
        AuspData[] info = sampleObject.valueList1;
        Log.e("tableDataObject","::tableDataObject:1:"+info.length+"::"+loopCount);
        for (int x = 0; x < loopCount; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], LayoutParams.MATCH_PARENT);

            String rowVal = info[x].isTodayAusp() ? ausp_work_yes_no[0] : ausp_work_yes_no[1];

            TextView textViewB = this.bodyTextView(rowVal);
            if(slNo%2==0){
                textViewB.setBackgroundColor(Color.parseColor("#EEEEEE"));
            }else{
                textViewB.setBackgroundResource(R.drawable.border3);
            }

            if (info[x].isTodayAusp()) {
                textViewB.setTextColor(context.getResources().getColor(R.color.orange));
                textViewB.setTypeface(Typeface.DEFAULT_BOLD);
            }
            info[x].setTitle(info[x].getName() + " " + rowVal);

            textViewB.setTag(info[x]);
            textViewB.setOnClickListener(v -> {
                AuspData obj = (AuspData) v.getTag();


                BottomDialogFragment bottomSheetDialog = BottomDialogFragment.getInstance(obj,1);

                bottomSheetDialog.show(((MainActivity) context).getSupportFragmentManager(), "Custom Bottom Sheet");

            });


            taleRowForTableD.addView(textViewB, params);


           // textViewB.setBackgroundResource(R.drawable.border3);


        }

        return taleRowForTableD;
    }


    TextView bodyTextView(String label) {

        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.CENTER);
        int px = Helper.getInstance().dpToPx(10, context);
        bodyTextView.setPadding(px, px, px, px);

        return bodyTextView;
    }

    TextView headerTextView(String label) {

        TextView headerTextView = new TextView(this.context);
        headerTextView.setText(label);
        headerTextView.setGravity(Gravity.CENTER);
        int px = Helper.getInstance().dpToPx(10, context);
        headerTextView.setPadding(px, px, px, px);
        return headerTextView;
    }

    void resizeHeaderHeight() {

        TableRow productNameHeaderTableRow = (TableRow) this.tableA.getChildAt(0);
        TableRow productInfoTableRow = (TableRow) this.tableB.getChildAt(0);

        int rowAHeight = this.viewHeight(productNameHeaderTableRow);
        int rowBHeight = this.viewHeight(productInfoTableRow);

        TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
        int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

        this.matchLayoutHeight(tableRow, finalHeight);
    }

    void getTableRowHeaderCellWidth() {

        int tableAChildCount = ((TableRow) this.tableA.getChildAt(0)).getChildCount();
        int tableBChildCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();


        for (int x = 0; x < (tableAChildCount + tableBChildCount); x++) {

            if (x == 0) {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.tableA.getChildAt(0)).getChildAt(x));
            } else {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.tableB.getChildAt(0)).getChildAt(x - 1));
            }

        }
    }

    void resizeBodyTableRowHeight() {

        int tableC_ChildCount = this.tableC.getChildCount();

        for (int x = 0; x < tableC_ChildCount; x++) {

            TableRow productNameHeaderTableRow = (TableRow) this.tableC.getChildAt(x);
            TableRow productInfoTableRow = (TableRow) this.tableD.getChildAt(x);

            int rowAHeight = this.viewHeight(productNameHeaderTableRow);
            int rowBHeight = this.viewHeight(productInfoTableRow);

            TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
            int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

            this.matchLayoutHeight(tableRow, finalHeight);
        }

    }

    private void matchLayoutHeight(TableRow tableRow, int height) {

        int tableRowChildCount = tableRow.getChildCount();
        if (tableRow.getChildCount() == 1) {

            View view = tableRow.getChildAt(0);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);

            return;
        }
        for (int x = 0; x < tableRowChildCount; x++) {

            View view = tableRow.getChildAt(x);

            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();

            if (!isTheHeighestLayout(tableRow, x)) {
                params.height = height - (params.bottomMargin + params.topMargin);
                return;
            }
        }

    }

    private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {

        int tableRowChildCount = tableRow.getChildCount();
        int heighestViewPosition = -1;
        int viewHeight = 0;

        for (int x = 0; x < tableRowChildCount; x++) {
            View view = tableRow.getChildAt(x);
            int height = this.viewHeight(view);

            if (viewHeight < height) {
                heighestViewPosition = x;
                viewHeight = height;
            }
        }

        return heighestViewPosition == layoutPosition;
    }

    private int viewHeight(View view) {
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    private int viewWidth(View view) {
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }

    class MyHorizontalScrollView extends HorizontalScrollView {

        public MyHorizontalScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            String tag = (String) this.getTag();
            if (tag.equalsIgnoreCase("horizontal scroll view b")) {
                horizontalScrollViewD.scrollTo(l, 0);
            } else {
                horizontalScrollViewB.scrollTo(l, 0);
            }
        }

    }

    class MyScrollView extends ScrollView {

        public MyScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {

            String tag = (String) this.getTag();
            if (tag.equalsIgnoreCase("scroll view c")) {
                scrollViewD.scrollTo(0, t);
            } else {
                scrollViewC.scrollTo(0, t);
            }
        }
    }


}