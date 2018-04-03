package com.zhr.cat.tools.net;

import java.util.Date;

/**
 * Created by ZHR on 2017/05/25.
 */

public abstract class Data<E> {
    enum Type {shortText, longText, image, voice, videos}
    private Type type;
    private E data;
    private Date date;

    public void setData(E data) {
        this.data = data;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public E getData() {
        return data;
    }

    public Type getType() {
        return type;
    }
}
