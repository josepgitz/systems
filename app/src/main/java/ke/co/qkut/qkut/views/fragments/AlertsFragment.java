package ke.co.qkut.qkut.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ke.co.qkut.qkut.R;
import ke.co.qkut.qkut.interfaces.OnReceiveingNewService;
import ke.co.qkut.qkut.messages.MessageReceivers;
import ke.co.qkut.qkut.models.Service;

public class AlertsFragment extends Fragment  {
ListView listView;
private  static List<Service> serviceList= new ArrayList<>();
AlertAdapter alertAdapter;
FrameLayout no_notification_frame;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_alert,container,false);
        listView= view.findViewById(R.id.alert_list_view);
        no_notification_frame=view.findViewById(R.id.no_notification_frame);
        listView.setAdapter(alertAdapter=new AlertAdapter());
        MessageReceivers.addNewServiceListener(alertAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        return view;
    }



    class AlertAdapter extends BaseAdapter implements OnReceiveingNewService{

        @Override
        public int getCount() {
            if (serviceList.size()>0){
                no_notification_frame.setVisibility(View.GONE);

            }else{
                no_notification_frame.setVisibility(View.VISIBLE);

            }
            return serviceList.size();

        }

        @Override
        public Object getItem(int position) {
            return serviceList.get(position);

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView==null)
            {
                convertView=LayoutInflater.from(getContext()).inflate(R.layout.activity_alert_row,parent,false);

            }
            if (position==0){
                convertView.findViewById(R.id.image_new).setVisibility(View.VISIBLE);
            }
           final LinearLayout linearLayout=convertView.findViewById(R.id.moreItemAlert);
           final Button button= convertView.findViewById(R.id.more_button);
           button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (linearLayout.getVisibility()==View.VISIBLE){
                        linearLayout.setVisibility(View.GONE);
                        button.setText("More");

                    }else{
                        linearLayout.setVisibility(View.VISIBLE);
                        button.setText("Less");



                    }
                   // changeColor(convertView);
                }
            });
            return convertView;
        }

        @Override
        public void OnRecevingNewServiceNotication(final Service service) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    serviceList= new ArrayList<>();
                    serviceList.add(service);
                    notifyDataSetChanged();

                }
            });
            Log.e("new service","recived");

        }
    }

}
