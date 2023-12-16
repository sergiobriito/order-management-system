# Order Management System

This project is an Order Management System built using Java Spring, HTML, CSS, and JavaScript. 
It allows users to create orders, save them to a PostgreSQL, complete orders, and send messages via RabbitMQ. 
It also includes a listener on a queue and uses WebSocket to update the front end with completed orders from the queue.

## Technologies Used

- **Java Spring**: Backend framework for handling HTTP requests and managing the application.
- **HTML/CSS/JavaScript**: Frontend technologies for creating the user interface and handling client-side interactions.
- **PostgreSQL**: Database used to store order information.
- **RabbitMQ**: Message broker for sending and receiving messages related to order completion.
- **WebSocket**: Real-time communication protocol used to update the frontend with completed orders.

## Features

- **Order Creation**: Users can create orders through the user interface.
- **Database Integration**: Orders are saved and retrieved from the PostgreSQL database.
- **Order Completion**: Completed orders trigger messages sent via RabbitMQ.
- **Queue Listener**: Backend includes a listener on the queue for incoming messages.
- **WebSocket Communication**: Real-time updates to the front end with completed orders using WebSocket.

## Setup

```bash
git clone https://github.com/sergiobriito/ordering-system-with-messaging.git
mvn clean install -DskipTests
docker-compose build
docker-compose up

