**Project of Distributed and Pervasive Systems course, A.Y. 2022/23**

In a smart city named Greenfield, a fleet of robots moves around the districts of the city to clean their streets.
![image](https://github.com/sofiagavanelli/Greenfield2023/assets/49269518/862094a7-0d06-4b8a-aafd-26558633e751)
The cleaning robots move around the four smart city districts. Occasionally, such robots need to go for maintenance issues to the mechanic of Greenfield, which can handle only a single robot at a time. Each robot is also equipped with a sensor that periodically detects the air pollution levels of Greenfield.
Such pollution measurements are periodically transmitted from the robots of each district to an Administrator Server through MQTT. The Administrator Server is in charge of dynamically registering and removing cleaning robots from the system. Moreover, it collects and analyses the air pollution levels of Greenfield in order to provide pollution information to the experts (Administrator Client) of the environmental department of Greenfield.
The goal of the project is to implement the Administrator Server, the Administrator Client, and a peer-to-peer system of cleaning robots that periodically send pollution measurements to the Administrator Server through MQTT, and autonomously organize themselves through gRPC when they concurrently need to go to the mechanic of the smart city.

The entirety of the project is explained in the "Project DPS 2023 - Greenfield" pdf file.
