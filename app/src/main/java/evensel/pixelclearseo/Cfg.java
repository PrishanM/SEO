package evensel.pixelclearseo;

public class Cfg
{
	public String getUrlRank()
	{
		return getUrl() + "/get_rank";	
	}

	public String getUrl()
	{
		return "http://45.55.202.167:8000";		
	}

	public static Cfg getInst()
	{
		return p_Inst;	
	}

	public static Cfg p_Inst = new Cfg();
}
