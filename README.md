🔐 UserAuthService

A full-stack User Authentication Service built using Spring Boot for the backend and React for the frontend. This project provides secure user registration, login, and role-based access control using JWT authentication, making it suitable for modern and scalable web applications.


✨ Features

- User Registration & Login
- JWT-based Authentication & Authorization
- Role-Based Access Control (RBAC)
- Secure Password Encryption (Spring Security)
- RESTful APIs
- React-based Frontend Integration
- CORS Configuration for Frontend–Backend Communication



🛠️ Tech Stack

* Backend
  
- Java
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- JPA / Hibernate
- MySQL

* Frontend

- React
- JavaScript
- Html
- Css
- React Router
 


📂 Project Structure
UserAuthService/
│── backend/
│ ├── controller/
│ ├── service/
│ ├── repository/
│ ├── model/
│ ├── security/
│ └── application.properties
│
│── frontend/
│ ├── src/
│ ├── components/
│ ├── pages/
│ └── services/


⚙️ Backend Configuration (application.properties)

Before running the application, you must configure the following properties in application.properties.


1️⃣ Server Configuration
server.port=8080 ( Change the port if required )


2️⃣ Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/userauthdb
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

  Create a MySQL database named userauthdb
  Replace username and password with your MySQL credentials

3️⃣ JPA / Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

  update automatically updates database tables
  show-sql prints SQL queries in the console

4️⃣ JWT Configuration
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000

jwt.secret: Use a strong secret key
jwt.expiration: Token validity in milliseconds (24 hours here)

5️⃣ CORS Configuration (if applicable)
frontend.url=http://localhost:3000 ( Ensure this matches your React frontend URL )

▶️ Running the Backend (Spring Boot)
# Navigate to backend folder
cd Server

# Run the application
mvn spring-boot:run

Backend will start at:
http://localhost:8080

▶️ Running the Frontend (React)
# Navigate to frontend folder
cd client

# Install dependencies
npm install

# Start the React app
npm start

Frontend will run at:
http://localhost:3000



🔑 API Endpoints
Method	  Endpoint	               Description
POST	    /api/auth/register	     Register new user
POST	    /api/auth/login	User     login & JWT generation
GET	      /api/user/profile	       Get logged-in user details


🔒 Authentication Flow

- User registers or logs in via React UI
- Backend validates credentials
- JWT token is generated and returned
- Token is stored on the frontend
- Protected APIs require JWT in Authorization header

Authorization: Bearer <JWT_TOKEN>



🚀 Future Improvements

Refresh Token Support
OAuth2 (Google/GitHub Login)
Docker Support



🤝 Contributing
Contributions are welcome! Feel free to fork this repository and submit a pull request.


⭐ If you like this project, consider giving it a star on GitHub!
