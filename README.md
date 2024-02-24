Spring Boot Interactions Service
Overview

This Spring Boot application serves as an interactions service, providing endpoints to retrieve status, URLs, and interaction information.
Features

    Status Endpoint: Get the status of the service.
    URL Endpoint: Get the URL obtained by the service.
    Interactions Endpoint: Get interaction information based on provided parameters such as server URL, start time, and end time.

Prerequisites

Before running the application, ensure you have the following installed:

    Java Development Kit (JDK) 11 or higher
    Apache Maven

Running the Application

Clone the repository:

    bash

    git clone https://github.com/Anirudhxx/interactsh.git

Navigate to the project directory:

    bash

    cd interactsh

Build the application using Maven:

    bash

    mvn clean package

Run the application:

    bash

    java -jar target/interactsh.jar

Endpoints

    GET /status: Get the status of the service.
    GET /getURL: Get the URL obtained by the service.
    GET /getInteractions: Get interaction information based on provided parameters (server URL, start time, end time).

API Parameters for GET /getInteractions

    serverUrl (required): The URL of the testing server.
    startTime (optional): Start timestamp to filter interactions. Format: YYYY-MM-DD HH:MM:SS.
    endTime (optional): End timestamp to filter interactions. Format: YYYY-MM-DD HH:MM:SS.

Example Usage:

    http

    GET /getInteractions?serverUrl=https://example.com&startTime=2024-02-24%2003:31:36&endTime=2024-02-25%2003:31:36

Contributing

Contributions are welcome! Please feel free to submit pull requests for any improvements or additional features.
License

This project is licensed under the MIT License. See the LICENSE file for details.
Contact

For any inquiries or feedback, please contact anirudhchauhan74000@gmail.com.
Docker Usage
Pull the Docker image from Docker Hub:
        
    bash

    docker pull anirudhchauhan10/interactsh

Run the Docker container:

    bash

    docker run -d -p 8080:8080 anirudhchauhan10/interactsh:v5

To Do

    Implement functionality to take the number of test servers as a parameter and run the interactsh-client command accordingly.
This format provides a clear and structured view of the Spring Boot Interactions Service, its features, how to run it, and additional details like endpoints, contributing guidelines, and Docker usage. Let me know if you need further assistance!