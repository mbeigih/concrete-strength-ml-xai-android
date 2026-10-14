B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=StaticCode
Version=13.3
@EndOfDesignText@
'Code module
'Subs in this code module will be accessible from all modules.
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Private Trees As List

	Private BaseScore As Double
	Dim n As Double = 0
End Sub

'====================================================
' Load Model
'====================================================
Public Sub LoadModel(Dir As String, FileName As String)
	Trees.Initialize
	Trees.Clear

	Dim jp As JSONParser
	jp.Initialize(File.ReadString(Dir, FileName))

	Dim Root As Map = jp.NextObject


	Dim Learner As Map = Root.Get("learner")

	Dim LearnerModelParam As Map = Learner.Get("learner_model_param")

	BaseScore = LearnerModelParam.Get("base_score")


	Dim GradientBooster As Map = Learner.Get("gradient_booster")

	Dim BoosterModel As Map = GradientBooster.Get("model")

	Dim TreesList As List = BoosterModel.Get("trees")


	For Each TreeMap As Map In TreesList

		Dim T As Map
		T.Initialize

		T.Put("left_children", TreeMap.Get("left_children"))
		T.Put("right_children", TreeMap.Get("right_children"))

		T.Put("split_indices", TreeMap.Get("split_indices"))
		T.Put("split_conditions", TreeMap.Get("split_conditions"))

		T.Put("base_weights", TreeMap.Get("base_weights"))

		Trees.Add(T)

	Next


End Sub


'====================================================
Public Sub Predict(Features() As Double) As Double

	n = n + 1
	Dim Result As Double

	Result = BaseScore

	For i = 0 To Trees.Size - 1


		Dim Tree As Map = Trees.Get(i)

		Dim TreeOutput As Double

		TreeOutput = EvaluateTree(Tree, Features)


		Result = Result + TreeOutput


	Next


	Return Result

End Sub

'====================================================
Private Sub EvaluateTree(Tree As Map, Features() As Double) As Double

	Dim LeftChildren As List = Tree.Get("left_children")
	Dim RightChildren As List = Tree.Get("right_children")
	Dim SplitIndices As List = Tree.Get("split_indices")
	Dim SplitConditions As List = Tree.Get("split_conditions")
	Dim BaseWeights As List = Tree.Get("base_weights")

	Dim Node As Int = 0


	Do While True

		Dim LeftChild As Int = LeftChildren.Get(Node)

		If LeftChild = -1 Then Exit

		Dim RightChild As Int = RightChildren.Get(Node)

		Dim FeatureIndex As Int = SplitIndices.Get(Node)

		Dim Threshold As Double = SplitConditions.Get(Node)

		If Features(FeatureIndex) < Threshold Then


			Node = LeftChild

		Else

			Node = RightChild

		End If

	Loop

	Dim LeafValue As Double

	LeafValue = BaseWeights.Get(Node)

	Return LeafValue

End Sub