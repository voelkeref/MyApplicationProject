package com.example.myapplication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class MainActivity extends FragmentActivity
{
    global gl = new global();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            LoginFragment fv = new LoginFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, fv);
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed()
    {
    }

    public class LoginFragment extends Fragment
    {
        private Button btnClick;

        public LoginFragment()
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            btnClick = (Button) rootView.findViewById(R.id.signinbutton);
            btnClick.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    HttpClient client = new DefaultHttpClient();
                    gl.username = ((EditText) findViewById(R.id.editText)).getText().toString();
                    String password = ((EditText) findViewById(R.id.editText2)).getText().toString();
                    password = createsha(password);
                    password = createmd5(password);
                    HttpGet httpGet = new HttpGet("community.dur.ac.uk/cs.seg03/api/?core=login&username="+gl.username+"&password="+password+")");
                    try
                    {
                        HttpResponse response = client.execute(httpGet);
                        StatusLine statusLine
                                = response.getStatusLine();
                        if (statusLine.getStatusCode() == 200)
                        {
                            HttpEntity entity = response.getEntity();
                            InputStream content = entity.getContent();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                            String line = reader.readLine();
                            if(line.contains("error"))
                            {
                                TextView tv = (TextView)findViewById(R.id.editText);
                                CharSequence error = "Invalid Login";
                                tv.append(error);
                            }
                            else
                            {
                                Intent intent = new Intent(MainActivity.this, AnimalSlider.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                    catch(Exception e){}
                }
            });
            return rootView;
        }

        public String createsha(String input)
        {
            try
            {
                MessageDigest mDigest = MessageDigest.getInstance("SHA1");
                byte[] result = mDigest.digest(input.getBytes());
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < result.length; i++) {
                    sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
                }
                return sb.toString();
            }
            catch(Exception e){}
            return null;
        }

        public String createmd5(String input)
        {
            try
            {
                MessageDigest mDigest = MessageDigest.getInstance("MD5");
                byte[] result = mDigest.digest(input.getBytes());
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < result.length; i++) {
                    sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
                }
                return sb.toString();
            }
            catch(Exception e){}
            return null;
        }
    }
}
