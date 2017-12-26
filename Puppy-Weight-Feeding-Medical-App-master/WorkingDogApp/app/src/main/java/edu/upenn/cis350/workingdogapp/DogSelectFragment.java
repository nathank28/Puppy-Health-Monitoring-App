package edu.upenn.cis350.workingdogapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import edu.upenn.cis350.workingdogapp.Database.DogEntry;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link InteractionListener}
 * interface.
 */
public class DogSelectFragment extends Fragment implements Database.DataBaseObserver{
    // Fragment that handles the dog list screen

    private InteractionListener mListener;
    protected List<DogEntry> mDogsList;
    private List<DogEntry> mVisibleDogsList;
    protected RecyclerView mDogsListView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DogSelectFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDogsList = Database.getInstance().getDogEntryList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mVisibleDogsList = new ArrayList<>(mDogsList);

        View view = inflater.inflate(R.layout.fragment_dog_list, container, false);
        View listView = view.findViewById(R.id.dogList);

        if(listView instanceof RecyclerView){
            DogListAdapter dogListAdapter = new DogListAdapter(mVisibleDogsList, this, getContext());
            mDogsListView = (RecyclerView) listView;
            mDogsListView.setAdapter(dogListAdapter);
        }

        EditText searchBox = (EditText) view.findViewById(R.id.dogListSearch); // Instantiates search bar
        searchBox.addTextChangedListener(new SearchTextProcessor());
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InteractionListener) {
            mListener = (InteractionListener) context;
            Database.getInstance().addObserver(this);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Database.getInstance().removeObserver(this);
        mListener = null;
        super.onDetach();
    }

    // Updates the list of dogs upon a change from the database
    @Override
    public void onDataBaseChanged() {
        mDogsList = Database.getInstance().getDogEntryList();
        updateDogsListView();
    }

    protected void updateDogsListView(){
        mVisibleDogsList.clear();
        mVisibleDogsList.addAll(mDogsList);
        mDogsListView.getAdapter().notifyDataSetChanged();
    }

    public void onDogSelectListInteraction(DogEntry item){
        mListener.onDogSelectListInteraction(item);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface InteractionListener {
        void onDogSelectListInteraction(DogEntry item);

    }

    private class SearchTextProcessor implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mVisibleDogsList.clear();

            for(DogEntry dog : mDogsList){
                if(dog.name.toLowerCase().startsWith(s.toString().toLowerCase())){
                    mVisibleDogsList.add(dog);
                }
            }

            mDogsListView.getAdapter().notifyDataSetChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
