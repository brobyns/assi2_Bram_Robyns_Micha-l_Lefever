package org.khl.assignment2.adapter;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.Expense;
import model.Group;
import model.Member;
import model.Facade.Facade;

import org.khl.assignment2.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class GroupDetailAdapter extends BaseAdapter {

	private Activity activity;
	private List<Member> members;
	private static LayoutInflater inflater=null;
	private Facade facade;
	private int groupId;
	//		public ImageLoader imageLoader; 

	public GroupDetailAdapter(Activity a, List<Member> members, Facade facade, int groupId) {
		activity = a;
		this.members=members;
		this.facade = facade;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.groupId = groupId;
		//			imageLoader=new ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
		return members.size();
	}

	public Member getItem(int position) {
		return members.get(position);
	}

	public long getItemId(int position) {
		return members.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(convertView==null){
			view = inflater.inflate(R.layout.group_detail_row, null);
		}
		LinearLayout amountLayout = (LinearLayout)view.findViewById(R.id.amountLayout);
		TextView memberName = (TextView)view.findViewById(R.id.memberName); // member
//		TextView amountText = (TextView)view.findViewById(R.id.amount); // who owes who how much
		ImageView image = (ImageView)view.findViewById(R.id.list_image);

		// Setting all values in listview
			Member m = getItem(position);
			Group group = facade.getGroups().get(groupId);
			memberName.setText(m.toString());
			Log.v("bram","id in adapter: "+ m.getId());
			Map<Integer, Double> amountsPaid = facade.getAmountsPaid(m.getId(), group);
			Map<Integer, Double> amountsReceived = facade.getAmountsReceived(m.getId(), group);
			String amountString ="";
			for(Entry e : amountsPaid.entrySet()){
				amountString += createAmountString(facade.getMembers().get((Integer)e.getKey()).toString(), (Double)e.getValue());
			}
			for(Entry e : amountsReceived.entrySet()){
				amountString += createAmountString(facade.getMembers().get((Integer)e.getKey()).toString(), (Double)e.getValue());
			}
	//		amountText.setText(amountString);
			TextView amountText = new TextView(activity);
			amountText.setText(amountString);
			amountText.setLayoutParams(new LayoutParams(
		            LayoutParams.WRAP_CONTENT,
		            LayoutParams.WRAP_CONTENT));
			amountLayout.addView(amountText);
			//image.setImageBitmap(m.getExpenses().get(2).getPhoto());
		//imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
		return view;
	}
	
	private String createAmountString(String name, double amount){
		String amountString ="";
		if(amount > 0){
			amountString = name + " owes you "+ amount+" "+facade.getCurrency()+" \n";
		//	amountText.setTextColor(Color.GREEN);
		}else if(amount < 0){
			amount *= -1;
			amountString = "You owe "+ name +" "+ amount+" "+ facade.getCurrency()+"\n";
		//	amountText.setTextColor(Color.RED);
		}
		return amountString;
	}
}