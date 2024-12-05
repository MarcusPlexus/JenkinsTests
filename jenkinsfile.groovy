pipeline {
    agent { label 'linux' } // Ensure the correct agent is selected
    
    environment {
        DOCKER_IMAGE = 'webgoat/webgoat-8.0' // The Docker image for WebGoat
        GIT_REPO = 'https://github.com/WebGoat/WebGoat' // GitHub repository URL
        GIT_BRANCH = 'main' // Ensure this matches the correct branch name in WebGoat repo
    }

    tools {
        git 'Default' // Ensures that the correct Git installation is used
    }

    stages {
        stage('Checkout Code') {
            steps {
                // Pull the code from the WebGoat repository and ensure the correct branch is used
                checkout scm: [
                    $class: 'GitSCM', 
                    branches: [[name: "*/${GIT_BRANCH}"]], 
                    userRemoteConfigs: [[url: "${GIT_REPO}"]]
                ]
            }
        }

        stage('Build the Application') {
            steps {
                script {
                    // Build the application (Assuming Maven is being used)
                    sh 'mvn clean install -DskipTests' // Adjust if WebGoat uses another build tool
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image from the current Dockerfile in the repo
                    sh 'docker build -t webgoat/webgoat-8.0 .'
                }
            }
        }

        stage('Run WebGoat Docker Container') {
            steps {
                script {
                    // Run the WebGoat container and expose the necessary ports
                    sh 'docker run -d -p 8081:8080 webgoat/webgoat-8.0'
                }
            }
        }

        stage('Test WebGoat Accessibility') {
            steps {
                script {
                    // Check if WebGoat is running by sending a request to the exposed port
                    sh 'curl -s http://localhost:8081 || exit 1' // Option
