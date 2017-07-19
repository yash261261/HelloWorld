package com.sargent.mark.todolist;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sargent.mark.todolist.data.Contract;
/**
 * Created by mark on 7/4/17.
 */

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ItemHolder> {

    private Cursor cursor;
    private ItemClickListener listener;
   // private ItemLongClickListener longClickListener;
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



//    public interface ItemLongClickListner{
//        void onItemLongClick(int pos, long id, boolean done);
//    }

    public ToDoListAdapter(Cursor cursor, ItemClickListener listener) {
        this.cursor = cursor;
        this.listener = listener;
    //    this.longClickListener= longClickListener;
      //  ItemLongClickListener longClickListener
    }

    public void swapCursor(Cursor newCursor){
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

      //  ItemLongClickListener longClickListener
        TextView descr;
        TextView due;
        String duedate;
        String description;
        String category;
        TextView cat;
        long id;
        boolean done;


        ItemHolder(View view) {
            super(view);
            descr = (TextView) view.findViewById(R.id.description);
            due = (TextView) view.findViewById(R.id.dueDate);
            cat=(TextView) view.findViewById(R.id.category);
            view.setOnClickListener(this);
         //   view.setOnLongClickListener(this);
        }

        public void bind(ItemHolder holder, int pos) {
            cursor.moveToPosition(pos);
            id = cursor.getLong(cursor.getColumnIndex(Contract.TABLE_TODO._ID));
            Log.d(TAG, "deleting id: " + id);

            duedate = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE));
            description = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DESCRIPTION));
            category = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_CATEGORY));
           //  done=cursor.getInt(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DONE)) == 1;
            descr.setText(description);
            due.setText(duedate);
            cat.setText("Category: " + category);

            holder.itemView.setTag(id);

        //    setItemBackgroundBasedOnDoneStatus();
        }









        @Override
        public void onClick(View v) {

            int pos = getAdapterPosition();

            listener.onItemClick(pos, description, duedate, category, id);

            
        }
        
        
//        @Override public boolean onLongClick(View v){
//            Toast.makeText(v.getContext(), "Long Press To Mark Done/Undone", Toast.LENGTH_SHORT).show();
//            int pos=getAdapterPosition();
//
//            done=!done;
//            setItemBackgroundBasedOnDoneStatus();
//            longClickListener.onItemLongClick(pos,id,done);
//            return true;
//
//        }
//
//         private void setItemBackgroundBasedOnDoneStatus(){
//            if(done){
//                descr.setPaintFlags(descr.getPaintFlags() |
//                Paint.STRIKE_THRU_TEXT_FLAG);
//
//                due.setPaintFlags(due.getPaintFlags() |
//
//                        Paint.STRIKE_THRU_TEXT_FLAG);
//
//                cat.setPaintFlags(cat.getPaintFlags() |
//
//                            Paint.STRIKE_THRU_TEXT_FLAG);
//
//
//            }
//            else
//            {
//                descr.setPaintFlags(descr.getPaintFlags() &
//
//                        Paint.STRIKE_THRU_TEXT_FLAG);
//
//                due.setPaintFlags(due.getPaintFlags()  &
//
//                        Paint.STRIKE_THRU_TEXT_FLAG);
//
//                cat.setPaintFlags(cat.getPaintFlags() &
//
//
//              Paint.STRIKE_THRU_TEXT_FLAG
//                );
//            }
//        }


    }

}
