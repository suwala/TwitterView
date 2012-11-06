package com.example.twiiterview;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	static final String CALLBACK_URL = "TEST";
	static final String CONSUMER_ID = "CN1krVYeragTQdJYEm4BA";
	static final String CONSUMER_SECRET = "1DESgiLUiUnvfMnoxO90XZPExIhiJt1cS5IAFbI1w";
	private RequestToken _reqToken = null;
	private Twitter _twitter = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.nownow();
               
    }
   

    public void nownow(){
    	this._twitter = new TwitterFactory().getInstance();
    	this._twitter.setOAuthConsumer(CONSUMER_ID, CONSUMER_SECRET);

    	try{
    		this._reqToken = this._twitter.getOAuthRequestToken(CALLBACK_URL);

    	}catch(TwitterException e){
    		//finish();
    	}
    	//Log.d("main","aaa"+this._reqToken.getAuthorizationURL());
    	//Toast.makeText(this, this._reqToken.getAuthorizationURL(), Toast.LENGTH_SHORT).show();
    }
    
    
}