package edu.upenn.cis350.workingdogapp;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Database provides a singleton object which can be used to read and write to the online Firebase
 * database.
 */

public class Database {

    private static Database mInstance;

    private DatabaseReference mDataBase;

    private Set<DataBaseObserver> mObservers;

    private Map<String, DogEntry> mDogEntries;
    private Map<String, DogEntry> mArchiveDogEntries;
    private Map<String, String> mPasswords;

    private final String ACTIVE_DOGS_INFO = "active dogs";
    private final String DOGS_HISTORY = "active dogs history";
    private final String PASSWORDS = "passwords";
    private final String ARCHIVE_DOGS_INFO = "archive dogs";

    private Database() {
        mDogEntries = new HashMap<>();
        mArchiveDogEntries = new HashMap<>();
        mPasswords = new HashMap<>();

        mObservers = new HashSet<>();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        //async update of mDogEntries Map with listeners
        DatabaseReference mDataBaseActiveDogs = mDataBase.child(ACTIVE_DOGS_INFO);
        mDataBaseActiveDogs.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DogEntry dogEntry = dataSnapshot.getValue(DogEntry.class);
                mDogEntries.put(dogEntry.name, dogEntry);
                notifyObservers();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                DogEntry dogEntry = dataSnapshot.getValue(DogEntry.class);
                mDogEntries.put(dogEntry.name, dogEntry);
                notifyObservers();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                DogEntry dogEntry = dataSnapshot.getValue(DogEntry.class);
                mDogEntries.remove(dogEntry.name);
                notifyObservers();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw new IllegalStateException("Database Error:" + databaseError.getMessage());
            }
        });

        //async update of mArchiveDogEntries Map with listeners
        DatabaseReference mDataBaseArchiveDogs = mDataBase.child(ARCHIVE_DOGS_INFO);
        mDataBaseArchiveDogs.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DogEntry dogEntry = dataSnapshot.getValue(DogEntry.class);
                mArchiveDogEntries.put(dogEntry.name, dogEntry);
                notifyObservers();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                DogEntry dogEntry = dataSnapshot.getValue(DogEntry.class);
                mArchiveDogEntries.put(dogEntry.name, dogEntry);
                notifyObservers();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                DogEntry dogEntry = dataSnapshot.getValue(DogEntry.class);
                mArchiveDogEntries.remove(dogEntry.name);
                notifyObservers();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw new IllegalStateException("Database Error: " + databaseError.getMessage());
            }
        });

        //async update of mPasswords Map with listeners
        DatabaseReference mDataBasePasswords = mDataBase.child(PASSWORDS);
        mDataBasePasswords.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mPasswords.put(dataSnapshot.getKey(), (String)dataSnapshot.getValue());
                notifyObservers();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mPasswords.put(dataSnapshot.getKey(), (String)dataSnapshot.getValue());
                notifyObservers();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                mPasswords.remove(dataSnapshot.getKey());
                notifyObservers();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw new IllegalStateException("Database Error:" + databaseError.getMessage());
            }
        });
    }

    /**
     * Getter method for singleton
     * @return Database
     */
    public static Database getInstance(){
        if(mInstance == null){
            mInstance = new Database();
        }

        return mInstance;
    }

    /**
     * Firebase works on an async update model where the time when data is read is not certain.
     * Registering an observer sends an update whenever data changes.
     * @param observer Object implementing the DataBaseObserver interface
     */
    public void addObserver(DataBaseObserver observer){
        mObservers.add(observer);
    }

    /**
     * Remove observer object from list of observers to be notified
     * @param observer Object implementing the DataBaseObserver interface
     */
    public void removeObserver(DataBaseObserver observer){
        mObservers.remove(observer);
    }

    private void notifyObservers(){
        for(DataBaseObserver observer : mObservers){
            observer.onDataBaseChanged();
        }
    }

    /**
     * onDataBaseChanged called whenever asynchronous data arrives from Firebase.
     */
    public interface DataBaseObserver {
        void onDataBaseChanged();
    }

    /**
     * Returns list of all normal DogEntry objects
     * @return DogEntry
     */
    public List<DogEntry> getDogEntryList(){
        return new ArrayList<>(mDogEntries.values());
    }

    /**
     * Returns DogEntry by name
     * @param name DogEntry name String
     * @return DogEntry
     */
    public DogEntry getDogEntryByName(String name) {
        return mDogEntries.get(name);
    }

    /**
     * Returns copy of database map for DogEntries
     * @return Map
     */
    public Map<String, DogEntry> getDogEntries(){
        return new HashMap<String, DogEntry>(mDogEntries);
    }

    /**
     * Returns list of all archived DogEntry objects to be accessed only by Admin
     * @return DogEntry
     */
    public List<DogEntry> getArchivedDogEntryList(){
        return new ArrayList<DogEntry>(mArchiveDogEntries.values());
    }

    /**
     * Returns copy of database map for ArchiveDogs
     * @return Map
     */
    public Map<String, DogEntry> getArchiveDogEntries(){
        return new HashMap<String, DogEntry>(mArchiveDogEntries);
    }

    public void getDogWeightHistory(DogEntry entry, final Map<Long, Double> weightHistoryMap,
                                       final DataBaseObserver observer){

        if(!mDogEntries.containsKey(entry.name)){
            observer.notify();
            return;
        }

        DatabaseReference mDataBaseDogHistory = mDataBase.child(DOGS_HISTORY).child(entry.name);
        mDataBaseDogHistory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dateData : dataSnapshot.getChildren()){

                    Log.d("weightinfo", dateData.getKey() + ", " + dateData.getValue());

                    weightHistoryMap.put(
                            Long.parseLong(dateData.getKey()),
                            ((Long)dateData.getValue()).doubleValue());
                }

                observer.onDataBaseChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                observer.notify();
            }
        });
    }

    /**
     * Returns password associated with a specified username, or null if username not found.
     * @param username Username String
     * @return String
     */
    public String getPassword(String username) {
        return mPasswords.get(username);
    }

    /**
     * Replaces an existing DogEntry object with the same name with the input DogEntry, or inserts
     * a DogEntry with a new name into the database
     * @param updated Database.DogEntry object
     */
    public void updateDogData(DogEntry updated){
        if(updated == null || updated.name == null || updated.name.equals("")){
            throw new IllegalArgumentException();
        }

        updated.systemTimeMillis = System.currentTimeMillis();

        mDogEntries.put(updated.name, new DogEntry(updated));

        DatabaseReference mDataBaseActiveDogs = mDataBase.child(ACTIVE_DOGS_INFO);
        mDataBaseActiveDogs.child(updated.name).setValue(updated);

        DatabaseReference mDataBaseDogsHistory = mDataBase.child(DOGS_HISTORY);
        mDataBaseDogsHistory.child(updated.name)
                .child(Long.toString(updated.systemTimeMillis))
                .setValue(updated.weightLb);

        notifyObservers();
    }

    /**
     * Moves a DogEntry in the database with the same name from the list of normal DogEntries to
     * the list of archived DogEntries. Take no action if the name is not found.
     * @param archiveEntry Database.DogEntry object
     */
    public void archiveDogEntry(DogEntry archiveEntry){
        if(archiveEntry == null || archiveEntry.name == null || archiveEntry.name.equals("")){
            throw new IllegalArgumentException();
        }

        mDogEntries.remove(archiveEntry.name);

        DatabaseReference mDataBaseActiveDogs = mDataBase.child(ACTIVE_DOGS_INFO);
        mDataBaseActiveDogs.child(archiveEntry.name).removeValue();

        DatabaseReference mDataBaseArchiveDogs = mDataBase.child(ARCHIVE_DOGS_INFO);
        mDataBaseArchiveDogs.child(archiveEntry.name).setValue(archiveEntry);

        notifyObservers();
    }

    /**
     * Moves a DogEntry in the database with the same name from the list of archived DogEntries to
     * the list of normal DogEntries. Take no action if the name is not found.
     * @param archiveEntry Database.DogEntry object
     */
    public void restoreDogEntry(DogEntry archiveEntry){
        if(archiveEntry == null || archiveEntry.name == null || archiveEntry.name.equals("")){
            throw new IllegalArgumentException();
        }

        mArchiveDogEntries.remove(archiveEntry.name);

        DatabaseReference mDataBaseActiveDogs = mDataBase.child(ARCHIVE_DOGS_INFO);
        mDataBaseActiveDogs.child(archiveEntry.name).removeValue();

        DatabaseReference mDataBaseArchiveDogs = mDataBase.child(ACTIVE_DOGS_INFO);
        mDataBaseArchiveDogs.child(archiveEntry.name).setValue(archiveEntry);

        notifyObservers();
    }

    /**
     * Change the password for the specified username or add a new username-password entry
     * if the username is not found.
     * @param userName Username String
     * @param newPass New password String
     */
    public void changePassword(String userName, String newPass){
        if(userName == null || newPass == null){
            throw new IllegalArgumentException();
        }

        DatabaseReference mDataBasePasswords = mDataBase.child(PASSWORDS);
        mDataBasePasswords.child(userName).setValue(newPass);
    }

    public static class DogEntry implements Serializable {
        public String name;
        public String breed;
        public String fosterParent;
        public String comments;
        public String feederType;
        public String foodType;
        public double feedingAmount;
        public String treatRestrictions;
        public double weightLb;
        public double heightInches;
        public double bodyScore;
        public int age;
        public double meals;
        public String allergies;
        public String fishOil;
        public long systemTimeMillis;

        public DogEntry(){
        }

        /**
         * Copy constructor for Database.DogEntry object
         * @param copy Database.DogEntry object to be copied
         */
        public DogEntry(DogEntry copy){
            name = copy.name;
            weightLb = copy.weightLb;
            heightInches = copy.heightInches;
            breed = copy.breed;
            fosterParent = copy.fosterParent;
            comments = copy.comments;
            feederType = copy.feederType;
            foodType = copy.foodType;
            feedingAmount = copy.feedingAmount;
            treatRestrictions = copy.treatRestrictions;
            bodyScore = copy.bodyScore;
            age = copy.age;
            meals = copy.meals;
            allergies = copy.allergies;
            fishOil = copy.fishOil;
            systemTimeMillis = copy.systemTimeMillis;
        }
    }
}
