package evensel.pixelclearseo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class ActSplash extends ActBase
{
	@Override
	protected void onCreate(Bundle pSavedInstance)
	{
		super.onCreate(pSavedInstance);
		setContentView(R.layout.act_splash);

		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				startActivity(new Intent(ActSplash.this, ActDomains.class));
			}
		}, 2000);
	}
}
