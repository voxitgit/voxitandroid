package com.triton.voxit.model;

import java.io.Serializable;

public class OptionsBean implements Serializable {
    private int id;

    private String value;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "OptionsBean{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
