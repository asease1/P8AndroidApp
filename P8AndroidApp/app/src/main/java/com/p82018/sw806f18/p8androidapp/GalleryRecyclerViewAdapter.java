package com.p82018.sw806f18.p8androidapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ItemHolder> {

    private List<Uri> itemsUri;
    private LayoutInflater layoutInflater;
    private Context context;
    private OnItemClickListener onItemClickListener;
    GalleryActivity galleryActivity;

    public GalleryRecyclerViewAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        itemsUri = new ArrayList<Uri>();
        galleryActivity = (GalleryActivity)context;
    }

    @Override
    public GalleryRecyclerViewAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView itemCardView = (CardView)layoutInflater.inflate(R.layout.layout_cardview, parent, false);
        return new ItemHolder(itemCardView, this);
    }

    @Override
    public void onBindViewHolder(GalleryRecyclerViewAdapter.ItemHolder holder, int position) {
        Uri targetUri = itemsUri.get(position);
        holder.setItemUri(targetUri.getPath());

        if (targetUri != null) {

            try {
                holder.setImageView(Util.convertImageFileToBitmap(targetUri.getPath(), 75, 85));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemsUri.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void remove(int selectedIndex) {
        itemsUri.remove(selectedIndex);
        notifyItemRemoved(selectedIndex);

        if(itemsUri.size() == 0){
            galleryActivity.showNoImagesText();
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(ItemHolder item, int position);
    }

    public void add(int location, Uri iUri) {
        itemsUri.add(location, iUri);
        notifyItemInserted(location);
    }

    public void clearAll() {
        int itemCount = itemsUri.size();

        if (itemCount > 0) {
            itemsUri.clear();
            notifyItemRangeRemoved(0, itemCount);
        }
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private GalleryRecyclerViewAdapter parent;
        private CardView cardView;
        ImageView imageView;
        String itemUri;

        public ItemHolder(CardView cardView, GalleryRecyclerViewAdapter parent) {
            super(cardView);
            itemView.setOnClickListener(this);
            this.cardView = cardView;
            this.parent = parent;
            imageView = (ImageView) cardView.findViewById(R.id.item_image);
        }

        public void setItemUri(String itemUri){
            this.itemUri = itemUri;
        }

        public String getItemUri(){
            return itemUri;
        }

        public void setImageView(Bitmap bitmap){
            imageView.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = parent.getOnItemClickListener();
            if(listener != null){
                listener.onItemClick(this, getLayoutPosition());
                //or use
                //listener.onItemClick(this, getAdapterPosition());
            }
        }
    }
}