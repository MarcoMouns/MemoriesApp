package dude.memories.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickClick;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import dude.memories.Adapter.CustomInfoWindowAdapter;
import dude.memories.Helper.DB_Helper;
import dude.memories.Models.MemoryModel;
import dude.memories.R;
import es.dmoral.toasty.Toasty;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int CAM_CODE = 1888;
    byte[] byteArray;
    Uri data1;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatbutton;
    @BindView(R.id.home_bar)
    Toolbar toolbar;
    long time;
    SharedPreferences prefs;
    DB_Helper databaseHelper;
    MemoryModel memoryModel;
    SharedPreferences.Editor sh;
    PickImageDialog pickImageDialog;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        toolbar.setTitle("Memories Map");
        setSupportActionBar(toolbar);


        databaseHelper = new DB_Helper(this);
        memoryModel = new MemoryModel();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.current_location);
        mapFragment.getMapAsync(this);

        prefs = getSharedPreferences("sh", MODE_PRIVATE);

        floatbutton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapsActivity.this);
                alertDialogBuilder.setCancelable(true);
                LayoutInflater layoutInflater = LayoutInflater.from(MapsActivity.this);
                View popupInputDialogView = layoutInflater.inflate(R.layout.popup_newmemory, null);
                alertDialogBuilder.setView(popupInputDialogView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();
                final EditText title = popupInputDialogView.findViewById(R.id.popup_title);
                final EditText description = popupInputDialogView.findViewById(R.id.popup_description);
                prefs.edit().remove("new_location_lat").apply();
                prefs.edit().remove("new_location_long").apply();
                sh = getSharedPreferences("sh", MODE_PRIVATE).edit();
                sh.remove("new_location_lat").apply();
                sh.remove("new_location_long").apply();
                Button popup_image = popupInputDialogView.findViewById(R.id.popup_image);
                popup_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(MapsActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                1);
                        uploadimage();
                    }
                });
                Button popup_location = popupInputDialogView.findViewById(R.id.popup_location);
                popup_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MapsActivity.this, SelectLocationActivity.class));
                        Toasty.warning(MapsActivity.this, "Press Long to select location").show();
                    }
                });
                Button cancel = popupInputDialogView.findViewById(R.id.button_cancel_user_data);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toasty.warning(MapsActivity.this, "You have add no data.").show();
                        alertDialog.dismiss();
                    }
                });

                Button add = popupInputDialogView.findViewById(R.id.button_save_user_data);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (prefs.contains("new_location_lat") && prefs.contains("new_location_long")) {
                            memoryModel.setLat(prefs.getString(("new_location_lat"), null));
                            memoryModel.setLng(prefs.getString(("new_location_long"), null));
                            memoryModel.setDescription(description.getText().toString());
                            memoryModel.setTitle(title.getText().toString());
                            memoryModel.setPicture(byteArray);
                            databaseHelper.addUser(memoryModel);

                            alertDialog.dismiss();
                            startActivity(new Intent(MapsActivity.this, MapsActivity.class));
                            finish();
                        } else {
                            Toasty.warning(MapsActivity.this, "plz select location").show();
                        }
                    }
                });
            }
        });

    }

    public void uploadimage() {
        byteArray = null;
        pickImageDialog = PickImageDialog.build(new PickSetup()).setOnClick(new IPickClick() {
            @Override
            public void onGalleryClick() {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1889);
                pickImageDialog.dismiss();
            }

            @Override
            public void onCameraClick() {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1888);
                pickImageDialog.dismiss();
            }
        }).show(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAM_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();

            }
        }
        if (requestCode == 1889 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                data1 = data.getData();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                FileInputStream fis;
                try {
                    fis = new FileInputStream(new File(getRealPathFromURI(data1)));
                    byte[] buf = new byte[1024];
                    int n;
                    while (-1 != (n = fis.read(buf)))
                        baos.write(buf, 0, n);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                byteArray = baos.toByteArray();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapsActivity.this);
                alertDialogBuilder.setCancelable(true);
                LayoutInflater layoutInflater = LayoutInflater.from(MapsActivity.this);

                final View popupInputDialogView = layoutInflater.inflate(R.layout.popup_newmemory, null);
                alertDialogBuilder.setView(popupInputDialogView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();
                LinearLayout locationselect = popupInputDialogView.findViewById(R.id.locationselect);
                locationselect.setVisibility(View.GONE);

                Button cancel = popupInputDialogView.findViewById(R.id.button_cancel_user_data);
                Button image = popupInputDialogView.findViewById(R.id.popup_image);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(MapsActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                1);
                        uploadimage();
                    }
                });
                final EditText title = popupInputDialogView.findViewById(R.id.popup_title);
                final EditText description = popupInputDialogView.findViewById(R.id.popup_description);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toasty.warning(MapsActivity.this, "You have add no data.").show();
                        alertDialog.dismiss();
                    }
                });

                Button add = popupInputDialogView.findViewById(R.id.button_save_user_data);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MemoryModel memoryModel = new MemoryModel();
                        memoryModel.setTitle(title.getText().toString());
                        memoryModel.setDescription(description.getText().toString());
                        memoryModel.setLat(String.valueOf(latLng.latitude));
                        memoryModel.setLng(String.valueOf(latLng.longitude));
                        memoryModel.setPicture(byteArray);
                        databaseHelper.addUser(memoryModel);
                        alertDialog.dismiss();
                        startActivity(new Intent(MapsActivity.this, MapsActivity.class));
                    }
                });

            }
        });
        Gson gson = new Gson();
        if (databaseHelper.getAllMemories().size() != 0) {
            for (int i = 0; i < databaseHelper.getAllMemories().size(); i++) {
                Double lat = Double.valueOf(databaseHelper.getAllMemories().get(i).getLat());
                Double lng = Double.valueOf(databaseHelper.getAllMemories().get(i).getLng());
                String title2 = databaseHelper.getAllMemories().get(i).getTitle();
                String info = gson.toJson(databaseHelper.getAllMemories().get(i));
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(title2)).setSnippet(info);
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
            }
        }

    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        if (menu instanceof MenuBuilder) {

            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {

            fm.popBackStack();
        } else {
            if (time + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                finish();
            } else {
                Toasty.info(this, "Press Back Again.", Toast.LENGTH_SHORT, true).show();
            }
            time = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.memories_list) {
            Toasty.success(MapsActivity.this, "List...").show();
            startActivity(new Intent(MapsActivity.this, MemoriesListActivity.class));
        } else if (item.getItemId() == R.id.edit_profile) {
            Toasty.success(MapsActivity.this, "Edit...").show();
            startActivity(new Intent(MapsActivity.this, ProfileActivity.class));
        } else if (item.getItemId() == R.id.log_out) {
            prefs.edit().remove("id").apply();
            Toasty.success(MapsActivity.this, "Out...").show();
            startActivity(new Intent(MapsActivity.this, HomeActivity.class));
            finish();
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(MapsActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }
}
