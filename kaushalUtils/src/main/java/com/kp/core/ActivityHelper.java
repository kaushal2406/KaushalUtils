package com.kp.core;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import com.kaushalprajapti.util.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;

/**
 * @author kaushal Prajapati (kaushal2406@gmail.com)
 *
 */
public class ActivityHelper {
	public static String TAG = "ActivityHelper";

	// Dialog Action
	public static final int NO_ACTION = 1;
	public static final int CLOSE_ACTIVITY = 2;

	public static void hideKeyboard(final Activity p_context) {
		try {
			InputMethodManager imm = (InputMethodManager) p_context.getSystemService(Context.INPUT_METHOD_SERVICE);
			// imm.hideSoftInputFromWindow(((EditText) p_view).getWindowToken(),
			// 0);
			imm.hideSoftInputFromWindow(p_context.getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// Progress dialog
	private static ProgressDialog m_progressDialog;

	public static void showProgressDialog(final Context p_context, final String p_loadingMessage,
			final boolean p_isCancelable) {
		Log.d(TAG, " showProgressDialog  showProgressDialog ");
		// m_progressDialog = null;
		if (m_progressDialog != null && m_progressDialog.isShowing()) {
			// m_progressDialog.isShowing();
			return;
		}
		m_progressDialog = new ProgressDialog(p_context);
		m_progressDialog.setMessage(p_loadingMessage);
		m_progressDialog.setCancelable(p_isCancelable);
		// m_progressDialog.findViewById(android.R.id)
		m_progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		if (!((Activity) p_context).isFinishing()) {
			m_progressDialog.show();
		}
	}

	public static void dismissProgressDialog() {
		Log.d(TAG, "dismissProgressDialog dismissProgressDialog  ");
		try {
			if (m_progressDialog != null && m_progressDialog.isShowing()) {
				m_progressDialog.dismiss();
			}
			m_progressDialog = null;
		} catch (Throwable e) {
			Log.d(TAG, "" + e.getMessage());
		}
	}

	public static void showDialog(final Context p_context, String p_message, final int p_action) {
		AlertDialog.Builder m_builder = new AlertDialog.Builder(p_context);
		m_builder.setTitle(p_context.getString(R.string.app_name));
		m_builder.setMessage(p_message);
		m_builder.setCancelable(false);
		m_builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface p_dialog, int p_id) {

				if (p_action == NO_ACTION) {

				} else if (p_action == CLOSE_ACTIVITY) {
					if (p_context instanceof Activity) {
						((Activity) p_context).finish();
					}
				}

			}
		});

		AlertDialog m_alertDialog = m_builder.create();
		m_alertDialog.show();
	}

	public static boolean isConnectingToInternet(final Context context) {
		
		 boolean haveConnectedWifi = false;
		    boolean haveConnectedMobile = false;
		    
		try {
			
			
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
//				NetworkInfo[] info = connectivity.getAllNetworkInfo();
//				if (info != null)
//					for (int i = 0; i < info.length; i++)
//						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//							return true;
//						}
				
//				getActiveNetworkInfo
				 NetworkInfo[] netInfo = connectivity.getAllNetworkInfo();
				    for (NetworkInfo ni : netInfo) {
//				    	ni.getState()
				        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				            if (ni.isConnected())
				                haveConnectedWifi = true;
				        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				            if (ni.isConnected())
				                haveConnectedMobile = true;
				    }
				    return haveConnectedWifi || haveConnectedMobile;
			}

		} catch (Exception e) {
			Log.d(TAG, "Exception e " + e.getMessage());
			// Toast.makeText(context, " isConnectingToInternet = "
			// +e.getMessage() , Toast.LENGTH_LONG).show();
		}
		return false;
	}

	public static void showMenuPermanant(Context context) {
		try {
			ViewConfiguration config = ViewConfiguration.get(context);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			Log.d("ViewConfiguration  ", " ViewConfiguration === " + e.getMessage());
		}
	}

	// To animate view slide out from bottom to top
	public static void slideToTop(final View view) {
		// TranslateAnimation animate = new
		// TranslateAnimation(0,0,0,-view.getHeight());
		// animate.setDuration(500);
		//// animate.setFillAfter(true);
		// view.startAnimation(animate);
		// view.setVisibility(View.GONE);
		final int initialHeight = view.getMeasuredHeight();

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				if (interpolatedTime == 1) {
					view.setVisibility(View.GONE);
				} else {
					view.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
					view.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 1dp/ms
		a.setDuration((int) (initialHeight / view.getContext().getResources().getDisplayMetrics().density));
		view.startAnimation(a);
	}

	// To animate view slide out from top to bottom
	public static void slideToBottom(final View view) {
		// TranslateAnimation animate = new
		// TranslateAnimation(0,0,0,view.getHeight());
		// animate.setDuration(500);
		//// animate.setFillAfter(true);
		// view.startAnimation(animate);
		// view.setVisibility(View.VISIBLE);
		view.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final int targetHeight = view.getMeasuredHeight();

		// Older versions of android (pre API 21) cancel animations for views
		// with a height of 0.
		view.getLayoutParams().height = 1;
		view.setVisibility(View.VISIBLE);
		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				view.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT
						: (int) (targetHeight * interpolatedTime);
				view.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		// 1dp/ms
		a.setDuration((int) (targetHeight / view.getContext().getResources().getDisplayMetrics().density));
		view.startAnimation(a);
	}

	public static String getIMEI(Context context) {
		String number = ""; 
//		PackageManager pm = context.getPackageManager();
//		if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)){
//		    //has Telephony features.
//		}
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
			number = tm.getDeviceId();
		} catch (Exception e) {
			Log.d("getIMEI", "getIMEI " + e.getMessage());
		}
		
		return number;
	}

	public static String getDeviceId(Context context) {
		// TelephonyManager tm =
		// (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
		// String number = tm.getDeviceId();
		String androidId = android.provider.Settings.Secure.getString(context.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
		return androidId;
	}
	 public static void webViewSettings(WebView wv_msg_body) {

		    WebSettings webSettings = wv_msg_body.getSettings();
	      webSettings.setLoadWithOverviewMode(true);
	      webSettings.setUseWideViewPort(true);
	      webSettings.setBuiltInZoomControls(true);
	      webSettings.setSupportZoom(true);
	      webSettings.setDisplayZoomControls(false);
	      webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
	      webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
	  //    wv_docket_msg_body.setInitialScale(110);
	      wv_msg_body.setWebChromeClient(new WebChromeClient());///////////////////////
	      wv_msg_body.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
	      wv_msg_body.setScrollbarFadingEnabled(false);
	      wv_msg_body.clearCache(true); 
	      
	  }
	 public static String CallJSONService(String url,String json,String method) throws Exception{
			
		 StringBuilder params=new StringBuilder("");
		    String result="";
		    String url1 =url ;
		    
		    Log.d("CallXMLService ", "CallXMLService  url  " + url1 );
		    URL obj = new URL(url1);
		    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		    con.setRequestMethod(method);
		    con.setRequestProperty("User-Agent", "Mozilla/5.0");
		    con.setRequestProperty("Accept-Language", "UTF-8");
		    con.setRequestProperty("Content-Type", "application/json");
		    con.setRequestProperty("Accept", "application/json");
		    con.setConnectTimeout(60000); 
		    con.setReadTimeout(60000); 
		    con.setUseCaches( false );
		    if(method.equalsIgnoreCase("POST")){
//		    	JSONObject json = new JSONObject();
//		        json.putAll( parameter );
		    	con.setDoOutput(true);
			    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
			    outputStreamWriter.write(json);
			    outputStreamWriter.flush();
		    }
		    int responseCode = con.getResponseCode();
		    System.out.println("\nSending 'POST' request to URL : " + url);
		    System.out.println("Post parameters : " + params);
		    System.out.println("Response Code : " + responseCode);
		    Log.d("CallJSONService ", "CallJSONService  json  " + json );
		    Log.d("CallJSONService ", "CallJSONService  responseCode  " + responseCode );
		    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		    String inputLine;
		    StringBuffer response = new StringBuffer();

		    while ((inputLine = in.readLine()) != null) {
		        response.append(inputLine + "\n");
		    }
		    in.close();
		    Log.d("CallXMLService ", "CallXMLService  result  " + result );
		        result = response.toString();
//		    } catch (UnsupportedEncodingException e) {
//		        e.printStackTrace();
//		    } catch (MalformedURLException e) {
//		        e.printStackTrace();
//		    } catch (ProtocolException e) {
//		        e.printStackTrace();
//		    } catch (IOException e) {
//		        e.printStackTrace();
//		    }catch (Exception e) {
//		        e.printStackTrace();
//		    }finally {
		    	 Log.d("TEST jsonText jsonText ", result);
		    	return  result;
//		    }
		 
	}
	 
		public static void setLocale(Context context) {

	       SharedPreferences sharedPreferences = context.getSharedPreferences("App", context.MODE_PRIVATE);
		   String language  = sharedPreferences.getString(LANGUAGE, "ru");
	       Resources res = context.getResources();
	       DisplayMetrics dm = res.getDisplayMetrics();
		   android.content.res.Configuration conf = res.getConfiguration();
		   conf.locale = new Locale(language.toLowerCase());
		   res.updateConfiguration(conf, dm);
		   
//		   FontsOverride.setDefaultFont(context, "normal", "fonts/SanFranciscoText-Regular.ttf");
//		   FontsOverride.setDefaultFont(context, "monospace", "fonts/Proxima_Nova_Light.otf"); //DEFAULT
//		   FontsOverride.setDefaultFont(context, "monospace", "fonts/SanFranciscoText-Medium.ttf"); 
		   FontsOverride.setDefaultFont(context, "DEFAULT", "fonts/SanFranciscoText-Regular.ttf");
	       FontsOverride.setDefaultFont(context, "MONOSPACE", "fonts/SanFranciscoText-Medium.ttf");
//	       FontsOverride.setDefaultFont(context, "BOLD", "fonts/SanFranciscoText-Bold.ttf");
//	       FontsOverride.setDefaultFont(context, "SANS_SERIF", "fonts/SanFranciscoText-Light.ttf");
	       new FontsOverride(context).loadFonts();
	    }
		public static final String PARAM_DEV_TOKEN = "PARAM_DEV_TOKEN";
		public static final String PARAM_IS_ENABLE = "PARAM_IS_ENABLE";
		public static final String PHONETYPE = "ANDROID";
		public static final String LANGUAGE = "LANGUAGE";
		
	public String getEncoded64ImageStringFromBitmap(Bitmap bitmap,int sampling) {
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    bitmap.compress(CompressFormat.PNG, sampling, stream);
	    byte[] byteFormat = stream.toByteArray();
	    // get the base 64 string
	    String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
	    return imgString;
	}
	public static void generateSpinnerList(final Context mContext,final Spinner spinner,final ArrayList<String> headerList){
		Log.d("generateProxySpinnerList ", " generateProxySpinnerList spinner ==-=-=- " + headerList.size());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, headerList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
}
