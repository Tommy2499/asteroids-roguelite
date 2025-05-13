# Walking Skeleton Outline
This document contains our plan for setting up a minimal version of our tech stack. It requires that we have a front end that interacts with the user and can communicate to a backend application, and can fetch and store information into a database.

## Requirements
Here is a list of the required components of the Walking Skeleton and their specifications. These may be subject to change.
- A Front End application written in Javascript/React
    - This front end will be run in a docker container on an Ubuntu Image located locally on the User's PC
    - It will have a way to interact with the User, through the terminal or some other means 
- A Back End application written in Java 
    - This back end will be run in a docker container on an Ubuntu Image
    - It can be located locally or on a VM
    - It should be a server that contains a simple function call to retrieve information from the Database
- A communication channel between the front end container and back end container
    - This can be done via gRPC, HTTP, or REST API
- A MySql Database
    - For the purposes of this walking skeleton, it should contain a single (test) table that the User can store/fetch information from through a function call from the back end
    - It is run in a Docker container on an Ubuntu Image, located on the VM
- A communication channel between the Back End container and the MySql Database container
- Dockerfiles for each of the three components, with the required dependencies


