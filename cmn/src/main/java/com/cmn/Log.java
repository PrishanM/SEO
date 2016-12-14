package com.cmn;

public class Log
{
	//****************************************************************************************************
	public static StackTraceElement getCurStackElement()
	{
		StackTraceElement aoStack[] = Thread.currentThread().getStackTrace();
		return aoStack[6];
	}

	//****************************************************************************************************
	public static String getFncCur()
	{
		StackTraceElement pStackElement = getCurStackElement();
		return pStackElement.getClassName() + "::" + pStackElement.getMethodName();
	}

	//****************************************************************************************************
	public static String getLogTitle()
	{
		return getFncCur() + "::" + getLineCur();
	}

	public static int getLineCur()
	{
		return getCurStackElement().getLineNumber();
	}

	//****************************************************************************************************
	public static void d(String sMsg)
	{
		if (sMsg == null)
			sMsg = "NULL_STR_FROM_TO_LOG";

		if (sMsg.equals(""))
			sMsg = "EMPTY_STR_TO_LOG";

		android.util.Log.d(getLogTitle(), sMsg);
	}

	//****************************************************************************************************
	public static void i(String sMsg)
	{
		d(sMsg);
	}

	//****************************************************************************************************
	public static void e(String sMsg)
	{
		stack();
		android.util.Log.e(getLogTitle(), sMsg);
	}

	//****************************************************************************************************
	public static void e(String sMsg, Exception pExe)
	{
		stack();
		android.util.Log.e(getLogTitle(), sMsg, pExe);
	}

	//****************************************************************************************************
	public static void w(String sMsg)
	{
		android.util.Log.w(getFncCur(), sMsg);
	}

	//****************************************************************************************************
	public static void l()
	{
		android.util.Log.d(getLogTitle(), "line");
	}

	//****************************************************************************************************
	public static void errRet()
	{
		android.util.Log.e(getLogTitle(), "err_ret");
	}

	//****************************************************************************************************
	public static void stack()
	{
		StackTraceElement aoStack[] = Thread.currentThread().getStackTrace();
		for (int iIdx = 0; iIdx < aoStack.length; iIdx ++)
		{
			StackTraceElement pElement = aoStack[iIdx];
			android.util.Log.d("call_stack", pElement.getFileName() + "::" + pElement.getClassName() + "::" + pElement.getMethodName() + "::" + pElement.getLineNumber());
		}
	}
}
