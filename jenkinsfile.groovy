pipeline {
    agent { label 'linux' }

    stages {
        stage('Pull Hello World Docker Image') {
            steps {
                script {
                    sh 'docker pull hello-world'
                }
            }
        }

        stage('Run Hello World Docker Image') {
            steps {
                script {
                    sh 'docker run hello-world'
                }
            }
        }
    }
}
