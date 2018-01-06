package com.phemie.scnu.laolekang.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.phemie.scnu.laolekang.AddMedicinePage;
import com.phemie.scnu.laolekang.DateInfo;
import com.phemie.scnu.laolekang.Medicine;
import com.phemie.scnu.laolekang.MedicineAlarm;
import com.phemie.scnu.laolekang.MedicineDataHelper;
import com.phemie.scnu.laolekang.R;
import com.phemie.scnu.laolekang.detialFrequency;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;


public class SecondFragment extends Fragment {
    private static final int requestcode = 22220;

    AlarmManager alarmManager;
    DateInfo date = new DateInfo();
    View Fragview;
    Vector<Medicine> medicines;
    Button btn_add;
    ImageButton imbtn_add;
    Button btn_edit;
    ImageButton imbtn_edit;
    Boolean isShow;
    MedicineDataHelper medicineDataHelper;
    private TextView day;
    private TextView month;
    private TextView year;
    private TextView way;
    private ListView medicineList;
    private MyBaseAdapter mAdapter;
    private MyBaseAdapter2 mAdapter2;

    //private Intent mIntent; //用于Service
    // private AlarmConn mconn;//用于实现连接服务
    // private MedicineAlarmService.AlarmBinder binder;

    public SecondFragment() {
        // Required empty public constructor


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Fragview = inflater.inflate(R.layout.fragment_second, container, false);
        Init();
        setListener();
        mAdapter = new MyBaseAdapter();
        medicineList.setAdapter(mAdapter);

        setAlarm();
        return Fragview;
    }

    private void Init() {
        medicineDataHelper = new MedicineDataHelper(getActivity());
        medicines = new Vector<>();
        downloadMedicines();

        day = (TextView) Fragview.findViewById(R.id.day);
        month = (TextView) Fragview.findViewById(R.id.month);
        year = (TextView) Fragview.findViewById(R.id.year);
        way = (TextView) Fragview.findViewById(R.id.way);
        date.getData();
        day.setText(date.getDay());
        month.setText(date.getMonth());
        year.setText(date.getYear());
        way.setText("星期" + date.getWay());

        isShow = true;
        btn_add = (Button) Fragview.findViewById(R.id.btn_add);
        imbtn_add = (ImageButton) Fragview.findViewById(R.id.imageButton_add);
        btn_edit = (Button) Fragview.findViewById(R.id.btn_edit);
        imbtn_edit = (ImageButton) Fragview.findViewById(R.id.imbtn_edit);

        medicineList = (ListView) Fragview.findViewById(R.id.medicine_list);

        /*
        ArrayList<detialFrequency> t = new ArrayList<detialFrequency>();
        t.add(new detialFrequency("上午", "8", "30", "无餐饮说明"));
        medicines.add(new Medicine("999", "粒", R.drawable.pill, R.drawable.medicine_brown, "1", "2017", "12", "12", "2017", "12", "13", "3", t, "1111"));
*/
        // mconn=new AlarmConn();
        // mIntent=new Intent(getActivity(),MedicineAlarmService.class);
        //getActivity().bindService(mIntent,mconn,BIND_AUTO_CREATE);
    }

    private void setListener() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setClass(getActivity(), AddMedicinePage.class);

                startActivityForResult(intent, 2221);
            }
        });
        imbtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setClass(getActivity(), AddMedicinePage.class);

                startActivityForResult(intent, 2221);
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isShow) {
                    btn_edit.setText("完成");
                    imbtn_edit.setVisibility(View.GONE);
                    mAdapter2 = new MyBaseAdapter2();
                    medicineList.setAdapter(mAdapter2);

                } else {
                    btn_edit.setText("编辑");
                    //imbtn_edit.setVisibility(View.INVISIBLE);
                    imbtn_edit.setVisibility(View.VISIBLE);
                    mAdapter = new MyBaseAdapter();
                    medicineList.setAdapter(mAdapter);

                }
                isShow = !isShow;
            }
        });
        imbtn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isShow) {
                    btn_edit.setText("完成");
                    imbtn_edit.setVisibility(View.GONE);
                    mAdapter2 = new MyBaseAdapter2();
                    medicineList.setAdapter(mAdapter2);

                } else {
                    btn_edit.setText("编辑");
                    //imbtn_edit.setVisibility(View.INVISIBLE);
                    imbtn_edit.setVisibility(View.VISIBLE);
                    mAdapter = new MyBaseAdapter();
                    medicineList.setAdapter(mAdapter);

                }
                isShow = !isShow;
            }
        });

        medicineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Medicine medicine = medicines.get(position);
                Intent intent = new Intent(getActivity(), AddMedicinePage.class);
                intent.putExtra("edit", true);
                intent.putExtra("pos", position);
                intent.putExtra("data", medicine);
                startActivityForResult(intent, 2221);

            }
        });
    }

    private void downloadMedicines() {
        SQLiteDatabase db = medicineDataHelper.getReadableDatabase();
        //查询所有信息
        Cursor cursor = db.rawQuery("select * from Medicine", null);
        while (cursor.moveToNext()) {
            List<detialFrequency> list = new ArrayList<detialFrequency>();
            Cursor c = db.rawQuery("select * from DetialFrequency where mid=?", new String[]{"" + cursor.getInt(0)});
            while (c.moveToNext()) {
                String p = c.getString(1);
                String h = c.getString(2);
                String m = c.getString(3);
                String d = c.getString(4);
                list.add(new detialFrequency(p, h, m, d));
            }
            c.close();
            //把信息添加到存储列表中
            medicines.add(new Medicine(cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), list, cursor.getString(13)));

        }

        cursor.close();
        db.close();

    }

    private void deleteMedicine_db(int pos) {
        SQLiteDatabase db = medicineDataHelper.getWritableDatabase();
        Medicine m = medicines.get(pos);
        Cursor cursor = db.rawQuery("select id from Medicine", null);
        if (cursor.moveToNext()) {
            db.delete("DetialFrequency", "mid=?", new String[]{"" + cursor.getInt(0)});
        }
        cursor.close();
        db.delete("Medicine", "name = ? and syear=?and smonth=? and sday=?", new String[]{m.getmName(), m.getmStartyear(), m.getmStartmonth(), m.getmStartday()});
        db.close();
    }

    //创建表Medicine，字段：识别号（id）,药名（name），规格（speci），形状（shape），颜色（color），用量（dosage），开始年（syear），开始月（smonth），开始日（sday）
    //结束年（eyear），结束月（emonth），结束日（eday），频率（frequency），特殊说明（specialill）

    private void addMedicine_db(Medicine m) {
        SQLiteDatabase db = medicineDataHelper.getWritableDatabase();

        //添加数据到数据库
        ContentValues values;
        values = new ContentValues();
        values.put("name", m.getmName());
        values.put("speci", m.getmSpeci());
        values.put("shape", m.getmShape());
        values.put("color", m.getmColor());
        values.put("dosage", m.getmDosage());
        values.put("syear", m.getmStartyear());
        values.put("smonth", m.getmStartmonth());
        values.put("sday", m.getmStartday());
        values.put("eyear", m.getmEndyear());
        values.put("emonth", m.getmEndmonth());
        values.put("eday", m.getmEndday());
        values.put("frequency", m.getmFrequency());
        db.insert("Medicine", null, values);

        //创建表DetialFrequency，字段：识别号（id），时段（period），时（hour），分（minute），餐饮说明（dining），所属药物（mid ， 外键）
        Cursor cursor = db.query("Medicine", new String[]{"id"}, "name = ? and syear=?and smonth=? and sday=?", new String[]{m.getmName(), m.getmStartyear(), m.getmStartmonth(), m.getmStartday()}, null, null, null, null);
        if (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            List<detialFrequency> t = m.getMdetialFrequency();
            for (int i = 0; i < t.size(); i++) {
                values = new ContentValues();
                values.put("period", t.get(i).getPeriod());
                values.put("hour", t.get(i).getHour());
                values.put("minute", t.get(i).getMin());
                values.put("dining", t.get(i).getDiningIllustrate());
                values.put("mid", id);
                db.insert("DetialFrequency", null, values);
            }
        }
        cursor.close();
        db.close();
    }

    private void updateMedicine_db(int pos, Medicine m) {
        deleteMedicine_db(pos);
        addMedicine_db(m);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2221) {
            if (resultCode == 1) {
                Bundle bundle = data.getExtras();
                Medicine t = (Medicine) bundle.get("medicine");
                if (!t.getmName().equals("")) {
                    if (data.getBooleanExtra("edit", false)) {
                        updateMedicine_db(data.getIntExtra("pos", 0), t);
                        medicines.get(data.getIntExtra("pos", 0)).reset(t);
                        updateAlarm(data.getIntExtra("pos", 0));
                    } else {
                        medicines.add(t);
                        addMedicine_db(t);
                        updateAlarm(medicines.size() - 1);
                    }
                    if (isShow)
                        medicineList.setAdapter(new MyBaseAdapter());
                    else
                        medicineList.setAdapter(new MyBaseAdapter2());
                }
            }


        }

    }

    private void updateAlarm(int pos) {
        Medicine m = medicines.get(pos);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Integer.parseInt(date.getYear()), Integer.parseInt(date.getMonth()) - 1, Integer.parseInt(date.getDay()));
        long curt = calendar1.getTimeInMillis();
        calendar1.set(Integer.parseInt(m.getmStartyear()), Integer.parseInt(m.getmStartmonth()) - 1, Integer.parseInt(m.getmStartday()));
        long st = calendar1.getTimeInMillis();
        calendar1.set(Integer.parseInt(m.getmEndyear()), Integer.parseInt(m.getmEndmonth()) - 1, Integer.parseInt(m.getmEndday()));
        long et = calendar1.getTimeInMillis();
        if (st <= curt && curt <= et) {
            List<detialFrequency> d = m.getMdetialFrequency();
            for (int j = 0; j < d.size(); j++) {
                Calendar calendar = Calendar.getInstance();
                int h = Integer.parseInt(d.get(j).getHour());
                if (d.get(j).getPeriod().equals("下午"))
                    h += 12;
                calendar.set(Integer.parseInt(date.getYear()), Integer.parseInt(date.getMonth()) - 1, Integer.parseInt(date.getDay()), h, Integer.parseInt(d.get(j).getMin()), 0);//**月份从0开始计算
                long timer = calendar.getTimeInMillis();

                Date currentTime = new Date();
                if (timer > currentTime.getTime()) {
                    Intent i = new Intent(getActivity(), MedicineAlarm.class);
                    i.putExtra("val", m.getmName());
                    PendingIntent pi = PendingIntent.getActivity(getActivity(), (requestcode + pos) * 100 + j, i, FLAG_UPDATE_CURRENT);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, timer, pi);
                }
            }
        }
    }

    private void setAlarm() {
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);


        for (int k = 0; k < medicines.size(); k++) {
            Medicine m = medicines.get(k);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Integer.parseInt(date.getYear()), Integer.parseInt(date.getMonth()) - 1, Integer.parseInt(date.getDay()));
            long curt = calendar1.getTimeInMillis();
            calendar1.set(Integer.parseInt(m.getmStartyear()), Integer.parseInt(m.getmStartmonth()) - 1, Integer.parseInt(m.getmStartday()));
            long st = calendar1.getTimeInMillis();
            calendar1.set(Integer.parseInt(m.getmEndyear()), Integer.parseInt(m.getmEndmonth()) - 1, Integer.parseInt(m.getmEndday()));
            long et = calendar1.getTimeInMillis();
            if (st <= curt && curt <= et) {
                List<detialFrequency> d = m.getMdetialFrequency();
                for (int j = 0; j < d.size(); j++) {
                    Calendar calendar = Calendar.getInstance();
                    long currentTime = calendar.getTimeInMillis();
                    int h = Integer.parseInt(d.get(j).getHour());
                    if (d.get(j).getPeriod().equals("下午"))
                        h += 12;
                    //calendar.set(Integer.parseInt(date.getYear()), Integer.parseInt(date.getMonth()) - 1, Integer.parseInt(date.getDay()), h, Integer.parseInt(d.get(0).getMin()), 0);//**月份从0开始计算
                    calendar.set(Integer.parseInt(date.getYear()), Integer.parseInt(date.getMonth()) - 1, Integer.parseInt(date.getDay()), h, Integer.parseInt(d.get(j).getMin()), 0);
                    long timer = calendar.getTimeInMillis();

                    if (timer > currentTime) {
                        Intent i = new Intent(getActivity(), MedicineAlarm.class);
                        i.putExtra("val", m.getmName());
                        PendingIntent pi = PendingIntent.getActivity(getActivity(), (requestcode + k) * 100 + j, i, FLAG_UPDATE_CURRENT);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timer, pi);
                    }
                }
            }
        }


    }

    /**
     * 构建ListView适配器1
     */
    class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return medicines.size();
        }

        @Override
        public Object getItem(int pos) {
            return medicines.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            //将设计好的列表项布局转换成view对象
            View view = View.inflate(Fragview.getContext(), R.layout.item_quickshowmedicine, null);
            //显示药物名称
            TextView name = (TextView) view.findViewById(R.id.mshow_name);
            name.setText(medicines.get(pos).getmName());
            //title.setText(medicines.get(pos));
            //显示药物用法
            TextView usage = (TextView) view.findViewById(R.id.mshow_brief);
            usage.setText("每日" + medicines.get(pos).getmFrequency() + "次");
            //显示药物形状
            ImageView shape = (ImageView) view.findViewById(R.id.mshow_shape);
            shape.setBackgroundResource(medicines.get(pos).getmShape());
            //显示药物颜色
            ImageView color = (ImageView) view.findViewById(R.id.mshow_color);
            color.setBackgroundResource(medicines.get(pos).getmColor());

            return view;
        }
    }

    /**
     * 构建ListView适配器2
     */
    class MyBaseAdapter2 extends BaseAdapter {
        @Override
        public int getCount() {
            return medicines.size();
        }

        @Override
        public Object getItem(int pos) {
            return medicines.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(final int pos, View convertView, ViewGroup parent) {
            //将设计好的列表项布局转换成view对象
            View view = View.inflate(Fragview.getContext(), R.layout.item_quickdeletemedicine, null);
            //显示药物名称
            TextView name = (TextView) view.findViewById(R.id.mdel_name);
            name.setText(medicines.get(pos).getmName());
            //title.setText(medicines.get(pos));
            //显示药物用法
            TextView usage = (TextView) view.findViewById(R.id.mdel_brief);
            usage.setText("每日" + medicines.get(pos).getmFrequency() + "次");
            //显示药物形状
            ImageView shape = (ImageView) view.findViewById(R.id.mdel_shape);
            shape.setBackgroundResource(medicines.get(pos).getmShape());
            //显示药物颜色
            ImageView color = (ImageView) view.findViewById(R.id.mdel_color);
            color.setBackgroundResource(medicines.get(pos).getmColor());
            ImageButton imbtn = (ImageButton) view.findViewById(R.id.mdel_imbtn_del);
            imbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteMedicine_db(pos);
                    medicines.remove(pos);
                    for (int j = pos; j < medicines.size(); j++) {
                        updateAlarm(j);
                    }
                    Intent i = new Intent(getActivity(), MedicineAlarm.class);
                    PendingIntent pi = PendingIntent.getActivity(getActivity(), requestcode + medicines.size(), i, FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(pi);
                    mAdapter2 = new MyBaseAdapter2();
                    medicineList.setAdapter(mAdapter2);
                }
            });
            return view;
        }
    }
/*
    private class AlarmConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //binder=(MedicineAlarmService.AlarmBinder)service;
            //binder.setTime(2018,1,1,23,00);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
*/
}

/*
switchTog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
    }
});
 */