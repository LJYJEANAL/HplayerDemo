package smc.hplayerdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandablelistActivity extends BaseActivity {
    ExpandableListView mainlistview = null;
    List<String> parent = null;
    Map<String, List<String>> map = null;


    private TextView sender;
    private TextView content;
    private IntentFilter receiveFilter;
    private MessageReceiver messageReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandablelist);
        mainlistview = (ExpandableListView) this
                .findViewById(R.id.main_expandablelistview);

        sender = (TextView) findViewById(R.id.sender);
        content = (TextView) findViewById(R.id.content);
        receiveFilter = new IntentFilter();
        receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        messageReceiver = new MessageReceiver();
        registerReceiver(messageReceiver, receiveFilter);

        initData();
        mainlistview.setAdapter(new MyAdapter());
        Button toVF_btn = (Button) findViewById(R.id.toVF_btn);
        toVF_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExpandablelistActivity.this,VFActivity.class));
            }
        });
        Button sendNotic = (Button) findViewById(R.id.sendNotic);
        sendNotic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                Notification notification=new Notification(R.drawable.ic_visibility_cyan_300_24dp, "This is ticker text",
                        System.currentTimeMillis());
                RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notify);
                notification.contentView=contentView;
                Uri soundUri = Uri.fromFile(new File("/system/media/audio/ringtones/ Basic_tone.ogg"));
                notification.sound = soundUri;
//                如果想要让手机在通知到来的时候立刻振动1 秒，然后静止1 秒，再振动1 秒，代码就可以写成：
                long[] vibrates = {0, 1000, 1000, 1000};
                notification.vibrate = vibrates;
//                当通知到来时，如果想要实现LED 灯以绿色的灯光一闪一闪的效果
                notification.ledARGB = Color.GREEN;
                notification.ledOnMS = 1000;
                notification.ledOffMS = 1000;
                notification.flags = Notification.FLAG_SHOW_LIGHTS;
                manager.notify(1,notification);

            }
        });
    }

    // 初始化数据
    public void initData() {
        parent = new ArrayList<String>();
        parent.add("parent1");
        parent.add("parent2");
        parent.add("parent3");

        map = new HashMap<String, List<String>>();

        List<String> list1 = new ArrayList<String>();
        list1.add("child1-1");
        list1.add("child1-2");
        list1.add("child1-3");
        map.put("parent1", list1);

        List<String> list2 = new ArrayList<String>();
        list2.add("child2-1");
        list2.add("child2-2");
        list2.add("child2-3");
        map.put("parent2", list2);

        List<String> list3 = new ArrayList<String>();
        list3.add("child3-1");
        list3.add("child3-2");
        list3.add("child3-3");
        map.put("parent3", list3);

    }

    class MyAdapter extends BaseExpandableListAdapter {

        //得到子item需要关联的数据
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String key = parent.get(groupPosition);
            return (map.get(key).get(childPosition));
        }

        //得到子item的ID
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        //设置子item的组件
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            String key = ExpandablelistActivity.this.parent.get(groupPosition);
            String info = map.get(key).get(childPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ExpandablelistActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_children, null);
            }
            TextView tv = (TextView) convertView
                    .findViewById(R.id.second_textview);
            tv.setText(info);
            return tv;
        }

        //获取当前父item下的子item的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            String key = parent.get(groupPosition);
            int size=map.get(key).size();
            return size;
        }
        //获取当前父item的数据
        @Override
        public Object getGroup(int groupPosition) {
            return parent.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return parent.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        //设置父item组件
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ExpandablelistActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_parent, null);
            }
            TextView tv = (TextView) convertView
                    .findViewById(R.id.parent_textview);
            tv.setText(ExpandablelistActivity.this.parent.get(groupPosition));
            return tv;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }


    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Object[] pdus = (Object[]) bundle.get("pdus"); // 提取短信消息
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            String address = messages[0].getOriginatingAddress(); // 获取发送方号码
            String fullMessage = "";
            for (SmsMessage message : messages) {
                fullMessage += message.getMessageBody(); // 获取短信内容
            }
            sender.setText(address);
            content.setText(fullMessage);
        }
    }
}
