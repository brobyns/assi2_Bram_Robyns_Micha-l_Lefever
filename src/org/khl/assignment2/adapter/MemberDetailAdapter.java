package org.khl.assignment2.adapter;

import java.util.List;

import org.khl.assignment2.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import model.Expense;
import model.Member;
import model.Facade.Facade;

public class MemberDetailAdapter extends BaseAdapter {

	private Activity activity;
	private List<Expense> expenses;
	private static LayoutInflater inflater = null;
	private Facade facade;
	private int memberid;

	public MemberDetailAdapter(Activity a, List<Expense> expenses,
			Facade facade, int memberid) {
		activity = a;
		this.memberid = memberid;
		this.facade = facade;
		this.expenses = expenses;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// imageLoader=new ImageLoader(activity.getApplicationContext());
	}

	@Override
	public int getCount() {
		return expenses.size();
	}

	@Override
	public Object getItem(int position) {
		return expenses.get(position);
	}

	@Override
	public long getItemId(int position) {
		return expenses.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if(convertView==null){
			view = inflater.inflate(R.layout.group_detail_row, null);
		}
		TextView memberName = (TextView)view.findViewById(R.id.memberName); // member
		TextView amountText = (TextView)view.findViewById(R.id.amount); // who owes who how much

		// Setting all values in listview
			Expense m = expenses.get(position);
			Log.v("IdinADAPTER", m.getId() + "");
			//memberName.setText(m.toString());
			double amount = m.getAmount();
			//facade.getAmountsReceived(id, group)
			String amountString ="";
			Member e = facade.getMembers().get(memberid);
			//if (m.getSender().getId() == e.getId()){
				amountString = " "+ amount;
				memberName.setText("You paid");
				amountText.setTextColor(Color.GREEN);
			/*}
			else if (m.getGroupId() == e.g){
				//amountString = " " + amount;
				//amountText.setTextColor(Color.RED);
				//memberName.setText("You owe");
			}
			if(amount > 0){
				amountString = " "+ amount;
				memberName.setText("You paid");
				amountText.setTextColor(Color.GREEN);
			}else if(amount < 0){
				amount *= -1;
				amountString = " " + amount;
				amountText.setTextColor(Color.RED);
				memberName.setText("Owes you");
			}else{
				amountString = "settled";
			}*/
			amountText.setText(amountString);
		//song.get(CustomizedListView.KEY_ARTIST));
		//members.setText(song.get(CustomizedListView.KEY_DURATION));
		//imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
		return view;
	}
}
