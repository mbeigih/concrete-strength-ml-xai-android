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

public class plot extends Activity implements B4AActivity{
	public static plot mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.plot");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (plot).");
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
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.plot");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.plot", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (plot) Create " + (isFirst ? "(first time)" : "") + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (plot) Resume **");
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
		return plot.class;
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
            BA.LogInfo("** Activity (plot) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (plot) Pause event (activity is not paused). **");
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
            plot mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (plot) Resume **");
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
public static String[] _featurenames = null;
public static double _basevalue = 0;
public static double[] _shap2 = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
public b4a.example.main _main = null;
public b4a.example.xgboost _xgboost = null;
public b4a.example.shap _shap = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper _cvs = null;
int _w = 0;
int _h = 0;
int _marginleft = 0;
int _marginbottom = 0;
int _charttop = 0;
int _barwidth = 0;
int _gap = 0;
int _axiscolor = 0;
int _poscolor = 0;
int _negcolor = 0;
int _txtcolor = 0;
double _finalpred = 0;
int _i = 0;
double _minv = 0;
double _maxv = 0;
double _cumulative = 0;
double _rangev = 0;
int _chartheight = 0;
double _currentvalue = 0;
int _y0 = 0;
int _x = 0;
double _startvalue = 0;
double _endvalue = 0;
int _ystart = 0;
int _yend = 0;
int _topy = 0;
int _bottomy = 0;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _r = null;
int _yfinal = 0;
 //BA.debugLineNum = 22;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 24;BA.debugLine="Activity.LoadLayout(\"Layout2\")";
mostCurrent._activity.LoadLayout("Layout2",mostCurrent.activityBA);
 //BA.debugLineNum = 30;BA.debugLine="featureNames= Array As String(\"C\", \"BFS\", \"FA\", \"";
mostCurrent._featurenames = new String[]{"C","BFS","FA","W","SP","CA","FAg","Age"};
 //BA.debugLineNum = 31;BA.debugLine="baseValue = 37.844437430216566";
_basevalue = 37.844437430216566;
 //BA.debugLineNum = 33;BA.debugLine="SHAP2=Main.SHAP1";
_shap2 = mostCurrent._main._shap1 /*double[]*/ ;
 //BA.debugLineNum = 34;BA.debugLine="pnl.RemoveAllViews";
mostCurrent._pnl.RemoveAllViews();
 //BA.debugLineNum = 36;BA.debugLine="Dim cvs As Canvas";
_cvs = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 37;BA.debugLine="cvs.Initialize(pnl)";
_cvs.Initialize((android.view.View)(mostCurrent._pnl.getObject()));
 //BA.debugLineNum = 39;BA.debugLine="Dim W As Int = pnl.Width";
_w = mostCurrent._pnl.getWidth();
 //BA.debugLineNum = 40;BA.debugLine="Dim H As Int = pnl.Height";
_h = mostCurrent._pnl.getHeight();
 //BA.debugLineNum = 45;BA.debugLine="Dim marginLeft As Int = 5dip";
_marginleft = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5));
 //BA.debugLineNum = 46;BA.debugLine="Dim marginBottom As Int = 60dip";
_marginbottom = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60));
 //BA.debugLineNum = 47;BA.debugLine="Dim chartTop As Int = 40dip";
_charttop = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40));
 //BA.debugLineNum = 49;BA.debugLine="Dim barWidth As Int = 40dip";
_barwidth = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40));
 //BA.debugLineNum = 50;BA.debugLine="Dim gap As Int = 2dip";
_gap = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2));
 //BA.debugLineNum = 52;BA.debugLine="Dim axisColor As Int = Colors.Black";
_axiscolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 53;BA.debugLine="Dim posColor As Int = Colors.RGB(40,180,90)";
_poscolor = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (40),(int) (180),(int) (90));
 //BA.debugLineNum = 54;BA.debugLine="Dim negColor As Int = Colors.RGB(220,70,70)";
_negcolor = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (220),(int) (70),(int) (70));
 //BA.debugLineNum = 56;BA.debugLine="Dim txtColor As Int = Colors.Black";
_txtcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 62;BA.debugLine="Dim finalPred As Double = baseValue";
_finalpred = _basevalue;
 //BA.debugLineNum = 64;BA.debugLine="For i = 0 To SHAP2.Length - 1";
{
final int step20 = 1;
final int limit20 = (int) (_shap2.length-1);
_i = (int) (0) ;
for (;_i <= limit20 ;_i = _i + step20 ) {
 //BA.debugLineNum = 65;BA.debugLine="finalPred = finalPred + SHAP2(i)";
_finalpred = _finalpred+_shap2[_i];
 }
};
 //BA.debugLineNum = 68;BA.debugLine="Dim minV As Double = Min(baseValue, finalPred)";
_minv = anywheresoftware.b4a.keywords.Common.Min(_basevalue,_finalpred);
 //BA.debugLineNum = 69;BA.debugLine="Dim maxV As Double = Max(baseValue, finalPred)";
_maxv = anywheresoftware.b4a.keywords.Common.Max(_basevalue,_finalpred);
 //BA.debugLineNum = 71;BA.debugLine="Dim cumulative As Double = baseValue";
_cumulative = _basevalue;
 //BA.debugLineNum = 73;BA.debugLine="For i = 0 To SHAP2.Length - 1";
{
final int step26 = 1;
final int limit26 = (int) (_shap2.length-1);
_i = (int) (0) ;
for (;_i <= limit26 ;_i = _i + step26 ) {
 //BA.debugLineNum = 75;BA.debugLine="cumulative = cumulative + SHAP2(i)";
_cumulative = _cumulative+_shap2[_i];
 //BA.debugLineNum = 77;BA.debugLine="If cumulative < minV Then minV = cumulative";
if (_cumulative<_minv) { 
_minv = _cumulative;};
 //BA.debugLineNum = 78;BA.debugLine="If cumulative > maxV Then maxV = cumulative";
if (_cumulative>_maxv) { 
_maxv = _cumulative;};
 }
};
 //BA.debugLineNum = 82;BA.debugLine="Dim rangeV As Double = maxV - minV";
_rangev = _maxv-_minv;
 //BA.debugLineNum = 84;BA.debugLine="If rangeV = 0 Then rangeV = 1";
if (_rangev==0) { 
_rangev = 1;};
 //BA.debugLineNum = 90;BA.debugLine="Dim chartHeight As Int = H - marginBottom - chart";
_chartheight = (int) (_h-_marginbottom-_charttop);
 //BA.debugLineNum = 92;BA.debugLine="cvs.DrawLine( _         marginLeft, _         cha";
_cvs.DrawLine((float) (_marginleft),(float) (_charttop),(float) (_marginleft),(float) (_h-_marginbottom),_axiscolor,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 //BA.debugLineNum = 104;BA.debugLine="Dim currentValue As Double = baseValue";
_currentvalue = _basevalue;
 //BA.debugLineNum = 107;BA.debugLine="Dim y0 As Int = _         chartTop + chartHeight";
_y0 = (int) (_charttop+_chartheight-((_basevalue-_minv)/(double)_rangev)*_chartheight);
 //BA.debugLineNum = 111;BA.debugLine="cvs.DrawLine( _         marginLeft - 10dip, _";
_cvs.DrawLine((float) (_marginleft-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),(float) (_y0),(float) (_w-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))),(float) (_y0),anywheresoftware.b4a.keywords.Common.Colors.LightGray,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1))));
 //BA.debugLineNum = 119;BA.debugLine="cvs.DrawText( _         \"Base=\" & NumberFormat2(b";
_cvs.DrawText(mostCurrent.activityBA,"Base="+anywheresoftware.b4a.keywords.Common.NumberFormat2(_basevalue,(int) (1),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (250))),(float) (_y0),anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD,(float) (14),_txtcolor,BA.getEnumFromString(android.graphics.Paint.Align.class,"LEFT"));
 //BA.debugLineNum = 132;BA.debugLine="Dim x As Int = marginLeft + 20dip";
_x = (int) (_marginleft+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20)));
 //BA.debugLineNum = 134;BA.debugLine="For i = 0 To SHAP2.Length - 1";
{
final int step40 = 1;
final int limit40 = (int) (_shap2.length-1);
_i = (int) (0) ;
for (;_i <= limit40 ;_i = _i + step40 ) {
 //BA.debugLineNum = 136;BA.debugLine="Dim startValue As Double = currentValue";
_startvalue = _currentvalue;
 //BA.debugLineNum = 137;BA.debugLine="Dim endValue As Double = currentValue + SHAP2(i)";
_endvalue = _currentvalue+_shap2[_i];
 //BA.debugLineNum = 139;BA.debugLine="Dim yStart As Int = _             chartTop + cha";
_ystart = (int) (_charttop+_chartheight-((_startvalue-_minv)/(double)_rangev)*_chartheight);
 //BA.debugLineNum = 143;BA.debugLine="Dim yEnd As Int = _             chartTop + chart";
_yend = (int) (_charttop+_chartheight-((_endvalue-_minv)/(double)_rangev)*_chartheight);
 //BA.debugLineNum = 147;BA.debugLine="Dim topY As Int = Min(yStart, yEnd)";
_topy = (int) (anywheresoftware.b4a.keywords.Common.Min(_ystart,_yend));
 //BA.debugLineNum = 148;BA.debugLine="Dim bottomY As Int = Max(yStart, yEnd)";
_bottomy = (int) (anywheresoftware.b4a.keywords.Common.Max(_ystart,_yend));
 //BA.debugLineNum = 150;BA.debugLine="Dim r As Rect";
_r = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 151;BA.debugLine="r.Initialize( _             x, _             top";
_r.Initialize(_x,_topy,(int) (_x+_barwidth),_bottomy);
 //BA.debugLineNum = 157;BA.debugLine="If SHAP2(i) >= 0 Then";
if (_shap2[_i]>=0) { 
 //BA.debugLineNum = 158;BA.debugLine="cvs.DrawRect(r, posColor, True, 0)";
_cvs.DrawRect((android.graphics.Rect)(_r.getObject()),_poscolor,anywheresoftware.b4a.keywords.Common.True,(float) (0));
 }else {
 //BA.debugLineNum = 160;BA.debugLine="cvs.DrawRect(r, negColor, True, 0)";
_cvs.DrawRect((android.graphics.Rect)(_r.getObject()),_negcolor,anywheresoftware.b4a.keywords.Common.True,(float) (0));
 };
 //BA.debugLineNum = 164;BA.debugLine="cvs.DrawText( _             NumberFormat2(SHAP2(";
_cvs.DrawText(mostCurrent.activityBA,anywheresoftware.b4a.keywords.Common.NumberFormat2(_shap2[_i],(int) (1),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False),(float) (_x+_barwidth/(double)2),(float) (_topy-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD,(float) (12),_txtcolor,BA.getEnumFromString(android.graphics.Paint.Align.class,"CENTER"));
 //BA.debugLineNum = 182;BA.debugLine="cvs.DrawTextRotated( _             featureNames(";
_cvs.DrawTextRotated(mostCurrent.activityBA,mostCurrent._featurenames[_i],(float) (_x+_barwidth/(double)2),(float) (_h-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))),anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT,(float) (12),_txtcolor,BA.getEnumFromString(android.graphics.Paint.Align.class,"CENTER"),(float) (90));
 //BA.debugLineNum = 192;BA.debugLine="cvs.DrawLine( _             x + barWidth, _";
_cvs.DrawLine((float) (_x+_barwidth),(float) (_yend),(float) (_x+_barwidth+_gap),(float) (_yend),anywheresoftware.b4a.keywords.Common.Colors.Gray,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 //BA.debugLineNum = 200;BA.debugLine="currentValue = endValue";
_currentvalue = _endvalue;
 //BA.debugLineNum = 202;BA.debugLine="x = x + barWidth + gap";
_x = (int) (_x+_barwidth+_gap);
 }
};
 //BA.debugLineNum = 210;BA.debugLine="Dim yFinal As Int = _         chartTop + chartHei";
_yfinal = (int) (_charttop+_chartheight-((_finalpred-_minv)/(double)_rangev)*_chartheight);
 //BA.debugLineNum = 214;BA.debugLine="cvs.DrawLine( _         marginLeft - 10dip, _";
_cvs.DrawLine((float) (_marginleft-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),(float) (_yfinal),(float) (_w-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))),(float) (_yfinal),anywheresoftware.b4a.keywords.Common.Colors.Blue,(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (3))));
 //BA.debugLineNum = 222;BA.debugLine="cvs.DrawText( _         \"Prediction=\" & _";
_cvs.DrawText(mostCurrent.activityBA,"Prediction="+anywheresoftware.b4a.keywords.Common.NumberFormat2(_finalpred,(int) (1),(int) (2),(int) (2),anywheresoftware.b4a.keywords.Common.False),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),(float) (_yfinal-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD,(float) (15),anywheresoftware.b4a.keywords.Common.Colors.Blue,BA.getEnumFromString(android.graphics.Paint.Align.class,"LEFT"));
 //BA.debugLineNum = 232;BA.debugLine="pnl.Invalidate";
mostCurrent._pnl.Invalidate();
 //BA.debugLineNum = 235;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 241;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 243;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 237;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 239;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 13;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 16;BA.debugLine="Dim featureNames() As String";
mostCurrent._featurenames = new String[(int) (0)];
java.util.Arrays.fill(mostCurrent._featurenames,"");
 //BA.debugLineNum = 17;BA.debugLine="Dim baseValue As Double";
_basevalue = 0;
 //BA.debugLineNum = 18;BA.debugLine="Dim SHAP2() As Double";
_shap2 = new double[(int) (0)];
;
 //BA.debugLineNum = 19;BA.debugLine="Private pnl As Panel";
mostCurrent._pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 11;BA.debugLine="End Sub";
return "";
}
}
