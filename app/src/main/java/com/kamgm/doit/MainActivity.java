package com.kamgm.doit;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Task> mTasksData;
    private TaskAdapter mAdapter;
    private TaskDbHelper mDB;
    private Cursor cursor;
    private int estado;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (menu.findItem(R.id.estado).getTitle().equals(R.string.finalizadas)) {
            cursor = mDB.getAlltaskIncompleted();
        } else {
            cursor = mDB.getAlltaskCompleted();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Check if the correct item was clicked
        if (estado == R.string.incompletas) {
            cursor = mDB.getAlltaskCompleted();
            estado = R.string.finalizadas;
            item.setTitle(R.string.incompletas);
        } else {
            cursor = mDB.getAlltaskIncompleted();
            estado = R.string.incompletas;
            item.setTitle(R.string.finalizadas);
        }
        mAdapter = new TaskAdapter(this, cursor, mDB);
        mRecyclerView.setAdapter(mAdapter);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        cursor = mDB.getAlltaskIncompleted();
        mAdapter = new TaskAdapter(this, cursor, mDB);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        estado = R.string.incompletas;
        setContentView(R.layout.activity_main);

        mDB = new TaskDbHelper(this);
        cursor = mDB.getAlltaskIncompleted();
        //Initialize the RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView);
        //Set the Layout Manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Initialize the ArrayLIst that will contain the data
        mTasksData = new ArrayList<>();
        //Initialize the adapter and set it ot the RecyclerView
        mAdapter = new TaskAdapter(this, cursor, mDB);
        mRecyclerView.setAdapter(mAdapter);


        //Get the data
        //initializeData();

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT |
                ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.LEFT |
                ItemTouchHelper.RIGHT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mAdapter.deleteTask(viewHolder.getAdapterPosition(), estado);
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        }
        );
        helper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * Add new task method.
     *
     * @param view view
     */
    public void addTask(View view) {
        Intent detailIntent = new Intent(this, NewTaskActivity.class);
        this.startActivity(detailIntent);
    }
}
