package b4a.example;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, true))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create " + (isFirst ? "(first time)" : "") + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (main) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static double[] _shap1 = null;
public static double _bs = 0;
public static anywheresoftware.b4a.phone.Phone.PhoneWakeState _pw1 = null;
public anywheresoftware.b4a.objects.LabelWrapper[] _lbl = null;
public anywheresoftware.b4a.objects.EditTextWrapper[] _et = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button1 = null;
public b4a.example.xgboost _xgboost = null;
public b4a.example.plot _plot = null;
public b4a.example.shap _shap = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (plot.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
String[] _headers = null;
String[] _firstrow = null;
int _i = 0;
 //BA.debugLineNum = 32;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 33;BA.debugLine="Activity.LoadLayout(\"Layout\")";
mostCurrent._activity.LoadLayout("Layout",mostCurrent.activityBA);
 //BA.debugLineNum = 34;BA.debugLine="Dim headers() As String=Array As String(\"Cement\",";
_headers = new String[]{"Cement","Blast Furnace Slag","Fly Ash","Water","Superplasticizer","Coarse Aggregate","Fine Aggregate","Age","NumBackground"};
 //BA.debugLineNum = 35;BA.debugLine="Dim firstRow() As String=Array As String(139.6,20";
_firstrow = new String[]{BA.NumberToString(139.6),BA.NumberToString(209.4),BA.NumberToString(0),BA.NumberToString(192),BA.NumberToString(0),BA.NumberToString(1047),BA.NumberToString(806.9),BA.NumberToString(3),BA.NumberToString(1000)};
 //BA.debugLineNum = 36;BA.debugLine="For i=0 To 8";
{
final int step4 = 1;
final int limit4 = (int) (8);
_i = (int) (0) ;
for (;_i <= limit4 ;_i = _i + step4 ) {
 //BA.debugLineNum = 37;BA.debugLine="lbl(i).Initialize(\"\")";
mostCurrent._lbl[_i].Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 38;BA.debugLine="Activity.AddView(lbl(i), 0%x, i*9%y+5%y, 50%x, 1";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lbl[_i].getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) (_i*anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA)+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 39;BA.debugLine="lbl(i).Text= headers(i)";
mostCurrent._lbl[_i].setText(BA.ObjectToCharSequence(_headers[_i]));
 //BA.debugLineNum = 40;BA.debugLine="lbl(i).Gravity=Gravity.CENTER";
mostCurrent._lbl[_i].setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 41;BA.debugLine="lbl(i).TextSize=20";
mostCurrent._lbl[_i].setTextSize((float) (20));
 //BA.debugLineNum = 42;BA.debugLine="et(i).Initialize(\"\")";
mostCurrent._et[_i].Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 43;BA.debugLine="Activity.AddView(et(i), 50%x, i*9%y+5%y, 20%x, 1";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._et[_i].getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),(int) (_i*anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA)+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 44;BA.debugLine="et(i).Text= firstRow(i)";
mostCurrent._et[_i].setText(BA.ObjectToCharSequence(_firstrow[_i]));
 //BA.debugLineNum = 45;BA.debugLine="et(i).Gravity=Gravity.CENTER";
mostCurrent._et[_i].setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 46;BA.debugLine="et(i).TextSize=20";
mostCurrent._et[_i].setTextSize((float) (20));
 }
};
 //BA.debugLineNum = 48;BA.debugLine="Button1.Top=9*9%y+5%y";
mostCurrent._button1.setTop((int) (9*anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA)+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA)));
 //BA.debugLineNum = 49;BA.debugLine="Button1.Width=100%x";
mostCurrent._button1.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 50;BA.debugLine="Button1.Text=\"Predict & Interpret\"";
mostCurrent._button1.setText(BA.ObjectToCharSequence("Predict & Interpret"));
 //BA.debugLineNum = 51;BA.debugLine="Button1.Left=0";
mostCurrent._button1.setLeft((int) (0));
 //BA.debugLineNum = 52;BA.debugLine="Button1.Height=10%y";
mostCurrent._button1.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 54;BA.debugLine="XGBoost.LoadModel(File.DirAssets, \"xgb_model.json";
mostCurrent._xgboost._loadmodel /*String*/ (mostCurrent.activityBA,anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"xgb_model.json");
 //BA.debugLineNum = 55;BA.debugLine="SHAP.LoadData(\"data.csv\")";
mostCurrent._shap._loaddata /*String*/ (mostCurrent.activityBA,"data.csv");
 //BA.debugLineNum = 57;BA.debugLine="pw1.KeepAlive(True)";
_pw1.KeepAlive(processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public static String  _button1_click() throws Exception{
double[] _x = null;
int _i = 0;
anywheresoftware.b4a.objects.collections.Map _m = null;
double _t1 = 0;
 //BA.debugLineNum = 61;BA.debugLine="Sub Button1_Click";
 //BA.debugLineNum = 62;BA.debugLine="Dim x(8) As Double";
_x = new double[(int) (8)];
;
 //BA.debugLineNum = 63;BA.debugLine="For i=0 To 7";
{
final int step2 = 1;
final int limit2 = (int) (7);
_i = (int) (0) ;
for (;_i <= limit2 ;_i = _i + step2 ) {
 //BA.debugLineNum = 64;BA.debugLine="x(i) = et(i).Text";
_x[_i] = (double)(Double.parseDouble(mostCurrent._et[_i].getText()));
 }
};
 //BA.debugLineNum = 67;BA.debugLine="Dim m As Map";
_m = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 68;BA.debugLine="m.Initialize";
_m.Initialize();
 //BA.debugLineNum = 69;BA.debugLine="Dim SHAP1() As Double";
_shap1 = new double[(int) (0)];
;
 //BA.debugLineNum = 70;BA.debugLine="Dim t1 As Double = DateTime.Now";
_t1 = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 71;BA.debugLine="m = SHAP.ExplainPrediction(x, et(8).Text)";
_m = mostCurrent._shap._explainprediction /*anywheresoftware.b4a.objects.collections.Map*/ (mostCurrent.activityBA,_x,(int)(Double.parseDouble(mostCurrent._et[(int) (8)].getText())));
 //BA.debugLineNum = 72;BA.debugLine="Log(DateTime.Now - t1)";
anywheresoftware.b4a.keywords.Common.LogImpl("3196619",BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.getNow()-_t1),0);
 //BA.debugLineNum = 73;BA.debugLine="Dim SHAP1() As Double=m.Get(\"shap_values\")";
_shap1 = (double[])(_m.Get((Object)("shap_values")));
 //BA.debugLineNum = 74;BA.debugLine="Dim bs As Double=m.Get(\"base_value\")";
_bs = (double)(BA.ObjectToNumber(_m.Get((Object)("base_value"))));
 //BA.debugLineNum = 75;BA.debugLine="For i = 0 To 7";
{
final int step13 = 1;
final int limit13 = (int) (7);
_i = (int) (0) ;
for (;_i <= limit13 ;_i = _i + step13 ) {
 //BA.debugLineNum = 76;BA.debugLine="Log(\"Feature \" & i & _ 	        \" SHAP = \" & SHA";
anywheresoftware.b4a.keywords.Common.LogImpl("3196623","Feature "+BA.NumberToString(_i)+" SHAP = "+BA.NumberToString(_shap1[_i]),0);
 }
};
 //BA.debugLineNum = 79;BA.debugLine="Log(\"Base Value = \" & m.Get(\"base_value\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("3196626","Base Value = "+BA.ObjectToString(_m.Get((Object)("base_value"))),0);
 //BA.debugLineNum = 80;BA.debugLine="Log(\"Prediction = \" & m.Get(\"prediction\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("3196627","Prediction = "+BA.ObjectToString(_m.Get((Object)("prediction"))),0);
 //BA.debugLineNum = 81;BA.debugLine="Log(\"Sum SHAP = \" & m.Get(\"sum_prediction\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("3196628","Sum SHAP = "+BA.ObjectToString(_m.Get((Object)("sum_prediction"))),0);
 //BA.debugLineNum = 82;BA.debugLine="Log(XGBoost.n)";
anywheresoftware.b4a.keywords.Common.LogImpl("3196629",BA.NumberToString(mostCurrent._xgboost._n /*double*/ ),0);
 //BA.debugLineNum = 83;BA.debugLine="StartActivity(Plot)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._plot.getObject()));
 //BA.debugLineNum = 84;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 25;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 27;BA.debugLine="Dim lbl(13) As Label";
mostCurrent._lbl = new anywheresoftware.b4a.objects.LabelWrapper[(int) (13)];
{
int d0 = mostCurrent._lbl.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._lbl[i0] = new anywheresoftware.b4a.objects.LabelWrapper();
}
}
;
 //BA.debugLineNum = 28;BA.debugLine="Dim et(13) As EditText";
mostCurrent._et = new anywheresoftware.b4a.objects.EditTextWrapper[(int) (13)];
{
int d0 = mostCurrent._et.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._et[i0] = new anywheresoftware.b4a.objects.EditTextWrapper();
}
}
;
 //BA.debugLineNum = 29;BA.debugLine="Dim Button1 As Button";
mostCurrent._button1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
xgboost._process_globals();
plot._process_globals();
shap._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 19;BA.debugLine="Dim SHAP1() As Double";
_shap1 = new double[(int) (0)];
;
 //BA.debugLineNum = 20;BA.debugLine="Dim bs As Double";
_bs = 0;
 //BA.debugLineNum = 21;BA.debugLine="Dim pw1 As PhoneWakeState";
_pw1 = new anywheresoftware.b4a.phone.Phone.PhoneWakeState();
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
}
