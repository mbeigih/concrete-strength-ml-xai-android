Explainable Android Application for HPC Compressive Strength Prediction Using XGBoost and SHAP
Overview
This repository accompanies the research study entitled "An Explainable Android-Based Decision Support System for High-Performance Concrete Strength Prediction Using XGBoost and Monte Carlo SHAP."
The project provides an Android application for predicting the compressive strength (CS) of high-performance concrete (HPC) using a trained XGBoost regression model and interpreting individual predictions using coalition-based Shapley values.
The application was developed using Basic4Android (B4A) and is designed to perform model inference and feature-level explanation locally on an Android device. The repository includes the Android application source code, a trained XGBoost model in JSON format, the HPC dataset, a Python notebook, and the compiled Android application package (APK).
Repository Contents
The repository contains the following files and resources:
File or directory	Description
android app source code/
Source-code directory for the Android application developed using Basic4Android (B4A).
Code.ipynb
Python notebook containing the associated model development and analysis code. Refer to the notebook for the operations implemented in Python.
hpc.apk
Compiled Android application package for installation on a compatible Android device or emulator.
hpc.csv
HPC dataset used in the project.
xgb_model.json
Trained XGBoost model exported in JSON format for model inference.
README.md
Project documentation and instructions.
Application Modules
The Android application integrates four principal functional modules:
1.	Data Input Module: Accepts the eight concrete mixture parameters.
2.	XGBoost Prediction Module: Predicts the compressive strength of the specified concrete mixture.
3.	Explainability Module: Calculates feature-level Shapley values using exhaustive coalition enumeration and background sampling.
4.	SHAP Visualization Module: Displays the feature contributions in a waterfall-style plot to support interpretation of the prediction.
The application connects the prediction and explanation stages in a single workflow, allowing users to examine both the predicted compressive strength and the contributions of individual mixture parameters.
Input Features
The trained model uses eight input features in the following order:
Feature	Description
C	Cement content
BFS	Blast furnace slag content
FA	Fly ash content
W	Water content
SP	Superplasticizer content
CA	Coarse aggregate content
FAg	Fine aggregate content
Age	Concrete age
The input order must be preserved when using the trained model. Input values must follow the units and conventions used during model development.
The model output, CS, represents the predicted compressive strength in MPa.
