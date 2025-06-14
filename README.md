**URL Shortener Service**

This is a simple, yet robust, URL shortening service built with Spring Boot, leveraging PostgreSQL for data persistence and Docker for containerization. It allows you to transform long, unwieldy URLs into concise, shareable short codes.
## <a name="_oav806jfk69d"></a>**Features**
- **URL Shortening:** Converts a long URL into a unique, short code.
- **Redirection:** Redirects users from the short URL to the original long URL.
- **Click Tracking:** Increments a click counter each time a short URL is accessed.
- **API-First Design:** Provides a clean RESTful API for integration.
- **Dockerized:** Easily deployable using Docker Compose for local development and production environments
## <a name="_sprhfsy9d5q7"></a>**Technologies Used**
- **Backend:** Java 17+ (or preferred Java version)
- **Framework:** Spring Boot 3.x
- **Database:** PostgreSQL
- **ORM/Persistence:** Spring Data JPA, Hibernate
- **Build Tool:** Maven
- **Containerization:** Docker, Docker Compose
## <a name="_v00klr1pjmcw"></a>**Setup and Installation**
Follow these steps to get the URL Shortener service up and running on your local machine.
### <a name="_j7cmtgutopbd"></a>**Prerequisites**
- **Java Development Kit (JDK):** Version 17 or higher.
- **Apache Maven:** Version 3.6 or higher.
- **Docker & Docker Compose:** Ensure Docker Desktop (or Docker Engine with Compose) is installed and running.
### <a name="_z0k655isgb6n"></a>**1. Clone the Repository**
First, clone the project repository to your local machine

### <a name="_9gdxwklquv3r"></a>**2. Build the Spring Boot Application**
Navigate to the project root directory (where pom.xml is located) and build the Spring Boot application:

    mvn clean install

This command compiles the code, runs tests, and packages the application into a JAR file.
### <a name="_nt742fdo0v6q"></a>**3. Run with Docker Compose**
Ensure Docker is running. From the project root directory (where docker-compose.yml is located), execute:

    docker-compose up -d --build

`- docker-compose up`: Starts the services defined in `docker-compose.yml`.
- `-d`: Runs the containers in detached mode (in the background).
- `--build`: Forces a rebuild of the url-service image, ensuring the latest changes are applied.

This will:

- Build the url-service Docker image.
- Start a PostgreSQL database container (postgres\_db).
- Start a pgAdmin container (pgadmin) for database management (optional, but convenient).
- Start your Spring Boot application container (url\_shortener\_spring\_app).
- Connect all services to a shared Docker network.

Give the database a few moments to initialize before the Spring Boot application fully starts.
## <a name="_rc02i9x638ph"></a>**API Endpoints**
The service exposes the following RESTful API endpoints:
### <a name="_klc8kj43eaa"></a>**1. Shorten a URL**
 - **Endpoint:** POST /shorten
 - **Description:** Creates a new short URL for a given long URL.
 - **Request Body (JSON):**

    {
    `  `"url": "https://www.example.com/very/long/url/that/needs/shortening"
    }

 - **Response (JSON - 200 OK):**

    {
    `  `"shortUrl": "http://localhost:8080/yourcode"
    }

**Error Responses (JSON):**

- 400 Bad Request: If url is missing or invalid.

Example using curl:

    curl -X POST \
    `  `http://localhost:8080/shorten \
    `  `-H 'Content-Type: application/json' \
    `  `-d '{"url": "https://www.google.com/search?q=url+shortener+project+example&oq=url+shortener+project+example"}'

### <a name="_9fluiabg9qfs"></a>**Redirect from Short URL**
- **Endpoint:** GET /{shortCode}
- **Description:** Redirects the client to the original long URL associated with the shortCode. Also increments the click count.
- **Response:**
  - 302 Found: Redirects to the original URL.
  - 404 Not Found: If the shortCode does not exist.

**Example (open in browser):**

Open your web browser and navigate to:

    http://localhost:8080/yourcode

(Replace yourcode with an actual short code generated from /shorten.)
### <a name="_s2asycvg35lj"></a>**3. Get URL Details (Optional)**
- **Endpoint:** GET /details/{shortCode}
- **Description:** Retrieves details about a shortened URL, including the original URL and click count, without redirecting.
- **Response (JSON - 200 OK):**

    {
    `  `"shortCode": "yourcode",
    `  `"longUrl": "https://www.example.com/original",
    `  `"createdAt": "2025-06-14T12:34:56.789",
    `  `"clickCount": 15
    }

- **Error Response:**
  - 404 Not Found: If the shortCode does not exist.

**Example using curl:**

    curl <http://localhost:8080/details/yourcode>

## <a name="_yfz4qrmrola8"></a>**Configuration**
The application can be configured via src/main/resources/application.properties (or application.yml).

- **shortener.base.url**: The base URL for your shortened links (e.g., http://localhost:8080/, https://yourdomain.com/). This is used when constructing the full short URL in the API response.
  - **Default:** http://localhost:8080/ (set via @Value annotation).
  
 Example in application.properties:

    shortener.base.url=<http://localhost:8080/>

Example in application.yml:

    shortener:
    `  `base:
    `    `url: http://localhost:8080/

Database connection details are defined within docker-compose.yml (POSTGRES\_DB, POSTGRES\_USER, POSTGRES\_PASSWORD) and mirrored in application.properties (spring.datasource.url, spring.datasource.username, spring.datasource.password). Ensure these match.
## <a name="_neigcdykr2qi"></a>**Future Improvements (Potential Enhancements)**
- **URL Expiration:** Implement a feature for short URLs to expire after a certain time or number of clicks.
- **Caching Layer:** Integrate Redis or another caching solution to improve performance for high-traffic redirects.
- **Rate Limiting:** Implement rate limiting to prevent abuse of the shortening service.

