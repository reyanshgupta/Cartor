import numpy
import cv2
import os
import argparse

def init_centroids(num_clusters,image):
    image = image/255 #normalize pixel values to 0/1 from 255
    H,W,C = image.shape
    numbers = np.random.randint(H*W,size=num_clusters) #random centroids
    centroids_init = image.reshape(-1,C)[numbers] #2D array with C color channels
    return centroids_init

def update_centroids(centroids, image, max_iter=30,print_every=10):
    image=image/255
    num_of_clusters=len(centroids) #each centroid is one cluster
    H,W,C = image.shape
    image = image.reshape(-1,C)
    dist = np.empty([num_of_clusters, H*W]) #to store distance between each pixel and cluster center/centroid

    for i in range(max_iter):
        for j in range(num_of_clusters):
            dist[j]=np.sum((image-centroids[j])**2, axis=1) #calculating euclidean distance
        clustering = np.argmin(dist,axis=0).reshape(-1,1)
        new_centroids = np.empty([num_of_clusters, C])

        for j in range(num_of_clusters):
            cluster_j = (clustering == j)
            new_centroids[j] = np.sum(cluster_j * image, axis=0) / np.sum(cluster_j)

        if (i + 1) % print_every == 0: #calculate loss every x no of steps later
            loss = (image - new_centroids[clustering.squeeze()]) ** 2 #MSE
            loss = np.sum(loss)
            print(f'loss: {loss:.2f}')

        if np.array_equal(centroids, new_centroids):
            break
        centroids = new_centroids

    return new_centroids

def update_image(image,centroids):
    image=image/255
    num_of_clusters=len(centroids)
    H,W, C = image.shape
    image = image.reshape(-1, C)
    distance = np.empty([num_of_clusters, H * W])
    for j in range(num_of_clusters):
        distance[j] = np.sum((image-centroids[j])**2,axis=1)
    clustering = np.argmin(distance,axis=0) #assigning pixel to closest cluster center
    new_image=centroids[clustering].reshape(H,W,C) #image segmentation done using KMeans
    return new_image
    return image

def main(image_path):  # Pass the image path as an argument to the main function
    original_image = cv2.imread(image_path)
    original_image = cv2.resize(original_image, (0, 0), fx=0.1, fy=0.1)
    original_image = cv2.cvtColor(original_image, cv2.COLOR_BGR2RGB)

    num_clusters = 30  # You can specify the number of clusters here
    centroids = init_centroids(num_clusters, original_image)
    updated_centroids = update_centroids(centroids, original_image, max_iter=30, print_every=10)
    compressed_image = update_image(original_image, updated_centroids)

    # Calculate the sizes
    original_image_size = os.path.getsize(image_path)
    original_image_size_kb = original_image_size / 1024

    # Here, we're converting the compressed image array to bytes to simulate the size of the compressed image
    compressed_image_bytes = cv2.imencode('.jpg', compressed_image)[1]
    compressed_image_size_kb = len(compressed_image_bytes) / 1024

    return compressed_image, compressed_image_size_kb

if __name__ == '__main__':
    # Create an argument parser
    parser = argparse.ArgumentParser()
    parser.add_argument("image_path", type=str, help="Path to the input image")

    # Parse the command-line arguments
    args = parser.parse_args()

    # Call the main function with the provided image path
    main(args.image_path)