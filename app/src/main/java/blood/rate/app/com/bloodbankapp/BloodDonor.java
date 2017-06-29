package blood.rate.app.com.bloodbankapp;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by sagarsrao on 28-06-2017.
 */

@IgnoreExtraProperties
public class BloodDonor {


    public String name;

    public long phone;

    public String bloodGroup;

    public String city;

    public String date;

    public BloodDonor() {

    }


    public BloodDonor(String name, long phone, String bloodGroup, String city, String date) {
        this.name = name;
        this.phone = phone;
        this.bloodGroup = bloodGroup;
        this.city = city;
        this.date = date;
    }
}
