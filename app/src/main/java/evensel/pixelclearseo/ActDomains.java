package evensel.pixelclearseo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ActDomains extends ActBaseTitleBar
{
	@Override
	protected void onCreate(Bundle pSavedInstance)
	{
		super.onCreate(pSavedInstance);
		setContentView(R.layout.act_domains);

		p_LstDomain = (ListView) findViewById(R.id.lst_domain);

		p_CmdAdd = (Button) findViewById(R.id.cmd_add_domain);

		p_CmdAdd.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent pIntent = new Intent(ActDomains.this, ActDomain.class);			
				pIntent.putExtra(ActDomain.Arg.getIsNew(), true);
				startActivity(pIntent);
			}
		});

		p_LblName = (TextView) findViewById(R.id.lbl_name);
		p_LblName.setText(Data.getInst().getName(this));

		p_LblEmail = (TextView) findViewById(R.id.lbl_email);
		p_LblEmail.setText(Data.getInst().getEmail(this));
	
		p_LblContactNumber = (TextView) findViewById(R.id.lbl_contact_number);
		p_LblContactNumber.setText(Data.getInst().getContactNumber(this));
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		refresh();
	}

	private void refresh()
	{
		ArrayList<Domain> pLDomain = Data.getInst().getADomain(this);

		p_Adapter = new ActDomainsAdapter(this, pLDomain);
		p_LstDomain.setAdapter(p_Adapter);
	}

	private ActDomainsAdapter p_Adapter;
	private ListView p_LstDomain;
	private TextView p_LblName;
	private TextView p_LblEmail;
	private TextView p_LblContactNumber;
	private Button p_CmdAdd;
}