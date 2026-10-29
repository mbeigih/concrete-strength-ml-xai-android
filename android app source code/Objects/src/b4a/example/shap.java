package b4a.example;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class shap {
private static shap mostCurrent = new shap();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.collections.List _backgrounddata = null;
public static int _num_features = 0;
public b4a.example.main _main = null;
public b4a.example.xgboost _xgboost = null;
public b4a.example.plot _plot = null;
public static anywheresoftware.b4a.objects.collections.Map  _calculateshap(anywheresoftware.b4a.BA _ba,double[] _x,int _numbackground) throws Exception{
anywheresoftware.b4a.objects.collections.Map _result = null;
anywheresoftware.b4a.objects.collections.List _backgroundindices = null;
double[] _shap = null;
double _basevalue = 0;
int _b = 0;
int _index = 0;
double[] _backgroundx = null;
double[] _coalitionpredictions = null;
int _mask = 0;
double[] _coalitionx = null;
int _feature = 0;
int _i = 0;
double _phi = 0;
int _ssize = 0;
int _maskwithfeature = 0;
double _marginalcontribution = 0;
double _weight = 0;
 //BA.debugLineNum = 90;BA.debugLine="Public Sub CalculateSHAP(x() As Double, NumBackgro";
 //BA.debugLineNum = 92;BA.debugLine="Dim Result As Map";
_result = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 93;BA.debugLine="Result.Initialize";
_result.Initialize();
 //BA.debugLineNum = 95;BA.debugLine="If BackgroundData.Size = 0 Then";
if (_backgrounddata.getSize()==0) { 
 //BA.debugLineNum = 96;BA.debugLine="Log(\"Background data is empty.\")";
anywheresoftware.b4a.keywords.Common.LogImpl("31048582","Background data is empty.",0);
 //BA.debugLineNum = 97;BA.debugLine="Return Result";
if (true) return _result;
 };
 //BA.debugLineNum = 100;BA.debugLine="If NumBackground < 1 Then";
if (_numbackground<1) { 
 //BA.debugLineNum = 101;BA.debugLine="NumBackground = 1";
_numbackground = (int) (1);
 };
 //BA.debugLineNum = 104;BA.debugLine="If NumBackground > BackgroundData.Size Then";
if (_numbackground>_backgrounddata.getSize()) { 
 //BA.debugLineNum = 105;BA.debugLine="NumBackground = BackgroundData.Size";
_numbackground = _backgrounddata.getSize();
 };
 //BA.debugLineNum = 112;BA.debugLine="Dim BackgroundIndices As List";
_backgroundindices = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 113;BA.debugLine="BackgroundIndices = RandomSampleIndices(Backgroun";
_backgroundindices = _randomsampleindices(_ba,_backgrounddata.getSize(),_numbackground);
 //BA.debugLineNum = 115;BA.debugLine="Dim SHAP(8) As Double";
_shap = new double[(int) (8)];
;
 //BA.debugLineNum = 116;BA.debugLine="Dim BaseValue As Double = 0";
_basevalue = 0;
 //BA.debugLineNum = 122;BA.debugLine="For b = 0 To BackgroundIndices.Size - 1";
{
final int step17 = 1;
final int limit17 = (int) (_backgroundindices.getSize()-1);
_b = (int) (0) ;
for (;_b <= limit17 ;_b = _b + step17 ) {
 //BA.debugLineNum = 124;BA.debugLine="Dim Index As Int = BackgroundIndices.Get(b)";
_index = (int)(BA.ObjectToNumber(_backgroundindices.Get(_b)));
 //BA.debugLineNum = 126;BA.debugLine="Dim BackgroundX() As Double";
_backgroundx = new double[(int) (0)];
;
 //BA.debugLineNum = 127;BA.debugLine="BackgroundX = BackgroundData.Get(Index)";
_backgroundx = (double[])(_backgrounddata.Get(_index));
 //BA.debugLineNum = 133;BA.debugLine="Dim CoalitionPredictions(256) As Double";
_coalitionpredictions = new double[(int) (256)];
;
 //BA.debugLineNum = 135;BA.debugLine="For Mask = 0 To 255";
{
final int step22 = 1;
final int limit22 = (int) (255);
_mask = (int) (0) ;
for (;_mask <= limit22 ;_mask = _mask + step22 ) {
 //BA.debugLineNum = 137;BA.debugLine="Dim CoalitionX(8) As Double";
_coalitionx = new double[(int) (8)];
;
 //BA.debugLineNum = 139;BA.debugLine="For Feature = 0 To 7";
{
final int step24 = 1;
final int limit24 = (int) (7);
_feature = (int) (0) ;
for (;_feature <= limit24 ;_feature = _feature + step24 ) {
 //BA.debugLineNum = 141;BA.debugLine="If Bit.And(Mask, Bit.ShiftLeft(1, Feature)) <>";
if (anywheresoftware.b4a.keywords.Common.Bit.And(_mask,anywheresoftware.b4a.keywords.Common.Bit.ShiftLeft((int) (1),_feature))!=0) { 
 //BA.debugLineNum = 143;BA.debugLine="CoalitionX(Feature) = x(Feature)";
_coalitionx[_feature] = _x[_feature];
 }else {
 //BA.debugLineNum = 147;BA.debugLine="CoalitionX(Feature) = BackgroundX(Feature)";
_coalitionx[_feature] = _backgroundx[_feature];
 };
 }
};
 //BA.debugLineNum = 153;BA.debugLine="CoalitionPredictions(Mask) = XGBoost.Predict(Co";
_coalitionpredictions[_mask] = mostCurrent._xgboost._predict /*double*/ (_ba,_coalitionx);
 }
};
 //BA.debugLineNum = 162;BA.debugLine="BaseValue = BaseValue + CoalitionPredictions(0)";
_basevalue = _basevalue+_coalitionpredictions[(int) (0)];
 //BA.debugLineNum = 168;BA.debugLine="For i = 0 To NUM_FEATURES - 1";
{
final int step34 = 1;
final int limit34 = (int) (_num_features-1);
_i = (int) (0) ;
for (;_i <= limit34 ;_i = _i + step34 ) {
 //BA.debugLineNum = 170;BA.debugLine="Dim Phi As Double = 0";
_phi = 0;
 //BA.debugLineNum = 175;BA.debugLine="For Mask = 0 To 255";
{
final int step36 = 1;
final int limit36 = (int) (255);
_mask = (int) (0) ;
for (;_mask <= limit36 ;_mask = _mask + step36 ) {
 //BA.debugLineNum = 178;BA.debugLine="If Bit.And(Mask, Bit.ShiftLeft(1, i)) = 0 Then";
if (anywheresoftware.b4a.keywords.Common.Bit.And(_mask,anywheresoftware.b4a.keywords.Common.Bit.ShiftLeft((int) (1),_i))==0) { 
 //BA.debugLineNum = 180;BA.debugLine="Dim SSize As Int = PopCount(Mask)";
_ssize = _popcount(_ba,_mask);
 //BA.debugLineNum = 182;BA.debugLine="Dim MaskWithFeature As Int";
_maskwithfeature = 0;
 //BA.debugLineNum = 183;BA.debugLine="MaskWithFeature = Bit.Or(Mask, Bit.ShiftLeft(";
_maskwithfeature = anywheresoftware.b4a.keywords.Common.Bit.Or(_mask,anywheresoftware.b4a.keywords.Common.Bit.ShiftLeft((int) (1),_i));
 //BA.debugLineNum = 185;BA.debugLine="Dim MarginalContribution As Double";
_marginalcontribution = 0;
 //BA.debugLineNum = 186;BA.debugLine="MarginalContribution = CoalitionPredictions(M";
_marginalcontribution = _coalitionpredictions[_maskwithfeature]-_coalitionpredictions[_mask];
 //BA.debugLineNum = 189;BA.debugLine="Dim Weight As Double";
_weight = 0;
 //BA.debugLineNum = 190;BA.debugLine="Weight = Factorial(SSize) * _";
_weight = _factorial(_ba,_ssize)*_factorial(_ba,(int) (_num_features-_ssize-1))/(double)_factorial(_ba,_num_features);
 //BA.debugLineNum = 194;BA.debugLine="Phi = Phi + Weight * MarginalContribution";
_phi = _phi+_weight*_marginalcontribution;
 };
 }
};
 //BA.debugLineNum = 200;BA.debugLine="SHAP(i) = SHAP(i) + Phi";
_shap[_i] = _shap[_i]+_phi;
 }
};
 }
};
 //BA.debugLineNum = 210;BA.debugLine="For i = 0 To 7";
{
final int step51 = 1;
final int limit51 = (int) (7);
_i = (int) (0) ;
for (;_i <= limit51 ;_i = _i + step51 ) {
 //BA.debugLineNum = 211;BA.debugLine="SHAP(i) = SHAP(i) / NumBackground";
_shap[_i] = _shap[_i]/(double)_numbackground;
 }
};
 //BA.debugLineNum = 214;BA.debugLine="BaseValue = BaseValue / NumBackground";
_basevalue = _basevalue/(double)_numbackground;
 //BA.debugLineNum = 216;BA.debugLine="Result.Put(\"shap_values\", SHAP)";
_result.Put((Object)("shap_values"),(Object)(_shap));
 //BA.debugLineNum = 217;BA.debugLine="Result.Put(\"base_value\", BaseValue)";
_result.Put((Object)("base_value"),(Object)(_basevalue));
 //BA.debugLineNum = 218;BA.debugLine="Result.Put(\"num_background\", NumBackground)";
_result.Put((Object)("num_background"),(Object)(_numbackground));
 //BA.debugLineNum = 220;BA.debugLine="Return Result";
if (true) return _result;
 //BA.debugLineNum = 222;BA.debugLine="End Sub";
return null;
}
public static anywheresoftware.b4a.objects.collections.Map  _explainprediction(anywheresoftware.b4a.BA _ba,double[] _x,int _numbackground) throws Exception{
anywheresoftware.b4a.objects.collections.Map _result = null;
anywheresoftware.b4a.objects.collections.Map _shapresult = null;
double[] _shapvalues = null;
double _basevalue = 0;
double _finalprediction = 0;
double _sumshap = 0;
int _i = 0;
double _reconstructedprediction = 0;
 //BA.debugLineNum = 229;BA.debugLine="Public Sub ExplainPrediction(x() As Double, NumBac";
 //BA.debugLineNum = 231;BA.debugLine="Dim Result As Map";
_result = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 232;BA.debugLine="Result.Initialize";
_result.Initialize();
 //BA.debugLineNum = 238;BA.debugLine="Dim SHAPResult As Map";
_shapresult = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 239;BA.debugLine="SHAPResult = CalculateSHAP(x, NumBackground)";
_shapresult = _calculateshap(_ba,_x,_numbackground);
 //BA.debugLineNum = 241;BA.debugLine="If SHAPResult.Size = 0 Then";
if (_shapresult.getSize()==0) { 
 //BA.debugLineNum = 242;BA.debugLine="Return Result";
if (true) return _result;
 };
 //BA.debugLineNum = 245;BA.debugLine="Dim SHAPValues() As Double";
_shapvalues = new double[(int) (0)];
;
 //BA.debugLineNum = 246;BA.debugLine="SHAPValues = SHAPResult.Get(\"shap_values\")";
_shapvalues = (double[])(_shapresult.Get((Object)("shap_values")));
 //BA.debugLineNum = 248;BA.debugLine="Dim BaseValue As Double";
_basevalue = 0;
 //BA.debugLineNum = 249;BA.debugLine="BaseValue = SHAPResult.Get(\"base_value\")";
_basevalue = (double)(BA.ObjectToNumber(_shapresult.Get((Object)("base_value"))));
 //BA.debugLineNum = 255;BA.debugLine="Dim FinalPrediction As Double";
_finalprediction = 0;
 //BA.debugLineNum = 256;BA.debugLine="FinalPrediction = XGBoost.Predict(x)";
_finalprediction = mostCurrent._xgboost._predict /*double*/ (_ba,_x);
 //BA.debugLineNum = 262;BA.debugLine="Dim SumSHAP As Double = 0";
_sumshap = 0;
 //BA.debugLineNum = 264;BA.debugLine="For i = 0 To 7";
{
final int step15 = 1;
final int limit15 = (int) (7);
_i = (int) (0) ;
for (;_i <= limit15 ;_i = _i + step15 ) {
 //BA.debugLineNum = 265;BA.debugLine="SumSHAP = SumSHAP + SHAPValues(i)";
_sumshap = _sumshap+_shapvalues[_i];
 }
};
 //BA.debugLineNum = 268;BA.debugLine="Dim ReconstructedPrediction As Double";
_reconstructedprediction = 0;
 //BA.debugLineNum = 269;BA.debugLine="ReconstructedPrediction = BaseValue + SumSHAP";
_reconstructedprediction = _basevalue+_sumshap;
 //BA.debugLineNum = 275;BA.debugLine="Result.Put(\"shap_values\", SHAPValues)";
_result.Put((Object)("shap_values"),(Object)(_shapvalues));
 //BA.debugLineNum = 276;BA.debugLine="Result.Put(\"base_value\", BaseValue)";
_result.Put((Object)("base_value"),(Object)(_basevalue));
 //BA.debugLineNum = 277;BA.debugLine="Result.Put(\"prediction\", FinalPrediction)";
_result.Put((Object)("prediction"),(Object)(_finalprediction));
 //BA.debugLineNum = 278;BA.debugLine="Result.Put(\"reconstructed_prediction\", Reconstruc";
_result.Put((Object)("reconstructed_prediction"),(Object)(_reconstructedprediction));
 //BA.debugLineNum = 280;BA.debugLine="Result.Put(\"additivity_error\", _         FinalPre";
_result.Put((Object)("additivity_error"),(Object)(_finalprediction-_reconstructedprediction));
 //BA.debugLineNum = 283;BA.debugLine="Result.Put(\"num_background\", NumBackground)";
_result.Put((Object)("num_background"),(Object)(_numbackground));
 //BA.debugLineNum = 285;BA.debugLine="Return Result";
if (true) return _result;
 //BA.debugLineNum = 287;BA.debugLine="End Sub";
return null;
}
public static double  _factorial(anywheresoftware.b4a.BA _ba,int _n) throws Exception{
double _result = 0;
int _i = 0;
 //BA.debugLineNum = 358;BA.debugLine="Private Sub Factorial(n As Int) As Double";
 //BA.debugLineNum = 360;BA.debugLine="If n <= 1 Then";
if (_n<=1) { 
 //BA.debugLineNum = 361;BA.debugLine="Return 1";
if (true) return 1;
 };
 //BA.debugLineNum = 364;BA.debugLine="Dim Result As Double = 1";
_result = 1;
 //BA.debugLineNum = 366;BA.debugLine="For i = 2 To n";
{
final int step5 = 1;
final int limit5 = _n;
_i = (int) (2) ;
for (;_i <= limit5 ;_i = _i + step5 ) {
 //BA.debugLineNum = 367;BA.debugLine="Result = Result * i";
_result = _result*_i;
 }
};
 //BA.debugLineNum = 370;BA.debugLine="Return Result";
if (true) return _result;
 //BA.debugLineNum = 372;BA.debugLine="End Sub";
return 0;
}
public static String  _initialize(anywheresoftware.b4a.BA _ba) throws Exception{
 //BA.debugLineNum = 23;BA.debugLine="Public Sub Initialize";
 //BA.debugLineNum = 25;BA.debugLine="BackgroundData.Initialize";
_backgrounddata.Initialize();
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return "";
}
public static String  _loaddata(anywheresoftware.b4a.BA _ba,String _filename) throws Exception{
anywheresoftware.b4a.objects.collections.List _rows = null;
int _i = 0;
String _line = "";
String[] _parts = null;
double[] _x = null;
int _j = 0;
 //BA.debugLineNum = 37;BA.debugLine="Public Sub LoadData(FileName As String)";
 //BA.debugLineNum = 39;BA.debugLine="BackgroundData.Initialize";
_backgrounddata.Initialize();
 //BA.debugLineNum = 40;BA.debugLine="File.Copy(File.DirAssets, \"data.csv\", File.DirInt";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"data.csv",anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"data.csv");
 //BA.debugLineNum = 41;BA.debugLine="If File.Exists(File.DirInternal, FileName) = Fals";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 42;BA.debugLine="Log(\"Background file not found: \" & FileName)";
anywheresoftware.b4a.keywords.Common.LogImpl("3983045","Background file not found: "+_filename,0);
 //BA.debugLineNum = 43;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 46;BA.debugLine="Dim rows As List";
_rows = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 47;BA.debugLine="rows = File.ReadList(File.DirInternal, FileName)";
_rows = anywheresoftware.b4a.keywords.Common.File.ReadList(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename);
 //BA.debugLineNum = 49;BA.debugLine="For i = 1 To rows.Size - 1";
{
final int step9 = 1;
final int limit9 = (int) (_rows.getSize()-1);
_i = (int) (1) ;
for (;_i <= limit9 ;_i = _i + step9 ) {
 //BA.debugLineNum = 51;BA.debugLine="Dim line As String = rows.Get(i)";
_line = BA.ObjectToString(_rows.Get(_i));
 //BA.debugLineNum = 53;BA.debugLine="If line.Trim <> \"\" Then";
if ((_line.trim()).equals("") == false) { 
 //BA.debugLineNum = 55;BA.debugLine="Dim parts() As String";
_parts = new String[(int) (0)];
java.util.Arrays.fill(_parts,"");
 //BA.debugLineNum = 56;BA.debugLine="parts = Regex.Split(\",\", line)";
_parts = anywheresoftware.b4a.keywords.Common.Regex.Split(",",_line);
 //BA.debugLineNum = 58;BA.debugLine="If parts.Length >= 8 Then";
if (_parts.length>=8) { 
 //BA.debugLineNum = 60;BA.debugLine="Dim X(8) As Double";
_x = new double[(int) (8)];
;
 //BA.debugLineNum = 62;BA.debugLine="For j = 0 To 7";
{
final int step16 = 1;
final int limit16 = (int) (7);
_j = (int) (0) ;
for (;_j <= limit16 ;_j = _j + step16 ) {
 //BA.debugLineNum = 63;BA.debugLine="X(j) = parts(j).Trim";
_x[_j] = (double)(Double.parseDouble(_parts[_j].trim()));
 }
};
 //BA.debugLineNum = 66;BA.debugLine="BackgroundData.Add(X)";
_backgrounddata.Add((Object)(_x));
 };
 };
 }
};
 //BA.debugLineNum = 74;BA.debugLine="Log(\"Background samples loaded: \" & BackgroundDat";
anywheresoftware.b4a.keywords.Common.LogImpl("3983077","Background samples loaded: "+BA.NumberToString(_backgrounddata.getSize()),0);
 //BA.debugLineNum = 76;BA.debugLine="End Sub";
return "";
}
public static int  _popcount(anywheresoftware.b4a.BA _ba,int _value) throws Exception{
int _count = 0;
int _i = 0;
 //BA.debugLineNum = 337;BA.debugLine="Private Sub PopCount(Value As Int) As Int";
 //BA.debugLineNum = 339;BA.debugLine="Dim Count As Int = 0";
_count = (int) (0);
 //BA.debugLineNum = 341;BA.debugLine="For i = 0 To NUM_FEATURES - 1";
{
final int step2 = 1;
final int limit2 = (int) (_num_features-1);
_i = (int) (0) ;
for (;_i <= limit2 ;_i = _i + step2 ) {
 //BA.debugLineNum = 343;BA.debugLine="If Bit.And(Value, Bit.ShiftLeft(1, i)) <> 0 Then";
if (anywheresoftware.b4a.keywords.Common.Bit.And(_value,anywheresoftware.b4a.keywords.Common.Bit.ShiftLeft((int) (1),_i))!=0) { 
 //BA.debugLineNum = 344;BA.debugLine="Count = Count + 1";
_count = (int) (_count+1);
 };
 }
};
 //BA.debugLineNum = 349;BA.debugLine="Return Count";
if (true) return _count;
 //BA.debugLineNum = 351;BA.debugLine="End Sub";
return 0;
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 10;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 12;BA.debugLine="Private BackgroundData As List";
_backgrounddata = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 14;BA.debugLine="Private Const NUM_FEATURES As Int = 8";
_num_features = (int) (8);
 //BA.debugLineNum = 16;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.List  _randomsampleindices(anywheresoftware.b4a.BA _ba,int _n,int _k) throws Exception{
anywheresoftware.b4a.objects.collections.List _indices = null;
int _i = 0;
int _j = 0;
int _temp = 0;
anywheresoftware.b4a.objects.collections.List _result = null;
 //BA.debugLineNum = 294;BA.debugLine="Private Sub RandomSampleIndices(n As Int, k As Int";
 //BA.debugLineNum = 296;BA.debugLine="Dim indices As List";
_indices = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 297;BA.debugLine="indices.Initialize";
_indices.Initialize();
 //BA.debugLineNum = 299;BA.debugLine="For i = 0 To n - 1";
{
final int step3 = 1;
final int limit3 = (int) (_n-1);
_i = (int) (0) ;
for (;_i <= limit3 ;_i = _i + step3 ) {
 //BA.debugLineNum = 300;BA.debugLine="indices.Add(i)";
_indices.Add((Object)(_i));
 }
};
 //BA.debugLineNum = 305;BA.debugLine="For i = n - 1 To 1 Step -1";
{
final int step6 = -1;
final int limit6 = (int) (1);
_i = (int) (_n-1) ;
for (;_i >= limit6 ;_i = _i + step6 ) {
 //BA.debugLineNum = 307;BA.debugLine="Dim j As Int";
_j = 0;
 //BA.debugLineNum = 308;BA.debugLine="j = Rnd(0, i + 1)";
_j = anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (_i+1));
 //BA.debugLineNum = 310;BA.debugLine="Dim temp As Int";
_temp = 0;
 //BA.debugLineNum = 311;BA.debugLine="temp = indices.Get(i)";
_temp = (int)(BA.ObjectToNumber(_indices.Get(_i)));
 //BA.debugLineNum = 313;BA.debugLine="indices.Set(i, indices.Get(j))";
_indices.Set(_i,_indices.Get(_j));
 //BA.debugLineNum = 314;BA.debugLine="indices.Set(j, temp)";
_indices.Set(_j,(Object)(_temp));
 }
};
 //BA.debugLineNum = 318;BA.debugLine="Dim Result As List";
_result = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 319;BA.debugLine="Result.Initialize";
_result.Initialize();
 //BA.debugLineNum = 321;BA.debugLine="For i = 0 To k - 1";
{
final int step16 = 1;
final int limit16 = (int) (_k-1);
_i = (int) (0) ;
for (;_i <= limit16 ;_i = _i + step16 ) {
 //BA.debugLineNum = 322;BA.debugLine="Result.Add(indices.Get(i))";
_result.Add(_indices.Get(_i));
 }
};
 //BA.debugLineNum = 325;BA.debugLine="Return Result";
if (true) return _result;
 //BA.debugLineNum = 327;BA.debugLine="End Sub";
return null;
}
}
