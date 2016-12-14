package evensel.pixelclearseo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.cmn.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Data
{
	public void addDomain(Context pContext, String sDomain, String sKeyword)
	{
		Log.d("domain = " + sDomain + " keyword = " + sKeyword);

		if (sDomain == null || sDomain.isEmpty() || sKeyword == null || sKeyword.isEmpty())
		{
			String sMsg = "please enter valid arguments domain = (" + sDomain + ") keyword = (" + sKeyword + ")";
			Toast.makeText(pContext, sMsg, Toast.LENGTH_LONG);
			Log.d(sMsg);
			return;
		}

		try
		{
			SharedPreferences pSharedPreferences = getPref(pContext);

			String sLDomain = pSharedPreferences.getString("lst_domain", "[]");
			JSONArray pJLDomain = new JSONArray(sLDomain);

			Domain pDomain = new Domain(sDomain, sKeyword);
			JSONObject pJDomain = pDomain.toJson();

			pJLDomain.put(pJDomain);

			pSharedPreferences.edit().putString("lst_domain", pJLDomain.toString()).apply();

			Log.d("after add existing values = " + pSharedPreferences.getString("lst_domain", ""));
		}
		catch (JSONException pExe)
		{
			pExe.printStackTrace();
		}
	}

	public void rmDomain(Context pContext, int iIdx)
	{
		Log.d("domain = " + iIdx);

		try
		{
			SharedPreferences pSharedPreferences = getPref(pContext);

			String sLDomain = pSharedPreferences.getString("lst_domain", "[]");
			JSONArray pJLDomain = new JSONArray(sLDomain);

			if (pJLDomain.length() <= iIdx)
			{
				Log.e("invalid index " + iIdx);
				return;
			}

			JSONArray pJLDomainNew = new JSONArray();
			for (int iDomain = 0; iDomain < pJLDomain.length(); iDomain++)
			{
				if (iIdx != iDomain)
					pJLDomainNew.put(pJLDomain.getJSONObject(iDomain));
				else
					Log.d("skipped index " + iDomain);
			}

			pSharedPreferences.edit().putString("lst_domain", pJLDomainNew.toString()).apply();

			Log.d("after remove existing values = " + pSharedPreferences.getString("lst_domain", ""));
		}
		catch (JSONException pExe)
		{
			pExe.printStackTrace();
		}

	}

	public ArrayList<Domain> getADomain(Context pContext)
	{
		ArrayList<Domain> pLDomain = new ArrayList<>();

		try
		{
			String sLDomain = getPref(pContext).getString("lst_domain", "[]");
			JSONArray pJLDomain = new JSONArray(sLDomain);
			Log.d("domain = " + sLDomain);

			for (int iIdx = 0; iIdx < pJLDomain.length(); iIdx++)
			{
				JSONObject pJDomain = pJLDomain.getJSONObject(iIdx);
				Domain pDomain = new Domain();
				pDomain.load(pJDomain);

				pLDomain.add(pDomain);
			}
		}
		catch (JSONException pExe)
		{
			pExe.printStackTrace();
		}


		return pLDomain;
	}

	public boolean isRegistered(Context pContext)
	{
		if (getName(pContext).equals(""))
			return false;

		if (getEmail(pContext).equals(""))
			return false;

		if (getContactNumber(pContext).equals(""))
			return false;

		return true;
	}

	public void setName(Context pContext, String sName)
	{
		set(pContext, "name", sName);
	}

	public String getName(Context pContext)
	{
		return get(pContext, "name");
	}

	public void setEmail(Context pContext, String sEmail)
	{
		set(pContext, "email", sEmail);
	}

	public String getEmail(Context pContext)
	{
		return get(pContext, "email");
	}

	public void setContactNumber(Context pContext, String sNumber)
	{
		set(pContext, "contact_number", sNumber);
	}

	public String getContactNumber(Context pContext)
	{
		return get(pContext, "contact_number");
	}

	private void set(Context pContext, String sKey, String sVal)
	{
		getPref(pContext).edit().putString(sKey, sVal).apply();
	}

	private String get(Context pContext, String sKey)
	{
		return getPref(pContext).getString(sKey, "");
	}

	private SharedPreferences getPref(Context pContext)
	{
		return PreferenceManager.getDefaultSharedPreferences(pContext);
	}

	public static Data getInst()
	{
		return p_Inst;
	}

	private static String PREFERENCE_NAME = "data";
	private static Data p_Inst = new Data();
}
