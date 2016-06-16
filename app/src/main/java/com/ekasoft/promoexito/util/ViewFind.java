package com.ekasoft.promoexito.util;

import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * clase que facilita la codificación para buscar un elemento dentro de una
 * vista
 * 
 * @author edwin.ospina@iptotal.com
 * 
 */
public class ViewFind {
	private View view;

	public ViewFind(View view) {
		this.view = view;
	}

	public TextView textView(int id) {
		return (TextView) view.findViewById(id);
	}
	
	public EditText editText(int id) {
		return (EditText) view.findViewById(id);
	}

	public SearchView searchView(int id) {
		return (SearchView) view.findViewById(id);
	}
	
	public ImageView imageView(int id) {
		return (ImageView) view.findViewById(id);
	}
	public RelativeLayout relativeLayout(int id) {
		return (RelativeLayout) view.findViewById(id);
	}

	public LinearLayout linearLayout(int id) {
		return (LinearLayout) view.findViewById(id);
	}

	public VideoView videoView(int id) {
		return (VideoView) view.findViewById(id);
	}

	public WebView WebView(int id) {
		return (WebView) view.findViewById(id);
	}
	public FrameLayout frameLayout(int id) {
		return (FrameLayout) view.findViewById(id);
	}
	public ProgressBar progressbar(int id) {
		return (ProgressBar) view.findViewById(id);
	} 

	public ImageButton imageButton(int id) {
		return (ImageButton) view.findViewById(id);
	}
	public Button button(int id) {
		return (Button) view.findViewById(id);
	}
	public View view(int id)
	{
		return (View)view.findViewById(id);
	}
	public ListView listView(int id)
	{
		return (ListView) view.findViewById(id);
	}
	
	public Spinner spinner(int id) {
		return (Spinner) view.findViewById(id);
	}
}
