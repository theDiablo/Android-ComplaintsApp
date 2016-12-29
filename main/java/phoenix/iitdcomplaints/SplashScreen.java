package phoenix.iitdcomplaints;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/*
The SplashScreen Activity is the first activity called after the launch of the app.
It sets the view of the screen as the layout splash.xml for 2 seconds.
And finally calls the next class Home


 */


/**
 * Created by Gautam on 3/26/2016.
 */

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreen.this,Home.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}
