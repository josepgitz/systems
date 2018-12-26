package ke.co.qkut.qkut.controller.adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

import ke.co.qkut.qkut.Modeler.Department;
import ke.co.qkut.qkut.Modeler.Organisation;
import ke.co.qkut.qkut.Modeler.Service;
import ke.co.qkut.qkut.R;



public class ScheduledServiceAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List <Organisation> organisations;
    private Context context;
    View view;
    private ViewPager viewPager;
    public ScheduledServiceAdapter(AppCompatActivity context, List<Organisation> organisations, View view) {
        this.context = context;
        if (!(context==null)){
            layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        this.organisations = organisations;
        this.view=view;

    }

    @Override
    public int getCount() {
        return organisations.size();
    }

    @Override
    public Object getItem(int position) {
        return (Organisation)this.organisations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder rowHolder;
        convertView = layoutInflater.inflate(R.layout.scheduled_place_row, parent, false);
        Organisation organisation=organisations.get(position);
        try{
            List<Department> departments = organisation.getDepartmentList();

            Log.d("departments",organisation.getDepartmentList().toString());
        rowHolder = new ViewHolder();
        for (Department d:departments) {
                List<Service> services= d.getServices();
            for (Service service:services) {
                    rowHolder.serviceName =  convertView.findViewById(R.id.service_name);
                    rowHolder.serviceDescription = convertView.findViewById(R.id.service_description);
                    rowHolder.serviceDepartment = convertView.findViewById(R.id.service_department);
                    rowHolder.btnQueueInfo = convertView.findViewById(R.id.button_queued_info);
                    rowHolder.timelabel=convertView.findViewById(R.id.timeLabel);
                    rowHolder.serviceQueueStatement = convertView.findViewById(R.id.queue_status_statement);
                    rowHolder.serviceNameView = convertView.findViewById(R.id.confirm_service_name);
                    rowHolder. serviceDepartmentView = convertView.findViewById(R.id.confirm_service_department);
                    rowHolder.companyNameView = convertView.findViewById(R.id.confirm_company_name);
                    rowHolder. compnayAddress = convertView.findViewById(R.id.confirm_company_address);
                    rowHolder. queueSize1 = convertView.findViewById(R.id.service_queue_size);
                    rowHolder. queueSpeed1 = convertView.findViewById(R.id.service_queue_speed);
                    rowHolder. queueSize = convertView.findViewById(R.id.my_service_queue_size);
                    rowHolder. queueSpeed = convertView.findViewById(R.id.my_service_queue_speed);
                    rowHolder.timelabel=convertView.findViewById(R.id.timeLabel);
                    rowHolder.DatePicker=convertView.findViewById(R.id.DatePicker);

                    rowHolder.serviceName.setText  (Html.fromHtml("<b>Service<b>")+service.getName());
                    rowHolder.serviceDescription.setText(Html.fromHtml("<b>Description:<b>")+service.getDescription());
                    rowHolder.serviceDepartment.setText(Html.fromHtml("<b>Department:<b>")+service.getName());
                    rowHolder.queueSize.setText(Html.fromHtml("<b>Queue size:<b>"+service.getQueue_size()+""));
                    rowHolder.queueSpeed.setText(Html.fromHtml("<b>QueueSpeed:<b>"+service.getQueue_speed()+""));
                    rowHolder.queueSize1.setText(Html.fromHtml("<b>Queue size:<b>"+service.getQueue_size()+""));
                    rowHolder.queueSpeed1.setText(Html.fromHtml("<b>QueueSpeed:<b>"+service.getQueue_speed()+""));
                    rowHolder.compnayAddress.setText(Html.fromHtml("<b>Company Address:<b>")+organisation.getAddress());
                    rowHolder.serviceDepartmentView.setText(Html.fromHtml("<b>Department:<b>")+d.getName());
                    rowHolder.timelabel.setText(Html.fromHtml("<b>At:<b>")+service.getCreated_at());
                    rowHolder.DatePicker.setText(Html.fromHtml("<b>At:<b>")+service.getCreated_at());
                    rowHolder.serviceNameView.setText(Html.fromHtml("<b>Service:<b>")+service.getName());
                    rowHolder.companyNameView.setText(Html.fromHtml("<b>Company Name:<b>")+organisation.getName());
                   final View queueView = convertView.findViewById(R.id.confirm_queue_form);
                    if(service.getIs_queued().equals("1")){

                            rowHolder.btnQueueInfo.setText("Queue Info ");
                        rowHolder.btnQueueInfo.setBackgroundColor(
                                    context.getResources().getColor(R.color.colorPrimary));
                    }
                    else  {
                        rowHolder.btnQueueInfo.setText("Visit Info");
                        rowHolder.btnQueueInfo.setBackgroundColor(
                                    context.getResources().getColor(R.color.colorDanger));

                        }
                    rowHolder.btnQueueInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(queueView.getVisibility()==View.VISIBLE){
                                queueView.setVisibility(View.GONE);
                            }else {
                                queueView.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    }


                }
        }catch (Exception e){
            e.printStackTrace();

        }

        return convertView;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager=viewPager;
    }

    class ViewHolder{
        TextView serviceName;
        TextView serviceDepartment;
        TextView serviceDescription;
        TextView queueSize;
        TextView queueSpeed;
        TextView queueSize1;
        TextView queueSpeed1;
        Button btnQueueInfo;
        TextView DatePicker;
        TextView serviceQueueStatement;
        TextView serviceNameView;
        TextView serviceDepartmentView;
        TextView companyNameView;
        TextView compnayAddress;
        Button btnVisitInfo;
        TextView timelabel;
    }

}
