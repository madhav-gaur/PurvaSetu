"""
FastAPI Microservice for AI-Based Logistics Risk & Hazard Prediction
Part of the North Eastern Region (NER) Smart Logistics Intelligence Platform.
"""

import os
import joblib
import numpy as np
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field
from typing import Optional, List

app = FastAPI(
    title="PurvaSetu AI Risk Prediction Service",
    description="Microservice providing machine learning predictions for flood, landslide, weather, and road disruption risks.",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

MODEL_PATH = os.path.join(os.path.dirname(os.path.abspath(__file__)), "model", "risk_model.joblib")
model = None

@app.on_event("startup")
def load_model():
    global model
    if os.path.exists(MODEL_PATH):
        try:
            model = joblib.load(MODEL_PATH)
            print(f"Loaded trained ML model from {MODEL_PATH}")
        except Exception as e:
            print(f"Error loading model: {e}")
    else:
        print(f"Model file not found at {MODEL_PATH}. Running training now...")
        try:
            from model.train import train_and_save_model
            model = train_and_save_model()
        except Exception as e:
            print(f"Training on startup failed: {e}")

class RiskPredictionInput(BaseModel):
    rainfall3Hour: Optional[float] = Field(default=0.0, description="3-hour accumulated rainfall (mm)")
    rainfall1Day: Optional[float] = Field(default=0.0, description="24-hour accumulated rainfall (mm)")
    humidity: Optional[float] = Field(default=75.0, description="Relative humidity (%)")
    windSpeed: Optional[float] = Field(default=10.0, description="Wind speed (km/h)")
    roadCondition: Optional[float] = Field(default=0.1, description="Road surface damage factor (0.0=paved to 1.0=severely damaged)")
    recentReports: Optional[int] = Field(default=0, description="Number of active incident reports near corridor")
    floodHistory: Optional[float] = Field(default=0.2, description="Historical floodplain vulnerability index (0.0 to 1.0)")
    landslideHistory: Optional[float] = Field(default=0.25, description="Historical mountain slope landslide vulnerability (0.0 to 1.0)")
    securityRisk: Optional[float] = Field(default=0.05, description="Official curfew or operational restriction weight (0.0 to 1.0)")
    shipmentPriority: Optional[str] = Field(default="MEDIUM", description="Shipment priority: CRITICAL, HIGH, MEDIUM, LOW")

class RiskPredictionOutput(BaseModel):
    floodRisk: float
    landslideRisk: float
    roadDisruptionRisk: float
    weatherRisk: float
    securityRisk: float
    overallRisk: float
    riskLevel: str
    predictionSource: str
    contributingFactors: List[str]
    recommendation: str

@app.get("/health")
def health_check():
    return {
        "status": "HEALTHY",
        "service": "NER Logistics AI Risk Prediction Service",
        "modelLoaded": model is not None,
        "framework": "FastAPI + scikit-learn Random Forest"
    }

@app.get("/")
def root():
    return {
        "name": "NER AI Risk Engine API",
        "docs": "/docs",
        "health": "/health"
    }

@app.post("/predict-risk", response_model=RiskPredictionOutput)
def predict_risk(input_data: RiskPredictionInput):
    features = np.array([[
        input_data.rainfall3Hour,
        input_data.rainfall1Day,
        input_data.humidity,
        input_data.windSpeed,
        input_data.roadCondition,
        input_data.recentReports,
        input_data.floodHistory,
        input_data.landslideHistory,
        input_data.securityRisk
    ]])

    if model is not None:
        try:
            preds = model.predict(features)[0]
            flood_risk = float(np.clip(preds[0], 0.0, 1.0))
            landslide_risk = float(np.clip(preds[1], 0.0, 1.0))
            weather_risk = float(np.clip(preds[2], 0.0, 1.0))
            disruption_risk = float(np.clip(preds[3], 0.0, 1.0))
            overall_risk = float(np.clip(preds[4], 0.0, 1.0))
            source = "FASTAPI_RANDOM_FOREST_ML"
        except Exception as e:
            print(f"Prediction inference error: {e}, falling back to analytical rules.")
            flood_risk, landslide_risk, weather_risk, disruption_risk, overall_risk, source = fallback_prediction(input_data)
    else:
        flood_risk, landslide_risk, weather_risk, disruption_risk, overall_risk, source = fallback_prediction(input_data)

    sec_risk = float(input_data.securityRisk)

    if overall_risk >= 0.81:
        level = "CRITICAL"
    elif overall_risk >= 0.61:
        level = "HIGH"
    elif overall_risk >= 0.31:
        level = "MEDIUM"
    else:
        level = "LOW"

    factors = []
    if flood_risk > 0.50:
        factors.append(f"Elevated flood inundation probability ({int(flood_risk*100)}%)")
    if landslide_risk > 0.50:
        factors.append(f"Steep slope rain saturation triggering landslide risk ({int(landslide_risk*100)}%)")
    if weather_risk > 0.50:
        factors.append(f"Severe precipitation and gale winds ({int(weather_risk*100)}%)")
    if disruption_risk > 0.50:
        factors.append(f"Active road blockage or damaged infrastructure ({int(disruption_risk*100)}%)")
    if sec_risk > 0.50:
        factors.append("Official highway restriction or curfew advisory")

    if overall_risk > 0.60:
        recommendation = "High hazard risk detected. Reroute via safe disaster bypass recommended."
    else:
        recommendation = "Route is within acceptable safety parameters."

    return RiskPredictionOutput(
        floodRisk=round(flood_risk, 2),
        landslideRisk=round(landslide_risk, 2),
        roadDisruptionRisk=round(disruption_risk, 2),
        weatherRisk=round(weather_risk, 2),
        securityRisk=round(sec_risk, 2),
        overallRisk=round(overall_risk, 2),
        riskLevel=level,
        predictionSource=source,
        contributingFactors=factors,
        recommendation=recommendation
    )

def fallback_prediction(data: RiskPredictionInput):
    flood = min(1.0, (data.rainfall1Day / 120.0) * 0.45 + (data.rainfall3Hour / 35.0) * 0.30 + data.floodHistory * 0.25)
    landslide = min(1.0, (data.rainfall1Day / 90.0) * 0.40 + (data.rainfall3Hour / 25.0) * 0.30 + data.landslideHistory * 0.30)
    weather = min(1.0, (data.windSpeed / 45.0) * 0.40 + (data.rainfall3Hour / 30.0) * 0.40 + (data.humidity / 100.0) * 0.20)
    disruption = min(1.0, data.roadCondition * 0.40 + min(1.0, data.recentReports / 6.0) * 0.35 + landslide * 0.25)
    overall = min(1.0, 0.30 * flood + 0.25 * landslide + 0.20 * weather + 0.15 * disruption + 0.10 * data.securityRisk)
    return flood, landslide, weather, disruption, overall, "AI_SERVICE_ANALYTICAL_FALLBACK"

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("app:app", host="0.0.0.0", port=8000, reload=True)
