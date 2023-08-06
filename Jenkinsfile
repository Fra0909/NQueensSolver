pipeline {
    agent any
    parameters {
        choice(
                choices: ['Build Only', 'Test Only', 'Build and Containerize'],
                description: 'Choose build option',
                name: 'BUILD_TEST_OPTION'
        )
        choice(
                choices: ['Yes', 'No'],
                description: 'Run the container after building (for "Build and Containerize" option)',
                name: 'RUN_CONTAINER'
        )
    }
    environment {
        GRADLE_HOME = 'C:/gradle/gradle-8.0.2'  // Replace this with the actual path to your Gradle installation directory
    }
    stages {
        stage('Build') {
            when {
                expression { params.BUILD_TEST_OPTION != 'Test Only' }
            }
            steps {
                script {
                    bat "${env.GRADLE_HOME}/bin/gradle build"
                }
            }
        }
        stage('Test') {
            when {
                expression { params.BUILD_TEST_OPTION != 'Build Only' }
            }
            steps {
                script {
                    bat "${env.GRADLE_HOME}/bin/gradle test"
                }
            }
        }
        stage('Containerize') {
            when {
                expression { params.BUILD_TEST_OPTION == 'Build and Containerize' }
            }
            steps {
                script {
                    bat 'docker build -t nqueens .'
                }
            }
        }
        stage('Run') {
            when {
                expression { params.RUN_CONTAINER == 'Yes' }
            }
            steps {
                script {
                    bat 'docker run -p 8500:8080 nqueens'
                }
            }
        }
    }
}
