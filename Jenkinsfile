pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'gradle build'
      }
    }

    stage('Test') {
      steps {
        sh 'gradle test'
      }
    }

  }
}