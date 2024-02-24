Spring Boot Interactions Service

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

cd your-repository

Build the application using Maven:

bash

mvn clean package

Run the application:

bash

    java -jar target/your-application.jar

Endpoints

    GET /status: Get the status of the service.
    GET /getURL: Get the URL obtained by the service.
    GET /getInteractions: Get interaction information based on provided parameters (server URL, start time, end time).

API Parameters
GET /getInteractions

This endpoint retrieves information about interactions with a testing server.
Parameters:

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

Let me know if you need further modifications or assistance!