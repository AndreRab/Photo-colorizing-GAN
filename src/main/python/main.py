from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/process', methods=['GET'])
def process_image():
    data = request.json
    # Do GAN processing here
    return jsonify({'status': 'success'})

if __name__ == '__main__':
    print("Starting Flask app...", flush=True)
    app.run(host='0.0.0.0', port=5000)
