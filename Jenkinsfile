@Library('automata@improv-pipeline') _
automata {
  // Definições de Variáveis
  def version = '1.0.0'
  def builderImage = 'registry-ctn.prevnet/quarkus/ubi9-quarkus-mandrel-builder-image:jdk-21'
  def runtimeImage = 'registry-ctn.prevnet/quarkus/ubi9-quarkus-micro-image:2.0'

  // Parâmetros gerais
  type = 'CUSTOM'
  descriptor = 'pom.xml'
  skipHom = true
  notifications.add type: 'mail', condition: 'unstable, failure', to: 'danielgomes@dataprev.gov.br'

  // Parâmetros do Build
  build.script = {
    docker.image(builderImage).inside("--entrypoint=''") {
      writeFile file: '.m2/settings.xml', text: '''<settings>
  <mirrors>
    <mirror>
      <id>prevnet-mirror</id>
      <url>http://www-bin.prevnet/content/groups/public</url>
      <mirrorOf>*</mirrorOf>
    </mirror>
  </mirrors>
</settings>'''
      sh '''
        chmod +x ./mvnw
        export HOME="$PWD"
        export MAVEN_USER_HOME="$PWD/.m2"
        ./mvnw --batch-mode -U -s .m2/settings.xml -Dmaven.repo.local=.m2/repository -Dmaven.test.failure.ignore -Dnative org.jacoco:jacoco-maven-plugin:prepare-agent verify org.jacoco:jacoco-maven-plugin:report
      '''
    }
  }

  // Parâmetros de Qualidade/SonarQube
  qa.sonarOpts = "-Dsonar.projectKey=br.gov.dataprev.autobot:gateway -Dsonar.projectVersion=${version} -Dsonar.projectName='Daes Quarkus' -Dsonar.sources=src/main,pom.xml -Dsonar.tests=src/test " +
                 "-Dsonar.java.binaries=target/classes -Dsonar.java.test.binaries=target/test-classes -Dsonar.junit.reportPaths=target/surefire-reports"
  qa.encoding = 'UTF-8'

  // Containers
  containers.add descriptor: 'Dockerfile',
                 imageName: 'daes/daes-quarkus',
                 buildArgs: "--build-arg RUNTIME_IMAGE=${runtimeImage}"

  // Parâmetros de GitOps
  //gitOps.provider = 'GIT'
  //gitOps.namespace = 'daes-quarkus'
  //gitOps.repos = [dev: 'gitops-np/daes-quarkus', prd: 'gitops-p/daes-quarkus']
}
