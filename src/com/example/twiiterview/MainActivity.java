package com.example.twiiterview;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.ConfigurationContext;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {


	

	private Twitter _twitter = null;
	public Configuration config;
	public static OAuthAuthorization myOauth;
	public static RequestToken requestToken;
	
	static final String CALLBACK_URL = "http://twitter.com/";
	static final String CONSUMER_ID = "CN1krVYeragTQdJYEm4BA";
	static final String CONSUMER_SECRET = "1DESgiLUiUnvfMnoxO90XZPExIhiJt1cS5IAFbI1w";
	private RequestToken _reqToken = null;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO 自動生成されたメソッド・スタブ
		
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.activity_main, menu);
		
		return super.onCreateOptionsMenu(menu);
		
	}
	
	public void now(View v){

		SharedPreferences pref = getSharedPreferences("twitter", MODE_PRIVATE);

		String oauthToken  = pref.getString("oauth_token", "");
		String oauthTokenSecret = pref.getString("oauth_token_secret", "");

		//Log.d("oauthTOken",oauthToken);
		if(oauthToken.equals("")){
			this.logIn();

		}else{
			Log.d("token",oauthToken+":"+oauthTokenSecret);

			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(CONSUMER_ID);
			builder.setOAuthConsumerSecret(CONSUMER_SECRET);
			builder.setOAuthAccessToken(oauthToken);
			builder.setOAuthAccessTokenSecret(oauthTokenSecret);
			Configuration config = builder.build();

			Twitter twitter = new TwitterFactory(config).getInstance();
			try {
				Integer i = (int)(Math.random()*10);
				
				twitter.updateStatus("出目は"+i.toString()+" #ictTestQuiz");
				
			} catch (TwitterException e) {
				// TODO 自動生成された catch ブロック
				Toast.makeText(this, e.toString(),Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    
        
    }
    
    public void logIn(){
    	
    	ConfigurationBuilder builder = new ConfigurationBuilder();  
        builder.setOAuthConsumerKey(CONSUMER_ID);
        builder.setOAuthConsumerSecret(CONSUMER_SECRET);  
        Configuration configuration = builder.build();
        
        myOauth = new OAuthAuthorization(configuration);
        myOauth.setOAuthAccessToken(null);
        try {
        	requestToken = myOauth.getOAuthRequestToken(CALLBACK_URL);
        } catch (TwitterException e) {
        	// TODO 自動生成された catch ブロック
        	e.printStackTrace();
        }
        Log.d("oauthTOken","null");
        Intent intent = new Intent(this,OAuthActivity.class);
        intent.putExtra("auth_url", requestToken.getAuthorizationURL());
        this.startActivityForResult(intent, 0);

    }
 
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自動生成されたメソッド・スタブ
    	
    	switch (item.getItemId()) {
		case R.id.menu_settings:
			this.clearPref();
			break;
    	}
		return super.onOptionsItemSelected(item);
	}

    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
    	//super.onActivityResult(requestCode, resultCode, intent);
    	if(requestCode == 0){
    		Log.d("main","main");
    		try {
    			AccessToken accessToken = myOauth.getOAuthAccessToken(requestToken,intent.getExtras().getString("oauth_verifier"));
    			SharedPreferences pref = getSharedPreferences("twitter", MODE_PRIVATE);
    			SharedPreferences.Editor editor = pref.edit();
    			editor.putString("oauth_token", accessToken.getToken());
    			editor.putString("oauth_token_secret", accessToken.getTokenSecret());
    			editor.putString("status", "available");
    			editor.commit();

    			Log.d("result",accessToken.getToken()+":"+accessToken.getTokenSecret());

    		} catch (TwitterException e) {
    			// TODO 自動生成された catch ブロック
    			e.printStackTrace();
    		}
    	}
    	
    }
    
    public void clearPref(){
    	
    	SharedPreferences pref = getSharedPreferences("twitter", MODE_PRIVATE);
    	Editor editor = pref.edit();
    	editor.clear().commit();
    	
    }

}