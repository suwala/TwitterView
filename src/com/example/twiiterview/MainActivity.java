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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	static final String CALLBACK_URL = "http://twitter.com/";
	static final String CONSUMER_ID = "CN1krVYeragTQdJYEm4BA";
	static final String CONSUMER_SECRET = "1DESgiLUiUnvfMnoxO90XZPExIhiJt1cS5IAFbI1w";
	private RequestToken _reqToken = null;
	private Twitter _twitter = null;
	public Configuration config;
	public static OAuthAuthorization myOauth;
	public static RequestToken requestToken;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
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
        
        this.nownow();
        
        
        //setContentView(R.layout.activity_main);
               
    }
   

    public void nownow(){
    	this._twitter = new TwitterFactory().getInstance();
    	this._twitter.setOAuthConsumer(CONSUMER_ID, CONSUMER_SECRET);

    	try{
    		this._reqToken = this._twitter.getOAuthRequestToken(CALLBACK_URL);
    		//Toast.makeText(this, this._reqToken.getAuthorizationURL(), Toast.LENGTH_SHORT).show();
    	}catch(TwitterException e){
    		e.printStackTrace();
    	}
    	//Log.d("main","aaa"+this._reqToken.getAuthorizationURL());
    	//Toast.makeText(this, this._reqToken.getAuthorizationURL(), Toast.LENGTH_SHORT).show();
    	
    	this.callOauth(_reqToken.getAuthorizationURL());
    	
    	/*
    	ConfigurationBuilder builder = new ConfigurationBuilder();
    	builder.setOAuthConsumerKey(CONSUMER_ID);
    	builder.setOAuthConsumerSecret(CONSUMER_SECRET);
    	builder.setOAuthAccessToken(oAuthAccessToken)
    	*/
    }
    
    private void callOauth(String _url){
    	
    	WebView webView = new WebView(this);
    	
    	LinearLayout _ll = (LinearLayout)this.findViewById(R.id.LinearLayout01);
    	_ll.addView(webView);
    	webView.setWebViewClient(new WebViewClient(){
    		
    		// ページ描画完了時に呼ばれる。
    		public void onPageFinished(WebView view, String url) {
    			super.onPageFinished(view, url);
    			
    			//TwitterDevelopersで登録したCallbackURLが戻ってくるので　URLが一致したとき認証が成功したものとする
    			if(url != null && url.startsWith(CALLBACK_URL)){
    				//URLパラメータを分解する
    				 String[] urlParameters = url.split("\\?")[1].split("&");
    				 String oauthToken = "";
    				 String oauthVerifier = "";

    				 // oauth_tokenをURLパラメータから切り出す。
    				 if(urlParameters[0].startsWith("oauth_token")){
    					 oauthToken = urlParameters[0].split("=")[1];
    				 }else if(urlParameters[1].startsWith("oauth_token")){
    					 oauthToken = urlParameters[1].split("=")[1];
    				 }
    				 
    				 // oauth_verifierをURLパラメータから切り出す。
    				 if(urlParameters[0].startsWith("oauth_verifier")){
    					 oauthVerifier = urlParameters[0].split("=")[1];
    				 }else if(urlParameters[1].startsWith("oauth_verifier")){
    					 oauthVerifier = urlParameters[1].split("=")[1];
    				 }

    				 Toast.makeText(MainActivity.this, oauthToken, Toast.LENGTH_SHORT).show();
    				 Toast.makeText(MainActivity.this, oauthVerifier, Toast.LENGTH_SHORT).show();

    				 AccessToken accessToken = null;

    				 try {
    					 accessToken =
    						 myOauth.getOAuthAccessToken(requestToken,oauthVerifier);
    					 
    					 ConfigurationBuilder builder = new ConfigurationBuilder();
        				 builder.setOAuthConsumerKey(CONSUMER_ID);
        				 builder.setOAuthConsumerSecret(CONSUMER_SECRET);
        				 builder.setOAuthAccessToken(oauthToken);
        				 builder.setOAuthAccessTokenSecret(accessToken.getTokenSecret());
        				 Configuration config = builder.build();

        				 Twitter twitter = new TwitterFactory(config).getInstance();
        				 try {
        					 twitter.updateStatus("てすとなう");
        					 Log.d("test","test");
        				 } catch (TwitterException e) {
        					 // TODO 自動生成された catch ブロック
        					 e.printStackTrace();
        				 }
    					 
    					 
    				 } catch (TwitterException e1) {
    					 // TODO 自動生成された catch ブロック
    					 e1.printStackTrace();
    				 }

    				 
    			}
    		}

    	});
    	
    	//Activity1で設定した認証ページを表示。
    	webView.loadUrl(_url);
    	
    	

    	
    }
    
}