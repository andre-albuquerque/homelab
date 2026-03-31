def call (body) {
	def settings = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = settings
  body()
	
	container('alpine') {
    sh '''
			apk add git openssh-client

			GITEA_SSH_HOST="gitea.andrealbuquerque.me"
			REPO="andrealbuquerqueme/flux-cluster"
			APP_NAME="real-world-api"
			IMAGE_TAG="$(cat /artifacts/prod.artifact)"

			mkdir -p /root/.ssh
			ssh-keyscan -H gitea.andrealbuquerque.me >> /root/.ssh/known_hosts
			chmod 700 /root/.ssh
			chmod 600 /root/.ssh/known_hosts

			eval $(ssh-agent -s)
			chmod 600 $JENKINS_SSH_PRIVATE_KEY
			ssh-add $JENKINS_SSH_PRIVATE_KEY

			git clone -v git@${GITEA_SSH_HOST}:${REPO}.git
			cd flux-cluster

			sed -i "/image: .*${APP_NAME}:/ s|:[^[:space:]]*|:${IMAGE_TAG}|" \
					clusters/homelab/apps/real-world-api/deployment.yaml

			git config user.name "jenkins"
			git config user.email "jenkins@ci.local"
			git add .
			git commit -m "Deploy to production - build ${IMAGE_TAG}"
			git push origin main
    '''
	}
}