package com.example.limethecoder.eventkicker.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.limethecoder.eventkicker.model.EventItem;
import com.example.limethecoder.eventkicker.R;
import com.example.limethecoder.eventkicker.net.ServiceManager;
import com.example.limethecoder.eventkicker.net.ApiResponse;
import com.example.limethecoder.eventkicker.ui.adapter.CardAdapter;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

public class RecycleViewFragment extends Fragment {
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    ArrayList<EventItem> items;

    int pageNumber;
    RecyclerView mRecyclerView;

    public static RecycleViewFragment newInstance(int page) {
        RecycleViewFragment pageFragment = new RecycleViewFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_view_fragment, null);

        mRecyclerView = (RecyclerView) view;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // Set list for different number of page
        initializeList();

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initializeList() {
        ServiceManager.MyApiEndpointInterface apiService = ServiceManager.newService();
        Call<ApiResponse<EventItem>> call = apiService.loadEvents();

        call.enqueue(new Callback<ApiResponse<EventItem>>() {
            @Override
            public void onResponse(retrofit.Response<ApiResponse<EventItem>> response, Retrofit retrofit) {

                if (response.body().success) {
                    Toast.makeText(getContext(), "List loaded", Toast.LENGTH_LONG).show();

                    items = response.body().multiple;

                    RecyclerView.Adapter adapter;

                    CardAdapter.ViewHolder.ItemClickListener itemClickListener = new
                            CardAdapter.ViewHolder.ItemClickListener() {
                                @Override
                                public void onClick(View view, int position, boolean isLongClick) {
                                    Intent intent = new Intent(getContext(), EventPageActivity.class);

                                    EventItem event = items.get(position - 1);
                                    if(event.getPictureUrl() == null)
                                        event.setPictureUrl( "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");

                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    String converted = gson.toJson(items.get(position - 1));
                                    intent.putExtra("json", converted);
                                    startActivity(intent);
                                }
                            };

                    adapter = new RecyclerViewMaterialAdapter(new CardAdapter(items, itemClickListener, getContext()));
                    mRecyclerView.setAdapter(adapter);
                } else
                    Toast.makeText(getContext(), "Error to load list", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                Log.e("RecyclerViewFragment", t.getMessage());
            }
        });
    }
}