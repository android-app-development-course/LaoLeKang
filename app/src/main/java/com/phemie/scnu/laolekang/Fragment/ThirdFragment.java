package com.phemie.scnu.laolekang.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.phemie.scnu.laolekang.MainActivity;
import com.phemie.scnu.laolekang.R;
import com.phemie.scnu.laolekang.Setting_Main;


public class ThirdFragment extends Fragment {
    private View view;
    private ImageButton imageButton;
    public static String color;
    public ThirdFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_third, container, false);
        // Inflate the layout for this fragment
        imageButton=(ImageButton)view.findViewById(R.id.ssetting);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"打开",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),Setting_Main.class);
                startActivity(intent);
            }
        });
        if(MainActivity.counter==0) {
            color = "blue";
        }
        return view;
    }

    public void onResume() {
        if(color.equals("red")) {
            view.setBackgroundResource(R.drawable.six_redbackground);
        }else if(color.equals("gray")){
            view.setBackgroundResource(R.drawable.six_graybackground);
        } else
        {
            view.setBackgroundResource(R.drawable.background);
        }
        Log.i("color",color);
        super.onResume();
    }//当ThirdFrament重新回到当前页面是调用
    public void onPause() {
        super.onPause();
    }//ThirdFragemt进栈时调用
}
