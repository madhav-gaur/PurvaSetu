# AI Risk Prediction Microservice (PurvaSetu)

This service provides real-time machine learning predictions for multi-hazard logistics risk assessment across North East India.

## Features

- **Random Forest Regressor**: Trained on realistic domain-specific synthetic terrain, rainfall accumulation, slope vulnerability, and road degradation features.
- **REST API Endpoint**: `POST /predict-risk`
- **Fallback Integration**: Spring Boot smoothly falls back to its deterministic weighted formula if the microservice is offline.

## Setup & Running

```bash
# 1. Navigate to ai-service directory
cd ai-service

# 2. Install dependencies
pip install -r requirements.txt

# 3. Train the model
python model/train.py

# 4. Start the FastAPI server
uvicorn app:app --host 0.0.0.0 --port 8000 --reload
```

## API Documentation

Once started, interactive OpenAPI docs are available at: `http://localhost:8000/docs`
