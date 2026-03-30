def call (body) {
 
  def settings = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = settings
  body()

  def podYaml = libraryResource('jenkinsPod.yaml')
 
  pipeline {
    agent {
      kubernetes {
        yaml podYaml
      }
    }
    stages {
      stage('Unit test') {
        steps {
          goUnitTest{}
        }
        when{
          anyOf {
            branch pattern: 'main'
            branch pattern: 'master'
            branch pattern: 'hotfix-*'
          }
        }
      }
      stage ('Build and Push') {
        steps {
          kanikoBuildPush{}
        }
        when {
          anyOf {
            branch pattern: 'main'
            branch pattern: 'master'
          }
        }
      }
      stage ('Harbor Security Scan') {
        environment {
          HARBOR_CREDENTIALS = credentials('harbor-credentials')
        }
        steps {
          harborSecurityScan{}
        }
        when {
          anyOf {
            branch pattern: 'main'
            branch pattern: 'master'
          }
        }
      }
      stage('Deploy to Production') {
        steps {
          deployProd.groovy{}
        }
        when {
          anyOf {
            branch pattern: 'main'
            branch pattern: 'master'
          }
        }
      }
    }
  }
}