package com.example.pinewood.persistence;

import java.util.*;
import java.util.function.Function;

public class InFileCrudRepository<T> {
    private final JsonFileStore store;
    private final String fileName;
    private final Class<T> clazz;
    private final Function<T, String> idGetter;

    public InFileCrudRepository(JsonFileStore store, String fileName, Class<T> clazz, Function<T, String> idGetter) {
        this.store = store;
        this.fileName = fileName;
        this.clazz = clazz;
        this.idGetter = idGetter;
    }

    public synchronized List<T> list() {
        return store.readList(fileName, clazz);
    }

    public synchronized Optional<T> get(String id) {
        return list().stream().filter(x -> Objects.equals(idGetter.apply(x), id)).findFirst();
    }

    public synchronized T upsert(T entity) {
        List<T> all = list();
        String id = idGetter.apply(entity);
        boolean replaced = false;
        for (int i=0; i<all.size(); i++) {
            if (Objects.equals(idGetter.apply(all.get(i)), id)) {
                all.set(i, entity);
                replaced = true;
                break;
            }
        }
        if (!replaced) all.add(entity);
        store.writeList(fileName, all);
        return entity;
    }

    public synchronized void delete(String id) {
        List<T> all = list();
        all.removeIf(x -> Objects.equals(idGetter.apply(x), id));
        store.writeList(fileName, all);
    }
}
