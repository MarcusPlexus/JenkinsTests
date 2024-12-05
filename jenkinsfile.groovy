pipeline {
    agent { label 'linux' } // Ensure you are using the correct agent
    
    environment {
        DOCKER_IMAGE = 'webgoat/webgoat-8.0' // Set Docker image name if you're using an existing image
        GIT_REPO = 'https://github.com/WebGoat/WebGoat'
    }
    
    stages {
        stage('Checkout') {
            steps {
                // Pull code from GitHub
                git url: "${GIT_REPO}"
            }
        }
        
        stage('Build') {
            steps {
                script {
                    // Build the project (adjust based on WebGoat's build tool)
                    sh 'mvn clean install -DskipTests'  // Example for Maven, change if needed
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    // Build and run the Docker container
                    sh """
                        docker build -t webgoat/webgoat-8.0 .
                        docker run -d -p 8081:8080 webgoat/webgoat-8.0
                    """
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
                    // Verify the app is running (optional, could be a curl to check health)
                    sh 'curl -s http://localhost:8081 || exit 1'  // Checking if the page is accessible
                }
            }
        }
    }
    
    post {
        always {
            // Clean up Docker containers if needed
            sh 'docker ps -q | xargs docker stop || true'
        }
    }
}
