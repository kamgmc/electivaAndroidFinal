package com.kamgm.doit;

import android.provider.BaseColumns;

public class TaskContract {
    public static abstract class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "Task";
        public static final String TITULO = "titulo";
        public static final String DETALLE = "detalle";
        public static final String FECHA = "fecha";
        public static final String COMPLETADO = "completado";
        public static final String DATE_FORMAT = "yyyy-mm-dd hh:mm:ss";
    }
}