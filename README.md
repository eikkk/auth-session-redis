# Session Authentication Service

## Overview

Simple Spring Boot application that leverages Redis for session management and authentication.
- The application is designed to allow requests only from `localhost` environment. This ensures that no external traffic can access the service directly. Modify the SecurityConfig if you want to allow requests from other sources.

## API Endpoints

Provides endpoints for managing user sessions and authentication.

#### 1. Validate Session

- **URL:** `/api/session/validate`
- **Method:** `POST`
- **Description:** Validates the session cookie. If the request is authenticated, it returns session data.
- **Usage:** This endpoint should be called by your app with every request that has to be authenticated.
- **Returns:** The session data if the session is valid.

#### 2. Initiate Session

- **URL:** `/api/session/initiate`
- **Method:** `POST`
- **Description:** Initiates a session for the user with provided data and authorities.
- **Request Body:** Pass any object that you want to be associated with the session. You can retrieve it from the response of the `/api/session/validate` endpoint.
- **Headers:**
    - **`user`:** The username of the user.
    - **`authorities`:** A comma-separated list of user authorities/roles.
- **Usage:** This endpoint should be called by your app after credentials validation to log in the user and start the session.

## How to use:

1) Build the .jar file using command `mvn clean install` or your IDE. You should have a .jar file in the target directory.

2) Navigate to the root directory of the project and run:
### Option 1: Docker

<code>docker-compose up --build</code>


### Option 2: Podman

<code>podman-compose up --build</code>

### Option 3: Create podman pod

a) Build the image from the Dockerfile <br />
<code>podman build -t auth-java-service .</code> <br /> <br />
b) Create a pod (customize the exposed port if needed) <br /> 
<code>podman pod create --name auth-service-pod -p 5060:5060</code> <br /> <br />
c) Add the redis container to the pod <br />
<code>podman run -d --name redis-in-pod --pod auth-service-pod docker.io/library/redis:alpine redis-server --port 5050</code> <br /> <br />
d) Add the java app container to the pod <br />
<code>podman run -d --name auth-java-in-pod --pod auth-service-pod localhost/auth-java-service</code> <br /> <br />



