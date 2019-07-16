package com.kamgm.doit;

import android.content.ContentValues;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {

    private String id;
    private String titulo;
    private String detalle;
    private Date fecha;
    private Boolean completado;

    Task() {
    }

    Task(String titulo, String detalle, Date fecha, Boolean completado) {

        this.titulo = titulo;
        this.detalle = detalle;
        this.fecha = fecha;
        this.completado = completado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    Boolean getCompletado() {
        return completado;
    }

    void setCompletado(Boolean completado) {
        this.completado = completado;
    }

    /**
     * Método que convierte los atributos del Task en ContentValues
     *
     * @return ContentValues
     */

    ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.TITULO, this.titulo);
        values.put(TaskContract.TaskEntry.DETALLE, detalle);
        if (this.completado)
            values.put(TaskContract.TaskEntry.COMPLETADO, 1);
        else
            values.put(TaskContract.TaskEntry.COMPLETADO, 0);

        SimpleDateFormat format = new SimpleDateFormat(TaskContract.TaskEntry.DATE_FORMAT);
        values.put(TaskContract.TaskEntry.FECHA, format.format(fecha));

        return values;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}