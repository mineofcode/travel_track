package com.masaga.goyorider.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.masaga.goyorider.R;
import com.masaga.goyorider.forms.OrderStatus;
import com.masaga.goyorider.forms.Orientation;
import com.masaga.goyorider.forms.PendingOrdersView;
import com.masaga.goyorider.forms.PushOrder;
import com.masaga.goyorider.gloabls.Global;
import com.masaga.goyorider.model.model_pending;
import com.masaga.goyorider.model.model_push_order;
import com.masaga.goyorider.model.model_rider_list;
import com.masaga.goyorider.utils.VectorDrawableUtils;
import android.widget.AdapterView.OnItemSelectedListener;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by fajar on 29-May-17.
 */

public class PushOrderAdapter extends RecyclerView.Adapter<PushOrderViewHolder>{

    private List<model_push_order> mFeedList;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;
    Runnable updateTimerMethod;
    private int OrderNo;
    protected boolean stopFlag = false;
    HashMap<String, Runnable> threads = new HashMap<>();

   private ArrayList<model_rider_list> mRiderList;

    Integer RedAlert = 3;
    long timeInMillies = 0L;
    long startTime = 0L;

    private Handler myHandler = new Handler();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
//    SimpleDateFormat sdf=new SimpleDateFormat("hh:mm");
//    String currentTimeString = sdf.format(d);


    public PushOrderAdapter(List<model_push_order> feedList, Orientation orientation, boolean withLinePadding) {
        mFeedList = feedList;
        mOrientation = orientation;
        mWithLinePadding = withLinePadding;

    }

    @Override
    public int getItemViewType(int position) {
        return PendingOrdersView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public PushOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view;

        view = mLayoutInflater.inflate(R.layout.push_order_timeline, parent, false);

        return new PushOrderViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final PushOrderViewHolder holder, final int position) {

        final model_push_order timeLineModel = mFeedList.get(position);

        OrderNo=timeLineModel.ordno.size();

        holder.mOrderID.setText(timeLineModel.ordid+" ("+OrderNo+")");
        holder.mMarchant.setText(timeLineModel.olnm);
        holder.mAddr.setText(timeLineModel.area+", "+timeLineModel.city);
        holder.mTime.setText(timeLineModel.pchtm);
        holder.mCash.setText("₹ "+timeLineModel.totamt+"");


        //Spinner

//        CustomAdapter customAdapter = new CustomAdapter(mContext, RiderId, RiderNames);
//        holder.mRider.setAdapter(customAdapter);




//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item) {
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//
//                View v = super.getView(position, convertView, parent);
//                if (position == getCount()) {
//                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
//                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
//                }
//
//                return v;
//            }
//
//            @Override
//            public int getCount() {
//                return super.getCount()-1; // you dont display last item. It is used as hint.
//            }
//
//        };
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        adapter.add("Rider 1");
//        adapter.add("Rider 2");
//        adapter.add("Rider 3");
//        adapter.add("Rider 4");
//        adapter.add("Rider 5");
//        adapter.add("Select Rider"); //This is the text that will be displayed as hint.
//
//
//        holder.mRider.setAdapter(adapter);
//        holder.mRider.setSelection(adapter.getCount()); //set the hint the default selection so it appears on launch.
//        holder.mRider.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(adapter.getItem(position)!="Select Rider"){
//                    Toast.makeText(mContext,"You have selected "+adapter.getItem(position),Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        mRiderList= PushOrder.populateList();

        ArrayAdapter<model_rider_list> myAdapter = new ArrayAdapter<model_rider_list>(mContext, android.R.layout.simple_spinner_item, mRiderList);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.mRider.setAdapter(myAdapter);



        holder.mRider.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model_rider_list mModal;
                if(!(holder.mRider.getSelectedItem() == null))
                {
                    mModal = (model_rider_list) holder.mRider.getSelectedItem();
                    Toast.makeText(mContext,"You have selected " + mModal.RiderName + "\t Id: " + mModal.RiderId,Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });



        //Including a timer for to show alert
        updateTimerMethod = new Runnable() {


            public void run() {
                Date date2 = null;
                Date date1 = null;

                try {
                    date2 = simpleDateFormat.parse(timeLineModel.pchtm);
                    date1 = simpleDateFormat.parse(simpleDateFormat.format((new Date()).getTime()));
                    long difference = date1.getTime() - date2.getTime();
                    Integer minutes = (int) (difference / (1000 * 60));

                    // If time diffrence is greater than alert time text will blink and tuen to red color

                    if (minutes > RedAlert) {
                        holder.mAlertTime.setTextColor(Color.RED);
                        holder.mAlertTime.setTextColor(Color.RED);
                        Animation anim = new AlphaAnimation(0.4f, 1.0f);
                        anim.setDuration(1000); //You can manage the time of the blink with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        holder.mAlertTime.startAnimation(anim);
                    }
//                    Toast.makeText(mContext," minuts: "+minutes,Toast.LENGTH_SHORT).show();
                    Log.v("minuts", minutes + "");
                    holder.mAlertTime.setText("( " + minutes + " )");
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (!stopFlag)
                    myHandler.postDelayed(this, 2000);


            }
        }


        ;
//            startTime = SystemClock.uptimeMillis();
        threads.put(position + "", updateTimerMethod);
        myHandler.postDelayed(updateTimerMethod, 0);

//        if(TimeDiff>=RedAlert)
//
//    {
//
//
        final int newPosition = holder.getAdapterPosition();

        holder.Btn_Push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View m) {
                Toast.makeText(mContext,timeLineModel.olnm+" Order Pushed!", Toast.LENGTH_SHORT).show();
                mFeedList.remove(newPosition);
                notifyItemRemoved(newPosition);
                notifyItemRangeChanged(newPosition, mFeedList.size());
            }
        });
    }


    public void Kill() {
        Set<Map.Entry<String, Runnable>> s = threads.entrySet();
        Iterator it = s.iterator();
        stopFlag = true;
        while (it.hasNext()) {
            try {

                Map.Entry pairs = (Map.Entry) it.next();
                Runnable r = (Runnable) pairs.getValue();
                myHandler.removeCallbacks(r);
                r = null;
            } catch (Exception x) {

            }
        }
        s =null;
        it =null;
        threads.clear();

    }


    @Override
    public int getItemCount() {
        return (mFeedList != null ? mFeedList.size() : 0);
    }


}