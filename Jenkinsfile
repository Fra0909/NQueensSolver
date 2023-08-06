pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                // Build the N-Queens application using Gradle
                sh 'gradle build'
            }
        }

        stage('Test') {
            steps {
                // Run tests for the N-Queens application using Gradle
                sh 'gradle test'
            }
        }
    }
}
