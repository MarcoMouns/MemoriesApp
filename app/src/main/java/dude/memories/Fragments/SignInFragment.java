package dude.memories.Fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import dude.memories.APIs.BaseApiService;
import dude.memories.APIs.UtilsApi;
import dude.memories.Activities.MapsActivity;
import dude.memories.Models.LoginResponseModel;
import dude.memories.R;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class SignInFragment extends Fragment {


    @BindView(R.id.Create_Account)
    TextView _SignUP_view;
    @BindView(R.id.User_Login)
    EditText _UserEditText;
    @BindView(R.id.Password_Login)
    EditText _PasswordEditText;

    @BindView(R.id.Log_in)
    Button _LogIN;

    BaseApiService mApiService;
    SharedPreferences.Editor sh;

    ProgressDialog dialog;

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signin_layout, container, false);
        ButterKnife.bind(this, view);
        mApiService = UtilsApi.getAPIService();
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("User Login");
        dialog.setMessage("Please wait while Log-In your account !");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        sh = Objects.requireNonNull(getContext()).getSharedPreferences("sh", MODE_PRIVATE).edit();
        _LogIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLogin();
            }
        });
        _SignUP_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment fragment = new SignUpFragment();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.signin_layout, fragment);
                transaction.commit();
            }
        });
        return view;
    }

    private void requestLogin() {
        sh.remove("id").apply();
        mApiService.loginRequest(_UserEditText.getText().toString(), _PasswordEditText.getText().toString()).enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                if (response.isSuccessful()) {
                    Toasty.success(getContext(), "Login is successful.").show();
                    LoginResponseModel model = response.body();
                    sh.putString("id", model.getUid()).apply();
                    startActivity(new Intent(getContext(), MapsActivity.class));
                    Objects.requireNonNull(getActivity()).finish();
                } else {
                    Toasty.error(getContext(), response.message()).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {

            }
        });
        dialog.dismiss();
    }

}
