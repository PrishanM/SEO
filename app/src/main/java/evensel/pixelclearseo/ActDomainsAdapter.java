package evensel.pixelclearseo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ActDomainsAdapter extends ArrayAdapter<Domain>
{
	public ActDomainsAdapter(Context pContext, ArrayList<Domain> pLDomain)
	{
		super(pContext, -1, pLDomain);
		p_LVal = pLDomain;
	}

	@NonNull
	@Override
	public View getView(final int iPos, View pContentView, ViewGroup pParent)
	{
		LayoutInflater pInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View pView = pInflater.inflate(R.layout.act_domains_listview_item, pParent, false);

		pView.findViewById(R.id.cmd_check).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent pIntent = new Intent(getContext(), ActRank.class);
				pIntent.putExtra(ActRank.Arg.getDomain(), p_LVal.get(iPos).getName());
				pIntent.putExtra(ActRank.Arg.getKeyword(), p_LVal.get(iPos).getKeyword());
				getContext().startActivity(pIntent);
			}
		});

		pView.findViewById(R.id.cmd_rm).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				rm(iPos);
			}
		});

		p_LblDis = (TextView) pView.findViewById(R.id.lbl_dis);
		p_LblDis.setText("DOMAIN " + iPos);

		p_LblVal = (TextView) pView.findViewById(R.id.lbl_val);
		p_LblVal.setText(p_LVal.get(iPos).getName());

		return pView;
	}

	private void rm(int iPos)
	{
		Data.getInst().rmDomain(getContext(), iPos);
		p_LVal.remove(iPos);
		notifyDataSetChanged();
	}

	private ArrayList<Domain> p_LVal;
	private TextView p_LblDis;
	private TextView p_LblVal;
}
