B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=13.3
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: True
	#IncludeTitle: True
	
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim featureNames() As String
	Dim baseValue As Double
	Dim SHAP2() As Double
	Private pnl As Panel
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("Layout2")
'	Sub DrawWaterfall( _
'    pnl As Panel, _
'    SHAP() As Double, _
'    baseValue As Double, _
'    featureNames() As String)
	featureNames= Array As String("C", "BFS", "FA", "W", "SP", "CA", "FAg", "Age")
	baseValue = Main.bs

	SHAP2=Main.SHAP1
	pnl.RemoveAllViews

	Dim cvs As Canvas
	cvs.Initialize(pnl)

	Dim W As Int = pnl.Width
	Dim H As Int = pnl.Height

	'========================
	' style
	'========================
	Dim marginLeft As Int = 5dip
	Dim marginBottom As Int = 60dip
	Dim chartTop As Int = 40dip

	Dim barWidth As Int = 40dip
	Dim gap As Int = 2dip

	Dim axisColor As Int = Colors.Black
	Dim posColor As Int = Colors.RGB(40,180,90)
	Dim negColor As Int = Colors.RGB(220,70,70)

	Dim txtColor As Int = Colors.Black

	'========================
	' prediction range
	'========================

	Dim finalPred As Double = baseValue

	For i = 0 To SHAP2.Length - 1
		finalPred = finalPred + SHAP2(i)
	Next

	Dim minV As Double = Min(baseValue, finalPred)
	Dim maxV As Double = Max(baseValue, finalPred)

	Dim cumulative As Double = baseValue

	For i = 0 To SHAP2.Length - 1

		cumulative = cumulative + SHAP2(i)

		If cumulative < minV Then minV = cumulative
		If cumulative > maxV Then maxV = cumulative

	Next

	Dim rangeV As Double = maxV - minV

	If rangeV = 0 Then rangeV = 1

	'========================
	' axis
	'========================

	Dim chartHeight As Int = H - marginBottom - chartTop

	cvs.DrawLine( _
        marginLeft, _
        chartTop, _
        marginLeft, _
        H - marginBottom, _
        axisColor, _
        2dip)

	'========================
	' helper function
	'========================

	Dim currentValue As Double = baseValue

	'base line
	Dim y0 As Int = _
        chartTop + chartHeight - _
        ((baseValue - minV) / rangeV) * chartHeight

	cvs.DrawLine( _
        marginLeft - 10dip, _
        y0, _
        W - 20dip, _
        y0, _
        Colors.LightGray, _
        1dip)

	cvs.DrawText( _
        "Base=" & NumberFormat2(baseValue,1,2,2,False), _
        250dip, _
        y0, _
        Typeface.DEFAULT_BOLD, _
        14, _
        txtColor, _
        "LEFT")

	'========================
	' draw bars
	'========================

	Dim x As Int = marginLeft + 20dip

	For i = 0 To SHAP2.Length - 1

		Dim startValue As Double = currentValue
		Dim endValue As Double = currentValue + SHAP2(i)

		Dim yStart As Int = _
            chartTop + chartHeight - _
            ((startValue - minV) / rangeV) * chartHeight

		Dim yEnd As Int = _
            chartTop + chartHeight - _
            ((endValue - minV) / rangeV) * chartHeight

		Dim topY As Int = Min(yStart, yEnd)
		Dim bottomY As Int = Max(yStart, yEnd)

		Dim r As Rect
		r.Initialize( _
            x, _
            topY, _
            x + barWidth, _
            bottomY)

		If SHAP2(i) >= 0 Then
			cvs.DrawRect(r, posColor, True, 0)
		Else
			cvs.DrawRect(r, negColor, True, 0)
		End If

		'value text
		cvs.DrawText( _
            NumberFormat2(SHAP2(i),1,2,2,False), _
            x + barWidth/2, _
            topY - 8dip, _
            Typeface.DEFAULT_BOLD, _
            12, _
            txtColor, _
            "CENTER")

		'feature name
'		cvs.DrawText( _
'            featureNames(i), _
'            x + barWidth/2, _
'            H - 20dip, _
'            Typeface.DEFAULT, _
'            12, _
'            txtColor, _
'            "CENTER")
		cvs.DrawTextRotated( _
            featureNames(i), _
            x + barWidth/2, _
            H - 20dip, _
            Typeface.DEFAULT, _
            12, _
            txtColor, _
            "CENTER",90)
		
		'connector line
		cvs.DrawLine( _
            x + barWidth, _
            yEnd, _
            x + barWidth + gap, _
            yEnd, _
            Colors.Gray, _
            2dip)

		currentValue = endValue

		x = x + barWidth + gap

	Next

	'========================
	' final prediction
	'========================

	Dim yFinal As Int = _
        chartTop + chartHeight - _
        ((finalPred - minV) / rangeV) * chartHeight

	cvs.DrawLine( _
        marginLeft - 10dip, _
        yFinal, _
        W - 20dip, _
        yFinal, _
        Colors.Blue, _
        3dip)

	cvs.DrawText( _
        "Prediction=" & _
        NumberFormat2(finalPred,1,2,2,False), _
        10dip, _
        yFinal - 10dip, _
        Typeface.DEFAULT_BOLD, _
        15, _
        Colors.Blue, _
        "LEFT")

	pnl.Invalidate

'End Sub
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub
