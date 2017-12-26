package edu.upenn.cis350.workingdogapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.upenn.cis350.workingdogapp.Database.DogEntry;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DogEntry} and makes a call to the
 * specified {@link DogSelectFragment.InteractionListener}.
 */
public class DogListAdapter extends RecyclerView.Adapter<DogListAdapter.ViewHolder> {

    private List<DogEntry> mValues;
    private final DogSelectFragment mFragment;
    private final Context mContext;


    public DogListAdapter(List<DogEntry> items, DogSelectFragment fragment, Context context) {
        mValues = items;
        mFragment = fragment;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_dog, parent, false);
        return new ViewHolder(view);
    }



    // Builds the list contents
    private void redoList(ViewHolder holder, int position) {
        //changes the fontsize of the BindView items to setting size
        SharedPreferences sharedPreferences = (mContext.getSharedPreferences("settings", 0));
        holder.mContentView.setTextSize(sharedPreferences.getInt("fontSize", 18));

        //holder.mContentView.setTextSize(Settings.getInstance().fontSize);

        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).name);

        Context ctx = holder.mView.getContext();
        TypedValue value = new TypedValue();


        long timeMillis = holder.mItem.systemTimeMillis;
        double bodyScore = holder.mItem.bodyScore;


        //Highlight dog if bodyscore is bad
        if (bodyScore < 4 || bodyScore > 5) {
            ctx.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
            holder.mView.setBackgroundColor(value.data);

        }
        else {
            ctx.getTheme().resolveAttribute(R.attr.color, value, true);
            holder.mView.setBackgroundColor(value.data);
        }

        //put notifier (triangle exclamation) if dog has not been updated (admin/weigh-in) in 3+ days)
        if (System.currentTimeMillis() - timeMillis > (24*60*60*1000)) {
            holder.mContentView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_report_problem_black_24dp, 0);
        }
    }


    //initializes the dog list, does one call to redoList()
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        redoList(holder, position);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("NAME = " + holder.mItem.name);
                mFragment.onDogSelectListInteraction(holder.mItem);

                redoList(holder, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    //modified RecyclerView.ViewHolder class for the dog list on the front page
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public DogEntry mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
