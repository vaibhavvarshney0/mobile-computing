package com.zuccessful.a2;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mlistener;
    private Context mctx;
    private List<Contact> contactList;


    public RecyclerViewAdapter(Context mctx, List<Contact> contactList, OnItemClickListener mlistener) {
        this.mctx = mctx;
        this.contactList = contactList;
        this.mlistener = mlistener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_contact_list_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        ViewHolder viewHolder = new ViewHolder(view, mlistener);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.title.setText(contact.getTitle());

        if(contact.getImage()!=null){
            holder.imageView.setImageURI(Uri.parse(contact.getImage()));
            holder.initial.setVisibility(TextView.GONE);
        }
        else
        {
            String initial = String.valueOf(contact.getTitle().charAt(0));
            holder.imageView.setImageResource(R.drawable.circle_bg);
            holder.initial.setText(initial);
            holder.initial.setVisibility(TextView.VISIBLE);
        }

//        holder.number.setText(contact.getNumber().get(0));

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        OnItemClickListener mlistener;
        ImageView imageView;
        TextView title;
        TextView initial;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.contactName);
            initial = itemView.findViewById(R.id.initial);
            mlistener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mlistener.onItemClick(view, getAdapterPosition());
        }
    }

}
