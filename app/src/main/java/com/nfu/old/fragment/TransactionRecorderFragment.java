package com.nfu.old.fragment;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nfu.old.R;
import com.nfu.old.adapter.PolicyListAdapter;
import com.nfu.old.adapter.RecorderListAdapter;
import com.nfu.old.manager.ApiManager;
import com.nfu.old.model.Balance;
import com.nfu.old.model.NewsModel;
import com.nfu.old.model.Recorders;
import com.nfu.old.utils.LogUtil;
import com.nfu.old.utils.ToastUtil;
import com.nfu.old.view.ButtonExtendM;
import com.nfu.old.view.DoubleDatePickerDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017-7-27.
 */

public class TransactionRecorderFragment extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    @BindView(R.id.top_title)
    TextView tv_title;
    @BindView(R.id.et_carnum)
    EditText ed_cardnum;
    @BindView(R.id.btn_query)
    Button btn_query;

    @BindView(R.id.tv_btime)
    TextView tv_btime;
    @BindView(R.id.tv_etime)
    TextView tv_etime;
    @BindView(R.id.recoder_recyclerview)
    XRecyclerView recorde_xrecyclerview;

    private RecorderListAdapter recorderListAdapter;

    private final static int REFRESH_TYPE = 1001;
    private final static int LOADMORE_TYPE = 1002;

    private int currentPage =1;
    private int pageSize = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater, R.layout.transaction_recorder_fragment,container);
        initView();
        loadData();
        return rootView;
    }

    @Override
    protected void loadData() {
        Bundle bundle = getArguments();

    }

    @Override
    protected void initView() {
        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        tv_title.setText("消费记录");

        tv_btime.setOnClickListener(this);
        tv_etime.setOnClickListener(this);
        btn_query.setOnClickListener(this);

        recorde_xrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recorderListAdapter = new RecorderListAdapter(getContext(),null);
        recorde_xrecyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recorde_xrecyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recorde_xrecyclerview.setAdapter(recorderListAdapter);
        // policy_recyclerview.addItemDecoration(new MyItemDecoration(getContext(),MyItemDecoration.VERTICAL_LIST));
        recorde_xrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                LogUtil.i("policy_recyclerview--->onRefresh");
                String num = ed_cardnum.getText().toString();
                String btime = tv_btime.getText().toString();
                String etime = tv_etime.getText().toString();
                if (TextUtils.isEmpty(num)||TextUtils.isEmpty(btime)||TextUtils.isEmpty(etime)){
                    ToastUtil.showShortToast(getContext(),"请输入相应的查询条件");
                }else {
                    getRecorderDatas(num,1,pageSize,btime,etime,REFRESH_TYPE);
                }

            }

            @Override
            public void onLoadMore() {
                LogUtil.i("policy_recyclerview--->onRefresh");
                String num = ed_cardnum.getText().toString();
                String btime = tv_btime.getText().toString();
                String etime = tv_etime.getText().toString();
                if (TextUtils.isEmpty(num)||TextUtils.isEmpty(btime)||TextUtils.isEmpty(etime)){
                    ToastUtil.showShortToast(getContext(),"请输入相应的查询条件");
                }else {
                    getRecorderDatas(num,currentPage,pageSize,btime,etime,REFRESH_TYPE);
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_btime:
                showBDateDialog();
                break;
            case R.id.tv_etime:
                showEDateDialog();
                break;
            case R.id.btn_query:
                String num = ed_cardnum.getText().toString();
                String btime = tv_btime.getText().toString();
                String etime = tv_etime.getText().toString();
                if (TextUtils.isEmpty(num)||TextUtils.isEmpty(btime)||TextUtils.isEmpty(etime)){
                    ToastUtil.showShortToast(getContext(),"请输入相应的查询条件");
                }else {
                    getRecorderDatas(num,1,pageSize,btime,etime,REFRESH_TYPE);
                }

                break;
        }
    }

    private void getRecorderDatas(String cnum, int page, final int pagesize, String btime, String etime, final int type){
        ApiManager.getInstance().getOlderExpenseCalendar(cnum, page+"", pagesize+"", btime, etime, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("TransactionRecorderFragment--->getRecorderDatas--->"+e);
                ToastUtil.showShortToast(getContext(), R.string.str_errorinfo);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i("TransactionRecorderFragment--->getRecorderDatas--->onResponse"+response);
                Recorders recorders = new Gson().fromJson(response, Recorders.class);
                if (type == REFRESH_TYPE){
                    if (recorders!=null&&recorders.getItems()!=null){
                        recorderListAdapter.setNewsData(recorders.getItems());
                    }
                    currentPage = 2;
                    recorde_xrecyclerview.refreshComplete();
                }else {
                    if (recorders!=null&&recorders.getItems()!=null){
                        double totalPage = recorders.getTotal() / pagesize;
                        LogUtil.i("TransactionRecorderFragment--->getRecorderDatas--->totalpage:"+totalPage+",currentpage:"+currentPage);
                        if (currentPage<totalPage+1){
                            currentPage++;
                            recorderListAdapter.addNewsData(recorders.getItems());
                        }

                    }

                    recorde_xrecyclerview.loadMoreComplete();
                }

            }
        });
    }

    private void showBDateDialog(){
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //选中事件回调
                tv_btime.setText(getTime(date));
            }
        }).setTitleText("开始时间").setType(new boolean[]{true,true,true,false,false,false})
        .build();

        //pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    private void showEDateDialog(){
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //选中事件回调
                tv_etime.setText(getTime(date));
            }
        }).setTitleText("结束时间").setType(new boolean[]{true,true,true,false,false,false})
                .build();

        //pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
