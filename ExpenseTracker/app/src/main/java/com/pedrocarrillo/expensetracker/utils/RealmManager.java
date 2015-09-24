package com.pedrocarrillo.expensetracker.utils;

import com.pedrocarrillo.expensetracker.ExpenseTrackerApp;
import com.pedrocarrillo.expensetracker.entities.Category;
import com.pedrocarrillo.expensetracker.entities.Expense;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Pedro on 9/20/2015.
 */
public class RealmManager {

    private Realm realm;

    private static RealmManager ourInstance = new RealmManager();

    public static RealmManager getInstance() {
        return ourInstance;
    }

    public RealmManager(){
        realm = Realm.getInstance(ExpenseTrackerApp.getContext());
    }

    public Realm getRealmInstance() {
        return realm;
    }

    public <E extends RealmObject> void update(E object) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(object);
        realm.commitTransaction();
    }

    public <E extends RealmObject> void update(Iterable<E> object) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(object);
        realm.commitTransaction();
    }

    public <E extends RealmObject> void save(E object, Class<E> clazz) {
        realm.beginTransaction();
        checkDuplicateUUID(object, clazz);
        realm.copyToRealm(object);
        realm.commitTransaction();
    }

    public <E extends RealmObject> void delete(E object){
        realm.beginTransaction();
        object.removeFromRealm();
        realm.commitTransaction();
    }

    public <E extends RealmObject> RealmObject findById(Class<E> clazz, String id) {
        return realm.where(clazz).equalTo("id", id).findFirst();
    }

    public <E extends RealmObject>  void checkDuplicateUUID(E object, Class<E> clazz) {
        boolean repeated = true;
        while (repeated) {
            String id = UUID.randomUUID().toString();
            RealmObject realmObject = findById(clazz, id);
            if ( realmObject == null ) {
                if (object instanceof Expense) {
                    ((Expense)object).setId(id);
                } else {
                    ((Category)object).setId(id);
                }
                repeated = false;
            }
        }
    }

}