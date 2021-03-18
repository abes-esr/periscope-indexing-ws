//this is the scripted method with groovy engine
/*
Ce script Jenkinsfile permet de compiler et de deployer votre projet
sur les environnements de DEV, TEST et PROD
Ce script se veut le plus generique possible et comporte 2 zones a editer pour votre projet
 */
import hudson.model.Result

node {

    /*
   Cette zone correspond a la definition de la structure de votre projet.
   Habituellement, les projets comportent 3 sous-module : core, web et batch
   mais seulement web et batch sont a deployer.
   Si votre projet ne contient pas un sous-module definit ci-dessous, laissez les valeurs par defaut.
    */

    // **** DEBUT DE ZONE A EDITER n°1 ****

    // Configuration du projet
    def gitURL = "https://github.com/abes-esr/periscope-indexing-ws.git"
    def gitCredentials = 'Github'
    def slackChannel = "#notif-periscope"
    def artifactoryBuildName = "periscope-indexing"
    def applicationFinalName = "periscope-indexing"
    def modulesNames = ["web", "batch"]

    // Definition du module web
    def backTargetDir = "/usr/local/tomcat9-periscope-indexing/webapps/"
    def backServiceName = "tomcat9-periscope-indexing.service"

    // Definition du module batch
    def batchTargetDir = "/home/batch/periscope"

    // **** FIN DE ZONE A EDITER n°1 ****

    // Variables de configuration d'execution
    def candidateModules = []
    def executeBuild = []
    def executeTests = false
    def buildNumber = -1
    def executeDeploy = []
    def backTargetHostnames = []
    def batchTargetHostnames = []

    // Variables globales
    def ENV
    def maventool
    def rtMaven
    def mavenProfil
    def artifactoryServer

    // Definition des actions
    def choiceParams = ['Compiler', 'Compiler & Déployer', 'Déployer depuis un précédent build']
    for (int moduleIndex = 0; moduleIndex < modulesNames.size(); moduleIndex++) { //Pour chaque module du projet
        choiceParams.add("[${modulesNames[moduleIndex]}] Compiler le module")
        choiceParams.add("[${modulesNames[moduleIndex]}] Compiler & Déployer le module")
        choiceParams.add("[${modulesNames[moduleIndex]}] Déployer le module depuis un précédent build")
    }

    currentBuild.description = " Retrouver lemy new description"

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
                    choice(choices: choiceParams.join('\n'), description: 'Que voulez-vous faire ?', name: 'ACTION'),
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
                    stringParam(defaultValue: '', description: "Numéro du build à déployer. Retrouvez vos précédents builds sur https://artifactory.abes.fr/artifactory/api/build/${artifactoryBuildName}", name: 'BUILD_NUMBER'),
                    booleanParam(defaultValue: false, description: 'Voulez-vous exécuter les tests ?', name: 'executeTests'),
                    choice(choices: ['DEV', 'TEST', 'PROD'], description: 'Sélectionner l\'environnement cible', name: 'ENV')
            ])
    ])

    //-------------------------------
    // Etape 1 : Configuration
    //-------------------------------
    stage('Set environnement variables') {
        try {
            // Java
            env.JAVA_HOME = "${tool 'Open JDK 11'}"
            env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"

            // Maven & Artifactory
            maventool = tool 'Maven 3.3.9'
            rtMaven = Artifactory.newMavenBuild()
            artifactoryServer = Artifactory.server '-1137809952@1458918089773'
            rtMaven.tool = 'Maven 3.3.9'

            // Action a faire
            if (params.ACTION == null) {
                throw new Exception("Variable ACTION is null")
            }

            for (int moduleIndex = 0; moduleIndex < modulesNames.size(); moduleIndex++) { //Pour chaque module du projet

                if (params.ACTION == 'Compiler') {
                    candidateModules.add("${modulesNames[moduleIndex]}")
                    executeBuild.add(true)
                    executeDeploy.add(false)
                } else if (params.ACTION == 'Compiler & Déployer') {
                    candidateModules.add("${modulesNames[moduleIndex]}")
                    executeBuild.add(true)
                    executeDeploy.add(true)
                } else if (params.ACTION == "[${modulesNames[moduleIndex]}] Compiler & Déployer le module") {
                    candidateModules.add("${modulesNames[moduleIndex]}")
                    executeBuild.add(true)
                    executeDeploy.add(true)
                } else if (params.ACTION == "[${modulesNames[moduleIndex]}] Compiler le module") {
                    candidateModules.add("${modulesNames[moduleIndex]}")
                    executeBuild.add(true)
                    executeDeploy.add(false)
                } else if (params.ACTION == "Déployer depuis un précédent build" || params.ACTION == "[${modulesNames[moduleIndex]}] Déployer le module depuis un précédent build") {

                    if (params.BUILD_NUMBER == null || params.BUILD_NUMBER == -1) {
                        throw new Exception("No build number specified")
                    }
                    // On verifie si le build exists

                    candidateModules.add("${modulesNames[moduleIndex]}")
                    executeBuild.add(false)
                    executeDeploy.add(true)
                    buildNumber = params.buildNumber
                }
            }

            if (candidateModules.size() == 0) {
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
                mavenProfil = "dev"
                backTargetHostnames.add('hostname.server-back-1-dev')
                backTargetHostnames.add('hostname.server-back-2-dev')

                batchTargetHostnames.add('hostname.server-batch-1-dev')

            } else if (ENV == 'TEST') {
                mavenProfil = "test"
                backTargetHostnames.add('hostname.server-back-1-test')
                backTargetHostnames.add('hostname.server-back-2-test')

                batchTargetHostnames.add('hostname.server-batch-1-test')

            } else if (ENV == 'PROD') {
                mavenProfil = "prod"
                backTargetHostnames.add('hostname.server-back-1-prod')
                backTargetHostnames.add('hostname.server-back-2-prod')

                batchTargetHostnames.add('hostname.server-batch-1-prod')
            }

        } catch (e) {
            currentBuild.result = hudson.model.Result.NOT_BUILT.toString()
            notifySlack(slackChannel, "Failed to set environnement variables: " + e.getLocalizedMessage())
            throw e
        }
    }

    if (buildNumber == -1) {

        //-------------------------------
        // Etape 2 : Recuperation du code
        //-------------------------------
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
                notifySlack(slackChannel, "Failed to fetch SCM: " + e.getLocalizedMessage())
                throw e
            }
        }
    }

    for (int moduleIndex = 0; moduleIndex < candidateModules.size(); moduleIndex++) { //Pour chaque module du projet

        //-------------------------------
        // Etape 3 : Compilation
        //-------------------------------
        if ("${executeBuild[moduleIndex]}" == 'true') {

            //-------------------------------
            // Etape 3.1 : Edition des fichiers de proprietes
            //-------------------------------
            stage("[${candidateModules[moduleIndex]}] Edit properties files") {
                try {
                    echo "Edition application-${mavenProfil}.properties"
                    echo "--------------------------"

                    original = readFile "${candidateModules[moduleIndex]}/src/main/resources/application-${mavenProfil}.properties"
                    newconfig = original

                    /*
                   Cette zone correspond à l'edition des fichiers de proprietes.
                   C'est ici que l'on insere les donnees sensibles dans les fichiers de proprietes (application.properties)
                   Les donnees sensibles sont definit comme des Credentials Jenkins de type Secret Text.
                   A vous d'ajouter dans Jenkins vos credentials de donnees sensensibles et de les remplacer
                    */

                    // **** DEBUT DE ZONE A EDITER n°2 ****

                    // Module web
                    if ("${candidateModules[moduleIndex]}" == 'web') {
                        withCredentials([
                                string(credentialsId: "periscope.solr-${mavenProfil}", variable: 'url')
                        ]) {
                            newconfig = newconfig.replaceAll("solr.baseurl=*", "solr.baseurl=${url}")
                        }
                    }

                    // Module batch
                    if ("${candidateModules[moduleIndex]}" == 'batch') {
                        withCredentials([
                                string(credentialsId: "periscope.solr-${mavenProfil}", variable: 'url')
                        ]) {
                            newconfig = newconfig.replaceAll("solr.baseurl=*", "solr.baseurl=${url}")
                        }
                    }

                    // **** FIN DE ZONE A EDITER n°2 ****

                    writeFile file: "${candidateModules[moduleIndex]}/src/main/resources/application-${mavenProfil}.properties", text: "${newconfig}"

                } catch (e) {
                    currentBuild.result = hudson.model.Result.FAILURE.toString()
                    notifySlack(slackChannel, "Failed to edit module ${candidateModules[moduleIndex]} properties files: "+e.getLocalizedMessage())
                    throw e
                }
            }

            //-------------------------------
            // Etape 3.2 : Compilation
            //-------------------------------
            stage("[${candidateModules[moduleIndex]}] Compile package") {
                try {
                    sh "'${maventool}/bin/mvn' -Dmaven.test.skip='${!executeTests}' clean package  -pl ${candidateModules[moduleIndex]} -am -P${mavenProfil} -DfinalName='${applicationFinalName}' -DwebBaseDir='${backTargetDir}${applicationFinalName}' -DbatchBaseDir='${batchTargetDir}${applicationFinalName}'"

                } catch (e) {
                    currentBuild.result = hudson.model.Result.FAILURE.toString()
                    notifySlack(slackChannel, "Failed to build module ${candidateModules[moduleIndex]}: "+e.getLocalizedMessage())
                    throw e
                }
            }
        }

        //-------------------------------
        // Etape 3.4 : Archive to Artifactory
        //-------------------------------
        if ("${executeBuild[moduleIndex]}" == 'true') {
            stage("[${candidateModules[moduleIndex]}] Archive to Artifactory") {
                try {
                    rtMaven.deployer server: artifactoryServer, releaseRepo: 'libs-release-local', snapshotRepo: 'libs-snapshot-local'
                    rtMaven.opts = "-Xms1024m -Xmx4096m -Dmaven.test.skip=true -Dspring.profiles.activero   r   =${mavenProfil} -DfinalName='${applicationFinalName}' -DwebBaseDir='${backTargetDir}${applicationFinalName}' -DbatchBaseDir='${batchTargetDir}${applicationFinalName}'"

                    // On deploie
                    buildInfo = Artifactory.newBuildInfo()
                    buildInfo = rtMaven.run pom: 'pom.xml', goals: "-U clean install"
                    buildInfo.name = artifactoryBuildName
                    rtMaven.deployer.deployArtifacts buildInfo

                    // On publie
                    buildInfo = rtMaven.run pom: 'pom.xml', goals: "clean install -Dmaven.repo.local=.m2"
                    buildInfo.name = artifactoryBuildName
                    buildInfo.env.capture = true
                    artifactoryServer.publishBuildInfo buildInfo

                } catch (e) {
                    currentBuild.result = hudson.model.Result.FAILURE.toString()
                    notifySlack(slackChannel, "Failed to deploy and publish module ${candidateModules[moduleIndex]} to Artifactory: " + e.getLocalizedMessage())
                    throw e
                }
            }
        }

        //-------------------------------
        // Etape 4 : Deploiement
        //-------------------------------
        if ("${executeDeploy[moduleIndex]}" == 'true') {

            if(buildNumber != -1) {

                sh("ls -l")

                if ("${candidateModules[moduleIndex]}" == 'web') {

                    def downloadSpec = """{                    
                     "files": [
                      {   
                          "build": "${artifactoryBuildName}/${buildNumber}",
                          "pattern": "*.war",
                          "target": "${candidateModules[moduleIndex]}/target/${applicationFinalName}.war",
                          "flat" : false
                        }
                     ]
                    }"""
                    artifactoryServer.download spec: downloadSpec
                    sh("tree")
                }

                if ("${candidateModules[moduleIndex]}" == 'batch') {

                    def downloadSpec = """{                     
                     "files": [
                      {
                          "build": "${artifactoryBuildName}/${buildNumber}",
                          "pattern": "*.jar",
                          "target": "${candidateModules[moduleIndex]}/target/${applicationFinalName}.jar",
                          "flat" : false
                        }
                     ]
                    }"""
                    artifactoryServer.download spec: downloadSpec
                    sh("tree")
                }
            }

            //-------------------------------
            // Etape 4.1 : Serveur Web
            //-------------------------------
            if ("${candidateModules[moduleIndex]}" == 'web') {

                stage("Deploy to web servers") {

                    for (int i = 0; i < backTargetHostnames.size(); i++) { //Pour chaque serveur
                        withCredentials([
                                usernamePassword(credentialsId: 'tomcatuser', passwordVariable: 'pass', usernameVariable: 'username'),
                                string(credentialsId: "${backTargetHostnames[i]}", variable: 'hostname'),
                                string(credentialsId: 'service.status', variable: 'status'),
                                string(credentialsId: 'service.stop', variable: 'stop'),
                                string(credentialsId: 'service.start', variable: 'start')
                        ]) {

                            echo "Stop service on ${backTargetHostnames[i]}"
                            echo "--------------------------"

                            try {

                                try {
                                    echo 'get service status'
                                    sh "ssh -tt ${username}@${hostname} \"${status} ${backServiceName}\""

                                    echo 'stop the service'
                                    sh "ssh -tt ${username}@${hostname} \"${stop} ${backServiceName}\""

                                } catch (e) {
                                    // Maybe the tomcat is not running
                                    echo 'maybe the service is not running'

                                    echo 'we try to start the service'
                                    sh "ssh -tt ${username}@${hostname} \"${start} ${backServiceName}\""

                                    echo 'get service status'
                                    sh "ssh -tt ${username}@${hostname} \"${status} ${backServiceName}\""

                                    echo 'stop the service'
                                    sh "ssh -tt ${username}@${hostname} \"${stop} ${backServiceName}\""
                                }

                            } catch (e) {
                                currentBuild.result = hudson.model.Result.FAILURE.toString()
                                notifySlack(slackChannel, "Failed to stop the web service on ${backTargetHostnames[i]} :" + e.getLocalizedMessage())
                                throw e
                            }

                            echo "Deploy to ${backTargetHostnames[i]}"
                            echo "--------------------------"

                            try {
                                sh "ssh -tt ${username}@${hostname} \"rm -rf ${backTargetDir}${applicationFinalName} ${backTargetDir}${applicationFinalName}.war\""
                                sh "scp ${candidateModules[moduleIndex]}/target/${applicationFinalName}.war ${username}@${hostname}:${backTargetDir}"

                            } catch (e) {
                                currentBuild.result = hudson.model.Result.FAILURE.toString()
                                notifySlack(slackChannel, "Failed to deploy the webapp to ${backTargetHostnames[i]} :" + e.getLocalizedMessage())
                                throw e
                            }

                            echo "Restart service on ${backTargetHostnames[i]}"
                            echo "--------------------------"

                            try {
                                echo 'start service'
                                sh "ssh -tt ${username}@${hostname} \"${start} ${backServiceName}\""

                                echo 'get service status'
                                sh "ssh -tt ${username}@${hostname} \"${status} ${backServiceName}\""

                            } catch (e) {
                                currentBuild.result = hudson.model.Result.FAILURE.toString()
                                notifySlack(slackChannel, "Failed to restrat the web service on ${backTargetHostnames[i]} :" + e.getLocalizedMessage())
                                throw e
                            }
                        }

                    }//Pour chaque serveur
                }
            }

            //-------------------------------
            // Etape 4.2 : Serveur Batch
            //-------------------------------
            if ("${candidateModules[moduleIndex]}" == 'batch') {

                stage("Deploy to batch servers") {
                    for (int i = 0; i < batchTargetHostnames.size(); i++) { //Pour chaque serveur
                        withCredentials([
                                usernamePassword(credentialsId: 'batchuserpass', passwordVariable: 'pass', usernameVariable: 'username'),
                                string(credentialsId: "${batchTargetHostnames[i]}", variable: 'hostname')
                        ]) {
                            try {
                                echo "Deploy to ${batchTargetHostnames[i]}"
                                echo "--------------------------"

                                sh "ssh -tt ${username}@${hostname} \"rm -rf ${batchTargetDir}${applicationFinalName}.jar\""
                                sh "scp ${candidateModules[moduleIndex]}/target/${applicationFinalName}.jar ${username}@${hostname}:${batchTargetDir}"

                            } catch (e) {
                                currentBuild.result = hudson.model.Result.FAILURE.toString()
                                notifySlack(slackChannel, "Failed to deploy batch on ${batchTargetHostnames[i]} :" + e.getLocalizedMessage())
                                throw e
                            }
                        }
                    }
                }
            }
        }
    } //Pour chaque module du projet

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