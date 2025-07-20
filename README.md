# Technical Discussion

**Important: Please Read this README First.**

This document contains essential instructions on setting up the codebase and dependencies required for the discussion. Please follow the instructions below to ensure you are well prepared. By doing so, we can avoid spending time on setup during the pairing exercise.

## Pre-requisites
Please ensure you have the following set up on your system:
- **Java:** Make sure you have a recent version of Java installed on your machine. Download from the [official website](https://java.com/en/download/help/download_options.html). Project tested with OpenJdk v11.0.13.
- **Gradle:** Install Gradle on your machine. Download from the [official website](https://gradle.org/install/).
- **Docker:** Install Docker on your system to run the Juice Shop application in a container. Download from the [official website](https://www.docker.com/).

### Setting up Juice Shop Application
We will be using JuiceShop application during our discussion, please ensure that you have JuiceShop application running on your system in a docker container. Run the following to download the image and start the container:

`docker run -d -p 3000:3000 bkimminich/juice-shop:v13.2.0`

Open your web browser and navigate to http://localhost:3000 to ensure the Juice Shop application is up and running.

Please manually register a user in JuiceShop application. We will use this to write our tests against.

### Run the tests
- Run `gradle wrapper` from the project root.
- Rn the test with `./gradlew test` from the project root.

### Things we expect to see during our technical discussion
- **Test runs out of the box**: Ensure the test can be executed.
- **Be prepared to troubleshoot** - Address any issues during test execution confidently.
- **Communicate your thought process** - We would love to hear your thoughts while you write code.
- **Follow clean code practices** - Ensure the code has descriptive variables, indentation and modularity.
- **Locator Strategy** - Please ensure that locators choosen in test are stable and have less possibility to break in future.

**If you have any questions or face any issues during the setup process, please feel free to reach out to us. We look forward to have an insightful conversation during our time together.**