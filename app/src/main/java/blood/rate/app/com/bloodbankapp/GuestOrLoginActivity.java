package blood.rate.app.com.bloodbankapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class GuestOrLoginActivity extends AppCompatActivity {


    private static final String TAG = GuestOrLoginActivity.class.getSimpleName();

    EditText mName, mPhone, mBloodgroup, mCity, mDate;

    TextView txt_donor;

    Button mSubmit;

    String donorId;


    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_or_login);

        mName = (EditText) findViewById(R.id.edName);
        mPhone = (EditText) findViewById(R.id.edPhone);
        mBloodgroup = (EditText) findViewById(R.id.edBloodGroup);
        mCity = (EditText) findViewById(R.id.edCity);
        mDate = (EditText) findViewById(R.id.edDate);
        mSubmit = (Button) findViewById(R.id.submit);

        txt_donor = (TextView)findViewById(R.id.txt_donor);
//Get the Firebase Instance
        mFirebaseInstance = FirebaseDatabase.getInstance();
// get reference to 'BloodDonor' node
        mFirebaseDatabase = mFirebaseInstance.getReference("BloodDonor");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("Realtime Database");

        // app_title change listener
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");

                String appTitle = dataSnapshot.getValue(String.class);

                // update toolbar title
                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

        //save or update the user
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mName.getText().toString();

                //long phone = Long.valueOf(Integer.parseInt(mPhone.getText().toString()));
                long phone = Long.parseLong(mPhone.getText().toString());

                String bg = mBloodgroup.getText().toString();

                String city = mCity.getText().toString();

                String date = mDate.getText().toString();


                // Check for already existed userId
                if (TextUtils.isEmpty(donorId)) {
                    createUser(name, phone, bg, city, date);
                } else {
                    updateUser(name, phone, bg, city, date);
                }

            }
        });
        toggleButton();

    }
//change the text
    private void toggleButton() {
        if (TextUtils.isEmpty(donorId)) {
            mSubmit.setText("Save");
        } else {
            mSubmit.setText("Update");
        }
    }
    //create Donor
    private void createUser(String name, long phone, String bg, String city, String date) {


        if (TextUtils.isEmpty(donorId)) {
            donorId = mFirebaseDatabase.push().getKey();

            BloodDonor bloodDonor = new BloodDonor(name,phone,bg,city,date);

            mFirebaseDatabase.child(donorId).setValue(bloodDonor);

            addUserChangeListener();

        }
    }



    private void addUserChangeListener() {


        // User data change listener
        mFirebaseDatabase.child(donorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BloodDonor bloodDonor = dataSnapshot.getValue(BloodDonor.class);

                // Check for null
                if (bloodDonor == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + bloodDonor.name + ", " + bloodDonor.phone + ", "+bloodDonor.bloodGroup+", "+bloodDonor.city+", "+bloodDonor.date);

                // Display newly updated name and email
                txt_donor.setText(bloodDonor.name + ", " + bloodDonor.phone + ", "+bloodDonor.bloodGroup+", "+bloodDonor.city+", "+bloodDonor.date);

                // clear edit text
                mName.setText("");
                mPhone.setText("");
                mBloodgroup.setText("");
                mCity.setText("");
                mDate.setText("");
                toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }


    private void updateUser(String name,
                            long phone, String bg,
                            String city, String date) {



        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(donorId).child("name").setValue(name);

        if (!TextUtils.isEmpty((String.valueOf(phone))))
            mFirebaseDatabase.child(donorId).child("phone").setValue(phone);

        if (!TextUtils.isEmpty(bg))
            mFirebaseDatabase.child(donorId).child("bg").setValue(bg);


        if (!TextUtils.isEmpty(city))
            mFirebaseDatabase.child(donorId).child("city").setValue(city);

        if (!TextUtils.isEmpty(date))
            mFirebaseDatabase.child(donorId).child("date").setValue(date);

    }



}
