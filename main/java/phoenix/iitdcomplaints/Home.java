package phoenix.iitdcomplaints;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import phoenix.iitdcomplaints.R;

public class Home extends AppCompatActivity {

    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnRegister = (Button) findViewById(R.id.button);
// on clicking the registration button the person is taken to Login page
        btnRegister.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                              Intent intent = new Intent(Home.this, Login.class);
                                               startActivity(intent);


                                           }
                                       }
        );





    }

}
