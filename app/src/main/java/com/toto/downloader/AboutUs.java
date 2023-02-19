package com.toto.downloader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AboutUs extends VDFragment implements MainActivity.OnBackPressedListener, View.OnClickListener  {
    private View view;
    private FirebaseFirestore db;
    TextView aboutUsEd;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);

        if (view == null) {
            view = inflater.inflate(R.layout.about_us_indratech, container, false);

            aboutUsEd = view.findViewById(R.id.aboutUsEd);

            getVDActivity().setOnBackPressedListener(this);

            //Back
            ImageView btn_aboutUs_back = view.findViewById(R.id.btn_aboutUs_back);
            btn_aboutUs_back.setOnClickListener(this);

            // get about us text from firebase
            db = FirebaseFirestore.getInstance();

            db.collection("AboutUs")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(   Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                   String indratechAboutText = document.getString("indratechAboutText");
                                    aboutUsEd.setText(indratechAboutText);

                                }
                            }
                        }
                    });



        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_aboutUs_back:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onBackpressed() {
        getVDActivity().getBrowserManager().unhideCurrentWindow();
        getFragmentManager().beginTransaction().remove(this).commit();
    }
}
