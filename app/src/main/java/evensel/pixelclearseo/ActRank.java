package evensel.pixelclearseo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.cmn.HttpCon;
import com.cmn.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ActRank extends ActBase
{
	public static class Arg
	{
		public static String getDomain() { return getArg("domain"); }
		public static String getKeyword() { return getArg("keyword"); }

		private static String getArg(String sName) { return ActRank.class.getCanonicalName() + "_" + sName; }
	}

	@Override
	protected void onCreate(Bundle pSavedInstance)
	{
		super.onCreate(pSavedInstance);
		setContentView(R.layout.act_rank);

		final ActionBar abar = getSupportActionBar();
		View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_text, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.mytext);
		textviewTitle.setText("Website Rank");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		abar.setDisplayShowTitleEnabled(false);

		Intent pIntent = getIntent();
		String sDomain = pIntent.getStringExtra(Arg.getDomain());
		String sKeyword = pIntent.getStringExtra(Arg.getKeyword());

		Log.d("domain = " + sDomain + " keyword = " + sKeyword);

		if (sDomain == null || sDomain.isEmpty())
		{
			err("please enter valid domain", 2);
			return;
		}

		if (sKeyword == null || sKeyword.isEmpty())
		{
			err("please enter valid keyword", 2);
			return;
		}

		try
		{
			JSONObject pJMsg = new JSONObject();
			pJMsg.put("url", sDomain);
			pJMsg.put("keyword", sKeyword);
			pJMsg.put("name", Data.getInst().getName(this));
			pJMsg.put("email", Data.getInst().getEmail(this));
			pJMsg.put("telephone", Data.getInst().getContactNumber(this));

			HttpCon pHttp = new HttpCon(HttpCon.Method.Post, Cfg.getInst().getUrlRank(), pJMsg.toString(), new HttpCon.Callback()
			{
				@Override
				public void onOk(HttpCon pCon, String sResponse)
				{
					Log.d("response = " + sResponse);
					setRank(sResponse);
				}

				@Override
				public void onError(HttpCon pCon, String sErr)
				{
					Log.d("error " + sErr);
					setResponseErr(sErr);
				}
			});

			pHttp.send();
		}
		catch (JSONException pExe)
		{
			pExe.printStackTrace();
			dis("exception " + pExe.toString());
			finish();
		}

		p_LblDomain = (TextView) findViewById(R.id.lbl_domain);
		p_LblDomain.setText(sDomain);

		p_LblKeyword = (TextView) findViewById(R.id.lbl_keyword);
		p_LblKeyword.setText(sKeyword);
	}

	private void setRank(final String sData)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{	
					JSONObject pJData = new JSONObject(sData);
					String sRank = pJData.getString("rank");

					TextView pLblRank = ((TextView)findViewById(R.id.lbl_rank));
					pLblRank.setText(sRank);
					pLblRank.setVisibility(View.VISIBLE);

					findViewById(R.id.pro_rank).setVisibility(View.GONE);
				}
				catch (JSONException pExe)
				{
					pExe.printStackTrace();
				}
			}
		});
	}

	private void setResponseErr(final String sErr)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				TextView pLblRank = ((TextView)findViewById(R.id.lbl_rank));
				pLblRank.setText(sErr);
				pLblRank.setVisibility(View.VISIBLE);
;
				findViewById(R.id.pro_rank).setVisibility(View.GONE);
			}
		});
	}

	private TextView p_LblDomain;
	private TextView p_LblKeyword;
}
