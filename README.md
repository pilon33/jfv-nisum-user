## Test Java NISUM

### Sr Developer José Francisco valdez
Linkedin https://www.linkedin.com/in/jose-francisco-valdez-282a1b1a/

### Clone the repository:
1) git clone https://github.com/pilon33/nisum-user-jfv.git

### Navigate into the folder
2)$ cd nisum-test/ms-user

### Install dependencies
3)$./gradlew clean build

### Run the project
4)$./gradlew bootRun

###  POST --> http://localhost:8084/register-user  (Token is obtained)

![image](https://user-images.githubusercontent.com/12847173/139635482-0fcfb5fc-81ec-457d-95b4-3a36632f1563.png)



![image](https://user-images.githubusercontent.com/12847173/139636299-b2ad1bdf-f9a8-4554-bede-10fa908ce725.png)


###  GET --> http://localhost:8084/findUserByEmail  (Token in authorization )

![image](https://user-images.githubusercontent.com/12847173/139636502-202fb8f2-77c4-4ce6-916b-cd1e4afdd4d8.png)


![image](https://user-images.githubusercontent.com/12847173/139636716-f5280ecb-e538-453e-bf2b-15a486cd4d2d.png)

###  GET --> http://localhost:8084/findAllUsersl  (token en authorization )

![image](https://user-images.githubusercontent.com/12847173/139638324-a899b9b2-0c6a-4e6e-9ec8-d18d7d6eb684.png)



###  POST -->http://localhost:8084/authenticate  (User authentication)


![image](https://user-images.githubusercontent.com/12847173/139636943-ac428952-5ade-4feb-91f0-b553ec6e9d72.png)

###  PUT -->http://localhost:8084/user  (Token in authorization + data update user)

![image](https://user-images.githubusercontent.com/12847173/139637359-1794d38d-696d-4452-a1de-bccfebc92e1b.png)


![image](https://user-images.githubusercontent.com/12847173/139637394-3fe1d22c-95bb-4d92-9e5f-463bd59b8344.png)





###  Application package distribution

![image](https://user-images.githubusercontent.com/12847173/139638006-ab4fa163-7481-468a-8757-4a3e3f82fc11.png)





### Authentication process



![image](https://user-images.githubusercontent.com/12847173/139641672-7c9eb23d-11c9-48a0-9bd7-9552a7481a5c.png)



