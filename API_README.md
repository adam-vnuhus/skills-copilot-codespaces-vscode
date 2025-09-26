# Signature Routing Service

A Spring Boot 3 application that provides routing functionality for digital signature services, routing requests to different CA providers based on channel configuration.

## Features

- **Java 17** with Spring Boot 3
- **Route Management**: Database-driven routing configuration
- **Multi-Channel Support**: Routes to different APIs based on channel (VIETTEL, VNPT → old API, SAVIS → new API)  
- **Request Transformation**: Automatic conversion between API formats
- **H2 Database**: In-memory database for routing configuration
- **REST API**: RESTful endpoints for signature operations
- **Logging**: Comprehensive logging for debugging and monitoring

## Architecture

### Main Components

1. **SignatureRoutingController**: REST endpoint handler
2. **SignatureRoutingService**: Core business logic for routing
3. **HttpClientService**: HTTP client for external API calls
4. **Routing Entity**: Database model for routing configuration
5. **DTOs**: Request/Response data transfer objects

### API Endpoints

#### Main Endpoint
- `POST /external/v1/routing/employee/signature/sign_hashs`
  - Routes signature hash requests based on channel configuration
  - Supports both old and new API formats

#### Health Check
- `GET /external/v1/routing/employee/signature/health`
  - Simple health check endpoint

### Request/Response Format

#### Request Format
```json
{
  "meta_data": {
    "request_id": "string",
    "app_code": "string", 
    "channel": "VIETTEL | VNPT | SAVIS",
    "sign_pack": "string"
  },
  "data": {
    "user_id": "string",
    "access_token": true/false,
    "credential_id": "string",
    "hashs": [{
      "hash_value": "string",
      "hash_id": "string", 
      "hash_name": "string"
    }],
    "webhook": "string"
  }
}
```

#### Response Format
```json
{
  "errorCode": "0",
  "errorDesc": "Thành công",
  "data": {
    "traceId": "string",
    "transactionId": "string", 
    "channel": "string",
    "clientId": "string",
    "waiting_time_confirm": "string"
  }
}
```

## Configuration

### Database

The application uses H2 in-memory database with pre-configured routing rules:

- **VIETTEL**: Routes to old API format
- **VNPT**: Routes to old API format  
- **SAVIS**: Routes to new API format with message transformation

### External Service URLs

Configure external service URLs in `application.properties`:

```properties
external-services.old-api.base-url=http://localhost:8081
external-services.new-api.base-url=http://localhost:8082
```

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build and Run
```bash
# Build the application
mvn clean compile

# Run tests
mvn test

# Run the application
mvn spring-boot:run
```

The application will start on port 8080 by default.

### H2 Database Console

When running locally, you can access the H2 database console at:
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:routing_db`
- Username: `sa`
- Password: (empty)

## API Usage Examples

### Valid Request Example
```bash
curl -X POST http://localhost:8080/external/v1/routing/employee/signature/sign_hashs \
  -H "Content-Type: application/json" \
  -d '{
    "meta_data": {
      "request_id": "req-123",
      "app_code": "MY_APP", 
      "channel": "SAVIS",
      "sign_pack": "test"
    },
    "data": {
      "user_id": "user123",
      "access_token": true,
      "credential_id": "cred-456",
      "hashs": [{
        "hash_value": "abcd1234",
        "hash_id": "hash-1",
        "hash_name": "document.pdf"
      }],
      "webhook": "http://callback.example.com/webhook"
    }
  }'
```

## Development

### Project Structure
```
src/
├── main/java/com/vnuhus/signaturerouting/
│   ├── SignatureRoutingServiceApplication.java
│   ├── controller/
│   │   └── SignatureRoutingController.java
│   ├── service/
│   │   ├── SignatureRoutingService.java
│   │   └── HttpClientService.java
│   ├── model/
│   │   └── Routing.java
│   ├── repository/
│   │   └── RoutingRepository.java
│   ├── dto/
│   │   ├── SignatureHashRequest.java
│   │   ├── SignatureHashResponse.java
│   │   ├── NewApiRequest.java
│   │   └── NewApiResponse.java
│   └── config/
│       ├── DataInitializer.java
│       └── WebClientConfig.java
└── test/java/
    └── com/vnuhus/signaturerouting/
        ├── SignatureRoutingServiceApplicationTests.java
        └── controller/
            └── SignatureRoutingControllerTest.java
```

### Adding New Channels

To add support for new CA providers:

1. Add routing configuration to `DataInitializer.java`
2. If using a different API format, create new DTOs
3. Update `SignatureRoutingService` to handle the new format
4. Add tests for the new functionality

## Error Handling

The application provides comprehensive error handling:

- **Validation Errors**: Returns 400 with validation details
- **Routing Not Found**: Returns 400 with descriptive error message  
- **External API Errors**: Returns the original HTTP status and response
- **System Errors**: Returns 500 with error details

## Logging

Logs are configured to show:
- Request/response details at DEBUG level
- Business logic flow at INFO level
- Errors at ERROR level

Configure logging levels in `application.properties`:
```properties
logging.level.com.vnuhus.signaturerouting=DEBUG
```