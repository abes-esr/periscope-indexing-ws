//this is the scripted method with groovy engine
import hudson.model.Result

node {

    // Configuration du projet
    def gitURL = "https://github.com/abes-esr/periscope-indexing-ws.git"
    def gitCredentials = 'Github'
    def slackChannel = "#notif-periscope"
    def appFinalName = "periscope-indexing"
    def executeTests = false

    // Definition des modules du projet
    def projectModules = ["web","batch"]
    def executeBuild = []
    def executeDeploy = []

    // Definition du module web
    def webModuleDir = "web/"
    def webTargetHostnames = []
    def webTargetDir = "/usr/local/tomcat9-periscope-indexing/webapps/"
    def webServiceName = "tomcat9-periscope-indexing.service"

    // Definition du module batch
    def batchModuleDir = "batch/"
    def batchTargetHostnames = []
    def batchTargetDir = "/usr/local/tomcat9-periscope-indexing/webapps/"

    // Variables globales
    def maventool
    def rtMaven
    def server
    def ENV
    def profil

    // Configuration du job Jenkins
    // On garde les 5 derniers builds par branche
    // On scanne les branches et les tags du Git
    properties([
            buildDiscarder(
                    logRotator(
                            artifactDaysToKeepStr: '',
                            artifactNumToKeepStr: '',
                            daysToKeepStr: '',
                            numToKeepStr: '5')
            ),
            parameters([
                    choice(choices: ['Compiler', 'Compiler & Deployer', 'Compiler & Deployer : Web', 'Compiler & Deployer : Batch'], description: 'Que voulez-vous faire ?', name: 'ACTION'),
                    gitParameter(
                            branch: '',
                            branchFilter: 'origin/(.*)',
                            defaultValue: 'develop',
                            description: 'Sélectionner la branche ou le tag',
                            name: 'BRANCH_TAG',
                            quickFilterEnabled: false,
                            selectedValue: 'NONE',
                            sortMode: 'DESCENDING_SMART',
                            tagFilter: '*',
                            type: 'PT_BRANCH_TAG'),
                    booleanParam(defaultValue: false, description: 'Voulez-vous exécuter les tests ?', name: 'executeTests'),
                    choice(choices: ['DEV', 'TEST', 'PROD'], description: 'Sélectionner l\'environnement cible', name: 'ENV')
            ])
    ])

    stage('Set environnement variables') {
        try {
            // Java
            env.JAVA_HOME = "${tool 'Open JDK 11'}"
            env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"

            // Maven
            maventool = tool 'Maven 3.3.9'
            rtMaven = Artifactory.newMavenBuild()
            server = Artifactory.server '-1137809952@1458918089773'
            rtMaven.tool = 'Maven 3.3.9'
            rtMaven.opts = '-Xms1024m -Xmx4096m'

            // Action a faire
            if (params.ACTION == null) {
                executeBuild.add(false) // build module web
                executeDeploy.add(false) // deploy module web

                executeBuild.add(false) // build module batch
                executeDeploy.add(false) // deploy module batch

            } else if (params.ACTION == 'Compiler') {
                executeBuild.add(true) // build module web
                executeDeploy.add(false) // deploy module web

                executeBuild.add(true) // build module batch
                executeDeploy.add(false) // deploy module batch

            } else if (params.ACTION == 'Compiler & Deployer') {
                executeBuild.add(true) // build module web
                executeDeploy.add(true) // deploy module web

                executeBuild.add(true) // build module batch
                executeDeploy.add(true) // deploy module batch

            } else if (params.ACTION == 'Compiler & Deployer : Web') {
                executeBuild.add(true) // build module web
                executeDeploy.add(true) // deploy module web

                executeBuild.add(false) // build module batch
                executeDeploy.add(false) // deploy module batch

            } else if (params.ACTION == 'Compiler & Deployer : Batch') {
                executeBuild.add(false) // build module web
                executeDeploy.add(false) // deploy module web

                executeBuild.add(true) // build module batch
                executeDeploy.add(true) // deploy module batch

            } else {
                throw new Exception("Unable to decode variable ACTION")
            }

            // Branche a deployer
            if (params.BRANCH_TAG == null) {
                throw new Exception("Variable BRANCH_TAG is null")
            } else {
                echo "Branch to deploy =  ${params.BRANCH_TAG}"
            }

            // Booleen d'execution des tests
            if (params.executeTests == null) {
                executeTests = false
            } else {
                executeTests = params.executeTests
            }
            echo "executeTests =  ${executeTests}"

            // Environnement de deploiement
            if (params.ENV == null) {
                throw new Exception("Variable ENV is null")
            } else {
                ENV = params.ENV
                echo "Target environnement =  ${ENV}"
            }

            if (ENV == 'DEV') {
                profil = "dev"
                webTargetHostnames.add('hostname.server1-dev')
                webTargetHostnames.add('hostname.server2-dev')

                batchTargetHostnames.add('hostname.server1-dev')
                batchTargetHostnames.add('hostname.server2-dev')

            } else if (ENV == 'TEST') {
                profil = "test"
                webTargetHostnames.add('hostname.server1-test')
                webTargetHostnames.add('hostname.server2-test')

                batchTargetHostnames.add('hostname.server1-test')
                batchTargetHostnames.add('hostname.server2-test')

            } else if (ENV == 'PROD') {
                profil = "prod"
                webTargetHostnames.add('hostname.server1-prod')
                webTargetHostnames.add('hostname.server2-prod')

                batchTargetHostnames.add('hostname.server1-prod')
                batchTargetHostnames.add('hostname.server2-prod')
            }

        } catch (e) {
            currentBuild.result = hudson.model.Result.NOT_BUILT.toString()
            notifySlack(slackChannel,e.getLocalizedMessage())
            throw e
        }
    }

    stage('SCM checkout') {
        try {
            checkout([
                    $class                           : 'GitSCM',
                    branches                         : [[name: "${params.BRANCH_TAG}"]],
                    doGenerateSubmoduleConfigurations: false,
                    extensions                       : [],
                    submoduleCfg                     : [],
                    userRemoteConfigs                : [[credentialsId: "${gitCredentials}", url: "${gitURL}"]]
            ])

        } catch (e) {
            currentBuild.result = hudson.model.Result.FAILURE.toString()
            notifySlack(slackChannel,e.getLocalizedMessage())
            throw e
        }
    }

    for (int moduleIndex = 0; moduleIndex < projectModules.size(); moduleIndex++) { //Pour chaque module du projet

        //-------------------------------
        // Build
        //-------------------------------
        if ("${executeBuild[moduleIndex]}" == 'true') {

            stage("Edit properties files") {
                try {
                    withCredentials([
                            string(credentialsId: "periscope.solr-${profil}", variable: 'url')
                    ]) {
                        echo "Edition application-${profil}.properties"
                        echo "--------------------------"

                        original = readFile "${projectModules[moduleIndex]}/src/main/resources/application-${profil}.properties"
                        newconfig = original

                        newconfig = newconfig.replaceAll("solr.baseurl=*", "solr.baseurl=${url}")

                        writeFile file: "${projectModules[moduleIndex]}/src/main/resources/application-${profil}.properties", text: "${newconfig}"
                    }

                } catch (e) {
                    currentBuild.result = hudson.model.Result.FAILURE.toString()
                    notifySlack(slackChannel, e.getLocalizedMessage())
                    throw e
                }
            }

            stage("Compile package") {
                try {
                    sh "'${maventool}/bin/mvn' -Dmaven.test.skip='${!executeTests}' clean package  -pl ${projectModules[moduleIndex]} -am -P${profil} -DfinalName='${appFinalName}' -DwebBaseDir='${webTargetDir}${appFinalName}' -DbatchBaseDir='${batchTargetDir}${appFinalName}'"

                } catch (e) {
                    currentBuild.result = hudson.model.Result.FAILURE.toString()
                    notifySlack(slackChannel, e.getLocalizedMessage())
                    throw e
                }
            }

            if ("${projectModules[moduleIndex]}" == 'web') {

                stage("artifact") {
                    try {
                        archive "${warDir}${appFinalName}.war"

                    } catch (e) {
                        currentBuild.result = hudson.model.Result.FAILURE.toString()
                        notifySlack(slackChannel, e.getLocalizedMessage())
                        throw e
                    }
                }
            }
        }

        //-------------------------------
        // Deploy
        //-------------------------------
        if ("${executeDeploy[moduleIndex]}" == 'true') {

            //**************
            // on web servers
            if ("${projectModules[moduleIndex]}" == 'web') {

                stage("Deploy to web servers") {

                    for (int i = 0; i < webTargetHostnames.size(); i++) { //Pour chaque serveur
                        try {
                            withCredentials([
                                    usernamePassword(credentialsId: 'tomcatuser', passwordVariable: 'pass', usernameVariable: 'username'),
                                    string(credentialsId: "${webTargetHostnames[i]}", variable: 'hostname'),
                                    string(credentialsId: 'service.status', variable: 'status'),
                                    string(credentialsId: 'service.stop', variable: 'stop'),
                                    string(credentialsId: 'service.start', variable: 'start')
                            ]) {
                                echo "Stop service on ${webTargetHostnames[i]}"
                                echo "--------------------------"

                                try {
                                    sh "ssh -v -tt ${username}@${hostname} \"whoami\""

                                    echo 'get service status'
                                    sh "ssh -tt ${username}@${hostname} \"${status} ${webServiceName}\""

                                    echo 'stop the service'
                                    sh "ssh -tt ${username}@${hostname} \"${stop} ${webServiceName}\""

                                } catch (e) {
                                    // Maybe the tomcat is not running
                                    echo 'maybe the service is not running'

                                    echo 'we try to start the service'
                                    sh "ssh -tt ${username}@${hostname} \"${start} ${webServiceName}\""

                                    echo 'get service status'
                                    sh "ssh -tt ${username}@${hostname} \"${status} ${webServiceName}\""

                                    echo 'stop the service'
                                    sh "ssh -tt ${username}@${hostname} \"${stop} ${webServiceName}\""
                                }

                            }
                        } catch (e) {
                            currentBuild.result = hudson.model.Result.FAILURE.toString()
                            notifySlack(slackChannel, "Failed to stop the web service on ${webTargetHostnames[i]} :" +e.getLocalizedMessage())
                            throw e
                        }

                        try {
                            withCredentials([
                                    usernamePassword(credentialsId: 'tomcatuser', passwordVariable: 'pass', usernameVariable: 'username'),
                                    string(credentialsId: "${webTargetHostnames[i]}", variable: 'hostname')
                            ]) {
                                echo "Deploy to ${webTargetHostnames[i]}"
                                echo "--------------------------"

                                sh "ssh -tt ${username}@${hostname} \"rm -rf ${webTargetDir}${appFinalName} ${webTargetDir}${appFinalName}.war\""
                                sh "scp ${webModuleDir}target/${appFinalName}.war ${username}@${hostname}:${webTargetDir}"
                            }
                        } catch (e) {
                            currentBuild.result = hudson.model.Result.FAILURE.toString()
                            notifySlack(slackChannel, "Failed to deploy the webapp to ${webTargetHostnames[i]} :" +e.getLocalizedMessage())
                            throw e
                        }

                        try {
                            withCredentials([
                                    usernamePassword(credentialsId: 'tomcatuser', passwordVariable: 'pass', usernameVariable: 'username'),
                                    string(credentialsId: "${webTargetHostnames[i]}", variable: 'hostname'),
                                    string(credentialsId: 'service.status', variable: 'status'),
                                    string(credentialsId: 'service.start', variable: 'start')
                            ]) {
                                echo "Restart service on ${webTargetHostnames[i]}"
                                echo "--------------------------"

                                echo 'start service'
                                sh "ssh -tt ${username}@${hostname} \"${start} ${webServiceName}\""

                                echo 'get service status'
                                sh "ssh -tt ${username}@${hostname} \"${status} ${webServiceName}\""
                            }

                        } catch (e) {
                            currentBuild.result = hudson.model.Result.FAILURE.toString()
                            notifySlack(slackChannel, "Failed to restrat the web service on ${webTargetHostnames[i]} :" +e.getLocalizedMessage())
                            throw e
                        }

                    }//Pour chaque serveur
                }
            }

            //********
            // Batch
            //********
            if ("${projectModules[moduleIndex]}" == 'batch') {

                stage("Deploy to batch servers") {
                    for (int i = 0; i < batchTargetHostnames.size(); i++) { //Pour chaque serveur
                        try {
                            withCredentials([
                                    usernamePassword(credentialsId: 'tomcatuser', passwordVariable: 'pass', usernameVariable: 'username'),
                                    string(credentialsId: "${batchTargetHostnames[i]}", variable: 'hostname')
                            ]) {
                                echo "Deploy to ${batchTargetHostnames[i]}"
                                echo "--------------------------"

                                //sh "ssh -tt ${username}@${hostname} \"rm -rf ${webTargetDir}${appFinalName} ${webTargetDir}${appFinalName}.jar\""
                                //sh "scp ${batchModuleDir}target/${appFinalName}.jar ${username}@${hostname}:${webTargetDir}"
                            }

                        } catch (e) {
                            currentBuild.result = hudson.model.Result.FAILURE.toString()
                            notifySlack(slackChannel, e.getLocalizedMessage())
                            throw e
                        }
                    }
                }
            }
        }
    } //Pour chaque module du projet

    /*stage ('Artifactory configuration') {
        try {
            rtMaven.deployer server: server, releaseRepo: 'libs-release-local', snapshotRepo: 'libs-snapshot-local'
            buildInfo = Artifactory.newBuildInfo()
            buildInfo = rtMaven.run pom: 'pom.xml', goals: '-U clean install -Dmaven.test.skip=true '

            rtMaven.deployer.deployArtifacts buildInfo
            buildInfo = rtMaven.run pom: 'pom.xml', goals: 'clean install -Dmaven.repo.local=.m2 -Dmaven.test.skip=true'
            buildInfo.env.capture = true
            server.publishBuildInfo buildInfo

        } catch(e) {
            currentBuild.result = hudson.model.Result.FAILURE.toString()
            notifySlack(slackChannel,e.getLocalizedMessage())
            throw e
        }
    }*/

    currentBuild.result = hudson.model.Result.SUCCESS.toString()
    notifySlack(slackChannel,"Congratulation !")
}

def notifySlack(String slackChannel, String info = '') {
    def colorCode = '#848484' // Gray

    switch (currentBuild.result) {
        case 'NOT_BUILT':
            colorCode = '#FFA500' // Orange
            break
        case 'SUCCESS':
            colorCode = '#00FF00' // Green
            break
        case 'UNSTABLE':
            colorCode = '#FFFF00' // Yellow
            break
        case 'FAILURE':
            colorCode = '#FF0000' // Red
            break;
    }

    String message = """
        *Jenkins Build*
        Job name: `${env.JOB_NAME}`
        Build number: `#${env.BUILD_NUMBER}`
        Build status: `${currentBuild.result}`
        Branch or tag: `${params.BRANCH_TAG}`
        Target environment: `${params.ENV}`
        Message: `${info}`
        Build details: <${env.BUILD_URL}/console|See in web console>
    """.stripIndent()

    return slackSend(tokenCredentialId: "slack_token",
            channel: "${slackChannel}",
            color: colorCode,
            message: message)
}