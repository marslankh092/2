package com.toto.downloader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VideoSitesList extends RecyclerView.Adapter<VideoSitesList.VideoStreamingSiteItem> {
    private List<Site> sites;
    private MainActivity activity;


    class Site {
        int drawable;
        String title;
        String url;
        int color;

        Site(int drawable,int color, String title, String url) {
            this.drawable = drawable;
            this.title = title;
            this.url = url;
            this.color=color;
        }
    }

    VideoSitesList(MainActivity activity) {
        this.activity = activity;
        sites = new ArrayList<>();
        this.activity = activity;
        sites = new ArrayList<>();
        //sites.add(new Site(R.drawable.favicon_youtube, "youtube", "https://m.youtube.com"));
        sites.add(new Site(R.drawable.itech,R.color.facebook ,"Indratech", "https://codecanyon.net/user/indratech/portfolio"));
        sites.add(new Site(R.drawable.ic_facebook,R.color.facebook ,"Facebook", "https://m.facebook.com"));
        sites.add(new Site(R.drawable.favicon_dailymotion,R.color.dailymotion, "D Motion", "https://www" +
                ".dailymotion.com"));

        sites.add(new Site(R.drawable.ic_twitter,R.color.twitter, "Twitter", "https://mobile.twitter.com"));
        sites.add(new Site(R.drawable.ic_instagram, R.color.instagram,"Instagram", "https://www" +
                ".instagram.com"));
        //sites.add(new Site(R.drawable.favicon_veoh,, "veoh", "https://www.veoh.com"));
        //sites.add(new Site(R.drawable.favicon_vimeo,R.color.vimeo, "Vimeo", "https://vimeo.com"));
        sites.add(new Site(R.drawable.ic_vk,R.color.vk, "vk", "https://m.vk.com"));
        //sites.add(new Site(R.drawable.favicon_fc2, "fc2", "https://video.fc2.com"));
        sites.add(new Site(R.drawable.favicon_vlive,R.color.vlive, "Vlive", "https://m.vlive.tv"));
        //sites.add(new Site(R.drawable.favicon_naver, "naver", "https://m.tv.naver.com"));
        //sites.add(new Site(R.drawable.favicon_metacafe, "metacafe", "https://www.metacafe.com"));
        //sites.add(new Site(R.drawable.favicon_tudou,R.color.tudou, "Tudou", "https://www.tudou.com"));
        //sites.add(new Site(R.drawable.favicon_youku,R.color.youku, "Youku", "https://m.youku.com"));
        //sites.add(new Site(R.drawable.favicon_myspace, R.color.myspace,"Myspace", "https://myspace.com"));
        sites.add(new Site(R.drawable.ic_vine, R.color.vine,"Vine", "https://vine.co"));
        sites.add(new Site(R.drawable.ic_tumblr,R.color.tumblr, "Tumblr", "https://www.tumblr.com"));
        sites.add(new Site(R.drawable.whatsapp,R.color.vine, "Whatsapp", "https://web.whatsapp.com/"));
        sites.add(new Site(R.drawable.roposo,R.color.tumblr, "Roposo", "https://www.roposo.com/discover/video"));
        sites.add(new Site(R.drawable.ic_tiktok,R.color.tumblr, "TikTok", "https://www.tiktok.com/discover?lang=en"));


    }

    @Override
    public VideoStreamingSiteItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_sites_item_indratech, parent, false);
        return new VideoStreamingSiteItem(view);
    }

    @Override
    public void onBindViewHolder(VideoStreamingSiteItem holder, int position) {
        holder.bind(sites.get(position));
        holder.layout.setBackgroundResource(sites.get(position).color);
    }

    @Override
    public int getItemCount() {
        return sites.size();
    }

    class VideoStreamingSiteItem extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView title;
        private LinearLayout layout;

        VideoStreamingSiteItem(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.indratech_ivVideoSiteIcon);
            title = itemView.findViewById(R.id.indratech_ivVideoSiteName);
            layout = itemView.findViewById(R.id.imageBackground);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(sites.get(getAdapterPosition()).title.equals("Whatsapp")){
                        activity.whatsappClicked();
                    }else{
                        //Set url in search bar
                        EditText search = activity.findViewById(R.id.indratech_search_bar);
                        search.setText(sites.get(getAdapterPosition()).url);
                        // Start searching
                        activity.getBrowserManager().newWindow(sites.get(getAdapterPosition()).url);
                        activity.hideToolbar();
                        activity.hideSlider();
                    }
                }
            });

        }

        void bind(Site site) {
            icon.setImageDrawable(activity.getResources().getDrawable(site.drawable));
            title.setText(site.title);
        }
    }
}
