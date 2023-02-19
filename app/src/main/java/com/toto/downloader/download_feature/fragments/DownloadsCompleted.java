package com.toto.downloader.download_feature.fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.toto.downloader.R;
import com.toto.downloader.VDApp;
import com.toto.downloader.VDFragment;
import com.toto.downloader.download_feature.VideoThumbDownloader;
import com.toto.downloader.download_feature.lists.CompletedVideos;
import com.toto.downloader.utils.RenameDialog;
import com.toto.downloader.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadsCompleted extends VDFragment implements DownloadsInProgress.OnAddDownloadedVideoToCompletedListener {
    private View view;
    private RecyclerView downloadsList;
    private List<String> videos;
    private CompletedVideos completedVideos;
    private VideoThumbDownloader mVideoThumbLoader = new VideoThumbDownloader();

    private OnNumDownloadsCompletedChangeListener onNumDownloadsCompletedChangeListener;

    public interface OnNumDownloadsCompletedChangeListener {
        void onNumDownloadsCompletedChange();
    }

    public void setOnNumDownloadsCompletedChangeListener(OnNumDownloadsCompletedChangeListener
                                                                 onNumDownloadsCompletedChangeListener) {
        this.onNumDownloadsCompletedChangeListener = onNumDownloadsCompletedChangeListener;
    }

    public int getNumDownloadsCompleted() {
        return videos.size();
    }

    
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        videos = new ArrayList<>();
        completedVideos = CompletedVideos.load(getActivity());
        videos = completedVideos.getVideos();

        if (view == null) {
            view = inflater.inflate(R.layout.downloads_completed_indratech, container, false);

            downloadsList = view.findViewById(R.id.indratech_downloadsCompletedList);
            Button clearAllFinishedButton = view.findViewById(R.id.clearAllFinishedButton);
            Button indratech_goToFolderButton = view.findViewById(R.id.indratech_goToFolder);

            downloadsList.setAdapter(new DownloadedVideoAdapter());
            downloadsList.setLayoutManager(new LinearLayoutManager(getActivity()));
            downloadsList.setHasFixedSize(true);
            downloadsList.addItemDecoration(Utils.createDivider(getActivity()));

            clearAllFinishedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage(getResources().getString(R.string.empty_download_list_indratech))
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int length = videos.size();
                                    videos.clear();
                                    if (completedVideos != null) {
                                        completedVideos.save(getActivity());
                                    }
                                    downloadsList.getAdapter().notifyItemRangeRemoved(0, length);
                                    onNumDownloadsCompletedChangeListener.onNumDownloadsCompletedChange();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .create()
                            .show();
                }
            });

            indratech_goToFolderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File path = Environment.getExternalStoragePublicDirectory(getString(R.string.app_name));
                    Intent galleryIntent = new Intent();
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setDataAndType(Uri.fromFile(path), "video/*");
                    galleryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(galleryIntent);
                }
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<String> nonExistentFiles = new ArrayList<>();
        for (String video : videos) {
            File videoFile = new File(Environment.getExternalStoragePublicDirectory(getString(R.string.app_name)), video);
            if (!videoFile.exists()) {
                nonExistentFiles.add(video);
            }
        }
        for (String nonExistentVideo : nonExistentFiles) {
            videos.remove(nonExistentVideo);
        }
        downloadsList.getAdapter().notifyDataSetChanged();
        completedVideos.save(VDApp.getInstance().getApplicationContext());
        onNumDownloadsCompletedChangeListener.onNumDownloadsCompletedChange();
    }

    @Override
    public void onAddDownloadedVideoToCompleted(final String name, final String type) {
        if (completedVideos == null) {
            completedVideos = new CompletedVideos();
        }
        completedVideos.addVideo(getActivity(), name + "." + type);
        videos = completedVideos.getVideos();
        downloadsList.getAdapter().notifyItemInserted(0);
        onNumDownloadsCompletedChangeListener.onNumDownloadsCompletedChange();
    }

    private class DownloadedVideoAdapter extends RecyclerView.Adapter<VideoItem> {

        @Override
        public VideoItem onCreateViewHolder( ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.downloads_completed_item_indratech, parent, false);
            return new VideoItem(view);
        }

        @Override
        public void onBindViewHolder( VideoItem holder, int position) {
            holder.bind(videos.get(position));
        }

        @Override
        public int getItemCount() {
            return videos.size();
        }
    }

    private class VideoItem extends RecyclerView.ViewHolder implements ViewTreeObserver.OnGlobalLayoutListener {
        private TextView name;
        private TextView size;
        private ImageView thumbnail;
        private String baseName;
        private String type;

        private boolean adjustedlayout;

        VideoItem(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.indratech_downloadCompletedName);
            size = itemView.findViewById(R.id.indratech_downloadCompletedSize);
            thumbnail = itemView.findViewById(R.id.indratech_downloadThumnail);
            itemView.findViewById(R.id.indratech_download_menu).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(), view);
                    popup.getMenuInflater().inflate(R.menu.indratech_download_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            int i = item.getItemId();
                            if (i == R.id.download_delete) {
                                new AlertDialog.Builder(getActivity())
                                        .setMessage("Are you sure you want to delete?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                int position = getAdapterPosition();
                                                videos.remove(position);
                                                completedVideos.save(getActivity());
                                                downloadsList.getAdapter().notifyItemRemoved(position);
                                                onNumDownloadsCompletedChangeListener.onNumDownloadsCompletedChange();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .create()
                                        .show();
                                return true;
                            }
                            else if (i == R.id.download_rename){
                                new RenameDialog(getActivity(), baseName) {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {

                                    }

                                    @Override
                                    public void onOK(String newName) {
                                        File downloadsFolder = Environment.getExternalStoragePublicDirectory(getString(R.string.app_name));
                                        File renamedFile = new File(downloadsFolder, newName + "." + type);
                                        File file = new File(downloadsFolder, baseName + "." + type);
                                        if (file.renameTo(renamedFile)) {
                                            videos.set(getAdapterPosition(), newName + "." + type);
                                            completedVideos.save(getActivity());
                                            downloadsList.getAdapter().notifyItemChanged(getAdapterPosition());
                                        } else {
                                            Toast.makeText(getActivity(), "Failed: Invalid Filename", Toast
                                                    .LENGTH_SHORT).show();
                                        }
                                    }
                                };
                                return true;
                            }
                            else if (i == R.id.download_share) {
                                File file = new File(Environment.getExternalStoragePublicDirectory(getString(R.string.app_name)), baseName + "." + type);
                                Uri fileUri = FileProvider.getUriForFile(getActivity(), "com.toto.downloader.fileprovider", file);

                                StringBuilder msg = new StringBuilder();
                                msg.append(getString(R.string.msg_share_indratech));
                                msg.append("\n");
                                msg.append(getString(R.string.app_link_indratech));

                                if (fileUri != null) {
                                    Intent shareIntent = new Intent();
                                    shareIntent.setAction(Intent.ACTION_SEND);
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                                    shareIntent.setType("*/*");
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, msg.toString());
                                    shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                                    try {
                                        startActivity(Intent.createChooser(shareIntent, "Share via"));
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(getActivity().getApplicationContext(), "No App Available", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                return true;
                            }
                            else {
                                return onMenuItemClick(item);
                            }
                        }
                    });
                    popup.show();
                }
            });
            itemView.getViewTreeObserver().addOnGlobalLayoutListener(this);
            adjustedlayout = false;

            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File file = new File(Environment.getExternalStoragePublicDirectory(getString(R.string.app_name)), baseName + "." + type);
                    Uri fileUri = FileProvider.getUriForFile(getActivity(), "com.toto.downloader.fileprovider", file);
                    intent.setDataAndType(fileUri, "video/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                }
            });
        }

        void bind(String video) {
            baseName = video.substring(0, video.lastIndexOf("."));
            type = video.substring(video.lastIndexOf(".") + 1, video.length());
            name.setText(baseName);
            File file = new File(Environment.getExternalStoragePublicDirectory(getString(R.string.app_name)), video);
            if (file.exists()) {
                String length = Formatter.formatFileSize(getActivity(), file.length());
                size.setText(length);
            } else {
                int position = getAdapterPosition();
                videos.remove(position);
                completedVideos.save(getActivity());
                downloadsList.getAdapter().notifyItemRemoved(position);
                onNumDownloadsCompletedChangeListener.onNumDownloadsCompletedChange();
            }

            thumbnail.setTag(file.getAbsolutePath());// binding imageview
            thumbnail.setImageResource(R.drawable.ic_play); //default image
            mVideoThumbLoader.showThumbByAsynctack(file.getAbsolutePath(), thumbnail);
        }

        @Override
        public void onGlobalLayout() {
            if (!adjustedlayout) {
                if (itemView.getWidth() != 0) {
                    int totalMargin = (int) TypedValue.applyDimension(TypedValue
                                    .COMPLEX_UNIT_DIP, 35,
                            getActivity().getResources().getDisplayMetrics());
                    int nameMaxWidth = itemView.getMeasuredWidth() - totalMargin;
                    name.setMaxWidth(nameMaxWidth);
                    adjustedlayout = true;
                }
            }
        }
    }
}
