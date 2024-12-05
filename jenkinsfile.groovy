pipeline {
    agent { label 'linux' }

    stages {
        stage('Pull WebGoat Docker Image') {
            steps {
                script {
                    sh 'docker pull webgoat/webgoat-8.0'
                }
            }
        }

        stage('Run WebGoat Docker Image') {
            steps {
                script {
                    sh 'docker run -d -p 8080:8080 webgoat/webgoat-8.0'
                }
            }
        }
    }
}
