package ke.co.qkut.qkut.models;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ke.co.qkut.qkut.R;
import ke.co.qkut.qkut.constants.GLobalHeaders;
import ke.co.qkut.qkut.constants.URL;
import ke.co.qkut.qkut.util.newtork.local.NetworkConnection;
import ke.co.qkut.qkut.util.newtork.local.OnReceivingResult;
import ke.co.qkut.qkut.util.newtork.local.RemoteResponse;
import ke.co.qkut.qkut.views.dialogs.GeneralDialogBuilder;

public class QueueHandler {
    ProgressDialog progressDialog;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;
    AppCompatActivity context;
    private Queue activeQueue;
    View parentView;
    private EditText toDateEtxt;

    private SimpleDateFormat dateFormatter;
    private TextView progressText;
    private ImageView progressWarning;
    TextView editDate, editTime, ticketTime;
    private Calendar myCalendar;
    String dateFormat = "dd/MM/yyyy";
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener time;
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.UK);
    private View progressContainer;
    private EditText DatePicker;
    private Button confirmQueueButton;
    private Button cancelQueueButton;
    //private Button swap_Quest;
    private BusinessInfo businessInfo;
View mainview;

    //constructor
    //----------------------------------------------------------------------------
    public QueueHandler(AppCompatActivity context, View parentView) {
        progressDialog = new ProgressDialog(context);
        this.context = context;
        mainview = parentView;

        this.parentView = parentView.findViewById(R.id.activity_queue);
        myCalendar = Calendar.getInstance();

    }

    public QueueHandler(AppCompatActivity context, View parentView, BusinessInfo businessInfo) {
        progressDialog = new ProgressDialog(context);
        this.businessInfo = businessInfo;
        this.context = context;
        this.parentView = parentView;
        myCalendar = Calendar.getInstance();
    }
    //initialize variables
    //---------------------------------------------------------------------------------------

    public void initialize(final Queue activeQueue) {

        this.activeQueue = activeQueue;
        this.cancelQueueButton=parentView.findViewById(R.id.button_Cancel_queue);
        this.progressContainer = parentView.findViewById(R.id.container_progress_indicator);
        this.progressText = parentView.findViewById(R.id.progress_indicator_text);
        this.progressWarning = parentView.findViewById(R.id.progress_indicator_icon);
        this.confirmQueueButton = parentView.findViewById(R.id.button_confirm_queue);
            //confirmQueueButton.setEnabled(false);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        TextView serviceQueueStatement = parentView.findViewById(R.id.queue_status_statement);
        TextView serviceNameView = parentView.findViewById(R.id.confirm_service_name);
        TextView serviceDepartmentView = parentView.findViewById(R.id.confirm_service_department);
        TextView companyNameView = parentView.findViewById(R.id.confirm_company_name);
        TextView compnayAddress = parentView.findViewById(R.id.confirm_company_address);
        TextView queueSize = parentView.findViewById(R.id.service_queue_size);
        TextView queueSpeed = parentView.findViewById(R.id.service_queue_speed);
        editDate = parentView.findViewById(R.id.DatePicker);
        editTime = parentView.findViewById(R.id.TimePicker);
        currentHour = myCalendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = myCalendar.get(Calendar.MINUTE);
        confirmQueueButton.setEnabled(true);
        editTime.setText(String.format("%02d:%02d", currentHour, currentMinute));
        serviceNameView.setText(Html.fromHtml("<b>Service:</b> " + activeQueue.getService().getName()));

        serviceDepartmentView.setText(Html.fromHtml("<b>Department:</b> "
                + activeQueue.getService().getDepartmentName()));
        companyNameView.setText(Html.fromHtml("<b>Company:</b> "
                + activeQueue.getBusinessAccount().getName()));
        compnayAddress.setText(Html.fromHtml("<b>Address:</b> "
                + activeQueue.getBusinessAccount().getAddress()
                + "<br /><b>Industry:</b> " + activeQueue.getBusinessAccount().getSubIndustry()
                + " | " + activeQueue.getBusinessAccount().getIndustry()));
        queueSize.setText("Queue Size: " + activeQueue.getService().getQueueSize());
        queueSpeed.setText("Queue Speed: " + activeQueue.getService().getQueueSpeed());

        if (activeQueue.getService().getIsQueued() != 0) {
            serviceQueueStatement.setText(Html.fromHtml("<b>You are Queued here:</b>"));
//            cancelQueueButton.setEnabled(true);
//            swap_Quest.setEnabled(false);
            this.confirmQueueButton.setText("Queued ");
            this.confirmQueueButton.setBackgroundColor(
                    context.getResources().getColor(R.color.colorDanger));
            //   this.swap_Quest.setBackgroundColor(
            //    context.getResources().getColor(R.color.colorAccent));
            //serviceViewHolder.btnQueueHere.setPressed(true);
        } else {
            serviceQueueStatement.setText(Html.fromHtml("<b>Confirm you want to Queue here:</b>"));
            this.confirmQueueButton.setText("Confirm ");
            this.confirmQueueButton.setBackgroundColor(
                    context.getResources().getColor(R.color.colorPrimary));
        }
        this.confirmQueueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeQueue.getService().getIsQueued() == 0) {
                    confirmQueue(1);
                }
            }
        });
        this.cancelQueueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeQueue.getService().getIsQueued() == 0) {
                    confirmQueue(0);
                }
            }
        });
        long currentdate = System.currentTimeMillis();
        String dateString = sdf.format(currentdate);
        editDate.setText(dateString);
        // set calendar date and update editDate
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, Calendar.YEAR);
                myCalendar.set(Calendar.MONTH, Calendar.MONTH);
                myCalendar.set(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH);
                updateDate();
            }

        };
        // onclick - popup Timepicker
        editTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                currentHour = myCalendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = myCalendar.get(Calendar.MINUTE);
//                cancelQueueButton.setEnabled(true);
                confirmQueueButton.setEnabled(true);
                //  swap_Quest.setEnabled(true);
                timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {

                        editTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, false);

                editTime.setText(String.format("%02d:%02d", myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE)));
                timePickerDialog.show();
            }
        });
        // onclick - popup datepicker
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar = Calendar.getInstance();
                // TODO Auto-generated method stub
                int Day = myCalendar.get(Calendar.DAY_OF_MONTH);
                int Month = myCalendar.get(Calendar.MONTH);
                int Year = myCalendar.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year,
                                          int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDate();
                    }
                }, Day, Month, Year);
                updateDate();
                datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }

    private void updateDate() {
        String dateString = sdf.format(myCalendar.getTimeInMillis());
        editDate.setText(dateString);
    }

    //progress bar show
    //----------------------------------------------------------------------------------
    private void ShowProgressBar(boolean show, boolean hasWarning, String message) {
        if (progressContainer == null) {

            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Processing...");
            progressDialog.show();
        } else {
            progressContainer.setVisibility(show ? View.VISIBLE : View.GONE);
            progressWarning.setVisibility(hasWarning ? View.VISIBLE : View.GONE);
            progressText.setText(message);
        }
    }
    public void confirmQueue(int confirm) {

        ShowProgressBar(true, false, "Confirming ");

        String url = context.getString(R.string.app_base_link) + "/" + context.getString(R.string.url_user_queue);
        String selectedDate = this.editDate.getText() + " " + this.editTime.getText();
        // Log.d("Visit2222", selectedDate);
        activeQueue.setScheduledTime(selectedDate);
        JSONObject jsonObject = new JSONObject();


        try {
            jsonObject.put("id", activeQueue.getId());
            jsonObject.put("business_account_id", activeQueue.getBusinessAccount().getId());
            jsonObject.put("service_id", activeQueue.getService().getId());
            jsonObject.put("scheduled_time", activeQueue.getScheduledTime());
            Log.d("Dates12", activeQueue.getScheduledTime());
            jsonObject.put("status", confirm);
            jsonObject.put("device", android.os.Build.MODEL);
            jsonObject.put("created_through", context.getString(R.string.app_platform));
            Log.d("Visit1222", jsonObject.toString());

            NetworkConnection.makeAPostRequest(URL.ADD_USER_TO_QUEUE, jsonObject.toString(), GLobalHeaders.getGlobalHeaders(context), new OnReceivingResult() {
                @Override
                public void onErrorResult(IOException e) {

                }

                @Override
                public void onReceiving100SeriesResponse(RemoteResponse remoteResponse) {

                }

                @Override
                public void onReceiving200SeriesResponse(RemoteResponse remoteResponse) {
                    NetworkConnection.remoteResponseLogger(remoteResponse);

                    String resultStatus = "";
                    String resultData = "";
                    String ticketNumber;
                    JSONObject response = new JSONObject();
                    try {
                        response = new JSONObject(remoteResponse.getMessage());
                        resultStatus = response.getString("status").toString();
                        Log.d("Results", resultStatus);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressText.setText("Response error");
                    }
                    if (resultStatus.compareTo("ok") != 0) {
                        try {
                            resultData = response.getString("reason").toString();
                            ShowProgressBar(true, true, resultData);
                            //  ViewSwapper.ShowMainView(parentView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ShowProgressBar(true, true, "System Error");
                        }
                    } else { //good results
                        progressDialog.dismiss();
                        try {
                            resultData = response.get("reason").toString();
                            ticketNumber = response.getJSONObject("data").get("ticket").toString();
                            progressText.setText(resultData);

                            new GeneralDialogBuilder().model("Queued", "Your ticket number is :" + ticketNumber).build(context);
                           ShowPlaces(mainview,false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Errors", e.getMessage());
                            new GeneralDialogBuilder().model("Queued", "You did not get a ticket number ").build(context);
                        }
                    }
                }


                @Override
                public void onReceiving300SeriesResponse(RemoteResponse remoteResponse) {

                }

                @Override
                public void onReceiving400SeriesResponse(RemoteResponse remoteResponse) {
                    NetworkConnection.remoteResponseLogger(remoteResponse);

                }

                @Override
                public void onReceiving500SeriesResponse(RemoteResponse remoteResponse) {

                }
            });
        } catch (JSONException e) {

        }


        // Access the RequestVisit through your singleton class.
        //   ConnectionHandler.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }
    public static void Populate(final AppCompatActivity view, BusinessInfo b, Service s) {

        View queueView = view.findViewById(R.id.activity_queue);

        QueueHandler queueHandler = new QueueHandler(view, queueView);
        queueHandler.initialize(new Queue(b, s));

    }
    public void ShowPlaces(View view,boolean state){

        ListView business_list_view=view.findViewById(R.id.business_list_view) ;
        View QueueActivity= view.findViewById(R.id.activity_queue);
        View VisitActivity= view.findViewById(R.id.activity_visit);
        business_list_view.setVisibility(View.VISIBLE);
        QueueActivity.setVisibility(View.GONE);
        VisitActivity.setVisibility(View.GONE);

    }

}

