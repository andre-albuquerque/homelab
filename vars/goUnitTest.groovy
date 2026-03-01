def call (body) {
 
  def settings = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = settings
  body()
 
  container('go') {
    sh '''
      cd api
      go test ./... -buildvcs=false -p=1
    '''
  } 
}