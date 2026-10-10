package b4a.example.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_layout{

public static void LS_general(anywheresoftware.b4a.BA ba, android.view.View parent, anywheresoftware.b4a.keywords.LayoutValues lv, java.util.Map props,
java.util.Map<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) throws Exception {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 2;BA.debugLine="AutoScaleAll"[Layout/General script]
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
//BA.debugLineNum = 4;BA.debugLine="Label1.Left=0"[Layout/General script]
views.get("label1").vw.setLeft((int)(0d));
//BA.debugLineNum = 5;BA.debugLine="Label1.Top=0"[Layout/General script]
views.get("label1").vw.setTop((int)(0d));
//BA.debugLineNum = 6;BA.debugLine="Label1.Height=5%y"[Layout/General script]
views.get("label1").vw.setHeight((int)((5d / 100 * height)));
//BA.debugLineNum = 7;BA.debugLine="Label1.Width=50%x"[Layout/General script]
views.get("label1").vw.setWidth((int)((50d / 100 * width)));
//BA.debugLineNum = 8;BA.debugLine="Label1.Text=\"Feature\""[Layout/General script]
((TextViewWrapper)views.get("label1").vw).setText("Feature");
//BA.debugLineNum = 10;BA.debugLine="Label2.Left=50%x"[Layout/General script]
views.get("label2").vw.setLeft((int)((50d / 100 * width)));
//BA.debugLineNum = 11;BA.debugLine="Label2.Top=0"[Layout/General script]
views.get("label2").vw.setTop((int)(0d));
//BA.debugLineNum = 12;BA.debugLine="Label2.Height=5%y"[Layout/General script]
views.get("label2").vw.setHeight((int)((5d / 100 * height)));
//BA.debugLineNum = 13;BA.debugLine="Label2.Width=50%x"[Layout/General script]
views.get("label2").vw.setWidth((int)((50d / 100 * width)));
//BA.debugLineNum = 14;BA.debugLine="Label2.Text=\"Value\""[Layout/General script]
((TextViewWrapper)views.get("label2").vw).setText("Value");

}
}