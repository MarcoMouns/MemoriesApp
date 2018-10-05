package dude.memories.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import dude.memories.R;


public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_layout, container, false);

        Button signin = view.findViewById(R.id.Sign_In_Home);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment fragment = new SignInFragment();
                showFragment(fragment);
            }
        });

        TextView signUp = view.findViewById(R.id.Create_Account_Home);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignUpFragment fragment = new SignUpFragment();
                showFragment(fragment);
            }
        });
        return view;
    }


    public void showFragment(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.home_layout, fragment);
        transaction.addToBackStack("home");
        transaction.commit();
    }

}
