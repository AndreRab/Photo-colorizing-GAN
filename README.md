# Photo-colorizing-GAN
![Java Spring](https://img.shields.io/badge/Java-Spring-brightgreen)
![Python Flask](https://img.shields.io/badge/Python-Flask-blue)
![PyTorch](https://img.shields.io/badge/PyTorch-2.1.1-red)
![Torchvision](https://img.shields.io/badge/Torchvision-0.16.1-orange)
![Issues](https://img.shields.io/github/issues/AndreRab/Photo-colorizing-GAN)

## **Photo-colorizing GAN** is a deep learning model designed to add color to black-and-white photos, providing a vivid and realistic resul

### Description
This is my first project involving machine learning, where I decided to train a GAN for photo colorization. The **model architecture** and the **datasets** used for training can be found in the **Model Specification** section. To enhance the user experience, I’ve integrated some front-end features as well.

The application is divided into two main components:
1. A **Java Spring** server, which handles the core of the web page.
2. A **Python Flask** application, responsible for processing images through the GAN.

Feel free to try it out and add vibrant colors to your old black-and-white photos!

## Table of Contents

| Section                      | Description                                                         |
|-------------------------------|---------------------------------------------------------------------|
| [Installation using Gradle](#installation-using-gradle) | Instructions for installing the project using Gradle               |
| [Installation using Docker](#installation-using-docker) | Instructions for installing the project using Docker               |
| [Training Datasets](#training-datasets)   | Information about the datasets used for training the GAN model      |
| [Model Specification](#model-specification)   | A detailed breakdown of the model's architecture and training process |
| [Examples of Colorizing](#examples-of-colorizing)  | Sample images demonstrating the results of colorizing black-and-white photos |


## Installation using Gradle
**First of all** install docker (https://www.docker.com/) and gradle (https://gradle.org/) on your machine. Then run your docker-engine.
1. **Clone the Repository**:
   Open a terminal and clone the repository:
   ```bash
   git clone https://github.com/AndreRab/Photo-colorizing-GAN.git
   ```
   **Don't forget to clone the model with the help of [Git LFS](https://git-lfs.com/)**
   
3. **Navigate to the Project Directory**:
   Change your directory to the project folder:
    ```bash
    cd Photo-colorizing-GAN
    ```
4. **Build the Project**:
   Use Docker-compose to build the project:
   ```bash
   docker-compose up --build -d
   ```
5. **Access the Application**:
   Open your web browser and navigate to http://localhost:8082 to access the application.

## Installation using Docker
**First of all** install docker (https://www.docker.com/) and gradle (https://gradle.org/) on your machine. Then run your docker-engine.
1. **Clone the Repository**:
   Open a terminal and clone the repository:
   ```bash
   git clone https://github.com/AndreRab/Photo-colorizing-GAN.git
   ```
2. **Navigate to the Project Directory**:
   Change your directory to the project folder:
    ```bash
    cd Photo-colorizing-GAN
    ```
3. **Build the Project**:
   Use Gradle to build the project:
   ```bash
   ./gradlew deploy
   ```
   **Don't forget to clone the model with the help of [Git LFS](https://git-lfs.com/)**
   
4. **Access the Application**:
   Open your web browser and navigate to http://localhost:8082 to access the application.

## Training Datasets
For training datasets, I used the following sources: [Image Colorization Dataset](https://www.kaggle.com/datasets/aayush9753/image-colorization-dataset) and [ImageNet Mini](https://www.kaggle.com/datasets/ifigotin/imagenetmini-1000). I converted all images to grayscale mode and used them as input for my model, while the original images served as the expected output.

## Model Specification
Like all GANs, my GAN consists of a generator and a discriminator. You can see the architecture of my generator in the picture. Basically, I used a CNN with skip connections because I think that the best idea for colorizing a picture is to reference the original image and its features.
![photo_2024-09-25_14-57-39](https://github.com/user-attachments/assets/5cee7a37-3740-4a95-a337-a5682d03eaa5)


At the beginning of my training, the discriminator was really good while the generator wasn’t. That's why I trained the discriminator only for every 10 batches. When the results of my CNN started to be as good as the original pictures, my discriminator couldn't recognize which images were real and which were colorized, so I decided to focus on training only my generator.

This GAN resizes every image to 128x128 pixels, which is why it can colorize pretty good pictures at that size. However, if you provide a larger picture, the results may not be very good.

## Examples of Colorizing
Here are examples of colorizing for the pictures that my model did not see during training.
![image](https://github.com/user-attachments/assets/dc133bbb-193a-4b8f-a6d9-52b338f0adff)
![image](https://github.com/user-attachments/assets/3acb7513-7d0a-42ca-afc0-02346311d755)
![image](https://github.com/user-attachments/assets/59859f1e-4712-4f91-abde-d1f19566acb2)
![image](https://github.com/user-attachments/assets/e93350c4-025f-46f5-be61-2bc5838232bf)
![image](https://github.com/user-attachments/assets/eacc278e-f792-4e23-b4b2-077506e669cf)
![image](https://github.com/user-attachments/assets/faf67fea-c526-4d10-b4f0-6ceaa8475a7d)
![image](https://github.com/user-attachments/assets/5d1541f3-fa81-44ff-a301-84125656405a)
![image](https://github.com/user-attachments/assets/2c59b6f7-a080-4879-8428-0037f9c6db32)
![image](https://github.com/user-attachments/assets/05b588a3-962d-44af-8d04-fbc137c14d35)










