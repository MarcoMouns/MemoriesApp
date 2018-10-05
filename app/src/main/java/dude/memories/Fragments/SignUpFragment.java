package dude.memories.Fragments;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import dude.memories.APIs.BaseApiService;
import dude.memories.APIs.UtilsApi;
import dude.memories.Activities.MapsActivity;
import dude.memories.Models.UserModel;
import dude.memories.R;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class SignUpFragment extends Fragment {

    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar;
    @BindView(R.id.UserName_Register)
    EditText _Name;

    @BindView(R.id.Password_Register)
    EditText _Password;
    @BindView(R.id.Mail_Register)
    EditText _Mail;
    @BindView(R.id.Phone_Register)
    EditText _Phone;
    @BindView(R.id.Birth_Register)
    EditText _Birth;
    @BindView(R.id.Save)
    Button _Save;
    @BindView(R.id.HaveAccount)
    TextView _HaveAccount;
    BaseApiService mApiService;
    SharedPreferences.Editor sh;

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.signup_layout, container, false);
        ButterKnife.bind(this, view);
        mApiService = UtilsApi.getAPIService();
        sh = Objects.requireNonNull(getContext()).getSharedPreferences("sh", MODE_PRIVATE).edit();
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setTitle("Create Account");
        dialog.setMessage("Please wait while create your account !");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        myCalendar = Calendar.getInstance();
        datePickerDialogOnListener();
        _Birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog();
            }
        });
        _HaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignInFragment();
            }
        });
        _Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestRegister();
            }
        });
        return view;
    }

    private void goToSignInFragment() {
        SignInFragment fragment = new SignInFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.signup_layout, fragment);
        transaction.commit();
    }

    private void datePickerDialog() {
        new DatePickerDialog(getContext(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void datePickerDialogOnListener() {
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void requestRegister() {

        sh.remove("id").apply();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            mApiService.registerRequest(_Name.getText().toString(), _Password.getText().toString(), _Mail.getText().toString()
                    , _Phone.getText().toString(), format.parse(_Birth.getText().toString())).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()) {
                        Toasty.success(getContext(), "Register is successful").show();
                        UserModel model = response.body();
                        sh.putString("id", model.getId()).apply();
                        startActivity(new Intent(getContext(), MapsActivity.class));
                        Objects.requireNonNull(getActivity()).finish();
                    } else {
                        Toasty.error(getContext(), response.message()).show();
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {

                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        _Birth.setText(sdf.format(myCalendar.getTime()));
    }


}
