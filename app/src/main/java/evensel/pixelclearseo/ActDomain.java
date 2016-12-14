package evensel.pixelclearseo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cmn.Log;

public class ActDomain extends ActBaseTitleBar
{
	public static class Arg
	{
		public static final String getIsNew() { return getArg("is_new"); }

		public static final String getDomain() { return getArg("domain"); }

		public static final String getKeyword() { return getArg("keyword");}

		public static final String getName() { return getArg("name");}

		public static final String getEmail() { return getArg("email");}

		public static final String getContactNumber() { return getArg("contact_number");}

		private static final String getArg(String sName) { return ActDomain.class.getCanonicalName() + "_" + sName; }
	}

	@Override
	protected void onCreate(Bundle pSavedInstance)
	{
		super.onCreate(pSavedInstance);
		setContentView(R.layout.act_domain);

		p_TxtDomain = (EditText) findViewById(R.id.txt_domain);
		p_TxtKeyword = (EditText) findViewById(R.id.txt_keyword);
		p_TxtName = (EditText) findViewById(R.id.txt_name);
		p_TxtEmail = (EditText) findViewById(R.id.txt_email);
		p_TxtContactNumber = (EditText) findViewById(R.id.txt_contact_number);
		p_CmdCheck = (Button) findViewById(R.id.cmd_check);

		Intent pIntent = getIntent();
		final boolean bIsNew = pIntent.getBooleanExtra(Arg.getIsNew(), false);
		final String sDomain = pIntent.getStringExtra(Arg.getDomain());
		final String sKeyword = pIntent.getStringExtra(Arg.getKeyword());
		final String sName = Data.getInst().getName(this);
		final String sEmail = Data.getInst().getEmail(this);
		final String sContactNumber = Data.getInst().getContactNumber(this);

		if (sDomain != null && !sDomain.isEmpty() && sKeyword != null && !sKeyword.isEmpty())
		{
			p_TxtDomain.setText(sDomain);
			p_TxtDomain.setEnabled(false);

			p_TxtKeyword.setText(sKeyword);
			p_TxtKeyword.setEnabled(false);
		}

		p_TxtName.setText(sName);
		p_TxtEmail.setText(sEmail);
		p_TxtContactNumber.setText(sContactNumber);

		p_CmdCheck.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				String sDomain = p_TxtDomain.getText().toString().trim();
				String sKeyword = p_TxtKeyword.getText().toString().trim();
				String sName = p_TxtName.getText().toString().trim();
				String sEmail = p_TxtEmail.getText().toString().trim();
				String sContactNumber = p_TxtContactNumber.getText().toString().trim();

				Log.d("domain = (" + sDomain + ") keyword = (" + sKeyword + ") name = (" + sName + ")" +
					" email = (" + sEmail + ") contact_number = (" + sContactNumber + ")");

				String sErrMsg = "";

				if (sDomain == null || sDomain.isEmpty())
					sErrMsg += "please enter valid domain name.\n";
				else if (!Patterns.WEB_URL.matcher(sDomain).matches())
					sErrMsg += "please correct the syntax of url.\n";

				if (sKeyword == null || sKeyword.isEmpty())
					sErrMsg += "please enter valid keyword\n";

				if (!Data.getInst().isRegistered(ActDomain.this))
				{
					if (sName == null || sName.isEmpty())
						sErrMsg += "Please enter valid name.\n";
					else if (!sName.matches("[A-Za-z0-9 ]+"))
						sErrMsg += "Please enter valid name.\n";

					if (sEmail == null || sEmail.isEmpty())
						sErrMsg += "Please enter email.\n";
					else if (!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches())
						sErrMsg += "Please enter email address with valid syntax.\n";

					if (sContactNumber == null || sContactNumber.isEmpty())
						sErrMsg += "Please enter contact number.\n";
					else if (!sContactNumber.matches("[+]?[0-9]{10,13}"))
						sErrMsg += "Please enter contact number with valid syntax.\n";

					if (sErrMsg.isEmpty())
					{
						Data.getInst().setName(ActDomain.this, sName);
						Data.getInst().setEmail(ActDomain.this, sEmail);
						Data.getInst().setContactNumber(ActDomain.this, sContactNumber);
					}
				}

				if (sErrMsg.isEmpty())
				{
					if (bIsNew)
					{
						Data.getInst().addDomain(ActDomain.this, p_TxtDomain.getText().toString(), p_TxtKeyword.getText().toString());
					}

					Intent pIntent = new Intent(ActDomain.this, ActRank.class);
					pIntent.putExtra(ActRank.Arg.getDomain(), sDomain);
					pIntent.putExtra(ActRank.Arg.getKeyword(), sKeyword);
					startActivity(pIntent);
				}
				else
					dis(sErrMsg);
			}
		});

		if (Data.getInst().isRegistered(this))
		{
			p_TxtName.setEnabled(false);
			p_TxtEmail.setEnabled(false);
			p_TxtContactNumber.setEnabled(false);
		}
	}

	private EditText p_TxtDomain;
	private EditText p_TxtKeyword;
	private EditText p_TxtName;
	private EditText p_TxtEmail;
	private EditText p_TxtContactNumber;
	private Button p_CmdCheck;
}
