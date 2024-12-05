pipeline {
    agent { label 'linux' }

    environment {
        DOCKER_IMAGE = 'webgoat/webgoat-8.0'
        GIT_REPO = 'https://github.com/WebGoat/WebGoat'
        GIT_BRANCH = 'main'
    }

    tools {
        git 'Default'
    }

    stages {
        stage('Checkout Code') {
            steps {
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
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t webgoat/webgoat-8.0 .'
                }
            }
        }

        stage('Run WebGoat Docker Container') {
            steps {
                script {
                    sh 'docker run -d -p 8081:8080 webgoat/webgoat-8.0'
                }
            }
        }

        stage('Test WebGoat Accessibility') {
            steps {
                script {
                    sh 'curl -s http://localhost:8081 || exit 1'
                }
            }
        }
    }


}
