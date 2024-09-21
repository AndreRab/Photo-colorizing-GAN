from flask import Flask, request, jsonify, Response
import requests
from io import BytesIO

app = Flask(__name__)

@app.route('/process', methods=['POST'])
def get_image():
    print("Received something")

    if 'file' not in request.files:
        return "No file part", 400

    file = request.files['file']
    user_id = request.form['userId']
    if file.filename == '':
        return "No selected file", 400

    # Process the image with AI function
    processed_image = image_throw_ai(file)

    # Send the processed image to Java service
    return send_image(processed_image, user_id)

def send_image(image_file, user_id):
    # Convert image to byte stream
    image_stream = BytesIO(image_file.read())
    files = {
        'file': ('image.png', image_stream, 'image/png'),
        'userId': (None, user_id)  # Send userId as a form field, not a file
    }

    # Java endpoint
    java_endpoint = "http://java-app:8082/colorized"
    response = requests.post(java_endpoint, files=files)

    print(f'Status Code: {response.status_code}')

    # Return the response from Java service
    return Response(
        "Message",  # The content of the response
        status=response.status_code,  # The status code from the Java service
        mimetype=response.headers.get('Content-Type', 'application/octet-stream')  # The content type from the Java service
    )

def image_throw_ai(image):
    # Placeholder function - process the image here
    return image

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
