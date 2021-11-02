package com.goupec.recyclerviewwithoptionmenu.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.goupec.recyclerviewwithoptionmenu.MainActivity;
import com.goupec.recyclerviewwithoptionmenu.R;
import com.goupec.recyclerviewwithoptionmenu.models.MyList;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<MyList> list;
    private Context mCtx;

    public CustomAdapter(List<MyList> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustomAdapter.ViewHolder holder, int position) {
        MyList myList = list.get(position);
        holder.textViewHead.setText(myList.getHead());
        holder.textViewDesc.setText(myList.getDesc());

        holder.buttonViewOption.setOnClickListener(view -> {

            /* will show popup menu here */
            //creating a popup menu
            PopupMenu popup = new PopupMenu(mCtx, holder.buttonViewOption);
            //inflating menu from xml resource
            popup.inflate(R.menu.options_menu);
            //adding click listener
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.menu1:
                        //handle menu1 click
                        updateElement(position);
                        break;
                    case R.id.menu2:
                        //Delete item
                        confirmDeletion(position, myList.getHead());
                        break;
                }
                return false;
            });
            //displaying the popup
            popup.show();

        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewHead;
        public TextView textViewDesc;
        public TextView buttonViewOption;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
        }
    }

    private void confirmDeletion(int position, String element){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mCtx);
        builder1.setMessage("Are you sure you want to delete "+element+" ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Delete item for List and RecyclerView
                        list.remove(position);
                        MainActivity.adapter.notifyItemRemoved(position);
                        MainActivity.adapter.notifyDataSetChanged();
                        Toast.makeText(mCtx,"Item deleted " ,Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    private void updateElement(int position){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mCtx);
        LayoutInflater inflater = (LayoutInflater) mCtx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_update_item, null);
        alertDialogBuilder.setView(view);
        // alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();


        TextView edtTitre = (TextView) view.findViewById(R.id.titre);
        TextView edtDesc = (TextView) view.findViewById(R.id.desc);

        Button dialogBtn_cancel = (Button) dialog.findViewById(R.id.update);
        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get value
                String titre  = edtTitre.getText().toString();
                String desc  = edtDesc.getText().toString();

                // Update Item
                MyList newObject = new MyList(titre, desc);
                list.set(position,newObject);
                MainActivity.adapter.notifyItemChanged(position);
                Toast.makeText(mCtx,"Item update " ,Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

    }
}