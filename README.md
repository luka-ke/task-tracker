# Task Tracker Application

A simple task and project management system built with Spring Boot, JWT-based authentication, and role-based access
control.

## 🛠️ How to Run the Application

### Requirements

- Java 17+
- PostgreSQL
- Gradle

### Steps

1. **Clone the repository:**
 


2. **Configure Database:**
   Ensure PostgreSQL is running and a database named my_database is created. change the config in
   application.properties:

       spring.datasource.url=jdbc:postgresql://localhost:5432/my_database

       spring.datasource.username=

       spring.datasource.password=

3. **Build Project:**

    linux/mac `./gradlew build`

    windows `gradlew.bat build`


4. **Run the Project:**

   linux/mac `./gradlew bootRun`
 
   windows `gradlew.bat bootRun`


## 🔐 How Authentication Works

When using tools like Postman to call secured endpoints, you must provide a token via the `Authorization` section.  
Select **Bearer Token** as the type, and paste **only the token itself** (without the word "Bearer").

You initially receive the token in the **response body** after a successful login or registration request.

After that, every secured endpoint will return an **updated token** in the response headers under `X-Access-Token`.  
You should replace the old token with the new one to maintain continuous access.


## 📌 API Endpoint Summary

| Method | Endpoint                                               | Description                        | Authentication |
|--------|--------------------------------------------------------|------------------------------------|----------------|
| GET    | `/v1/public/login`                                     | User login via query params        | ❌ No          |
| GET    | `/v1/public/api/users`                                 | Get list of users                  | ❌ No          |
| GET    | `/api/tasks/1`                                         | Get task by ID                     | ✅ Yes         |
| GET    | `/api/tasks`                                           | Get all tasks                      | ✅ Yes         |
| POST   | `/api/tasks`                                           | Create new task                    | ✅ Yes         |
| GET    | `/api/projects/1`                                      | Get project by ID                  | ✅ Yes         |
| PUT    | `/api/projects/1`                                      | Update project                     | ✅ Yes         |
| POST   | `/api/projects`                                        | Create new project                 | ✅ Yes         |
| DELETE | `/api/projects/2`                                      | Delete project                     | ✅ Yes         |
| GET    | `/api/projects`                                        | Get all projects                   | ✅ Yes         |
| POST   | `/api/tasks/`                                          | Create new task                    | ✅ Yes         |
| PATCH  | `/api/tasks/1/status`                                  | Update task status                 | ✅ Yes         |
| POST   | `/api/tasks/1/assign/8526928180415049973`              | Assign task to user                | ✅ Yes         |

## 🔐 Roles and Permissions Overview

| Role   | Permissions                                                                 |
|--------|------------------------------------------------------------------------------|
| Admin  | Full access to all endpoints. Can create, update, assign, and delete tasks/projects. Manage users. |
| Manager| Can view and manage all tasks and projects, assign tasks to users. Cannot delete users. |
| User   | Can view and update only their own tasks. Cannot create projects or assign tasks. |

### Notes:
- [View Postman collection](./tasktracker/tasktracker-api.postman_collection.json)
- [View swagger collection](http://localhost:8080/swagger-ui/index.html#/)