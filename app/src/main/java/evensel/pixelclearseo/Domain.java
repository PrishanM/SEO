package evensel.pixelclearseo;

import org.json.JSONException;
import org.json.JSONObject;

public class Domain
{
	public Domain()
	{

	}

	public Domain(String sName, String sKeyword)
	{
		setName(sName);
		setKeyword(sKeyword);
	}

	public JSONObject toJson()
	{
		JSONObject pJson = new JSONObject();

		try
		{
			pJson.put("name", getName());
			pJson.put("keyword", getKeyword());
		}
		catch (JSONException pExe)
		{
			pExe.printStackTrace();
		}

		return pJson;
	}

	public void load(JSONObject pJson)
	{
		try
		{
			setName(pJson.getString("name"));
			setKeyword(pJson.getString("keyword"));
		}
		catch (JSONException pExe)
		{
			pExe.printStackTrace();
		}
	}
	
	private void setName(String sName)
	{
		s_Name = sName;
	}

	public String getName()
	{
		return s_Name;
	}

	private void setKeyword(String sKeyword)
	{
		s_Keyword = sKeyword;
	}

	public String getKeyword()
	{
		return s_Keyword;
	}

	private String s_Name;
	private String s_Keyword;
}
