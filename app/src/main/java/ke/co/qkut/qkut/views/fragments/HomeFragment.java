package ke.co.qkut.qkut.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ke.co.qkut.qkut.R;
import ke.co.qkut.qkut.constants.GLobalHeaders;
import ke.co.qkut.qkut.constants.URL;
import ke.co.qkut.qkut.controller.adapters.BusinessInfoAdapter;
import ke.co.qkut.qkut.datastore.LocalDatabase;
import ke.co.qkut.qkut.util.newtork.local.NetworkConnection;
import ke.co.qkut.qkut.util.newtork.local.OnReceivingResult;
import ke.co.qkut.qkut.util.newtork.local.RemoteResponse;
import ke.co.qkut.qkut.models.BusinessInfo;
import ke.co.qkut.qkut.views.activities.MainActivity;


public class HomeFragment extends Fragment {
ListView businessList;
ViewPager viewPager;
RelativeLayout loadingBusiness;
    SearchView searchView;
    FloatingActionButton fab;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.home_fragment_layout,container,false);
        loadingBusiness=view.findViewById(R.id.loadingBusiness);
        getBusinesses("");
        businessList=view.findViewById(R.id.business_list_view);

        viewPager= getActivity().findViewById(R.id.pager);

        return view;




    }

    @Override
    public void onResume() {
        searchView= getActivity().findViewById(R.id.search);
        searchView.setQueryHint(getString(R.string.search_hint));
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                viewPager.setCurrentItem(0);

                getBusinesses(newText);
                // Toast.makeText(MainActivity.this, newText+"", Toast.LENGTH_SHORT).show();
                // getBusinesses(newText);
                // your text view here
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {

                //getBusinesses(query);
                return true;
            }
        });
        super.onResume();
    }

    public void getBusinesses(String query) {

        JSONObject headers = new JSONObject();
        String url;
        String token;
        if ((token = LocalDatabase.getToken((getContext()))).equals(LocalDatabase.NOT_LOGGED_IN)) {
            url = URL.GET_PLACES_FOR_GUEST + query;
        } else {

            url = URL.GET_PLACES_FOR_NON_GUEST + query;
                headers= GLobalHeaders.getGlobalHeaders(getContext());


        }
        Log.e("SEARCH_URL",url);
        Log.e("Token",token);
        NetworkConnection.makeAGetRequest(url, headers, new OnReceivingResult() {
            @Override
            public void onErrorResult(IOException e) {
                Toast.makeText(getActivity(), "Please check your network", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceiving100SeriesResponse(RemoteResponse remoteResponse) {
                Log.e("Logged",remoteResponse.getMessage());
            }

            @Override
            public void onReceiving200SeriesResponse(RemoteResponse remoteResponse) {
                Log.e("Logged",remoteResponse.getMessage());
               String resultStatus = "";
                String resultData = "";
                JSONObject response = new JSONObject();
                try {
                    response= new JSONObject(remoteResponse.getMessage());
                    resultStatus = response.getString("status").toString();
                } catch (JSONException e) {
                    e.printStackTrace();


                }
                if (resultStatus.compareTo("ok") != 0) {

                } else { //good results
                    try {
                        JSONArray jsonBusinesses = response.getJSONArray("data");
                        populateBusinessList(jsonBusinesses);

                    } catch (JSONException e) {
                        Log.d("JSON_ERROR", e.getMessage());
                        e.printStackTrace();

                    }
                }


            }

            @Override
            public void onReceiving300SeriesResponse(RemoteResponse remoteResponse) {
                Log.e("Logged",remoteResponse.getMessage());
            }

            @Override
            public void onReceiving400SeriesResponse(RemoteResponse remoteResponse) {
                Log.e("Logged",remoteResponse.getMessage());
            }

            @Override
            public void onReceiving500SeriesResponse(RemoteResponse remoteResponse) {
                Log.e("Logged",remoteResponse.getMessage());
            }
        });


    }
    public void getBusinesses(String query,AppCompatActivity appCompatActivity) {

        JSONObject headers = new JSONObject();
        String url;
        String token;
        if ((token = LocalDatabase.getToken((appCompatActivity))).equals(LocalDatabase.NOT_LOGGED_IN)) {
            url = URL.GET_PLACES_FOR_GUEST + query;
        } else {

            url = URL.GET_PLACES_FOR_NON_GUEST + query;
            headers= GLobalHeaders.getGlobalHeaders(appCompatActivity);


        }
        Log.e("SEARCH_URL",url);
        Log.e("Token",token);
        NetworkConnection.makeAGetRequest(url, headers, new OnReceivingResult() {
            @Override
            public void onErrorResult(IOException e) {
                Toast.makeText(getContext(), "Please check your network", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceiving100SeriesResponse(RemoteResponse remoteResponse) {
                Log.e("Logged",remoteResponse.getMessage());
            }

            @Override
            public void onReceiving200SeriesResponse(RemoteResponse remoteResponse) {
                Log.e("Logged",remoteResponse.getMessage());
                String resultStatus = "";
                String resultData = "";
                JSONObject response = new JSONObject();
                try {
                    response= new JSONObject(remoteResponse.getMessage());
                    resultStatus = response.getString("status").toString();
                } catch (JSONException e) {
                    e.printStackTrace();


                }
                if (resultStatus.compareTo("ok") != 0) {

                } else { //good results
                    try {
                        JSONArray jsonBusinesses = response.getJSONArray("data");
                        populateBusinessList(jsonBusinesses);

                    } catch (JSONException e) {
                        Log.d("JSON_ERROR", e.getMessage());
                        e.printStackTrace();

                    }
                }


            }

            @Override
            public void onReceiving300SeriesResponse(RemoteResponse remoteResponse) {
                Log.e("Logged",remoteResponse.getMessage());
            }

            @Override
            public void onReceiving400SeriesResponse(RemoteResponse remoteResponse) {
                Log.e("Logged",remoteResponse.getMessage());
            }

            @Override
            public void onReceiving500SeriesResponse(RemoteResponse remoteResponse) {
                Log.e("Logged",remoteResponse.getMessage());
            }
        });


    }
   public void populateBusinessList(JSONArray jsonBusinesses) {
        List<BusinessInfo> businessInfoList= new ArrayList<>();
        for (int i = 0; i < jsonBusinesses.length(); i++) {
            try {
                Log.d("Valley", jsonBusinesses.getJSONObject(i).toString());
                businessInfoList.add(new BusinessInfo(jsonBusinesses.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
       loadingBusiness.setVisibility(View.GONE);
        Log.e("Business Info",businessInfoList.size()+"");
        BusinessInfoAdapter businessInfoAdapter = new BusinessInfoAdapter((AppCompatActivity)getActivity(), businessInfoList,getView());
        businessInfoAdapter.setViewPager(viewPager);
        this.businessList.setAdapter(businessInfoAdapter);

    }
    public static void ShowBarCodeForm(final View view){
        FloatingActionButton fab = view.findViewById(R.id.fab);
        view.findViewById(R.id.barcode_scan_form).setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        View scanView = view.findViewById(R.id.barcode_scan_form);
        //BarCodeHandler barCodeHandler = new BarCodeHandler(view.getContext(), scanView);

    }

}
