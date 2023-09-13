# Thesis - Project
This project was developed as a bachelor's thesis at the University of Applied Sciences in Hamburg (HAW).

## Abstract
In this work, a full stack application is developed, which in the context of a neighborhood
school aims to ensure the inventory management of physical media (books, iPads, laptops, etc.) and their lending and return. The application has a classic client-server architecture.
The backend is implemented using Spring Boot and the resulting REST API is secured
with Spring Security. Vue.js is used as a frontend framework and Tailwind CSS as an open
source CSS framework for the frontend. The work describes the requirements analysis,
specification, design and implementation.

# Tech-stack

## Backend
<ul>
  <li>Java 17 </li>
  <li>Spring-Boot </li>
  <li>Spring-MVC </li>
  <li>Spring-Data </li>
  <li>Spring-Security</li>
</ul>

## Frontend
<ul>
  <li>Vue.js </li>
  <li>Vue-Router </li>
  <li>Vuex </li>
  <li>Tailwind.css </li>
</ul>

# Requirements
<ul>
  <li>Java 17 or higher </li>
  <li>Node v18.16.0 or higher</li>
  <li>npm 9.5.1 or higher</li>
</ul>

# Usage
<h3>How to start the backend? </h3>

Download the project. If you use an IDE start backend via IDE. If you dont use an IDE:
<br>
1. Open console/Command Propt
2. Navigate to the  `bachelor-0.0.1-SNAPSHOT.jar` under ```thesis/bachelor/target```.   
3. run `java -jar bachelor-0.0.1-SNAPSHOT.jar`

Backend starts on localhost:8080. Now you can acces Swagger-UI under `http://localhost:8080/swagger-ui/index.html#/` 

<h3>How to start the frontend? </h3>

1. Navigate to the frontend directory under `thesis/bachelor/src/frontend`
2. You will probably need to install the dependencies. Run in the console/cmd prompt `npm install`
3. After successfully installing the requiered dependencies run `npm run dev` . This will start the vue dev server on localhost:5173
4. Navigate with a browser to localhost:5173
5. Log-in with username: `admin` and password: `user`.
6. Have fun (Frontend is german only :innocent:)

# Swagger-UI

Backend supports Swagger-UI. Start backend and navigate to `http://localhost:8080/swagger-ui/index.html#/`.
To properly test the backend API you will need to log-in first. 
<br>
<img src="https://github.com/3rim/thesis/assets/37411005/98f6d198-e630-4b5b-8c49-c44f26eb6ab7" width="70%">

Login example call (username: `admin`, password: `user`
<br>
<img src="https://github.com/3rim/thesis/assets/37411005/8c5df751-b03f-4b37-acf8-00c32e9d82ae" width="70%">

copy jwt in response body
<br>
<img src="https://github.com/3rim/thesis/assets/37411005/22214f8b-aeba-4b0f-b0bb-f082ae2cc07c" width="70%">

add jwt via authorize button at the top
<br>
<img src="https://github.com/3rim/thesis/assets/37411005/d5fd5ba5-c510-458d-b7f7-3e4c3770868a" width="70%">
<br>

You are now logged in as Admin. Every following request is authorized. Have fun.


