package evensel.pixelclearseo;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

public class ActBase extends Activity
{
	public void dis(String sMsg)
	{
		Toast.makeText(this, sMsg, Toast.LENGTH_LONG).show();
	}

	public void err(String sErr, int iExitTime)
	{
		dis(sErr);

		if (iExitTime == 0)
			finish();
		else if (iExitTime > 0)
			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					finish();
				}
			}, iExitTime * 1000);
	}
}
