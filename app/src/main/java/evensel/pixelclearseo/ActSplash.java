package evensel.pixelclearseo;

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

		getSupportActionBar().hide();

		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				if(Data.getInst().getName(getApplicationContext())==null || Data.getInst().getName(getApplicationContext()).isEmpty() || Data.getInst().getName(getApplicationContext()).equalsIgnoreCase("")){
					Intent pIntent = new Intent(ActSplash.this, ActDomain.class);
					pIntent.putExtra(ActDomain.Arg.getIsNew(), true);
					startActivity(pIntent);
				}else{
					startActivity(new Intent(ActSplash.this, ActDomains.class));
				}

			}
		}, 2000);
	}
}
