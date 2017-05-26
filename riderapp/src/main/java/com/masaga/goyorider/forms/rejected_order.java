package com.masaga.goyorider.forms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.masaga.goyorider.R;
import com.masaga.goyorider.adapters.ComplatedOrderAdapter;
import com.masaga.goyorider.adapters.RejectedOrderAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class rejected_order extends AppCompatActivity {
    private com.masaga.goyorider.adapters.RejectedOrderAdapter mTimeLineAdapter;
    private RecyclerView mRecyclerView;
    private List<PendingModel> mDataList = new ArrayList<>();
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOrientation = Orientation.VERTICAL;
        mWithLinePadding = true;

        setTitle(getResources().getString(R.string.Rejected_Order));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);
//
        initView();
    }

    private LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    private void initView() {
        setDataListItems();
        mTimeLineAdapter = new RejectedOrderAdapter(mDataList, mOrientation, mWithLinePadding);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    private void setDataListItems(){
        mDataList.add(new PendingModel("#198" , "Pizza Hut, Pralhad Nagar", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.ACTIVE,0.00));
        mDataList.add(new PendingModel("#199" , "Pizza Hut, Pralhad Nagar", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,0.00));
        mDataList.add(new PendingModel("#200" , "Pizza Hut, Pralhad Nagar", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,209.00));
        mDataList.add(new PendingModel("#201" , "Pizza Hut, Pralhad Nagar", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,349.00));
        mDataList.add(new PendingModel("#202" , "Pizza Hut, Pralhad Nagar", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,460.50));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu
        switch (item.getItemId()) {
            //When home is clicked
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //  @Override
    // protected void onSaveInstanceState(Bundle savedInstanceState) {
    //   if(mOrientation!=null)
    //         savedInstanceState.putSerializable(MainActivity.EXTRA_ORIENTATION, mOrientation);
    //    super.onSaveInstanceState(savedInstanceState);
    // }

    // @Override
    // protected void onRestoreInstanceState(Bundle savedInstanceState) {
    //    if (savedInstanceState != null) {
    //        if (savedInstanceState.containsKey(MainActivity.EXTRA_ORIENTATION)) {
    //            mOrientation = (Orientation) savedInstanceState.getSerializable(MainActivity.EXTRA_ORIENTATION);
    //        }
    //     }
    //     super.onRestoreInstanceState(savedInstanceState);
    // }
}