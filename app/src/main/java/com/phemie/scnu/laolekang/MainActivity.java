package com.phemie.scnu.laolekang;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;




public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView topBar;
    private TextView tabPerson;
    private TextView tabMedicine;
    private TextView tabHome;
    private TextView tabHealth;
    private TextView tabCall;

    private FrameLayout ly_content;

    private FirstFragment f1;
    private SecondFragment f2;
    private ThirdFragment f3;
    private FourthFragment f4;
    private  FifthFragment f5;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        bindView();
        onClick(tabHome);//将主页所在fragment设置为启动软件就可以看见
    }

    //UI组件初始化与事件绑定
    private void bindView() {
        topBar = (TextView)this.findViewById(R.id.txt_top);
        tabPerson = (TextView)this.findViewById(R.id.txt_pperson);
        tabMedicine = (TextView)this.findViewById(R.id.txt_mmedicine);
        tabHome = (TextView)this.findViewById(R.id.txt_hhome);
        tabHealth = (TextView)this.findViewById(R.id.txt_hhealth);
        tabCall=(TextView)this.findViewById(R.id.txt_ccall) ;
        ly_content = (FrameLayout) findViewById(R.id.fragment_container);

        tabPerson.setOnClickListener(this);
        tabMedicine.setOnClickListener(this);
        tabHome.setOnClickListener(this);
        tabHealth.setOnClickListener(this);
        tabCall.setOnClickListener(this);

    }

    //重置所有文本的选中状态,按钮的选中情况
    public void selected(){
        tabPerson.setSelected(false);
        tabMedicine.setSelected(false);
        tabHome.setSelected(false);
        tabHealth.setSelected(false);
        tabCall.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(f1!=null){
            transaction.hide(f1);
        }
        if(f2!=null){
            transaction.hide(f2);
        }

        if(f3!=null){
            transaction.hide(f3);
        }

        if(f4!=null){
            transaction.hide(f4);
        }
        if(f5!=null){
            transaction.hide(f5);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch(v.getId()){
            case R.id.txt_pperson:
                selected();
                tabPerson.setSelected(true);
                if(f1==null){
                    f1 = new FirstFragment();
                    transaction.add(R.id.fragment_container,f1);
                }else{
                    transaction.show(f1);
                }
                break;

            case R.id.txt_mmedicine:
                selected();
                tabMedicine.setSelected(true);

                if(f2==null){
                    f2 = new SecondFragment();
                    transaction.add(R.id.fragment_container,f2);

                }else{
                    transaction.show(f2);
                }
                break;



            case R.id.txt_hhome:
                selected();
                tabHome.setSelected(true);
                if(f3==null){
                    f3 = new ThirdFragment();
                    transaction.add(R.id.fragment_container,f3);
                }else{
                    transaction.show(f3);
                }
                break;

            case R.id.txt_hhealth:
                selected();
                tabHealth.setSelected(true);
                if(f4==null){
                    f4 = new FourthFragment();
                    transaction.add(R.id.fragment_container,f4);
                }else{
                    transaction.show(f4);
                }
                break;

            case R.id.txt_ccall:
                selected();
                tabCall.setSelected(true);
                if(f5==null){
                    f5 = new FifthFragment();
                    transaction.add(R.id.fragment_container,f5);
                }else{
                    transaction.show(f5);
                }
                break;
        }

        transaction.commit();
    }
}
