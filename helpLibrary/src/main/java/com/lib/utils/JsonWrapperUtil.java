package com.lib.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonWrapperUtil<T> {

    private enum DataType {
        TYPE_OBJECT, TYPE_LIST, TYPE_MAP, TYPE_SET
    }

    private T value;
    private String clazzKey;
    private String clazzValue;
    private DataType dataType;

    public JsonWrapperUtil(T value) throws Exception {
        this.value = value;
        initWrapperData();
    }

    public <K, V> T parse(Gson gson) throws Exception {
        String json = gson.toJson(value);
        switch (dataType) {
            case TYPE_OBJECT:
                return (T) gson.fromJson(json, Class.forName(clazzKey));
            case TYPE_LIST:
                List<T> list = gson.fromJson(json, new TypeToken<List<T>>() {}.getType());
                for (int i = 0; i < list.size(); i++) {
                    list.set(i, (T) gson.fromJson(gson.toJson(list.get(i)), Class.forName(clazzKey)));
                }
                return (T) list;
            case TYPE_MAP:
                Map<K, V> resultMap = new HashMap<>();
                Map<K, V> map = gson.fromJson(json, new TypeToken<Map<K, V>>(){}.getType());
                for (Map.Entry<K, V> entry : map.entrySet()) {
                    String entryKey = gson.toJson(entry.getKey());
                    K k = (K) gson.fromJson(entryKey, Class.forName(clazzKey));
                    String entryValue = gson.toJson(entry.getValue());
                    V v = (V) gson.fromJson(entryValue, Class.forName(clazzValue));
                    resultMap.put(k, v);
                }
                return (T) resultMap;
            case TYPE_SET:
                Set<T> resultSet = new HashSet<>();
                Set<T> set = gson.fromJson(json, new TypeToken<Set<T>>(){}.getType());
                for (T t : set) {
                    String valueJson = gson.toJson(t);
                    T value = (T) gson.fromJson(valueJson, Class.forName(clazzKey));
                    resultSet.add(value);
                }
                return (T) resultSet;
        }
        return null;
    }

    private void initWrapperData() throws Exception {
        if (List.class.isAssignableFrom(value.getClass())) {
            this.dataType = DataType.TYPE_LIST;
            List<?> list = (List<?>) value;
            if (!list.isEmpty()) {
                this.clazzKey = list.get(0).getClass().getName();
            }
        } else if (Map.class.isAssignableFrom(value.getClass())) {
            this.dataType = DataType.TYPE_MAP;
            Map<?, ?> map = (Map) value;
            if (!map.isEmpty()) {
                Map.Entry<?, ?> entry = map.entrySet().iterator().next();
                this.clazzKey = entry.getKey().getClass().getName();
                this.clazzValue = entry.getValue().getClass().getName();
            }
        } else if (Set.class.isAssignableFrom(value.getClass())) {
            this.dataType = DataType.TYPE_SET;
            Set<?> set = (Set<?>) value;
            if (!set.isEmpty()) {
                Iterator<?> iterator = set.iterator();
                if (iterator.hasNext()) {
                    this.clazzKey = iterator.next().getClass().getName();
                }
            }
        } else {
            this.dataType = DataType.TYPE_OBJECT;
            this.clazzKey = value.getClass().getName();
        }
    }

}
