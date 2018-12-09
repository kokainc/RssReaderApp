package com.example.fusion.rssreader.Interface.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.algorithmia.*;
import com.algorithmia.algo.*;

import com.example.fusion.rssreader.Interface.ItemClickListener;
import com.example.fusion.rssreader.MainActivity;
import com.example.fusion.rssreader.Model.RssObject;
import com.example.fusion.rssreader.R;

/**
 * Created by FUSION on 11/8/2018.
 */

class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener
{

    public TextView txtTitle, txtPubDate,txtContent;
    private ItemClickListener itemClickListener;



    public FeedViewHolder(View itemView) {
        super(itemView);

        txtTitle = (TextView)itemView.findViewById(R.id.textTitle);
        txtPubDate = (TextView)itemView.findViewById(R.id.textPubdate);
        txtContent = (TextView)itemView.findViewById(R.id.txtContent);

        //Set Event
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);



    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),true);
        return true;
    }
}

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {

    private RssObject rssObject;
    private Context mContext;
    private LayoutInflater inflater;

    public FeedAdapter(RssObject rssObject, Context mContext) {
        this.rssObject = rssObject;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }



    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.row,parent,false);
        return new FeedViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        holder.txtTitle.setText(rssObject.getItems().get(position).getTitle());
        holder.txtPubDate.setText(rssObject.getItems().get(position).getPubDate());
        holder.txtContent.setText(rssObject.getItems().get(position).getContent());



    //public int getItemCount()




        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                if(!isLongClick) {
                    Uri uri = Uri.parse(rssObject.getItems().get(position).getLink());
                    Intent browserInternt = new Intent(Intent.ACTION_VIEW, uri);
                    browserInternt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(browserInternt);
                }
                else{
                    String input = rssObject.getItems().get(position).getLink();
                    AlgorithmiaClient client = Algorithmia.client("simYzWmJq6ESnUvDZ00nHsJGs6T1");
                    Algorithm algo = client.algo("util/Html2Text/0.1.6");
                    try {
                        AlgoResponse result = algo.pipe(input);
                        Toast.makeText(mContext,result.toString(), Toast.LENGTH_SHORT).show();
                    } catch (APIException e) {
                        e.printStackTrace();
                    }


                    Toast.makeText(mContext,"Nothing!" , Toast.LENGTH_SHORT).show();


                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return (rssObject!=null && rssObject.items!=null)? rssObject.items.size():0;
        //return rssObject.items.size();
    }
}
