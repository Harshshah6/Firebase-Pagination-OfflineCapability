package legendary.streamer.firebasedbexample;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //enable firebase offline capabilities
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}