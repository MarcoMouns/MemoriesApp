package dude.memories.Activities;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import dude.memories.APIs.BaseApiService;
import dude.memories.APIs.UtilsApi;
import dude.memories.Models.UserModel;
import dude.memories.R;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    Calendar myCalendar;

    @BindView(R.id.UserName_Profile)
    EditText _Name;

    @BindView(R.id.Password_Profile)
    EditText _Password;
    @BindView(R.id.Mail_Profile)
    EditText _Mail;
    @BindView(R.id.Phone_Profile)
    EditText _Phone;
    @BindView(R.id.Birth_Profile)
    EditText _Birth;

    @BindView(R.id.Update_Profile)
    Button _Update_Profile;
    @BindView(R.id.Cancel_Profile)
    Button _Cancel_Profile;

    String ID;
    BaseApiService mApiService;
    SharedPreferences prefs;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        mApiService = UtilsApi.getAPIService();
        prefs = getSharedPreferences("sh", MODE_PRIVATE);
        if (prefs.contains("id")) {
            ID = prefs.getString("id", null);

        }
        myCalendar = Calendar.getInstance();
        datePickerDialogOnListener();
        _Birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog();
            }
        });
        updateProfile();
        _Cancel_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        _Update_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });
    }

    private void datePickerDialog() {

        new DatePickerDialog(ProfileActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void datePickerDialogOnListener() {
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

    }

    private void updateUser() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            mApiService.updateInfo(ID, _Name.getText().toString(), _Password.getText().toString()
                    , _Mail.getText().toString(), _Phone.getText().toString(), format.parse(_Birth.getText().toString())).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()) {
                        Toasty.success(ProfileActivity.this, "Update success").show();
                        finish();
                    } else {
                        Toasty.error(ProfileActivity.this, response.message()).show();
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

    private void updateProfile() {


        mApiService.getInfo(ID).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    UserModel userModel = response.body();
                    _Name.setText(userModel.getUsername());
                    _Mail.setText(userModel.getEmail());
                    _Phone.setText(userModel.getPhone());
                    Date date = userModel.getDateOfBirth();
                    _Birth.setText(dateFormat.format(date));

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        _Birth.setText(sdf.format(myCalendar.getTime()));
    }
}
