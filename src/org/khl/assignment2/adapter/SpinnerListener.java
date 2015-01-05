package org.khl.assignment2.adapter;

import org.khl.assignment2.AddExpenseActivity;

import model.Member;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class SpinnerListener implements OnItemSelectedListener{
	private Member selectedMember;
	private Member previouslySelectedMember;
	private boolean selected;
	
	public SpinnerListener(AddExpenseActivity a){
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		selected = true;
		previouslySelectedMember = selectedMember;
		selectedMember = (Member)parent.getItemAtPosition(pos);
	}

	public Member getSender(){
		return selectedMember;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		selected = false;
	}

/*	@Override
	public void notifyObservers() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addObserver(Observer o) {
		// TODO Auto-generated method stub
		
	}
*/
}