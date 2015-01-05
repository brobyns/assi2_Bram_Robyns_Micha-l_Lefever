package org.khl.assignment2.adapter;

import java.util.List;

import org.khl.assignment2.R;

import model.Member;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class AddedMemberAdapter extends BaseAdapter {

	private Activity activity;
	private List<Member> data, members;
	private ArrayAdapter<Member> memberAdapt;
	private Spinner spinnerReceive, spinnerSend;
	private static LayoutInflater inflater=null;
//	public ImageLoader imageLoader; 
	
	public AddedMemberAdapter(Activity a, List<Member> membersInvited, ArrayAdapter<Member> memberAdapt, 
			List<Member> members, Spinner spinner) {
		activity = a;
		data=membersInvited;
		this.members = members;
		this.memberAdapt = memberAdapt;
		this.spinnerReceive = spinner;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		imageLoader=new ImageLoader(activity.getApplicationContext());
	}

	public AddedMemberAdapter(Activity a, List<Member> membersInvited, ArrayAdapter<Member> memberAdapt, 
			List<Member> members, Spinner spinnerSend, Spinner spinnerReceive) {
		this(a, membersInvited, memberAdapt, members,spinnerReceive);
		this.spinnerSend = spinnerSend;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return data.get(position).getId();
	}

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		View view = convertView;
		if(convertView==null){
			view = inflater.inflate(R.layout.added_member_row, null);
		}
		TextView memberName = (TextView)view.findViewById(R.id.memberName); // member
		initializeDeleteBtn(position, view);
		Member member = data.get(position);
		memberName.setText(member.toString());
		return view;
	}
	
	private void initializeDeleteBtn(int position, View view){
		ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.deleteBtn);
		deleteBtn.setTag(position);
		deleteBtn.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            Integer index = (Integer) v.getTag(); 
	            Member m = data.get(index.intValue());
	            data.remove(index.intValue());
	            members.add(m);
	            notifyDataSetChanged();
	            memberAdapt.notifyDataSetChanged();
	            spinnerReceive.setAdapter(memberAdapt);
	            if(spinnerSend != null){
	            	spinnerSend.setAdapter(memberAdapt);
	            }
	        }
	    });
	}

}
