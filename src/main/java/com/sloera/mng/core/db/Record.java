package com.sloera.mng.core.db;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Record implements Serializable {
    public Map<String, Object> map = new HashMap();
    public Record(){
    }

    public Map<String, Object> getColumns() {
        return this.map;
    }
}
