package com.example.database2;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Dishes extends ListActivity {
	
	private static final String DB_NAME = "mydatabase.db";
	
    
	private SQLiteDatabase db;
	private ListView listView;
	private ArrayList<String> indigrients;
	private ArrayList<String> dishes;
	private ArrayList<Integer> ind_id;
	private ArrayList<Integer> OveralCount;
	private ArrayList<Integer> IngredCount;
	//////////////////////////////////////////
	private ArrayList<String> type = new ArrayList<String>();
	int count=0;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes);
        
        indigrients=getIntent().getExtras().getStringArrayList("indigridi");
        count = getIntent().getExtras().getInt("count");
        ind_id = getIntent().getExtras().getIntegerArrayList("id");
        
        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(this, DB_NAME);
        db = dbOpenHelper.openDataBase();    
        
        fillDish(); getOverallCountIngred();
        getCountIngred(); setUpList();//setUpList();  
        
        
        
    }	
	
	
	public void getCountIngred()
	{
		int falsh_count=0;
		Cursor friendCursor = null;		
		IngredCount = new ArrayList<Integer>();
		try
		{
		for(int i=0;i<ind_id.size();i++)
		{
			for(int j=0;j<count;j++)
		{					
		friendCursor = db.rawQuery("Select Count(_id) from ingredients WHERE id_dish= '"+ind_id.get(i)+"' AND name='"+indigrients.get(j)+"'", null);
		friendCursor.moveToFirst();
		if(!friendCursor.isAfterLast()) {
			do {
				String name = friendCursor.getString(0);
				if (name!="0")
				falsh_count+=Integer.parseInt(name);				
			} while (friendCursor.moveToNext());
		}
		
		}
			IngredCount.add(falsh_count-OveralCount.get(i));
			falsh_count=0;
		}
		}catch(Exception e){}
		friendCursor.close();
	}
	
	public void getOverallCountIngred()
	{
		Cursor friendCursor = null;		
		OveralCount = new ArrayList<Integer>();
		try
		{
		for(int i=0;i<ind_id.size();i++)
		{					
		friendCursor = db.rawQuery("Select Count(_id) from ingredients WHERE id_dish= '"+ind_id.get(i)+"'", null);
		friendCursor.moveToFirst();
		if(!friendCursor.isAfterLast()) {
			do {
				String name = friendCursor.getString(0);
				OveralCount.add(Integer.parseInt(name));
			} while (friendCursor.moveToNext());
		}
		
		}
		}catch(Exception e){}
		friendCursor.close();
	}
	
	
	
		private void fillDish() 
		{
			Cursor friendCursor = null;
			dishes = new ArrayList<String>();
			
			try
			{
			for(int i=0;i<ind_id.size();i++)
			{					
			friendCursor = db.rawQuery("SELECT dish_name FROM dish WHERE _id ='"+ind_id.get(i)+"'", null);
			friendCursor.moveToFirst();
			if(!friendCursor.isAfterLast()) {
				do {
					String name = friendCursor.getString(0);
					dishes.add(name);
				} while (friendCursor.moveToNext());
			}
			
			}
			}catch(Exception e){}
			friendCursor.close();
		}
		
	private void setUpList() {
		int falsh=0;
		String dish_falsh;
		try{
		for(int i = 0;i<IngredCount.size();i++)
			{
			if(IngredCount.get(i)<IngredCount.get(i+1))
			{
			falsh=IngredCount.get(i);
			dish_falsh=dishes.get(i);
			IngredCount.set(i, IngredCount.get(i+1));
			dishes.set(i, dishes.get(i+1));
			IngredCount.set(i+1,falsh);
			dishes.set(i+1,dish_falsh);
			}
			}
		}catch(Exception e){}
		setListAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, dishes));
		listView = getListView();
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position,long id) {
				Intent intent = new Intent(Dishes.this, Rezept_Dish.class);
			   	 intent.putExtra("dish", ((TextView) view).getText());
			   	intent.putExtra("color", "blue");
			       startActivity(intent);
							
			}
		});
	}
	
	/*public void typeOut()
	{getType();
		Cursor friendCursor = null;		
		
		for(int j=0;j<type.size();j++)
		{
			
		for(int i=0;i<ind_id.size();i++)
		{					
		friendCursor = db.rawQuery("SELECT dish_name FROM dish WHERE _id ='"+ind_id.get(i)+"' AND type='"+type.get(j)+"'", null);
		friendCursor.moveToFirst();
		if(!friendCursor.isAfterLast()) {
			do {
				String name = friendCursor.getString(0);
				if(!dishes.contains(name))
				dishes.add(name);
			} while (friendCursor.moveToNext());
		}		
		}
		TextView text = new TextView(this);
		text.setText(type.get(j));
		lay.addView(text);
		ListView l = new ListView(this);
		
		ArrayAdapter<String> ar = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, dishes);
		
		l.setAdapter(ar);
		//l = getListView();
		l.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position,long id) {
				Intent intent = new Intent(Dishes.this, Rezept_Dish.class);
			   	 intent.putExtra("dish", ((TextView) view).getText());
			       startActivity(intent);
							
			}
		});
		lay.addView(l);		
		}
		friendCursor.close();
		
		
	}
	
	public void getType()
	{
		Cursor friendCursor = null;
		
		for(int i=0;i<ind_id.size();i++)
		{					
		friendCursor = db.rawQuery("SELECT type FROM dish WHERE _id ='"+ind_id.get(i)+"'", null);
		friendCursor.moveToFirst();
		if(!friendCursor.isAfterLast()) {
			do {
				String name = friendCursor.getString(0);
				type.add(name);
			} while (friendCursor.moveToNext());
		}
		
		}
		friendCursor.close();
	}*/
	
	public void Back(View v)
	{
		Intent intent = new Intent(Dishes.this, MainActivity.class);
   	 indigrients.clear(); dishes.clear();ind_id.clear();
       startActivity(intent);       
       overridePendingTransition(R.animator.animation_form2_to_form1_in, R.animator.animation_form2_to_form1_out);
       this.finish();
	}
}
