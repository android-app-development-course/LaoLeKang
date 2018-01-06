package com.phemie.scnu.laolekang.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.phemie.scnu.laolekang.CircleImageView;
import com.phemie.scnu.laolekang.FirstFragment_SetAddress;
import com.phemie.scnu.laolekang.FirstFragment_SetContact;
import com.phemie.scnu.laolekang.MainActivity;
import com.phemie.scnu.laolekang.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class FirstFragment extends Fragment {




    // 各个组件
    private CircleImageView ivPersonalFigure;
    private ListView lvInformation;
    private TextView tvName;

    // 利用数组保存各个图标
    int[] title_icons = {R.drawable.one_address,
            R.drawable.one_contact, R.drawable.one_contact};
    int[] edit_icons = {R.drawable.one_update_address,
            R.drawable.one_add_a_member, R.drawable.one_add_a_member};


    public FirstFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_first, container, false);

        // 初始化各个控件
        ivPersonalFigure = (CircleImageView)v.findViewById(R.id.mysettings_iv_person_figure);
        lvInformation = (ListView)v.findViewById(R.id.mysettings_lv_information);
        tvName = (TextView)v.findViewById(R.id.mysettings_tv_name);

        // 添加事件监听器
        firstFragmentOnClick listener = new firstFragmentOnClick();
        ivPersonalFigure.setOnClickListener(listener);
        lvInformation.setOnItemClickListener(listener);

        // 添加数据适配器
        lvInformation.setAdapter(new mySettingsAdapter());

        return v;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap head;// 头像Bitmap
        CircleImageView touxiang = ivPersonalFigure;

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        setPicToView(head);// 保存在SD卡中
                        touxiang.setImageBitmap(head);// 用ImageButton显示出来
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {

        String path = "/sdcard/myHead/";// sd路径
        String sdStatus = Environment.getExternalStorageState();

        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // 事件监听器函数
    private class firstFragmentOnClick implements View.OnClickListener,
            AdapterView.OnItemClickListener {

        /*private ActivityCompat activity;
        firstFragmentOnClick(ActivityCompat a){
            this.activity = a;
        }*/

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // 当点击头像时，跳转到换头像的界面
                case R.id.mysettings_iv_person_figure:
                    // TODO: 添加跳转函数，并在函数中添加“照相机运行时权限”
                    showTypeDialog();
                    Toast.makeText(getActivity(), "跳转到换头像的界面", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.rreturn:
                    // 跳转到主界面
                    break;
            }
        }

        // 换头像函数
        private void showTypeDialog() {
            //显示对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final AlertDialog dialog = builder.create();
            View view = View.inflate(getActivity(), R.layout.fragment_first_change_head, null);
            TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
            TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
            tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                    //打开文件
                    intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent1, 1);
                    dialog.dismiss();
                }
            });
            tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
                        startActivityForResult(intent2, 2);// 采用ForResult打开
                        dialog.dismiss();
                    }
                    catch(Exception e){
                        Toast.makeText(getActivity(), "调用照相机时发生异常", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.setView(view);
            dialog.show();
        }

        // TODO：需要实现这个函数
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // ListView 被点击时相应的函数
            Intent intent=new Intent();
            if(edit_icons[position]==R.drawable.one_update_address){
                intent.setClass(getActivity(), FirstFragment_SetAddress.class);
            }else if(edit_icons[position]==R.drawable.one_add_a_member){
                intent.setClass(getActivity(), FirstFragment_SetContact.class);
            }
            startActivity(intent);

            // 给出提示信息
            Toast.makeText(getActivity(), "将会跳转到编辑界面", Toast.LENGTH_SHORT).show();
        }


    }

    // ListView 的适配器
    private class mySettingsAdapter extends BaseAdapter {

        String[] titles = {"家庭住址：", "联系人1：", "联系人2："};
        String[] contents = {"华南师范大学", "12345678910", "987654321"};

        // ViewHolder 数据类
        private class ViewHolder{
            ImageView ivTitle;
            TextView tvTitle;
            TextView tvContents;
            ImageView ivAddition;
        }

        public mySettingsAdapter() {
            super();

            saveMySetting();
        }

        @Override
        public int getCount() {
            return (titles != null)? titles.length: 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // 代码优化后的getView函数
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = LayoutInflater.from(
                        getActivity().getApplicationContext()).inflate(R.layout.fragment_first_list_item, parent, false);

                // 初始化Item控件
                holder = new ViewHolder();
                holder.ivTitle = (ImageView)convertView.findViewById(R.id.mysettings_listview_iv_title);
                holder.tvTitle = (TextView)convertView.findViewById(R.id.mysettings_listview_tv_title);
                holder.tvContents = (TextView)convertView.findViewById(R.id.mysettings_listview_tv_content);
                holder.ivAddition = (ImageView)convertView.findViewById(R.id.mysettings_listview_iv_edit);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }

            // 跳转到内容编辑界面


            // 读取数据库获取路径信息
            String settings_path = MainActivity.username + "_settings.txt";
            File file = new File(getActivity().getFilesDir(), settings_path);
            if(!file.exists()){
                try {
                    file.createNewFile();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }

            // 创建读写流并吧内容读取到内存
            try {
                FileInputStream f = getActivity().openFileInput(settings_path);
                byte[] b = new byte[f.available()];
                f.read(b);
                String s = new String(b);

                // 将数据转换为 titles 和contents 两个数组中
                String[] s0 = s.split("\n");
                String[] titles = new String[s0.length], contents = new String[s0.length];
                for(int i=0; i<s0.length; i++)
                {
                    String[] s00 = s0[i].split("：");
                    titles[i] = s00[0] + "：";
                    contents[i] = s00[1];
                }


                // 将数据显示到 ListView 中
                for(int i=0; i<titles.length; i++)
                {
                    holder.ivTitle.setBackgroundResource(title_icons[position]);
                    holder.tvTitle.setText(titles[position]);
                    holder.tvContents.setText(contents[position]);
                    holder.ivAddition.setBackgroundResource(edit_icons[position]);
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }

            // 测试
            /*String[] titles = {"家庭住址：", "联系人1："};
            String[] contents = {"华南师范大学", "12345678910"};
            holder.ivTitle.setBackgroundResource(title_icons[position]);
            holder.tvTitle.setText(titles[position]);
            holder.tvContents.setText(contents[position]);
            holder.ivAddition.setBackgroundResource(edit_icons[position]);*/

            // 返回
            return convertView;
        }


        // 保存用户“个人中心”的数据
        void saveMySetting(){
            String settings_path = MainActivity.username + "_settings.txt";
            File file = new File(getActivity().getFilesDir(), settings_path);
            if(!file.exists()){
                try {
                    file.createNewFile();
                }
                catch(IOException e)
                {

                    e.printStackTrace();
                }
            }

            try{
                FileOutputStream f = getActivity().openFileOutput(settings_path, MODE_PRIVATE);
                for(int i=0; i<titles.length; i++)
                {
                    f.write((titles[i]+contents[i]+"\n").getBytes());
                }
                f.close();
            }
            catch(IOException e)
            {
                Log.i("MainActivity1", "保存用户信息时出现错误");
                e.printStackTrace();
            }
        }
    }
}
