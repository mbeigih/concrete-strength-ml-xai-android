package b4a.example;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class xgboost {
private static xgboost mostCurrent = new xgboost();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.collections.List _trees = null;
public static double _basescore = 0;
public static double _n = 0;
public b4a.example.main _main = null;
public b4a.example.plot _plot = null;
public b4a.example.shap _shap = null;
public static double  _evaluatetree(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.collections.Map _tree,double[] _features) throws Exception{
anywheresoftware.b4a.objects.collections.List _leftchildren = null;
anywheresoftware.b4a.objects.collections.List _rightchildren = null;
anywheresoftware.b4a.objects.collections.List _splitindices = null;
anywheresoftware.b4a.objects.collections.List _splitconditions = null;
anywheresoftware.b4a.objects.collections.List _baseweights = null;
int _node = 0;
int _leftchild = 0;
int _rightchild = 0;
int _featureindex = 0;
double _threshold = 0;
double _leafvalue = 0;
 //BA.debugLineNum = 89;BA.debugLine="Private Sub EvaluateTree(Tree As Map, Features() A";
 //BA.debugLineNum = 91;BA.debugLine="Dim LeftChildren As List = Tree.Get(\"left_childre";
_leftchildren = new anywheresoftware.b4a.objects.collections.List();
_leftchildren = (anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(_tree.Get((Object)("left_children"))));
 //BA.debugLineNum = 92;BA.debugLine="Dim RightChildren As List = Tree.Get(\"right_child";
_rightchildren = new anywheresoftware.b4a.objects.collections.List();
_rightchildren = (anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(_tree.Get((Object)("right_children"))));
 //BA.debugLineNum = 93;BA.debugLine="Dim SplitIndices As List = Tree.Get(\"split_indice";
_splitindices = new anywheresoftware.b4a.objects.collections.List();
_splitindices = (anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(_tree.Get((Object)("split_indices"))));
 //BA.debugLineNum = 94;BA.debugLine="Dim SplitConditions As List = Tree.Get(\"split_con";
_splitconditions = new anywheresoftware.b4a.objects.collections.List();
_splitconditions = (anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(_tree.Get((Object)("split_conditions"))));
 //BA.debugLineNum = 95;BA.debugLine="Dim BaseWeights As List = Tree.Get(\"base_weights\"";
_baseweights = new anywheresoftware.b4a.objects.collections.List();
_baseweights = (anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(_tree.Get((Object)("base_weights"))));
 //BA.debugLineNum = 97;BA.debugLine="Dim Node As Int = 0";
_node = (int) (0);
 //BA.debugLineNum = 100;BA.debugLine="Do While True";
while (anywheresoftware.b4a.keywords.Common.True) {
 //BA.debugLineNum = 102;BA.debugLine="Dim LeftChild As Int = LeftChildren.Get(Node)";
_leftchild = (int)(BA.ObjectToNumber(_leftchildren.Get(_node)));
 //BA.debugLineNum = 104;BA.debugLine="If LeftChild = -1 Then Exit";
if (_leftchild==-1) { 
if (true) break;};
 //BA.debugLineNum = 106;BA.debugLine="Dim RightChild As Int = RightChildren.Get(Node)";
_rightchild = (int)(BA.ObjectToNumber(_rightchildren.Get(_node)));
 //BA.debugLineNum = 108;BA.debugLine="Dim FeatureIndex As Int = SplitIndices.Get(Node)";
_featureindex = (int)(BA.ObjectToNumber(_splitindices.Get(_node)));
 //BA.debugLineNum = 110;BA.debugLine="Dim Threshold As Double = SplitConditions.Get(No";
_threshold = (double)(BA.ObjectToNumber(_splitconditions.Get(_node)));
 //BA.debugLineNum = 112;BA.debugLine="If Features(FeatureIndex) < Threshold Then";
if (_features[_featureindex]<_threshold) { 
 //BA.debugLineNum = 115;BA.debugLine="Node = LeftChild";
_node = _leftchild;
 }else {
 //BA.debugLineNum = 119;BA.debugLine="Node = RightChild";
_node = _rightchild;
 };
 }
;
 //BA.debugLineNum = 125;BA.debugLine="Dim LeafValue As Double";
_leafvalue = 0;
 //BA.debugLineNum = 127;BA.debugLine="LeafValue = BaseWeights.Get(Node)";
_leafvalue = (double)(BA.ObjectToNumber(_baseweights.Get(_node)));
 //BA.debugLineNum = 129;BA.debugLine="Return LeafValue";
if (true) return _leafvalue;
 //BA.debugLineNum = 131;BA.debugLine="End Sub";
return 0;
}
public static String  _loadmodel(anywheresoftware.b4a.BA _ba,String _dir,String _filename) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _jp = null;
anywheresoftware.b4a.objects.collections.Map _root = null;
anywheresoftware.b4a.objects.collections.Map _learner = null;
anywheresoftware.b4a.objects.collections.Map _learnermodelparam = null;
anywheresoftware.b4a.objects.collections.Map _gradientbooster = null;
anywheresoftware.b4a.objects.collections.Map _boostermodel = null;
anywheresoftware.b4a.objects.collections.List _treeslist = null;
anywheresoftware.b4a.objects.collections.Map _treemap = null;
anywheresoftware.b4a.objects.collections.Map _t = null;
 //BA.debugLineNum = 15;BA.debugLine="Public Sub LoadModel(Dir As String, FileName As St";
 //BA.debugLineNum = 16;BA.debugLine="Trees.Initialize";
_trees.Initialize();
 //BA.debugLineNum = 17;BA.debugLine="Trees.Clear";
_trees.Clear();
 //BA.debugLineNum = 19;BA.debugLine="Dim jp As JSONParser";
_jp = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 20;BA.debugLine="jp.Initialize(File.ReadString(Dir, FileName))";
_jp.Initialize(anywheresoftware.b4a.keywords.Common.File.ReadString(_dir,_filename));
 //BA.debugLineNum = 22;BA.debugLine="Dim Root As Map = jp.NextObject";
_root = new anywheresoftware.b4a.objects.collections.Map();
_root = _jp.NextObject();
 //BA.debugLineNum = 25;BA.debugLine="Dim Learner As Map = Root.Get(\"learner\")";
_learner = new anywheresoftware.b4a.objects.collections.Map();
_learner = (anywheresoftware.b4a.objects.collections.Map) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.Map(), (java.util.Map)(_root.Get((Object)("learner"))));
 //BA.debugLineNum = 27;BA.debugLine="Dim LearnerModelParam As Map = Learner.Get(\"learn";
_learnermodelparam = new anywheresoftware.b4a.objects.collections.Map();
_learnermodelparam = (anywheresoftware.b4a.objects.collections.Map) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.Map(), (java.util.Map)(_learner.Get((Object)("learner_model_param"))));
 //BA.debugLineNum = 29;BA.debugLine="BaseScore = LearnerModelParam.Get(\"base_score\")";
_basescore = (double)(BA.ObjectToNumber(_learnermodelparam.Get((Object)("base_score"))));
 //BA.debugLineNum = 32;BA.debugLine="Dim GradientBooster As Map = Learner.Get(\"gradien";
_gradientbooster = new anywheresoftware.b4a.objects.collections.Map();
_gradientbooster = (anywheresoftware.b4a.objects.collections.Map) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.Map(), (java.util.Map)(_learner.Get((Object)("gradient_booster"))));
 //BA.debugLineNum = 34;BA.debugLine="Dim BoosterModel As Map = GradientBooster.Get(\"mo";
_boostermodel = new anywheresoftware.b4a.objects.collections.Map();
_boostermodel = (anywheresoftware.b4a.objects.collections.Map) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.Map(), (java.util.Map)(_gradientbooster.Get((Object)("model"))));
 //BA.debugLineNum = 36;BA.debugLine="Dim TreesList As List = BoosterModel.Get(\"trees\")";
_treeslist = new anywheresoftware.b4a.objects.collections.List();
_treeslist = (anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(_boostermodel.Get((Object)("trees"))));
 //BA.debugLineNum = 39;BA.debugLine="For Each TreeMap As Map In TreesList";
_treemap = new anywheresoftware.b4a.objects.collections.Map();
{
final anywheresoftware.b4a.BA.IterableList group12 = _treeslist;
final int groupLen12 = group12.getSize()
;int index12 = 0;
;
for (; index12 < groupLen12;index12++){
_treemap = (anywheresoftware.b4a.objects.collections.Map) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.Map(), (java.util.Map)(group12.Get(index12)));
 //BA.debugLineNum = 41;BA.debugLine="Dim T As Map";
_t = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 42;BA.debugLine="T.Initialize";
_t.Initialize();
 //BA.debugLineNum = 44;BA.debugLine="T.Put(\"left_children\", TreeMap.Get(\"left_childre";
_t.Put((Object)("left_children"),_treemap.Get((Object)("left_children")));
 //BA.debugLineNum = 45;BA.debugLine="T.Put(\"right_children\", TreeMap.Get(\"right_child";
_t.Put((Object)("right_children"),_treemap.Get((Object)("right_children")));
 //BA.debugLineNum = 47;BA.debugLine="T.Put(\"split_indices\", TreeMap.Get(\"split_indice";
_t.Put((Object)("split_indices"),_treemap.Get((Object)("split_indices")));
 //BA.debugLineNum = 48;BA.debugLine="T.Put(\"split_conditions\", TreeMap.Get(\"split_con";
_t.Put((Object)("split_conditions"),_treemap.Get((Object)("split_conditions")));
 //BA.debugLineNum = 50;BA.debugLine="T.Put(\"base_weights\", TreeMap.Get(\"base_weights\"";
_t.Put((Object)("base_weights"),_treemap.Get((Object)("base_weights")));
 //BA.debugLineNum = 52;BA.debugLine="Trees.Add(T)";
_trees.Add((Object)(_t.getObject()));
 }
};
 //BA.debugLineNum = 57;BA.debugLine="End Sub";
return "";
}
public static double  _predict(anywheresoftware.b4a.BA _ba,double[] _features) throws Exception{
double _result = 0;
int _i = 0;
anywheresoftware.b4a.objects.collections.Map _tree = null;
double _treeoutput = 0;
 //BA.debugLineNum = 61;BA.debugLine="Public Sub Predict(Features() As Double) As Double";
 //BA.debugLineNum = 63;BA.debugLine="n = n + 1";
_n = _n+1;
 //BA.debugLineNum = 64;BA.debugLine="Dim Result As Double";
_result = 0;
 //BA.debugLineNum = 66;BA.debugLine="Result = BaseScore";
_result = _basescore;
 //BA.debugLineNum = 68;BA.debugLine="For i = 0 To Trees.Size - 1";
{
final int step4 = 1;
final int limit4 = (int) (_trees.getSize()-1);
_i = (int) (0) ;
for (;_i <= limit4 ;_i = _i + step4 ) {
 //BA.debugLineNum = 71;BA.debugLine="Dim Tree As Map = Trees.Get(i)";
_tree = new anywheresoftware.b4a.objects.collections.Map();
_tree = (anywheresoftware.b4a.objects.collections.Map) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.Map(), (java.util.Map)(_trees.Get(_i)));
 //BA.debugLineNum = 73;BA.debugLine="Dim TreeOutput As Double";
_treeoutput = 0;
 //BA.debugLineNum = 75;BA.debugLine="TreeOutput = EvaluateTree(Tree, Features)";
_treeoutput = _evaluatetree(_ba,_tree,_features);
 //BA.debugLineNum = 78;BA.debugLine="Result = Result + TreeOutput";
_result = _result+_treeoutput;
 }
};
 //BA.debugLineNum = 84;BA.debugLine="Return Result";
if (true) return _result;
 //BA.debugLineNum = 86;BA.debugLine="End Sub";
return 0;
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 3;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 6;BA.debugLine="Private Trees As List";
_trees = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 8;BA.debugLine="Private BaseScore As Double";
_basescore = 0;
 //BA.debugLineNum = 9;BA.debugLine="Dim n As Double = 0";
_n = 0;
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
}
