package com.cmn;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import android.util.Log;


public class HttpCon
{
	public interface Callback
	{
		void onOk(HttpCon pCon, String sResponse);
		void onError(HttpCon pCon, String sErr);
	}

	public enum Method
	{
		Get,
		Post,
		Delete
	}

	//****************************************************************************************************
	public HttpCon(Method eMethod, String sUrl, String sMsg, Callback pCallback)
	{
		this.s_Method = toStr(eMethod);
		this.s_Url = sUrl;
		this.s_Msg = sMsg;
		this.p_Callback = pCallback;
	}

	//****************************************************************************************************
	public void send()
	{
		Runnable pRunnable = new Runnable()
		{
			public void run()
			{
				sendHttpMessage();
			}
		};
		new Thread(pRunnable).start();
	}

	//****************************************************************************************************
	private void sendHttpMessage()
	{
		try
		{
			HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());

			HttpURLConnection pCon = (HttpURLConnection) new URL(s_Url).openConnection();
			byte[] acData = new byte[0];
			if (s_Msg != null)
			{
				acData = s_Msg.getBytes("UTF-8");
			}

			pCon.setRequestMethod(s_Method);
			pCon.setUseCaches(false);
			pCon.setDoInput(true);
			pCon.setConnectTimeout(HTTP_TIMEOUT_MS);
			pCon.setReadTimeout(HTTP_TIMEOUT_MS);
			pCon.addRequestProperty("origin", HTTP_ORIGIN);
			boolean bDoOutput = false;

			if (s_Method.equals("POST"))
			{
				bDoOutput = true;
				pCon.setDoOutput(true);
				pCon.setFixedLengthStreamingMode(acData.length);
			}

			pCon.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

			if (bDoOutput && acData.length > 0)
			{
				OutputStream pOut = pCon.getOutputStream();
				pOut.write(acData);
				pOut.close();
			}

			int iResponseCode = pCon.getResponseCode();
			if (iResponseCode != 200)
			{
				p_Callback.onError(this, "Non-200 response to " + s_Method + " to URL: " + s_Url + " : " + pCon.getHeaderField(null));
				pCon.disconnect();
				return;
			}

			InputStream pResponseStream = pCon.getInputStream();
			String sResponse = drainStream(pResponseStream);
			pResponseStream.close();
			pCon.disconnect();
			p_Callback.onOk(this, sResponse);
		}
		catch (SocketTimeoutException pExe)
		{
			pExe.printStackTrace();
			p_Callback.onError(this, "HTTP " + s_Method + " to " + s_Url + " timeout");
		}
		catch (Exception pExe)
		{
			pExe.printStackTrace();
			p_Callback.onError(this, "HTTP " + s_Method + " to " + s_Url + " error: " + pExe.getMessage());
		}
	}

	//****************************************************************************************************
	private static String drainStream(InputStream pIn)
	{
		Scanner s = new Scanner(pIn).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	//****************************************************************************************************
	class NullHostNameVerifier implements HostnameVerifier
	{
		@Override
		public boolean verify(String hostname, SSLSession session)
		{
			Log.i("RestUtilImpl", "Approving certificate for " + hostname);
			return true;
		}

	}

	//****************************************************************************************************
	public static String toStr(Method eMethod)
	{
		switch (eMethod)
		{
			case Get:
				return "GET";
			case Post:
				return "POST";
			case Delete:
				return "DELETE";
		}

		return "NONE";
	}

	private static final int HTTP_TIMEOUT_MS = 30000;
	private static final String HTTP_ORIGIN = "https://45.55.202.167:8082";
	private final String s_Method;
	private final String s_Url;
	private final String s_Msg;
	private final Callback p_Callback;
}


