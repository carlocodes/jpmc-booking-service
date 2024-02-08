# JPMC Booking Service

---
The Booking Service, built using Java 11 and Spring Boot 2.6.11, facilitates seamless show
booking operations. It empowers both admins and buyers to efficiently manage show setups,
seat availability, ticket booking, and cancellation. Designed with object-oriented principles
and rigorously tested, it ensures a reliable and user-friendly experience.

# Starting the application

---
This application makes use of the Maven build tool. To start up the application, reroute
to the application's directory and use the following command:
```
mvn spring-boot:run
```

# Running tests

---
To run the application's test, please use the following command:
```
mvn test
```

# Endpoints

---
1. Setup
- Restriction: Admin
- To set up the number of seats per show.

Command:
```
curl -X POST http://localhost:8080/api/v1/show/admin/setup \
-H "Content-Type: application/json" \
-d '{
  "rows": 26,
  "seatsPerRow": 10,
  "cancellationWindowInMinutes": 2
}'
```

2. View
- Restriction: Admin
- To display show id, ticket number, buyer phone number, and seat numbers allocated to buyer.

Command:
```
curl -X GET http://localhost:8080/api/v1/booking/admin/view/1
```

3. Availability
- Restriction: Buyer
- To list all available seat numbers for a show e.g. A1, F4, etc.

Command:
```
curl -X GET http://localhost:8080/api/v1/booking/buyer/availability/1
```

4. Book
- Restriction: Buyer
- To book a ticket. This must generate a unique ticket number and display.

Command:
```
curl -X POST http://localhost:8080/api/v1/booking/buyer/book \
-H "Content-Type: application/json" \
-d '{
    "showId": 1,
    "phoneNumber": "09123456789",
    "seats": "A1,B2,C3"
}'
```

5. Cancel
- Restriction: Buyer
- To cancel a ticket.

Command:
```
curl -X DELETE http://localhost:8080/api/v1/booking/buyer/cancel \
-H "Content-Type: application/json" \
-d '{
    "phoneNumber": "09123456789",
    "ticketNumber": "084b296a-9bc7-4ee7-91ff-d8b676ba3c94"
}'
```

# Notes:

---
This application has no implementation of the restrictions for an Admin and a Buyer.
Meaning you can hit any endpoint you like. If I were to implement the restrictions for
these roles, I would add the Spring Security library and protect each endpoint (hence
the segregation in the path routes) by their role (e.g. ADMIN, BUYER, etc.).
