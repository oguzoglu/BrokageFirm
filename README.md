# BrokageFirm Application Help

This application uses Docker to run. Here's how to get started and how to use the provided `.http` file:

## Quick Start

1.  **Make sure you have Docker installed.**
2.  **Open your terminal in this folder.**
3.  **Run the application:**
    ```bash
    docker-compose up -d
    ```
    This will start the database and the application.

## Using the `client.http` File

The provided `.http` file contains several API requests to interact with the BrokageFirm application. To use this file, you'll need an HTTP client that supports the `.http` format. Popular options include:

* **Visual Studio Code (VS Code) with the REST Client extension by Huachao Mao.** This is a highly recommended and convenient option.
* **IntelliJ IDEA with the HTTP Client plugin.**
* 
## Understanding the API Requests

Here's a breakdown of the requests in the `.http` file:

* **`### Login User` and `### Login admin`**:
    * These are `POST` requests to the `/api/auth/login` endpoint.
    * They send JSON data containing `username` and `password` to authenticate a user or an administrator.
    * The `>` lines with `{% client.global.set("token", response.body.token); %}` are instructions for the REST Client to extract the `token` from the login response and store it as a global variable named `token`. This allows you to use the token in subsequent requests for authorization.

* **`### get assets`**:
    * This is a `GET` request to `/api/assets` with a query parameter `customerId=1`.
    * It retrieves the assets associated with customer ID 1.
    * It includes an `Authorization` header with the value `Bearer {{token}}`. The `{{token}}` will be automatically replaced by the token you obtained from a successful login.

* **`### create order`**:
    * This is a `POST` request to `/api/orders/create`.
    * It sends JSON data to create a new order with details like `customerId`, `assetName`, `size`, `orderSide` (SELL), and `price`.
    * It also includes the `Authorization` header with the `Bearer` token.

* **`### list orders by customrt id Omer user`**:
    * This is a `GET` request to `/api/orders` with a query parameter `customerId=1`.
    * It retrieves all orders for customer ID 1.
    * It includes the `Authorization` header.

* **`### list orders`**:
    * This is a `GET` request to `/api/orders/listAll`.
    * It retrieves a list of all orders in the system (likely requires administrator privileges based on the endpoint name).
    * It includes the `Authorization` header.

* **`### cancel order`**:
    * This is a `GET` request to `/api/orders/cancel/1/1`.
    * It attempts to cancel an order with ID `1` for customer ID `1`.
    * It includes the `Authorization` header.

* **`### match order`**:
    * This is a `GET` request to `/api/orders/match/9`.
    * It likely attempts to match an order with ID `9` against existing orders in the system.
    * It includes the `Authorization` header.

## Basic Commands

* **Stop the application:**
    ```bash
    docker-compose down
    ```
* **Start the application again:**
    ```bash
    docker-compose up -d
    ```
