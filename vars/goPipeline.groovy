def call (body) {
 
  def settings = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = settings
  body()
 
  pipeline {
    agent {
      kubernetes {
        yamlFile libraryResource('jenkinsPod.yaml')
      }
    }
    stages {
      stage('Test') {
        steps {
          container('go') {
            sh '''
              echo $USER
            '''
        }
      }
    }
  }
}