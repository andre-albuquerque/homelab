def call (body) {
 
  def settings = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = settings
  body()
 
  container('go') {
    sh '''
      cd api
      go build ./...
      go test -v ./...
    '''
  }
 
}