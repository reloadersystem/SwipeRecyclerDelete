package com.example.silvia.swiperecycler;

import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.silvia.swiperecycler.Adapter.CardListAdapter;
import com.example.silvia.swiperecycler.Helper.Common;
import com.example.silvia.swiperecycler.Helper.RecyclerItemTouchHelper;
import com.example.silvia.swiperecycler.Helper.RecyclerItemTouchHelperListener;
import com.example.silvia.swiperecycler.Model.IMenuRequest;
import com.example.silvia.swiperecycler.Model.Item;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    private final String URL_API="https://api.androidhive.info/json/menu.json";
    private RecyclerView recyclerView;
    private List<Item>list;
    private CardListAdapter adapter;
    private CoordinatorLayout rootLayout;

    IMenuRequest mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://api.androidhive.info/json/menu.json

        mService= Common.getMenuRequest();

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("EDMT Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=  findViewById(R.id.recycler_view);
        rootLayout= findViewById(R.id.rootLayout);

        list= new ArrayList<>();

        adapter= new CardListAdapter(this, list);

        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack
                = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);

        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);

        //request APi

        addItemToCart();



    }

    private void addItemToCart() {
        mService.getMenuList(URL_API)
                .enqueue(new Callback<List<Item>>() {
                    @Override
                    public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                        list.clear(); // remove old items
                        list.addAll(response.body());
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(Call<List<Item>> call, Throwable t) {

                    }
                });

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if(viewHolder instanceof  CardListAdapter.MyViewHolder)
        {
            String name= list.get(viewHolder.getAdapterPosition()).getName();
            final Item deleteItem= list.get(viewHolder.getAdapterPosition());
            final int deleteIndex= viewHolder.getAdapterPosition();

            adapter.removeItem(deleteIndex);

            Snackbar snackbar= Snackbar.make(rootLayout, name + "remove from cart!", Snackbar.LENGTH_SHORT);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    adapter.restoreItem(deleteItem, deleteIndex);

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }

    }
}
