package tech.iosd.alive2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hypertrack.lib.HyperTrack;
import com.hypertrack.lib.callbacks.HyperTrackCallback;
import com.hypertrack.lib.models.ErrorResponse;
import com.hypertrack.lib.models.SuccessResponse;
import com.hypertrack.lib.models.UserParams;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText Name, Phone, Code;
        Button submit,stop;
        checkForLocationSettings();
        HyperTrack.initialize(MainActivity.this, "sk_test_fd7776598c620489d5b89dac65ec1c4fce394371");


        Name = (EditText) findViewById(R.id.editText);
        Phone = (EditText) findViewById(R.id.editText2);
        Code = (EditText) findViewById(R.id.editText3);
        submit = (Button) findViewById(R.id.btn_click);
        stop = (Button)findViewById(R.id.button3);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Stop HyperTrack SDK
                HyperTrack.stopTracking();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserParams userParams = new UserParams().setName(Name.getText().toString()).setPhone(Phone.getText().toString()).setLookupId(Code.getText().toString());
                HyperTrack.getOrCreateUser(userParams, new HyperTrackCallback() {
                    @Override
                    public void onSuccess(@NonNull SuccessResponse successResponse) {
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();

                        onUserLoginSuccess();
                    }

                    @Override
                    public void onError(@NonNull ErrorResponse errorResponse) {
                        Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);

        if (requestCode == HyperTrack.REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                // Check if Location Settings are enabled to proceed
                checkForLocationSettings();

            } else {
                // Handle Location Permission denied error
                Toast.makeText(this, "Location Permission denied.",
                        Toast.LENGTH_SHORT).show();
                Log.e("*********","location permissaion denied" );
            }
        }


    }

        /**
         * Call this method when user has successfully logged in
         */



    private void checkForLocationSettings() {
        if (!HyperTrack.checkLocationPermission(this)) {
            HyperTrack.requestPermissions(this);
            return;
        }
        if (!HyperTrack.checkLocationServices(this)) {
            HyperTrack.requestLocationServices(this);
        }

    }
    private void onUserLoginSuccess() {
        HyperTrack.startTracking(new HyperTrackCallback() {
            @Override
            public void onSuccess(@NonNull SuccessResponse successResponse) {
                // Hide Login Button loader
                //loginBtnLoader.setVisibility(View.GONE);

                Toast.makeText(MainActivity.this, "Start Tracking success",Toast.LENGTH_SHORT).show();

                // Start User Session by starting MainActivity
                Intent mainActivityIntent = new Intent(MainActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainActivityIntent);
                finish();
            }

            @Override
            public void onError(@NonNull ErrorResponse errorResponse) {
                // Hide Login Button loader
                //loginBtnLoader.setVisibility(View.GONE);

                Toast.makeText(MainActivity.this,errorResponse.getErrorMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }



}
