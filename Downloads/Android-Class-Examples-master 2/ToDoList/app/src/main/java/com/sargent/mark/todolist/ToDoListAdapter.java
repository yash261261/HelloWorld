package com.sargent.mark.todolist;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sargent.mark.todolist.data.Contract;
/**
 * Created by mark on 7/4/17.
 */

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ItemHolder> {

    private Cursor cursor;
    private ItemClickListener listener;
    private String TAG = "todolistadapter";

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item, parent, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public interface ItemClickListener {
        void onItemClick(int pos, String description, String duedate, String category, long id);
    }


    public ToDoListAdapter(Cursor cursor, ItemClickListener listener) {

        this.cursor = cursor;

        this.listener = listener;


    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView descr;
        TextView due;
        String duedate;
        String description;
        String category;
        TextView cat;
        Button b;
        long id;
        boolean done;


        ItemHolder(View view) {
            super(view);
            descr = (TextView) view.findViewById(R.id.description);
            due = (TextView) view.findViewById(R.id.dueDate);
            cat = (TextView) view.findViewById(R.id.category);
            b = (Button) view.findViewById(R.id.checkbox);
            view.setOnClickListener(this);
        }

        public void bind(ItemHolder holder, int pos) {
            cursor.moveToPosition(pos);
            id = cursor.getLong(cursor.getColumnIndex(Contract.TABLE_TODO._ID));
            Log.d(TAG, "deleting id: " + id);
            setItemBackgroundBasedOnDoneStatus();


            duedate = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE));
            description = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DESCRIPTION));
            category = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_CATEGORY));
            done = cursor.getInt(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DONE)) == 1;
            descr.setText(description);
            due.setText(duedate);
            cat.setText("Category: " + category);


            holder.itemView.setTag(id);
            setItemBackgroundBasedOnDoneStatus();

            // On Click change the status to Done/Undone

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    done = !done;
                    Log.d(TAG, "Value is-------" + done);

                    setItemBackgroundBasedOnDoneStatus();
                    MainActivity.updatetodostatus(pos, id, done);

                }
            });


        }

        private void setItemBackgroundBasedOnDoneStatus() {
            if (done) {
                descr.setPaintFlags(descr.getPaintFlags() |
                        Paint.STRIKE_THRU_TEXT_FLAG);

                due.setPaintFlags(due.getPaintFlags() |

                        Paint.STRIKE_THRU_TEXT_FLAG);

                cat.setPaintFlags(cat.getPaintFlags() |

                        Paint.STRIKE_THRU_TEXT_FLAG);


            } else {
                descr.setPaintFlags(descr.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);

                due.setPaintFlags(due.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);

                cat.setPaintFlags(cat.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG
                );
            }
        }


        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(pos,description,duedate,category,id);

        }
    }

}


