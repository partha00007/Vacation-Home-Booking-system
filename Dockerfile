FROM python:3.11-slim

# Set the working directory to /app
WORKDIR /app

# Copy the requirements.txt file first (for better caching)
COPY requirements.txt /app/
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of the application files
COPY . /app/

# Expose the Django port
EXPOSE 8000

# Run Django from the current working directory
CMD ["python", "manage.py", "runserver", "0.0.0.0:8000"]
