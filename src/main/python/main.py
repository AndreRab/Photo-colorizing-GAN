from flask import Flask, request, Response
import requests
from io import BytesIO
import torch
from PIL import Image
from models import model
import torchvision.transforms as transforms

app = Flask(__name__)

model = None
image_size = 256

def load_model():
    global model
    if model is None:
        try:
            model = torch.load('full_model.pth')
            model.eval()  # Set the model to evaluation mode
        except Exception as e:
            print(f"Failed to load model: {e}")

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

    if processed_image is None:
        return "AI processing failed", 500

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
        response.content,  # Use the actual content from Java service
        status=response.status_code,
        mimetype=response.headers.get('Content-Type', 'application/octet-stream')
    )

def image_throw_ai(image):
    try:
        img = Image.open(image)
        img = img.convert("L")  # Ensure the image is grayscale

        # Define transformations to preprocess the image for your model
        preprocess = transforms.Compose([
            transforms.Resize((image_size, image_size)),  # Resize to match the input size of your NN
            transforms.ToTensor(),  # Convert the image to a tensor
            transforms.Normalize(mean=[0.5], std=[0.5])  # Normalize the grayscale image
        ])

        # Preprocess the image
        img_tensor = preprocess(img).unsqueeze(0)  # Add batch dimension

        # Pass the image through the neural network
        with torch.no_grad():
            output_tensor = model['generator'](img_tensor)  # Assuming model outputs a colorized image

        # Rescale the output tensor to [0, 1]
        output_tensor = (output_tensor + 1) / 2

        # Post-process the output tensor to convert it back to an image
        postprocess = transforms.ToPILImage()

        output_image = postprocess(output_tensor.squeeze(0))

        # Return the processed image as a file-like object
        output_image_io = BytesIO()
        output_image.save(output_image_io, format='PNG')
        output_image_io.seek(0)  # Move the cursor back to the start of the stream

        return output_image_io

    except Exception as e:
        print(f"Error during AI processing: {e}")
        return None

if __name__ == '__main__':
    load_model()
    app.run(host='0.0.0.0', port=5000)
