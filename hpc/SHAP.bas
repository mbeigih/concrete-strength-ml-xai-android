B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=StaticCode
Version=13.3
@EndOfDesignText@
'============================================================
' SHAP.bas
' Exact Shapley Values using all coalitions
' Adjustable number of background samples
'
' Number of features = 8
' Number of all coalitions = 2^8 = 256
'============================================================

Sub Process_Globals

	Private BackgroundData As List

	Private Const NUM_FEATURES As Int = 8
	Private Const NUM_COALITIONS As Int = 256

End Sub


'============================================================
' Initialize
'============================================================

Public Sub Initialize

	BackgroundData.Initialize

End Sub


'============================================================
' Load background data
'
' CSV format:
' C,BFS,FA,W,SP,CA,FAg,Age,CS
'============================================================

Public Sub LoadData(FileName As String)

	BackgroundData.Initialize
	File.Copy(File.DirAssets, "data.csv", File.DirInternal, "data.csv")
	If File.Exists(File.DirInternal, FileName) = False Then
		Log("Background file not found: " & FileName)
		Return
	End If

	Dim rows As List
	rows = File.ReadList(File.DirInternal, FileName)

	For i = 1 To rows.Size - 1

		Dim line As String = rows.Get(i)

		If line.Trim <> "" Then

			Dim parts() As String
			parts = Regex.Split(",", line)

			If parts.Length >= 8 Then

				Dim X(8) As Double

				For j = 0 To 7
					X(j) = parts(j).Trim
				Next

				BackgroundData.Add(X)

			End If

		End If

	Next

	Log("Background samples loaded: " & BackgroundData.Size)

End Sub


'============================================================
' Main SHAP calculation
'
' x              = input sample
' NumBackground  = number of background observations
'
' Returns:
'   shap_values
'   base_value
'============================================================

Public Sub CalculateSHAP(x() As Double, NumBackground As Int) As Map

	Dim Result As Map
	Result.Initialize

	If BackgroundData.Size = 0 Then
		Log("Background data is empty.")
		Return Result
	End If

	If NumBackground < 1 Then
		NumBackground = 1
	End If

	If NumBackground > BackgroundData.Size Then
		NumBackground = BackgroundData.Size
	End If

	'--------------------------------------------------------
	' Randomly select unique background samples
	'--------------------------------------------------------

	Dim BackgroundIndices As List
	BackgroundIndices = RandomSampleIndices(BackgroundData.Size, NumBackground)

	Dim SHAP(8) As Double
	Dim BaseValue As Double = 0

	'--------------------------------------------------------
	' Process each background observation
	'--------------------------------------------------------

	For b = 0 To BackgroundIndices.Size - 1

		Dim Index As Int = BackgroundIndices.Get(b)

		Dim BackgroundX() As Double
		BackgroundX = BackgroundData.Get(Index)

		'----------------------------------------------------
		' Calculate prediction for all 256 coalitions
		'----------------------------------------------------

		Dim CoalitionPredictions(256) As Double

		For Mask = 0 To 255

			Dim CoalitionX(8) As Double

			For Feature = 0 To 7

				If Bit.And(Mask, Bit.ShiftLeft(1, Feature)) <> 0 Then

					CoalitionX(Feature) = x(Feature)

				Else

					CoalitionX(Feature) = BackgroundX(Feature)

				End If

			Next

			CoalitionPredictions(Mask) = XGBoost.Predict(CoalitionX)

		Next

		'----------------------------------------------------
		' Background prediction = empty coalition
		' Mask = 0
		'----------------------------------------------------

		BaseValue = BaseValue + CoalitionPredictions(0)

		'----------------------------------------------------
		' Calculate exact Shapley value for every feature
		'----------------------------------------------------

		For i = 0 To NUM_FEATURES - 1

			Dim Phi As Double = 0

			'Subsets S not containing feature i
			'There are 2^(8-1) = 128 such subsets

			For Mask = 0 To 255

				'Only subsets that DO NOT contain feature i
				If Bit.And(Mask, Bit.ShiftLeft(1, i)) = 0 Then

					Dim SSize As Int = PopCount(Mask)

					Dim MaskWithFeature As Int
					MaskWithFeature = Bit.Or(Mask, Bit.ShiftLeft(1, i))

					Dim MarginalContribution As Double
					MarginalContribution = CoalitionPredictions(MaskWithFeature) - _
                                           CoalitionPredictions(Mask)

					Dim Weight As Double
					Weight = Factorial(SSize) * _
                             Factorial(NUM_FEATURES - SSize - 1) / _
                             Factorial(NUM_FEATURES)

					Phi = Phi + Weight * MarginalContribution

				End If

			Next

			SHAP(i) = SHAP(i) + Phi

		Next

	Next

	'--------------------------------------------------------
	' Average over background observations
	'--------------------------------------------------------

	For i = 0 To 7
		SHAP(i) = SHAP(i) / NumBackground
	Next

	BaseValue = BaseValue / NumBackground

	Result.Put("shap_values", SHAP)
	Result.Put("base_value", BaseValue)
	Result.Put("num_background", NumBackground)

	Return Result

End Sub


'============================================================
' Explain complete prediction
'============================================================

Public Sub ExplainPrediction(x() As Double, NumBackground As Int) As Map

	Dim Result As Map
	Result.Initialize

	'--------------------------------------------------------
	' Calculate exact SHAP values
	'--------------------------------------------------------

	Dim SHAPResult As Map
	SHAPResult = CalculateSHAP(x, NumBackground)

	If SHAPResult.Size = 0 Then
		Return Result
	End If

	Dim SHAPValues() As Double
	SHAPValues = SHAPResult.Get("shap_values")

	Dim BaseValue As Double
	BaseValue = SHAPResult.Get("base_value")

	'--------------------------------------------------------
	' Final XGBoost prediction
	'--------------------------------------------------------

	Dim FinalPrediction As Double
	FinalPrediction = XGBoost.Predict(x)

	'--------------------------------------------------------
	' Sum SHAP values
	'--------------------------------------------------------

	Dim SumSHAP As Double = 0

	For i = 0 To 7
		SumSHAP = SumSHAP + SHAPValues(i)
	Next

	Dim ReconstructedPrediction As Double
	ReconstructedPrediction = BaseValue + SumSHAP

	'--------------------------------------------------------
	' Store results
	'--------------------------------------------------------

	Result.Put("shap_values", SHAPValues)
	Result.Put("base_value", BaseValue)
	Result.Put("prediction", FinalPrediction)
	Result.Put("reconstructed_prediction", ReconstructedPrediction)

	Result.Put("additivity_error", _
        FinalPrediction - ReconstructedPrediction)

	Result.Put("num_background", NumBackground)

	Return Result

End Sub


'============================================================
' Random unique background sampling
'============================================================

Private Sub RandomSampleIndices(n As Int, k As Int) As List

	Dim indices As List
	indices.Initialize

	For i = 0 To n - 1
		indices.Add(i)
	Next

	'Fisher-Yates shuffle

	For i = n - 1 To 1 Step -1

		Dim j As Int
		j = Rnd(0, i + 1)

		Dim temp As Int
		temp = indices.Get(i)

		indices.Set(i, indices.Get(j))
		indices.Set(j, temp)

	Next

	Dim Result As List
	Result.Initialize

	For i = 0 To k - 1
		Result.Add(indices.Get(i))
	Next

	Return Result

End Sub


'============================================================
' Number of set bits in an integer
'
' Example:
' 00000111 -> 3
'============================================================

Private Sub PopCount(Value As Int) As Int

	Dim Count As Int = 0

	For i = 0 To NUM_FEATURES - 1

		If Bit.And(Value, Bit.ShiftLeft(1, i)) <> 0 Then
			Count = Count + 1
		End If

	Next

	Return Count

End Sub


'============================================================
' Factorial
'============================================================

Private Sub Factorial(n As Int) As Double

	If n <= 1 Then
		Return 1
	End If

	Dim Result As Double = 1

	For i = 2 To n
		Result = Result * i
	Next

	Return Result

End Sub