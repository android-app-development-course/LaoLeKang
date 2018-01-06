package com.phemie.scnu.laolekang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by duhuicheng on 2018/1/5.
 */

public class FirstFragment_SetContact extends AppCompatActivity implements View.OnClickListener{
    private EditText editText;//联系人姓名输入框
    private EditText editText1;//联系人联系方式输入框
    private ImageButton imageButton;//返回按钮
    private Button button;//保存按钮
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_first_set_contact);
        editText=(EditText)findViewById(R.id.firstFragment_setname);
        editText=(EditText)findViewById(R.id.firstFragment_setnumber);
        imageButton=(ImageButton)findViewById(R.id.rreturn);
        button=(Button)findViewById(R.id.save);
        imageButton.setOnClickListener((View.OnClickListener) this);//返回按钮的点击事件
        button.setOnClickListener((View.OnClickListener) this);//保存按钮的点击事件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rreturn:
                finish();
                break;//返回上一级菜单
            case R.id.save:
                //
                //
                //数据库修改操嘴
                finish();
                break;
        }
    }
}
