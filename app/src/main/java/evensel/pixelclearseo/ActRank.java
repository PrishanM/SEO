package evensel.pixelclearseo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmn.HttpCon;
import com.cmn.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ActRank extends ActBase {

	private Context context;

	public static class Arg {
		public static String getDomain() {
			return getArg("domain");
		}

		public static String getKeyword() {
			return getArg("keyword");
		}

		private static String getArg(String sName) {
			return ActRank.class.getCanonicalName() + "_" + sName;
		}
	}

	@Override
	protected void onCreate(Bundle pSavedInstance) {
		super.onCreate(pSavedInstance);
		setContentView(R.layout.act_rank);

		final ActionBar abar = getSupportActionBar();
		View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_text, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.mytext);
		textviewTitle.setText("Website Rank");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		abar.setDisplayShowTitleEnabled(false);

		context = this;

		Intent pIntent = getIntent();
		String sDomain = pIntent.getStringExtra(Arg.getDomain());
		String sKeyword = pIntent.getStringExtra(Arg.getKeyword());

		Log.d("domain = " + sDomain + " keyword = " + sKeyword);

		if (sDomain == null || sDomain.isEmpty()) {
			err("please enter valid domain", 2);
			return;
		}

		if (sKeyword == null || sKeyword.isEmpty()) {
			err("please enter valid keyword", 2);
			return;
		}

		try {
			JSONObject pJMsg = new JSONObject();
			pJMsg.put("url", sDomain);
			pJMsg.put("keyword", sKeyword);
			pJMsg.put("name", Data.getInst().getName(this));
			pJMsg.put("email", Data.getInst().getEmail(this));
			pJMsg.put("telephone", Data.getInst().getContactNumber(this));

			HttpCon pHttp = new HttpCon(HttpCon.Method.Post, Cfg.getInst().getUrlRank(), pJMsg.toString(), new HttpCon.Callback() {
				@Override
				public void onOk(HttpCon pCon, String sResponse) {
					Log.d("response = " + sResponse);
					setRank(sResponse);
				}

				@Override
				public void onError(HttpCon pCon, String sErr) {
					Log.d("error " + sErr);
					setResponseErr(sErr);
				}
			});

			pHttp.send();
		} catch (JSONException pExe) {
			pExe.printStackTrace();
			dis("exception " + pExe.toString());
			finish();
		}

		p_LblDomain = (TextView) findViewById(R.id.lbl_domain);
		p_LblDomain.setMovementMethod(LinkMovementMethod.getInstance());
		p_LblDomain.setText(Html.fromHtml("<font color=\"#FFFFFF\"><a href=" + sDomain + ">" + sDomain + "</font>"));
		p_LblKeyword = (TextView) findViewById(R.id.lbl_keyword);
		p_LblKeyword.setText(sKeyword);

		ImageView imageViewEmail = (ImageView) findViewById(R.id.actionEmail);
		ImageView imageViewCall = (ImageView) findViewById(R.id.actionCall);

		imageViewEmail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.fromParts("mailto", "info@pixelclear.com", null));
				startActivity(i);
			}
		});

		imageViewCall.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:0777656565"));
				if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					requestPermission(Manifest.permission.CALL_PHONE, 1000);
				}else{
					startActivity(callIntent);
				}

			}
		});
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

	private void requestPermission(String permissionName, int permissionRequestCode) {
		ActivityCompat.requestPermissions(this,
				new String[]{permissionName}, permissionRequestCode);
	}

	@Override
	public void onRequestPermissionsResult(
			int requestCode,
			String permissions[],
			int[] grantResults) {
		switch (requestCode) {
			case 1000:
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(context, "Permission Granted!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show();
				}
		}
	}
}
