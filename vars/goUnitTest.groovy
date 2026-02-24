def call (body) {
 
  def settings = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = settings
  body()
 
  container('go') {
    sh '''
      go build ./api/...
      go test ./api/... -cover
    '''
  }
 
}