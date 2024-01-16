# <p align="center">Accommodation Booking Service</p>


## Project goal

In this project, our goal is to make renting a home easier and more convenient. We want to create a user-friendly online system that simplifies the tasks for administrators and provides renters with an effortless way to find and secure accommodations. We aim to transform the housing rental experience, making it more accessible and enjoyable for everyone involved.

## Main Features

1. **Accommodation Management:** Efficiently manage your accommodations with our intuitive system. Add, update, or remove listings seamlessly, keeping your property inventory up-to-date and easily accessible. Simplify the process of showcasing available accommodations to potential renters.

2. **Rental Booking Management:** Streamline the booking process for renters with our user-friendly application. Easily add selected accommodations to your cart, review orders, and complete bookings hassle-free. Our system provides a straightforward order management system, enhancing the overall booking experience.

3. **Customer Management:** Effectively manage customer profiles and interactions within the application. Keep track of user information, and bookings.

4. **Security and JWT Tokens for User Auth:** Prioritize the security of user data and transactions in our application. Utilizing Spring Security, we've implemented robust measures to safeguard your information. The use of JWT (JSON Web Tokens) ensures secure authentication and authorization, allowing only authorized users access to sensitive features and data.

5. **Telegram Notifications Displaying:** Stay informed with notifications through our Telegram integration. Receive updates on new bookings, and cancellations directly on your Telegram account. Enhance communication and stay connected with the latest activities within the application.

6. **Payments Handling:** Simplify the payment process with our secure payment handling system. We integrate with Stripe to facilitate smooth and secure transactions. Users can confidently make payments for their bookings, and managers can efficiently track and manage payment statuses.

## Technologies Used


**Coding Language:**
- Java

**Web Crafting:**
- Spring MVC
- Servlets
- Tomcat

**Security Guard:**
- Spring Security

**Data Handling:**
- Spring Data JPA
- Hibernate
- MySQL

**Project Setup:**
- Spring Boot
- Maven

**Testing and Docs:**
- JUnit
- Mockito
- Swagger

**Version Control:**
- Git

## They Control our API

In our project, we've designed our controllers based on REST (Representational State Transfer) principles. Controllers play a crucial role in managing how our application handles requests and communicates responses over the web.

**User Controller:**
- Update User Role:
    - PUT: /users/{id}/role
    - Functionality: Allows users to update their roles, providing role-based access.

- Retrieve Current User Profile:
    - GET: /users/me
    - Functionality: Retrieves the profile information for the currently logged-in user.

- Update User Profile:
- PUT/PATCH: /users/me
- Functionality: Allows users to update their profile information.

**Authentication Controller:**
- Register a New Account:
    - POST: /register
    - Functionality: Allows users to create a new account in the system.

- Login and Get JWT Tokens:
    - POST: /login
    - Functionality: Grants JWT tokens to users upon successful authentication.

**Accommodation Controller:** Managing accommodation inventory.
- Create New Accommodation:
    - Endpoint: POST: /accommodations
    - Functionality: Allows the addition of new accommodations.

- List Accommodations:
    - Endpoint: GET: /accommodations
    - Functionality: Provides a list of available accommodations.

- Retrieve Specific Accommodation:
    - Endpoint: GET: /accommodations/{id}
    - Functionality: Retrieves detailed information about a specific accommodation.

- Update Accommodation Details:
    - Endpoint: PUT/PATCH: /accommodations/{id}
    - Functionality: Allows updates to accommodation details, including inventory management.

- Remove Accommodation:
    - Endpoint: DELETE: /accommodations/{id}
    - Functionality: Enables the removal of accommodations.

**Booking Controller Controller:** Managing Users' Bookings
- Create New Booking:
    - POST: /bookings
    - Functionality: Permits the creation of new accommodation bookings.

- Retrieve Bookings Based on User ID and Status:
    - GET: /bookings/?user_id=...&status=...
    - Functionality: Retrieves bookings based on user ID and their status. (Available for managers)

- Retrieve User's Bookings:
    - GET: /bookings/my
    - Functionality: Retrieves bookings for the currently logged-in user.

- Retrieve Specific Booking Information:
    - GET: /bookings/{id}
    - Functionality: Provides information about a specific booking.

- Update Booking Details:
    - PUT/PATCH: /bookings/{id}
    - Functionality: Allows users to update their booking details.

- Cancel Booking:
    - DELETE: /bookings/{id}
    - Functionality: Enables the cancellation of bookings.

**Payment Controller (Stripe) Controller:** Facilitating Payments
- Retrieve Payment Information for Users:
    - GET: /payments/?user_id=...
    - Functionality: Retrieves payment information for users.

- Initiate Payment Session:
    - POST: /payments/
    - Functionality: Initiates payment sessions for booking transactions.

- Handle Successful Payment Processing:
    - GET: /payments/success/
    - Functionality: Handles successful payment processing through Stripe redirection.

- Manage Payment Cancellation:
    - GET: /payments/cancel/
    - Functionality: Manages payment cancellation and returns payment paused messages during Stripe redirection.

## Postman Collection
We utilize Postman as a powerful tool for API testing. Postman simplifies the testing process, allowing us to create and execute test cases, automate workflows, and generate comprehensive reports. By providing a collection of pre-configured API requests, we ensure a standardized and efficient testing approach.
Link:

## Setup

1. **Clone the repository.**
2. **Configure required settings** in the `application.properties` and `.env` files.
3. **Ensure tables are created** using Liquibase.
4. **Build and run the project** using your preferred IDE or `mvn spring-boot:run`.

**Using Docked**

If you have Docker Compose installed, you can use the provided docker-compose.yml file to simplify the setup.

**Run the following commands:**
1. In the project directory: `docker-compose build`
2. Next step: `docker-compose up -d`

This will create and start the necessary containers, including the database.

## Challenges and Solutions

Our development journey was marked by various challenges.

1. **Complex Architecture Setup:**

- Establishing a scalable and intricate architecture demanded careful planning to ensure seamless communication between diverse components.

2. **Telegram Integration:**

- Integrating Telegram for notifications brought forth challenges in authentication, message formatting, and notification sending.

3. **Payment Integration (Stripe):**

- The integration of Stripe for payment processing required addressing security concerns, transaction flow, and data transmission.

4. **New Domain Area Implementation:**

- Venturing into a new domain area, specifically accommodation services, presented challenges as it was uncharted territory for our team.

5. **Automated Deployment (Docker):**

- Utilizing Docker for automated deployment presented challenges in containerization and ensuring consistency across different environments.

By overcoming these challenges, the development team demonstrated resilience, problem-solving skills, and a commitment to delivering functional accommodation booking service.


## Possible improvements

1. **Deployment on AWS:**
- Explore deploying the application on Amazon Web Services (AWS) servers. Leveraging AWS infrastructure can enhance scalability, reliability, and performance, contributing to a more robust hosting solution.

2. **Comprehensive Test Coverage:**
- Enhance test coverage by creating additional unit, integration, and end-to-end tests. Comprehensive testing helps identify and address potential issues, ensuring a robust and reliable application.

3. **Social Media Authentication:**
- Enable users to register and log in using their Google or other social media accounts. This improves user convenience and expands the range of registration options.

4. **Enhanced Error Handling:**
- Implement improved error handling mechanisms to provide clear and user-friendly error messages. This helps users understand issues and allows developers to troubleshoot more effectively.

5. **Multi-factor Authentication (MFA):**
- Introduce multi-factor authentication for an additional layer of security during user logins. This enhances the protection of user accounts and sensitive information.
  User Feedback and Ratings:

6. **User Feedback and Ratings:**
- Incorporate a user feedback and rating system for accommodations. This not only engages users but also helps potential renters make informed decisions based on others' experiences.

7. **Responsive Email Notifications:**
- Improve the content and design of email notifications, making them more visually appealing and responsive. Well-crafted emails can enhance user engagement and communication.

8. **Analytics Usage:**
- Integrate analytics tools to track user interactions, popular accommodations, and booking patterns. Analyzing this data can provide insights for future updates and feature enhancements.

Making these improvements can help our application grow and succeed in the long run, offering a better and more user-friendly experience.