package com.sqa.sqatest;


import android.content.Intent;
import android.os.Bundle;


import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlaylistEventListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;

public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

	static private final String DEVELOPER_KEY = "AIzaSyBubAsO8R403LPzMjuHQT6RB916OYxErAw";
    static private final String VIDEO_ID = "4OrCA1OInoo";
    static private final String LIST_ID = "PL-qzE-Xk6EcGUp-NVX3ryqtt1PZiwF-NE";
    //WJN_c61-uO8
    //UITm9ycomgA
	Intent intent;
	Bundle bundle;
	YouTubePlayerView youTubeView;
	YouTubePlayer bPlayer;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.youtubelayout);
		
		intent = this.getIntent();
		bundle = intent.getExtras();
		System.out.println("bundle: "+bundle.getString("item"));
		
	    youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
	    System.out.println("THIS: "+this);
	    youTubeView.initialize(DEVELOPER_KEY, this);
	    
	}
	
	@Override
	public void onInitializationFailure(Provider arg0, YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub
		System.out.println("onInitializationFailure");
		
	}

	@Override
	public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean arg2) {
		// TODO Auto-generated method stub
		System.out.println("onInitializationSuccess");
		bPlayer=player;
		player.setPlayerStateChangeListener(playerStateChangeListener);
		player.setPlaybackEventListener(playbackEventListener);
	    if (!arg2) {
	    	player.loadVideo(VIDEO_ID);
	    }
	}
	
	protected YouTubePlayer.Provider getYouTubePlayerProvider() {
		return (YouTubePlayerView) findViewById(R.id.youtube_view);
	}
	
	private PlaybackEventListener playbackEventListener = new PlaybackEventListener() {

		@Override
		public void onBuffering(boolean arg0) {
			System.out.println("onBuffering");
		}

		@Override
		public void onPaused() {
			System.out.println("onPaused");
		}

		@Override
		public void onPlaying() {
			System.out.println("onPlaying");
		}

		@Override
		public void onSeekTo(int arg0) {
			System.out.println("onSeekTo");
		}

		@Override
		public void onStopped() {
			System.out.println("onStopped");
		}

	};

	private PlayerStateChangeListener playerStateChangeListener = new PlayerStateChangeListener() {

		@Override
		public void onAdStarted() {
			System.out.println("onAdStarted");
		}

		@Override
		public void onLoaded(String arg0) {
			System.out.println("onLoaded");
		}

		@Override
		public void onLoading() {
			System.out.println("onLoading");
		}

		@Override
		public void onVideoEnded() {
			System.out.println("onVideoEnded");
			setValue("PASS");
			System.out.println();
			bPlayer.loadVideo(VIDEO_ID);
			//youTubeView.initialize(DEVELOPER_KEY, );
			//finish();
		}

		@Override
		public void onVideoStarted() {
			System.out.println("onVideoStarted");
		}

		@Override
		public void onError(com.google.android.youtube.player.YouTubePlayer.ErrorReason errorReason) {
			// TODO Auto-generated method stub
			System.out.println("onError");
			System.out.println(errorReason);
			setValue(errorReason.toString());
			finish();
			
		}
	};

	public void setValue(String tempResult){
		System.out.println(tempResult);
		bundle.putString("youtubeResult",tempResult);  
		intent.putExtras(bundle);
		setResult(1, intent);
	}

}
