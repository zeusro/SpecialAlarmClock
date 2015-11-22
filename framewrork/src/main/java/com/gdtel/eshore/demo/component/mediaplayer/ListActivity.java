package com.gdtel.eshore.demo.component.mediaplayer;

import java.util.Vector;

import com.gdtel.eshore.androidframework.component.mediaplayer.PlayActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ListActivity extends Activity {
    /** Called when the activity is first created. */
	private ListView listview;
	private int[] ids;
	private AlertDialog ad = null;
	private AlertDialog.Builder  builder = null;
	private Vector<String> v;
	 public static Uri uri;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        
        listview = new ListView(this);
        Cursor c  = this.getContentResolver()
        		.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        				new String[]{MediaStore.Video.Media.TITLE,
						MediaStore.Video.Media.DURATION,
						MediaStore.Video.Media._ID,
						MediaStore.Video.Media.DISPLAY_NAME ,
						MediaStore.Video.Media.DATA},
        				null, null, null);
        if (c==null || c.getCount()==0){
        	builder = new AlertDialog.Builder(this);
        	builder.setMessage("存储列表为空...").setPositiveButton("确定", null);
			ad = builder.create();
			ad.show();
        }
        c.moveToFirst();
        v=new Vector<String>();
        ids = new int[c.getCount()];
        for(int i=0;i<c.getCount();i++){
        	ids[i] = c.getInt(3);
        
        	v.add(c.getString(4));
        	System.out.println(v.get(i));
      
        	c.moveToNext();
        }
        listview.setAdapter(new VideoListAdapter(this, c));
        listview.setOnItemClickListener(new ListItemClickListener());
        setContentView(listview);
    }
   String string;
    class ListItemClickListener implements OnItemClickListener{

    	@Override
    	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {

    		Intent intent = new Intent(ListActivity.this,PlayActivity.class);
    		intent.putExtra("ids", ids);
    		string=v.get(position);
    		uri=Uri.parse(string);
            intent.putExtra("video_path", uri.toString());
    		intent.putExtra("position", position);
    		startActivity(intent);
    	}
    	
    }
   

    


}

