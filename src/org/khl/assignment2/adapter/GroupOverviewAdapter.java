package org.khl.assignment2.adapter;

import java.util.List;

import model.Group;
import model.Member;

import org.khl.assignment2.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GroupOverviewAdapter extends BaseAdapter {

	private Activity activity;
	private static LayoutInflater inflater=null;
	private List<Group> groups;
	//		public ImageLoader imageLoader; 

	public GroupOverviewAdapter(Activity a, List<Group> groups) {
		activity = a;
		this.groups=groups;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//			imageLoader=new ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
		return groups.size();
	}

	public Object getItem(int position) {
		return groups.get(position);
	}

	public long getItemId(int position) {
		return groups.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(convertView==null){
			view = inflater.inflate(R.layout.list_row, null);
		}
		TextView groupName = (TextView)view.findViewById(R.id.groupName); // group name
		TextView adminName = (TextView)view.findViewById(R.id.adminName); // admin name
		TextView membernames = (TextView)view.findViewById(R.id.membernames);
		//			ImageView thumb_image=(ImageView)view.findViewById(R.id.list_image); // thumb image


		groupName.setText(groups.get(position).getName());
		/*if (!groups.get(position).getAdmin().getFirstName().equals(null)){
			adminName.setText(groups.get(position).getAdmin().getFirstName());*/
		
		List<Member> membersInGroup = groups.get(position).getMembers();
		if(!membersInGroup.isEmpty()){
			String names="";
			for(int i = 0; i < membersInGroup.size(); i++){
				if(i != membersInGroup.size()-1){
					names += membersInGroup.get(i).toString()+ ", ";
				}else{
					names += membersInGroup.get(i).toString();
				}
			}
			membernames.setText(names);
		}
		//			adminName.setText(group.get("admin"));//song.get(CustomizedListView.KEY_ARTIST));
		//			members.setText(song.get(CustomizedListView.KEY_DURATION));
		//			imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
		return view;
	}
}