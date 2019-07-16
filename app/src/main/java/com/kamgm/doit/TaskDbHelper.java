package com.kamgm.doit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TaskDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final int DATABASE_OLD_VERSION = 2;
    private static final String DATABASE_NAME = "Task.db";
    private static final String TAG = "tag";
    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;


    TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "Construct WordListOpenHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " ("
                    + TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TaskContract.TaskEntry.TITULO + " TEXT NOT NULL,"
                    + TaskContract.TaskEntry.DETALLE + " TEXT ,"
                    + TaskContract.TaskEntry.FECHA + " TEXT NOT NULL,"
                    + TaskContract.TaskEntry.COMPLETADO + " INTEGER  NOT NULL );");
            fillDatabaseWithData(db);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d(TAG, "EXCEPTION! " + ex);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // SAVE USER DATA FIRST!!!
        int oldVersion = 1;
        Log.w(TaskDbHelper.class.getName(),
                "Upgrading database from version " + DATABASE_OLD_VERSION + " to "
                        + DATABASE_VERSION + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    /**
     * Método para insertar la data por defecto dentro de la base de datos
     *
     * @param sqLiteDatabase db
     */

    private void fillDatabaseWithData(SQLiteDatabase sqLiteDatabase) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Task[] tasks = new Task[0];
        try {
            tasks = new Task[]{new Task("Realizar el proyecto", "Lorem ipsum",
                    format.parse("2019-06-12 10:00:00"), false),
                    new Task("Realizar el informe", "Lorem ipsum",
                            format.parse("2019-06-12 10:00:00"), false),
                    new Task("Calificar el proyecto", "Lorem ipsum",
                            format.parse("2019-06-12 10:00:00"), false)
            };
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (Task task : tasks)
            taskFalsa(sqLiteDatabase, task);
    }

    private long taskFalsa(SQLiteDatabase db, Task task) {
        return db.insert(
                TaskContract.TaskEntry.TABLE_NAME,
                null,
                task.toContentValues());
    }

    /**
     * Método para obtener todas las tareas incompletas
     *
     * @return Cursor
     */

    Cursor getAlltaskIncompleted() {
        String columns[] = new String[]{
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.TITULO,
                TaskContract.TaskEntry.FECHA,
                TaskContract.TaskEntry.COMPLETADO};
        String orderBy = TaskContract.TaskEntry.FECHA + " DESC";
        String selection = TaskContract.TaskEntry.COMPLETADO + " = 0";
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(
                    TaskContract.TaskEntry.TABLE_NAME,
                    columns,
                    selection,
                    null,
                    null,
                    null,
                    orderBy);
            Log.d(TAG, "The total cursor count is " + cursor.getCount());
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
        } finally {
            return cursor;
        }
    }

    /**
     * Método para obtener todas las tareas completadas
     *
     * @return Cursor
     */

    Cursor getAlltaskCompleted() {
        String columns[] = new String[]{
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.TITULO,
                TaskContract.TaskEntry.FECHA,
                TaskContract.TaskEntry.COMPLETADO};
        String orderBy = TaskContract.TaskEntry.FECHA + " DESC";
        String selection = TaskContract.TaskEntry.COMPLETADO + " = 1";
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(
                    TaskContract.TaskEntry.TABLE_NAME,
                    columns,
                    selection,
                    null,
                    null,
                    null,
                    orderBy);
            Log.d(TAG, "The total cursor count is " + cursor.getCount());
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
        }
        return cursor;
    }

    /**
     * Método para obtener un task a partir de su id de la base de datos
     *
     * @param taskId id de la tarea
     * @return Task
     */

    Task getTaskById(int taskId) {
        String selection = TaskContract.TaskEntry._ID + " LIKE " + taskId;
        Task task = new Task();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(
                    TaskContract.TaskEntry.TABLE_NAME,
                    null,
                    selection,
                    null,
                    null,
                    null,
                    null);
            cursor.moveToFirst();
            task.setId(String.valueOf(taskId));
            task.setTitulo(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.TITULO)));
            task.setDetalle(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.DETALLE)));
            if ((cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COMPLETADO))) == 1)
                task.setCompletado(true);
            else
                task.setCompletado(false);
            SimpleDateFormat format = new SimpleDateFormat(TaskContract.TaskEntry.DATE_FORMAT);
            String fecha = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.FECHA));
            java.util.Date date = format.parse(fecha);
            task.setFecha(date);
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION! " + e);
        } finally {
            cursor.close();
        }
        return task;
    }

    /**
     * Método para actualizar el status de la tarea en la base de datos
     *
     * @param taskId id de la tarea
     * @param status estatus de la tarea
     * @return Boolean
     */

    Boolean updateStatus(int taskId, Boolean status) {
        Boolean flag;
        try {
            ContentValues values = new ContentValues();
            if (status) {
                values.put(TaskContract.TaskEntry.COMPLETADO, 1);
                SimpleDateFormat format = new SimpleDateFormat(TaskContract.TaskEntry.DATE_FORMAT);
                String dateString = format.format(new java.util.Date());
                values.put(TaskContract.TaskEntry.FECHA, dateString);
            } else
                values.put(TaskContract.TaskEntry.COMPLETADO, 0);
            getWritableDatabase().update(
                    TaskContract.TaskEntry.TABLE_NAME,
                    values,
                    " _id = " + taskId,
                    null);
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    boolean deleteTask(String id) {
        String where = TaskContract.TaskEntry._ID + " LIKE " + id;
        //String[] selectionArgs = {id};
        int i = getWritableDatabase().delete(
                TaskContract.TaskEntry.TABLE_NAME,
                where,
                null);
        return i > 0;
    }

    /**
     * Método para actualizar el estado de una tarea directamente en la base de datos.
     *
     * @param taskId id de la tarea
     * @param status estatus de la tarea
     */
    public int updateStatusTask(int taskId, boolean status) {
        int mNumberOfRowsUpdated = -1;
        try {

            if (this.mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            ContentValues values = new ContentValues();
            if (status) {
                values.put(TaskContract.TaskEntry.COMPLETADO, 0);
            } else {
                values.put(TaskContract.TaskEntry.COMPLETADO, 1);
            }
            mNumberOfRowsUpdated = mWritableDB.update(TaskContract.TaskEntry.TABLE_NAME,
                    values,
                    TaskContract.TaskEntry._ID + " = ?",
                    new String[]{String.valueOf(taskId)});
        } catch (Exception e) {
            Log.d(TAG, "UPDATE EXCEPTION! " + e.getMessage());
        }
        return mNumberOfRowsUpdated;
    }

    public void updateTask(int taskId, String titulo, String detalle, String fecha, boolean status) {
        try {
            ContentValues values = new ContentValues();
            values.put(TaskContract.TaskEntry.COMPLETADO, status);
            values.put(TaskContract.TaskEntry.TITULO, titulo);
            values.put(TaskContract.TaskEntry.FECHA, fecha);
            values.put(TaskContract.TaskEntry.DETALLE, detalle);

            //Actualizo los datos
            getWritableDatabase().update(
                    TaskContract.TaskEntry.TABLE_NAME,
                    values,
                    " _id = " + taskId,
                    null);
            Log.d(TAG, "UPDATE GERAL EXITOSO! ");
        } catch (Exception e) {
            Log.d(TAG, "UPDATE EXCEPTION! " + e.getMessage());
        }
    }

    void saveTask(String titulo, String detalle, String fecha, boolean status) {
        try {
            ContentValues values = new ContentValues();
            values.put(TaskContract.TaskEntry.COMPLETADO, status);
            values.put(TaskContract.TaskEntry.TITULO, titulo);
            values.put(TaskContract.TaskEntry.FECHA, fecha);
            values.put(TaskContract.TaskEntry.DETALLE, detalle);

            //Actualizo los datos
            getWritableDatabase().insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
            Log.d(TAG, "SAVE EXITOSO! ");
        } catch (Exception e) {
            Log.d(TAG, "SAVE EXCEPTION! " + e.getMessage());
        }
    }
}