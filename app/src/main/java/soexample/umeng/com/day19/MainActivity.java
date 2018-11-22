package soexample.umeng.com.day19;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import soexample.umeng.com.day19.adapter.MyAdapter;
import soexample.umeng.com.day19.bean.CartInfo;


public class MainActivity extends AppCompatActivity implements OnClickListener{
    String url = "http://www.zhaoapi.cn/product/getCarts?uid=71";
    private ExpandableListView elc_show_main;
    private CheckBox cb_allCheck_main;
    private TextView btn_allPrice_main;
    private Button btn_allNumber_main;
    private MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        //得到okhttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {

                final String string = response.body().string();
                runOnUiThread(new Runnable() {

                    private List<CartInfo.DataBean> sellerData;

                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        CartInfo cartInfo = gson.fromJson(string, CartInfo.class);
                        sellerData = cartInfo.getData();

                        //创建适配
                        myAdapter = new MyAdapter(sellerData, MainActivity.this);
                        //调用适配器的借口 来实现回电数据
                        myAdapter.setOnCartListChangeListener(new MyAdapter.OnCartListChangeListener() {

                            @Override
                            public void SellerSelectedChange(int groupPosition) {
                                //先得到 checkbox
                                boolean b = myAdapter.isCurrentSellerAllProductSelected(groupPosition);
                                //改变所有当前商家的选中状态
                                myAdapter.changeCurrentSellerAllProductSelected(groupPosition, !b);
                                myAdapter.notifyDataSetChanged();
                                refreshAllSelectedAndTotalPriceAndTotalNumber();

                            }

                            @Override
                            public void changeCurrentProductSelected(int groupPosition, int childPosition) {
                                myAdapter.changeCurrentProductSelected(groupPosition, childPosition);
                                myAdapter.notifyDataSetChanged();
                                refreshAllSelectedAndTotalPriceAndTotalNumber();
                            }

                            @Override
                            public void ProductNumberChange(int groupPosition, int childPosition, int number) {
                                myAdapter.changeCurrentProductNumber(groupPosition, childPosition, number);
                                myAdapter.notifyDataSetChanged();
                                //刷新底部的方法
                                refreshAllSelectedAndTotalPriceAndTotalNumber();
                            }
                        });

                        elc_show_main.setAdapter(myAdapter);
//
                        for (int i = 0; i < sellerData.size(); i++) {
                            elc_show_main.expandGroup(i);

                        }
                    }
                });
            }
        });
    }





    private void initView() {
        elc_show_main = (ExpandableListView) findViewById(R.id.elc_show_main);
        cb_allCheck_main = (CheckBox) findViewById(R.id.cb_allCheck_main);
        btn_allPrice_main = (TextView) findViewById(R.id.btn_allprice_main);
        btn_allNumber_main = (Button) findViewById(R.id.btn_allNumber_main);
        cb_allCheck_main.setOnClickListener(this);
        btn_allNumber_main.setOnClickListener(this);
    }
    private void  refreshAllSelectedAndTotalPriceAndTotalNumber(){

        boolean allProductsSelected = myAdapter.isAllProductsSelected();
        cb_allCheck_main.setChecked(allProductsSelected);
//计算总金额
        Double totalPrice = myAdapter.calculateTotalPrice();
        btn_allPrice_main.setText("总价：￥"+totalPrice);
        //计算总数量
        int totalNumber = myAdapter.calculateTotalNumber();
        btn_allNumber_main.setText("去结算("+totalNumber+")");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_allNumber_main:

                break;
            case R.id.cb_allCheck_main:
                boolean allProductsSelected = myAdapter.isAllProductsSelected();
                myAdapter.changeAllProductsSelected(!allProductsSelected);
                myAdapter.notifyDataSetChanged();
                //刷新底部的方法
                refreshAllSelectedAndTotalPriceAndTotalNumber();
                break;

        }
    }

}
