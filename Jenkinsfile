pipeline {
    agent any
    parameters {
        choice(
            choices: ['Build Only', 'Test Only', 'Build and Test'],
            description: 'Choose build option',
            name: 'BUILD_TEST_OPTION'
        )
    }

    switch (params.BUILD_TEST_OPTION) {
        case 'Build Only':
            steps {
                sh 'gradle build'
            }
        case 'Test Only':
            steps {
                sh 'gradle test'
            }
        case 'Build and Test':
            steps {
                sh 'gradle build'
                sh 'gradle test'
            }
    }
}
