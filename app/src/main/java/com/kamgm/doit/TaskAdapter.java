package com.kamgm.doit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHoler> {

    private Context mContext;
    private Cursor mCursor;
    private TaskDbHelper mDB;

    TaskAdapter(Context context, Cursor cursor, TaskDbHelper DB) {

        mContext = context;
        mCursor = cursor;
        mDB = DB;

    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoler(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHoler holder, int position) {
        mCursor.moveToPosition(position);
        holder.bindCursor(mCursor);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class ViewHoler extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTituloText;
        private TextView mFechaText;
        private TextView mHoraTetx;
        private CheckBox mCompletado;

        ViewHoler(View itemView) {
            super(itemView);

            //Initialize the views
            mTituloText = (TextView) itemView.findViewById(R.id.titulo);
            mFechaText = (TextView) itemView.findViewById(R.id.fecha);
            mHoraTetx = (TextView) itemView.findViewById(R.id.hora);
            mCompletado = (CheckBox) itemView.findViewById(R.id.checkBox);
            itemView.setOnClickListener(this);
        }

        void bindTo(Task currentTasK) {
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext);
            DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(mContext);
            mTituloText.setText(currentTasK.getTitulo());
            //mFechaText.setText(dateFormat.format(currentTasK.getFecha()));
            //mHoraTetx.setText(timeFormat.format(currentTasK.getFecha()));
            mCompletado.setChecked(currentTasK.getCompletado());
        }

        void bindCursor(Cursor cursor) {
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext);
            DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(mContext);
            mTituloText.setText(cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.TITULO)));

            //dando formato a la fecha
            SimpleDateFormat format = new SimpleDateFormat(TaskContract.TaskEntry.DATE_FORMAT);
            String fecha = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.FECHA));
            Date utilDate = null;
            Log.d("*** fecha y hora ***", fecha);
            try {
                utilDate = format.parse(fecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d("*** utilDate ***", utilDate.toString());
            mFechaText.setText(dateFormat.format(utilDate));

            mHoraTetx.setText(timeFormat.format(utilDate));

            mCompletado.setOnCheckedChangeListener(null);
            if ((cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COMPLETADO))) == 1)
                mCompletado.setChecked(true);
            else
                mCompletado.setChecked(false);
            mCompletado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int originalPosition = mCursor.getPosition();
                    mCursor.moveToPosition(getAdapterPosition());
                    mDB.updateStatus(mCursor.getInt(mCursor.getColumnIndex(TaskContract.TaskEntry._ID)), mCompletado.isChecked());
                    mCursor.moveToPosition(originalPosition);
                }


            });

        }

        @Override
        public void onClick(View view) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            int originalPosition = mCursor.getPosition();
            mCursor.moveToPosition(getAdapterPosition());

            Task task = mDB.getTaskById(mCursor.getInt(mCursor.getColumnIndex(TaskContract.TaskEntry._ID)));

            Intent detailIntent = new Intent(mContext, DetailActivity.class);
            detailIntent.putExtra(TaskContract.TaskEntry.TITULO, task.getTitulo());
            detailIntent.putExtra(TaskContract.TaskEntry.COMPLETADO, task.getCompletado());
            detailIntent.putExtra("fecha", dateFormat.format(task.getFecha()));
            detailIntent.putExtra("hora", timeFormat.format(task.getFecha()));
            detailIntent.putExtra(TaskContract.TaskEntry.DETALLE, task.getDetalle());
            detailIntent.putExtra(TaskContract.TaskEntry._ID, task.getId());

            mCursor.moveToPosition(originalPosition);
            mContext.startActivity(detailIntent);
        }
    }

    void deleteTask(int position, int estado) {
        int originalPosition = mCursor.getPosition();
        mCursor.moveToPosition(position);
        mDB.deleteTask(mCursor.getString(mCursor.getColumnIndex(TaskContract.TaskEntry._ID)));

        if (estado == R.string.incompletas) {
            mCursor = mDB.getAlltaskIncompleted();
        } else {
            mCursor = mDB.getAlltaskCompleted();
        }
        mCursor.moveToPosition(originalPosition);
    }
}