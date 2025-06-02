from fastapi import Request, HTTPException
import requests

def validate_session(request: Request):
    sessionid = request.cookies.get("sessionid")

    if not sessionid:
        raise HTTPException(status_code=401, detail="No session cookie found")

    # Replace this with your real Django server address
    DJANGO_URL = "http://127.0.0.1:8000/api/validate_session/"

    try:
        resp = requests.get(DJANGO_URL, cookies={"sessionid": sessionid})
        if resp.status_code == 200:
            return True
        else:
            raise HTTPException(status_code=401, detail="Invalid session")
    except Exception as e:
        raise HTTPException(status_code=500, detail="Could not reach Django")