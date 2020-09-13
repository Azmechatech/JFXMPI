/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.safeStore;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerArray;
import org.mapdb.serializer.SerializerArrayTuple;

/**
 *
 * @author mkfs
 */
public class MapDB {

    public HTreeMap<String, String> store;
    public BTreeMap<Object[], String> index;
    //public Map<String, byte[]> store; 
    DB db;

    public MapDB(String mapdbFolder) {
        db = DBMaker
                .fileDB(mapdbFolder + "/" + "file.db").transactionEnable()
                .fileMmapEnable()
                .make();
        store = db.hashMap("store")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.STRING).expireAfterUpdate(1440, TimeUnit.MINUTES)
                .expireAfterCreate(1440, TimeUnit.MINUTES)
                .createOrOpen();

        index = db.treeMap("index")
                // use array serializer for unknown objects
                .keySerializer(new SerializerArrayTuple(
                        Serializer.STRING, Serializer.STRING, Serializer.STRING))
                // or use wrapped serializer for specific objects such as String
                .keySerializer(new SerializerArray(Serializer.STRING))
                .createOrOpen();//Issue with keys may be...
    }

    public MapDB(String mapdbFile, boolean readOnly) {
        db = DBMaker
                .fileDB(mapdbFile)
                .readOnly()
                .fileMmapEnable()
                .make();
        store = db.hashMap("store")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.STRING).expireAfterUpdate(1440, TimeUnit.MINUTES)
                .expireAfterCreate(1440, TimeUnit.MINUTES)
                .createOrOpen();

        index = db.treeMap("index")
                // use array serializer for unknown objects
                .keySerializer(new SerializerArrayTuple(
                        Serializer.STRING, Serializer.STRING, Serializer.STRING))
                // or use wrapped serializer for specific objects such as String
                .keySerializer(new SerializerArray(Serializer.STRING))
                .createOrOpen();//Issue with keys may be...
    }

    public void save(String key, String data, String preview) {
        store.put(key, data);
        index.put(new Object[]{key, data.length()+"", ""}, preview);
    }

    public void close() {
        db.close();
    }

    ;
    public void commit() {
        db.commit();
    }

    ;

    public static void main(String... args) {
        System.out.println(System.getProperty("user.home"));
            MapDB mapdb=new MapDB(System.getProperty("user.home"));
            mapdb.save("Key", "Data", "miniData");
            mapdb.commit();
            mapdb.save("Key", "Data", "miniData");
            mapdb.commit();
            mapdb.save("Key", "Data", "miniData");
            mapdb.commit();
            mapdb.save("Key", "Data", "miniData");
            mapdb.store.put("Key", "Data");
            mapdb.commit();
            mapdb.close();
    }
}
