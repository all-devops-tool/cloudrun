# Wine Park Application

This is a Spring Boot application for managing wines in a wine park.

## Table of Contents

- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [Building the Application](#building-the-application)
  - [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Continuous Integration and Deployment](#continuous-integration-and-deployment)
- [Workflow for CI/CD](#workflow-for-ci/cd)
- [Flow_of_Workflow](#flow-of-workflow)
- [Technologies Used](#technologies-used)

## Introduction

This application provides RESTful APIs to perform CRUD operations on wines in a wine park. It uses Spring Boot, Spring Data JPA, and MySQL for data storage.

## Prerequisites

Make sure you have the following installed:

- Java 17
- Maven
- MySQL

## Getting Started

### Building the Application

mvn clean install

## Running the Application

1. Ensure you have JDK 17, Maven, and MySQL installed.
2. Clone this repository.
3. Configure the MySQL database settings in the application properties.
4. Run the application using mvn spring-boot:run.
5. Access the APIs via the defined endpoints to interact with the wine park.

### API Endpoints

| API | Description | Request | Response |
| --- | --- | --- | --- |
| `GET /user/list` | List all users from the database | N/A | JSON data |
| `POST /user/add` | Add a new user | JSON payload | Success or Failure response |
| `PUT /user/update/<int:id>` | Update an existing user | JSON payload | Success or Failure response |
| `DELETE /user/delete/<int:id>` | Delete a user | N/A | Success or Failure response |
| `GET /health` | Check service health | N/A | Success or Failure response with message |
| `GET /metrics` | Get service metrics | N/A | List of metrics in JSON format |
| `GET /test` | For testing purposes | N/A | Test API response |

### Continuous Integration and Deployment
This project uses GitHub Actions for Continuous Integration and Deployment. The CI/CD workflow is defined in the .github/workflows/ci-cd.yml file. It includes steps to build and test the application and deploy it to a target environment.

### Workflow for CI/CD
The CI/CD workflow includes the following steps:

- Build: Maven build to compile the code and package the application.
- Test: Run unit tests to ensure code quality.
- Generate code-coverage report: Use Jacoco to generate a code-coverage report.
- Upload code-coverage report as an artifact: Upload the code-coverage report as an artifact.
- Configure AWS credentials: Set up AWS credentials for deployment.
- Build project and package jar: Build the project and package the resulting JAR file.
- Build Docker Image: Build a Docker image for the application.
- Run Trivy vulnerability scanner: Use Trivy to scan for vulnerabilities in the Docker image.
- Handle Trivy results: Handle the results of the Trivy vulnerability scan.
- Login to Amazon ECR: Log in to Amazon ECR for Docker image registry.
- Push Docker image to Amazon ECR: Push the Docker image to Amazon ECR.

### Secrets
Ensure the following secrets are configured in your GitHub repository settings:

- SONAR_PROJECTKEY: SonarQube project key for code analysis.
- SONAR_HOST_URL: SonarQube server URL.
- SONAR_TOKEN: Token for authenticating with SonarQube.
- AWS_IAM_ARN: AWS IAM role ARN for deployment.
- AWS_REGION: AWS region for deployment.
- AWS_ACCOUNT_ID: AWS account ID for deployment.
- INSTANCE_PUBLIC_IP: EC2 instance public-ip for deployment.
- PRIVATE_SSH_KEY: To extract SSH private key

### Technologies Used
- Spring Boot
- Spring Data JPA
- MySQL
- Lombok
 
### Flow of Workflow
```yaml
# Define the CI/CD Workflow for Java code
name: ci Workflow for java code (pratiksha)

# Trigger the workflow on pushes to the specified branch
on:
  # github repository event 
  push:
    #The workflow will trigger when push event happen on feature-pratiksha branch
    branches:
      - feature-pratiksha
      
# Define permissions required for the workflow
permissions:
  id-token: write   # This is required for requesting the JWT whih is useful for authentication withing the wofkflow
  contents: read    # This permission allows reading the content of files in the repository

# The jobs to be executed in the workfloww
jobs:
  # Define the 'ci' job
  ci:
    # git-hub hosted runner
    runs-on: ubuntu-latest
    # Define the steps to be executed in the job
    steps:
    # check out repository's code into workflow's workspace
    # The actions/checkout action in GitHub Actions is used to check out or clone the source code repository into the GitHub Actions runner environment
    - name: Checkout code
      uses: actions/checkout@v3
 
          
    # Set up JDK 17
    # The action is named "Set up JDK 17," and it uses the actions/setup-java action to prepare the GitHub Actions environment with the specified version of the JDK.
    # The java-version parameter is set to '17', indicating that the workflow requires JDK version 17.
    # The distribution: 'temurin' parameter specifies the distribution of the JDK. In this case, it's using the Temurin distribution. Temurin is a distribution of the AdoptOpenJDK 
    #  project, providing prebuilt binaries of the OpenJDK.
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
       distribution: 'temurin'
       java-version: '17'

    # Run unit tests
    # this step is crucial for ensuring the quality of the Spring Boot application by running its unit tests.
    - name: Run unit test
      run: mvn test

    # Generate code-coverage report using jacoco
    # jacoco:report is a Maven goal provided by the JaCoCo plugin. It triggers the generation of a code coverage report for the project.
    - name : Get code-coverage report
      run : mvn jacoco:report
      
    # Upload code-coverage report as an artifact
    # uses field specifies the GitHub Actions action that should be used for this step. In this case, it's using the actions/upload-artifact action.
    # target/site/jacoco/index.html specifies the path to the file or directory that should be uploaded as an artifact
    - name: Code Coverage Report
      uses: actions/upload-artifact@v3
      with:
        name: Code Coverage Reports
        path: target/site/jacoco/index.html   # Code coverage report will be generate at 'target/site/jacoco/index.html'
    
    # Sonarqube Scan
    # this workflow step triggers the SonarQube analysis on the codebase using the specified Maven plugin and configuration. The analysis results will be sent to the specified SonarQube 
     # server for further inspection and reporting on code quality and security issues.
    - name: SonarQube Scan
      run: mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=${{ secrets.SONAR_PROJECTKEY }} -Dsonar.host.url=${{ secrets.SONAR_HOST_URL }}  -Dsonar.login=${{ secrets.SONAR_TOKEN }}
    #The command will run Maven in non-interactive mode (-B) to execute the verify phase and perform a SonarQube analysis using the specified project key, SonarQube server URL, and 
    # authentication token.

    # Check sonarqube quality gate
    # This action checks the status of the SonarQube quality gate for the specified project. It uses the SonarQube API to retrieve the project's quality gate status and then evaluates 
    # whether the status is "OK" or not
    - name: Check SonarQube Quality Gate
      run: |
       status=$(curl -s "${{ secrets.SONAR_HOST_URL }}/api/qualitygates/project_status?projectKey=${{ secrets.SONAR_PROJECTKEY }}" -u "${{ secrets.SONAR_TOKEN }}": | jq -r '.projectStatus.status')
       if [[ "$status" != "OK" ]]; then
          echo "SonarQube quality gate check failed." 
       else
          echo "SonarQube quality gate check succeeded. Continuing with the workflow..."
       fi
    # if exit code 1 is added after (echo "SonarQube quality gate check failed.") workflow will fail which will check for quality gate condition 
 
    # Configuring AWS credentials to assume a specified IAM role with a given session name in specified AWS region
    # the workflow needs to perform actions in AWS, and these actions should be done with elevated privileges, provided by assuming a specific IAM role.
    - name: configure aws credentials
      uses: aws-actions/configure-aws-credentials@v3
      with:
          role-to-assume: ${{ secrets.AWS_IAM_ARN }}
          role-session-name: samplerolesession
          aws-region: ${{ secrets.AWS_REGION }}

   # Build project and packages the resulting JAR file into the target directory
   # This workflow step is responsible for building the Spring Boot project and packaging it into a JAR (Java Archive) file
    - name: Build project and package jar
      run: mvn package

   # Builds the Docker image using Dockerfile & tags it with commit SHA & pushes it to ECR repository
   #
    - name: Build Docker Image
      run: |
         docker build -t winepark:${{ github.sha }} .
         docker tag winepark:${{ github.sha }} ${{ secrets.AWS_ACCOUNT_ID }}.dkr.ecr.${{ secrets.AWS_REGION }}.amazonaws.com/ecrrepoo:${{ github.sha }}

    #docker build -t winepark:${{ github.sha }} .: This command builds a Docker image using the Dockerfile in the current directory (.) and assigns it a tag (-t) with the name winepark 
    #and the unique identifier ${{ github.sha }}. The ${{ github.sha }} is typically the commit SHA, making the tag unique and associated with a specific version of the code.

    # docker tag winepark:${{ github.sha }} ${{ secrets.AWS_ACCOUNT_ID }}.dkr.ecr.${{ secrets.AWS_REGION }}.amazonaws.com/ecrrepoo:${{ github.sha }}: This command tags the locally built 
    # Docker image with the Amazon Elastic Container Registry (ECR) repository information. 
    # The entire purpose of this step is to build a Docker image from the codebase, assign it a unique tag, and then tag it with the ECR repository information. This prepares the Docker 
    # image for later steps in the workflow, such as pushing it to the ECR registry for deployment.

   # Enable Docker to interact with an Amazon ECR registry
   # The purpose of this step is to ensure that Docker is authenticated with the specified ECR registry before interacting with it. Docker needs valid AWS credentials to push or pull Docker images from the ECR registry. By using the # 
   # AWS CLI to obtain temporary credentials and passing them to Docker, this step ensures that the subsequent Docker commands in the workflow can interact with the ECR registry without authentication issues.
    - name: Login to Amazon ECR
      run: aws ecr get-login-password --region ${{ secrets.AWS_REGION }} | docker login --username AWS --password-stdin ${{secrets.AWS_ACCOUNT_ID}}.dkr.ecr.${{ secrets.AWS_REGION }}.amazonaws.com

  
   # Run Trivy vulnerability scanner on  specified Docker image in the ECR repository
   # this step runs the Trivy vulnerability scanner on the specified Docker image, checks for OS and library vulnerabilities with critical and high severity levels, and continues with the workflow regardless of the scan results. The 
   # scan results can be accessed and further processed using the id assigned to this step (trivy-scan)
    - name: Run Trivy vulnerability scanner
      id: trivy-scan
      uses: aquasecurity/trivy-action@master
      with:
        image-ref: '${{ secrets.AWS_ACCOUNT_ID }}.dkr.ecr.${{ secrets.AWS_REGION }}.amazonaws.com/githubactionsoidcecrrepo:${{ github.sha }}'
        format: 'table'
        exit-code: '1'
        ignore-unfixed: true
        vuln-type: 'os,library'
        severity: 'CRITICAL,HIGH'
      continue-on-error: true
      
    # Print message as Vulnerability scan failed which means vulnerability found in the previous step
    # The purpose of this action is to provide feedback in the workflow log when vulnerabilities are detected. If the Trivy scan step reports a failure, it means that critical or high-severity vulnerabilities were found in the Docker 
    # image. The subsequent message informs users and the workflow executor about the presence of vulnerabilities, allowing them to take appropriate actions.
    - name: Handle Trivy results
      if: steps.trivy-scan.outcome == 'failure'
      run: |
        echo "Vulnerability scan failed!"

    # Print message as Vulnerability scan succeeded which means vulnerability is not found 
    - name: Continue with other steps
      if: steps.trivy-scan.outcome == 'success'
      run: |
        echo "Vulnerability scan succeeded!"
     
    # Push Docker image to Amazon ECR
    - name: push docker image
      run: |
        docker push ${{ secrets.AWS_ACCOUNT_ID }}.dkr.ecr.${{ secrets.AWS_REGION }}.amazonaws.com/ecrrepoo:${{ github.sha }}
  
  # Job to deploy the application
  deploy:
    needs: ci  # This ensures that the deployment process only runs when the Docker image has been successfully built and pushed to Amazon ECR
    runs-on: ubuntu-latest

    steps:
      # Checkout the repository
      - name: Checkout Repository
        uses: actions/checkout@v2
        
      # Create SSH directory and known_hosts file
      # Creates an SSH directory and a known_hosts file to store information about remote hosts.
      - name: Create SSH directory and known_hosts file
        run: |
          mkdir -p ~/.ssh
          touch ~/.ssh/known_hosts

      # Adding private key to id_rsa
      # Extracts the private SSH key from the GitHub Secrets and writes it to the ~/.ssh/id_rsa file.
      - name: Extract SSH private key
        run: echo "${{ secrets.PRIVATE_SSH_KEY }}" > ~/.ssh/id_rsa
        shell: bash

      # Giving permission to id_rsa
      # Sets proper permissions (read and write for the owner only) on the id_rsa file to ensure it's secure.
      - name: Set proper permissions for the private key
        run: chmod 600 ~/.ssh/id_rsa

      # Install SSH Client
      # Installs the SSH client (openssh-client) on the runner machine.
      - name: Install SSH Client
        run: sudo apt-get install -y openssh-client  

      # Configuring AWS credentials to assume a specified IAM role with a given session name in specified AWS region
      - name: configure aws credentials
        uses: aws-actions/configure-aws-credentials@v3
        with:
          role-to-assume: ${{ secrets.AWS_IAM_ARN }}
          role-session-name: samplerolesession
          aws-region: ${{ secrets.AWS_REGION }}
  
      # Deploy to EC2 with Docker
      - name: Deploy to EC2 with Docker
        run: |
         ssh-keyscan -H ${{ secrets.INSTANCE_PUBLIC_IP }} >> ~/.ssh/known_hosts
         aws ecr get-login-password --region ${{ secrets.AWS_REGION }} | sudo docker login --username AWS --password-stdin ${{ secrets.AWS_ACCOUNT_ID }}.dkr.ecr.${{ secrets.AWS_REGION }}.amazonaws.com
         ssh -i ~/.ssh/id_rsa ubuntu@${{ secrets.INSTANCE_PUBLIC_IP }} '
         sudo usermod -aG docker $USER
         newgrp docker
         aws ecr get-login-password --region ${{ secrets.AWS_REGION }} | sudo docker login --username AWS --password-stdin ${{ secrets.AWS_ACCOUNT_ID }}.dkr.ecr.${{ secrets.AWS_REGION }}.amazonaws.com
         sudo docker pull ${{ secrets.AWS_ACCOUNT_ID }}.dkr.ecr.${{ secrets.AWS_REGION }}.amazonaws.com/ecrrepoo
         sudo docker run -d -p 8080:8080 ${{ secrets.AWS_ACCOUNT_ID }}.dkr.ecr.${{ secrets.AWS_REGION }}.amazonaws.com/ecrrepoo:${{ github.sha }}
         '

        #   Scans the SSH host (${{ secrets.INSTANCE_PUBLIC_IP }}) and appends it to the ~/.ssh/known_hosts file.
         #  Retrieves an authentication token from Amazon ECR to log in to Docker (aws ecr get-login-password).
         #Logs in to Docker using the obtained token.
         # Logs in to Docker using the obtained token.
          # SSH into the EC2 instance using the private key (~/.ssh/id_rsa) and runs a series of Docker-related commands:
         # Adds the user to the Docker group to run Docker commands without sudo.
         #  Switches to the new Docker group.
         # Logs in to Docker again inside the SSH session.
         # Pulls the Docker image from the specified ECR repository (ecrrepoo).
         # Runs the Docker container in detached mode (-d) on port 8080, mapping it to port 8080 on the host.
         # note: This workflow assumes that you have an EC2 instance running Docker, and the necessary AWS and Docker credentials are stored securely in GitHub Secrets.


