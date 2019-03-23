package ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.anonymous.catering.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ui.activities.LoginActivity;


/**
 * A simple {@link } subclass.
 */
public class ProfileFragment extends Fragment {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    public ProfileFragment() {
        // Required empty public constructor
    }

    //get firebase auth instance
    //auth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view;
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        };



        return view;

    }

    private void Logout() {
        auth.signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

//    public void openSignOut(View view) {
//        auth.signOut();
//        Intent i = new Intent(getActivity(), LoginActivityBackUp3.class);
//        startActivity(i);
//        //startActivity(new Intent(this, LoginActivityBackUp3.class));
//
//    }


}
