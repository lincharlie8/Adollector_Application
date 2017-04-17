package com.ntust.cmapp;




import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LeftNavBar_Fragment extends ListFragment {
	public ListView listview;
	View view;
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, People.NAME);
		ArrayAdapter<CharSequence> adapterSelect= ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.Navbar_Selection,android.R.layout.simple_list_item_1);	
		this.setListAdapter(adapterSelect);
		getListView().setOnItemClickListener(lvListener);
	
	}
	/*public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		listview=(ListView)view.findViewById(R.id.Listview_leftNavBar);
		listview.setAdapter(adapterSelect);
		listview.setOnItemClickListener(lvListener);
	
	}*/
	
	private ListView.OnItemClickListener lvListener =new ListView.OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			String SelectedItem =parent.getItemAtPosition(position).toString();
			String[] SelectItem=getResources().getStringArray(R.array.Navbar_Selection);
			if(SelectedItem.equals(SelectItem[1])){
				GetPhoto_Fragment replacefragment =new GetPhoto_Fragment();
				((CMRegister_Activity)getActivity()).replaceFragment(replacefragment);
			}
			//((CMRegister_Activity)getActivity()).cmregister_parent_layout.closeDrawer(((CMRegister_Activity)getActivity()).navbar);
			
			//lvText.setText(((TextView)view).getText().toString());
		}

		
	};
}
