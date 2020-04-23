package com.kobid.mygreatapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    private ArrayList<ExampleItem> mExampleList;
    private OnItemClickListener mListener;


    public interface OnItemClickListener{
        void onDeleteClick(ExampleItem item);
    }


    public ExampleAdapter(ArrayList<ExampleItem> exampleItem, OnItemClickListener listener) {

        mExampleList = exampleItem;
        mListener=listener;
    }


    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {

        holder.bind();
        //ExampleItem currentItem = mExampleItem.get(position);
        //holder.mImageView.setImageResource(currentItem.getImageResource());

    }


    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        //public TextView mDocId;
        public ImageView mDeleteImage;


        public ExampleViewHolder(final View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);

            mDeleteImage = itemView.findViewById(R.id.image_delete);


            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position= getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            listener.onDeleteClick(mExampleList.get(getAdapterPosition()));
                        }
                    }
                }
            });
        }


         void bind() {
            ExampleItem item  = mExampleList.get(getAdapterPosition());

            mTextView1.setText(item.getmText1());
            mTextView2.setText(item.getmText2());
        }
    }

}