from flask import Flask, request, Response
import requests
from io import BytesIO
import torch
from PIL import Image
from models import Discriminator, UNetGenerator, load_model
import torchvision.transforms as transforms

app = Flask(__name__)

image_size = 256

@app.route('/process', methods=['POST'])
def get_image():
    print("Received something")

    if 'file' not in request.files:
        return "No file part", 400

    file = request.files['file']
    user_id = request.form['userId']
    if file.filename == '':
        return "No selected file", 400

    print("Received image successfully")

    # Process the image with AI function
    processed_image = image_throw_ai(file, model)

    if processed_image is None:
        return "AI processing failed", 500

    print("AI processing success")
    print("Sent image to the java-server")

    return send_image(processed_image, user_id)

def send_image(image_file, user_id):
    image_stream = BytesIO(image_file.read())
    files = {
        'file': ('image.png', image_stream, 'image/png'),
        'userId': (None, user_id)
    }

    java_endpoint = "http://java-app:8082/colorized"
    response = requests.post(java_endpoint, files=files)

    print(f'Status Code: {response.status_code}')

    # Return the response from Java service
    return Response(
        response.content,  # Use the actual content from Java service
        status=response.status_code,
        mimetype=response.headers.get('Content-Type', 'application/octet-stream')
    )

def image_throw_ai(image, model):
    try:
        img = Image.open(image)
        original_width, original_height = img.size
        img = img.convert("L")

        preprocess = transforms.Compose([
            transforms.Resize((image_size, image_size)),
            transforms.ToTensor()
        ])

        # Preprocess the image
        img_tensor = preprocess(img)

        if img_tensor.shape[0] == 1:
            img_tensor = img_tensor.repeat(3, 1, 1)

        img_tensor = img_tensor[0, :, :].unsqueeze(0).unsqueeze(0)

        with torch.no_grad():
            img_colorised = model['generator'](img_tensor)


        postprocess = transforms.Compose([
            transforms.Resize((original_height, original_width)),
            transforms.ToPILImage()
        ])

        output_image = postprocess(img_colorised.squeeze(0))

        output_image_io = BytesIO()
        output_image.save(output_image_io, format='PNG')
        output_image_io.seek(0)

        return output_image_io

    except Exception as e:
        print(f"Error during AI processing: {e}")
        return None


if __name__ == '__main__':
    global model
    model = load_model(image_size)
    app.run(host='0.0.0.0', port=5000)
