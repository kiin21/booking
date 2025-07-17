# Booking Application

A Spring Boot-based homestay booking system that enables users to search for accommodations, make bookings, and process payments through VNPay integration.

## Project Overview

This is a RESTful API application built with Spring Boot 3.5.3 and Java 21, designed to manage homestay bookings with location-based search capabilities, payment processing, and user profile management.

### Key Features

- **Location-based homestay search** with radius filtering
- **Booking management** with status tracking
- **VNPay payment integration** for secure transactions
- **User profile management**
- **PostGIS integration** for geospatial queries
- **PostgreSQL database** with comprehensive schema

### Technology Stack

- **Framework**: Spring Boot 3.5.3
- **Language**: Java 21
- **Database**: PostgreSQL with PostGIS extension
- **ORM**: Spring Data JPA with Hibernate
- **Mapping**: MapStruct
- **Validation**: Bean Validation (Hibernate Validator)
- **Utilities**: Lombok, Hypersistence Utils
- **Payment**: VNPay integration

## API Endpoints

### 1. Homestay Management

#### Get Homestay by ID

- **Method**: `GET`
- **Endpoint**: `/api/v1/homestays/{id}`
- **Description**: Retrieve detailed information about a specific homestay
- **Path Parameters**:
  - `id` (String): Homestay ID
- **Response**:

```json
{
  "meta": {
    "status": "success",
    "message": "Operation completed successfully"
  },
  "payload": {
    "id": 1,
    "name": "Cozy Beach House",
    "description": "Beautiful beachfront property",
    "images": [
      "image1.jpg",
      "image2.jpg"
    ],
    "type": 1,
    "status": 1,
    "phone_number": "+84123456789",
    "guests": 4,
    "bedrooms": 2,
    "address": "123 Beach Street, Da Nang",
    "longitude": 108.2022,
    "latitude": 16.0471,
    "total_amount": 1500000.0,
    "version": 1
  }
}
```

#### Search Homestays

- **Method**: `GET`
- **Endpoint**: `/api/v1/homestays`
- **Description**: Search for available homestays based on location and date criteria
- **Query Parameters**:
  - `longitude` (Double, required): Longitude coordinate
  - `latitude` (Double, required): Latitude coordinate
  - `radius` (Double, required): Search radius in kilometers
  - `checkin_date` (String, required): Check-in date (YYYY-MM-DD format)
  - `checkout_date` (String, required): Check-out date (YYYY-MM-DD format)
  - `guests` (Integer, required): Number of guests
- **Response**:

```json
{
  "meta": {
    "status": "success",
    "message": "Search completed successfully"
  },
  "payload": [
    {
      "id": 1,
      "name": "Cozy Beach House",
      "description": "Beautiful beachfront property",
      "images": [
        "image1.jpg",
        "image2.jpg"
      ],
      "type": 1,
      "status": 1,
      "phone_number": "+84123456789",
      "guests": 4,
      "bedrooms": 2,
      "address": "123 Beach Street, Da Nang",
      "longitude": 108.2022,
      "latitude": 16.0471,
      "total_amount": 1500000.0,
      "version": 1
    }
  ]
}
```

### 2. Booking Management

#### Create Booking

- **Method**: `POST`
- **Endpoint**: `/api/v1/bookings`
- **Description**: Create a new homestay booking with payment initialization
- **Request Body**:

```json
{
  "request_id": "REQ-2024-001",
  "user_id": "123",
  "homestay_id": "456",
  "checkin_date": "2024-08-01",
  "checkout_date": "2024-08-05",
  "guests": 2,
  "note": "Early check-in requested"
}
```

- **Response**:

```json
{
  "booking": {
    "booking_id": "BK-2024-001",
    "user_id": "123",
    "homestay_id": "456",
    "checkin_date": "2024-08-01",
    "checkout_date": "2024-08-05",
    "guests": 2,
    "status": 1,
    "subtotal": 6000000.0,
    "discount": 0.0,
    "total_amount": 6000000.0,
    "currency": "VND",
    "request_id": "REQ-2024-001",
    "note": "Early check-in requested"
  },
  "payment": {
    "payment_url": "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?...",
    "transaction_id": "TXN-2024-001",
    "amount": 6000000.0,
    "currency": "VND"
  }
}
```

#### Get Booking Status

- **Method**: `GET`
- **Endpoint**: `/api/v1/bookings/{bookingId}/status`
- **Description**: Retrieve the current status of a booking
- **Path Parameters**:
  - `bookingId` (Long): Booking ID
- **Response**:

```json
{
  "meta": {
    "status": "success",
    "message": "Booking status retrieved successfully"
  },
  "payload": {
    "booking_id": "BK-2024-001",
    "user_id": "123",
    "homestay_id": "456",
    "status": "BOOKED"
  }
}
```

### 3. Payment Processing

#### VNPay IPN Handler

- **Method**: `GET`
- **Endpoint**: `/api/v1/payments/vnpay_ipn`
- **Description**: Handle VNPay Instant Payment Notification (IPN) callbacks
- **Query Parameters**: All VNPay IPN parameters as key-value pairs
- **Response**:

```json
{
  "RspCode": "00",
  "Message": "Confirm Success"
}
```

### 4. User Profile Management

#### Get User Profile

- **Method**: `GET`
- **Endpoint**: `/api/v1/profiles/{id}`
- **Description**: Retrieve user profile information
- **Path Parameters**:
  - `id` (Long): User ID
- **Response**:

```json
{
  "user_id": 123,
  "avatar": "https://example.com/avatar.jpg",
  "work": "Software Engineer",
  "about": "Travel enthusiast and tech lover",
  "interests": [
    "travel",
    "technology",
    "photography"
  ],
  "status": 1,
  "extra_data": {},
  "version": 1
}
```

## Data Models

### Database Schema

The application uses PostgreSQL with PostGIS extension for geospatial capabilities. Key entities include:

- **Users**: User account information
- **Profiles**: Extended user profile data
- **Homestays**: Property listings with geospatial data
- **Bookings**: Booking records with status tracking
- **Homestay Availabilities**: Date-based availability and pricing
- **Amenities**: Property amenities and features
- **Geographic Data**: Provinces, districts, and wards

### Key Constants

- **Booking Status**: Pending, Confirmed, Cancelled, Completed
- **Homestay Types**: Various accommodation types
- **User Types**: Guest, Host, Admin
- **Payment Status**: Pending, Success, Failed

## Configuration

### Application Properties

The application is configured through `application.yml`:

```yaml
server:
  port: 8080

spring:
  application:
    name: booking
  datasource:
    url: jdbc:postgresql://localhost:5432/booking_database
    username: user
    password: pass
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: true

payment:
  vnpay:
    tmn-code: ${VNPAY_TMN_CODE}
    init-payment-url: https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
    return-url: https://booking.com/order/%s/status
    timeout: 15
    secret-key: ${VNPAY_SECRET_KEY}
```

### Environment Variables

- `VNPAY_TMN_CODE`: VNPay Terminal Code
- `VNPAY_SECRET_KEY`: VNPay Secret Key for signature verification

## Running the Application

### Prerequisites

- Java 21
- PostgreSQL with PostGIS extension
- Gradle 8.x

### Setup

1. **Database Setup**:

   ```bash
   # Create database and run schema
   psql -U postgres -d booking_database -f sql/schema.sql
   psql -U postgres -d booking_database -f sql/dump-data.sql
   ```

2. **Environment Configuration**:

   ```bash
   export VNPAY_TMN_CODE=your_tmn_code
   export VNPAY_SECRET_KEY=your_secret_key
   ```

3. **Run Application**:
   ```bash
   ./gradlew bootRun
   ```

The application will start on `http://localhost:8080`

## Error Handling

The application uses a standardized response format with error handling:

```json
{
  "meta": {
    "status": "error",
    "message": "Validation failed",
    "errors": [
      {
        "field": "checkin_date",
        "message": "checkin_date cannot be blank"
      }
    ]
  },
  "payload": null
}
```

## Development Notes

- Uses MapStruct for DTO mapping
- Implements validation using Bean Validation
- Follows Domain-Driven Design principles
- Uses factory pattern for service creation
- Includes comprehensive logging with SLF4J
- PostGIS integration for geospatial queries with radius-based search
