"""
Machine Learning Training Pipeline for North Eastern Region (NER)
AI Multi-Hazard Risk Prediction Model (Random Forest / Gradient Boosting).

This script generates a realistic domain-grounded dataset representing the
meteorological and geomorphological risk factors of the 8 North Eastern states
(Assam, Meghalaya, Arunachal Pradesh, Nagaland, Manipur, Mizoram, Tripura, Sikkim)
and trains a multi-output Random Forest regressor to predict:
- Flood Risk (0.0 to 1.0)
- Landslide Risk (0.0 to 1.0)
- Road Disruption Risk (0.0 to 1.0)
- Weather Hazard Risk (0.0 to 1.0)
- Overall Composite Risk (0.0 to 1.0)
"""

import os
import joblib
import numpy as np
import pandas as pd
from sklearn.ensemble import RandomForestRegressor
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error, r2_score

def generate_synthetic_ner_dataset(n_samples: int = 5000, random_state: int = 42) -> pd.DataFrame:
    np.random.seed(random_state)
    
    # Feature inputs:
    # 1. rainfall3Hour: 0 to 60 mm
    rainfall3Hour = np.random.exponential(scale=10.0, size=n_samples).clip(0, 70)
    
    # 2. rainfall1Day: 0 to 250 mm (correlated with 3h)
    rainfall1Day = (rainfall3Hour * np.random.uniform(2.5, 5.0, size=n_samples) + 
                    np.random.exponential(scale=25.0, size=n_samples)).clip(0, 280)
    
    # 3. humidity: 40% to 100%
    humidity = np.random.uniform(50.0, 98.0, size=n_samples)
    
    # 4. windSpeed: 2 to 60 km/h
    windSpeed = np.random.weibull(a=1.8, size=n_samples) * 12.0
    windSpeed = windSpeed.clip(2.0, 65.0)
    
    # 5. roadCondition: 0.0 (good paved) to 1.0 (severely damaged/unpaved ghat)
    roadCondition = np.random.beta(a=2, b=3, size=n_samples)
    
    # 6. recentReports: 0 to 10 incidents reported in past 24h
    recentReports = np.random.poisson(lam=1.5, size=n_samples).clip(0, 12)
    
    # 7. floodHistory: historical floodplain vulnerability index (0.0 to 1.0)
    floodHistory = np.random.beta(a=2, b=4, size=n_samples)
    
    # 8. landslideHistory: steep mountain slope vulnerability index (0.0 to 1.0)
    landslideHistory = np.random.beta(a=3, b=3, size=n_samples)
    
    # 9. securityRisk: official restriction index (0.0 to 1.0)
    securityRisk = np.random.choice([0.0, 0.1, 0.2, 0.5, 0.8], p=[0.65, 0.20, 0.08, 0.05, 0.02], size=n_samples)
    
    # Calculate Ground-Truth Risk Targets based on geomorphological rules:
    floodRisk = (
        0.45 * (rainfall1Day / 120.0) +
        0.30 * (rainfall3Hour / 35.0) +
        0.25 * floodHistory +
        np.random.normal(0, 0.03, size=n_samples)
    ).clip(0.0, 1.0)
    
    landslideRisk = (
        0.40 * (rainfall1Day / 90.0) +
        0.30 * (rainfall3Hour / 25.0) +
        0.30 * landslideHistory +
        np.random.normal(0, 0.03, size=n_samples)
    ).clip(0.0, 1.0)
    
    weatherRisk = (
        0.40 * (windSpeed / 45.0) +
        0.40 * (rainfall3Hour / 30.0) +
        0.20 * (humidity / 100.0) +
        np.random.normal(0, 0.02, size=n_samples)
    ).clip(0.0, 1.0)
    
    roadDisruptionRisk = (
        0.40 * roadCondition +
        0.35 * (recentReports / 6.0).clip(0, 1.0) +
        0.25 * landslideRisk +
        np.random.normal(0, 0.02, size=n_samples)
    ).clip(0.0, 1.0)
    
    overallRisk = (
        0.30 * floodRisk +
        0.25 * landslideRisk +
        0.20 * weatherRisk +
        0.15 * roadDisruptionRisk +
        0.10 * securityRisk
    ).clip(0.0, 1.0)
    
    df = pd.DataFrame({
        'rainfall3Hour': rainfall3Hour,
        'rainfall1Day': rainfall1Day,
        'humidity': humidity,
        'windSpeed': windSpeed,
        'roadCondition': roadCondition,
        'recentReports': recentReports,
        'floodHistory': floodHistory,
        'landslideHistory': landslideHistory,
        'securityRisk': securityRisk,
        'floodRisk': floodRisk,
        'landslideRisk': landslideRisk,
        'weatherRisk': weatherRisk,
        'roadDisruptionRisk': roadDisruptionRisk,
        'overallRisk': overallRisk
    })
    
    return df

def train_and_save_model():
    print("Generating synthetic NER terrain & meteorological training dataset (N=5000)...")
    df = generate_synthetic_ner_dataset(n_samples=5000)
    
    features = [
        'rainfall3Hour', 'rainfall1Day', 'humidity', 'windSpeed',
        'roadCondition', 'recentReports', 'floodHistory', 'landslideHistory', 'securityRisk'
    ]
    
    targets = [
        'floodRisk', 'landslideRisk', 'weatherRisk', 'roadDisruptionRisk', 'overallRisk'
    ]
    
    X = df[features]
    y = df[targets]
    
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
    
    print("Training Random Forest Multi-Output Regressor (n_estimators=100)...")
    model = RandomForestRegressor(n_estimators=100, max_depth=12, random_state=42, n_jobs=-1)
    model.fit(X_train, y_train)
    
    y_pred = model.predict(X_test)
    r2 = r2_score(y_test, y_pred)
    mse = mean_squared_error(y_test, y_pred)
    
    print(f"Model Training Completed! R2 Score: {r2:.4f}, MSE: {mse:.4f}")
    
    model_dir = os.path.dirname(os.path.abspath(__file__))
    model_path = os.path.join(model_dir, "risk_model.joblib")
    joblib.dump(model, model_path)
    print(f"Model successfully saved to: {model_path}")
    return model

if __name__ == "__main__":
    train_and_save_model()
