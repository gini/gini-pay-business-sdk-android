#!/usr/bin/env groovy
pipeline {
    agent any
    environment {
        NEXUS_MAVEN = credentials('external-nexus-maven-repo-credentials')
        GIT = credentials('github')
        JAVA11 = '/Library/Java/JavaVirtualMachines/temurin-11.jdk/Contents/Home'
    }
    stages {
        stage('Import Pipeline Libraries') {
            steps{
                library 'android-tools'
            }
        }
        stage('Build') {
            when {
                anyOf {
                    not {
                        branch 'main'
                    }
                    allOf {
                        branch 'main'
                        expression {
                            def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                            return status == 0
                        }
                    }
                }
            }
            steps {
                sh './gradlew clean ginipaybusiness:assembleDebug ginipaybusiness:assembleRelease -Dorg.gradle.java.home=$JAVA11'
            }
        }
        stage('Unit Tests') {
            when {
                anyOf {
                    not {
                        branch 'main'
                    }
                    allOf {
                        branch 'main'
                        expression {
                            def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                            return status == 0
                        }
                    }
                }
            }
            steps {
                sh './gradlew ginipaybusiness:testDebugUnitTest -Dorg.gradle.java.home=$JAVA11'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'ginipaybusiness/build/outputs/test-results/testDebugUnitTest/*.xml'
                    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginipaybusiness/build/reports/tests/testDebugUnitTest', reportFiles: 'index.html', reportName: 'Unit Test Results', reportTitles: ''])
                }
            }
        }
        stage('Code Analysis') {
            when {
                anyOf {
                    not {
                        branch 'main'
                    }
                    allOf {
                        branch 'main'
                        expression {
                            def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                            return status == 0
                        }
                    }
                }
            }
            steps {
                sh './gradlew ginipaybusiness:lint ginipaybusiness:checkstyle ginipaybusiness:pmd -Dorg.gradle.java.home=$JAVA11'
                androidLint canComputeNew: false, defaultEncoding: '', healthy: '', pattern: 'ginipaybusiness/build/reports/lint-results.xml', unHealthy: ''
                checkstyle canComputeNew: false, defaultEncoding: '', healthy: '', pattern: 'ginipaybusiness/build/reports/checkstyle/checkstyle.xml', unHealthy: ''
                pmd canComputeNew: false, defaultEncoding: '', healthy: '', pattern: 'ginipaybusiness/build/reports/pmd/pmd.xml', unHealthy: ''
            }
        }
        stage('Build Documentation') {
            when {
                anyOf {
                    not {
                        branch 'main'
                    }
                    allOf {
                        branch 'main'
                        expression {
                            def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                            return status == 0
                        }
                    }
                }
            }
            steps {
                withEnv(["PATH+=/usr/local/bin"]) {
                    sh 'scripts/build-sphinx-doc.sh'
                }
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginipaybusiness/src/doc/build/html', reportFiles: 'index.html', reportName: 'Documentation', reportTitles: ''])
            }
        }
        stage('Generate Dokka') {
            when {
                anyOf {
                    not {
                        branch 'main'
                    }
                    allOf {
                        branch 'main'
                        expression {
                            def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                            return status == 0
                        }
                    }
                }
            }
            steps {
                sh './gradlew ginipaybusiness:dokkaHtml -Dorg.gradle.java.home=$JAVA11'
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginipaybusiness/build/dokka/ginipaybusiness', reportFiles: 'index.html', reportName: 'Gini Pay Bank KDoc', reportTitles: ''])
            }
        }
        stage('Archive Artifacts') {
            when {
                anyOf {
                    not {
                        branch 'main'
                    }
                    allOf {
                        branch 'main'
                        expression {
                            def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                            return status == 0
                        }
                    }
                }
            }
            steps {
                archiveArtifacts 'ginipaybusiness/build/outputs/aar/*.aar'
            }
        }
        stage('Build Example App') {
            when {
                anyOf {
                    not {
                        branch 'main'
                    }
                    allOf {
                        branch 'main'
                        expression {
                            def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                            return status == 0
                        }
                    }
                }
            }
            steps {
                sh './gradlew app:clean app:assembleDebug -Dorg.gradle.java.home=$JAVA11'
            }
        }
        stage('Release Documentation') {
            when {
                expression {
                    def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                    return status == 0
                }
                expression {
                    boolean publish = false
                    try {
                        def version = sh(returnStdout: true, script: './gradlew -q printLibraryVersion -Dorg.gradle.java.home=$JAVA11').trim()
                        def sha = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                        input "Release documentation for ${version} from branch ${env.BRANCH_NAME} commit ${sha}?"
                        publish = true
                    } catch (final ignore) {
                        publish = false
                    }
                    return publish
                }
            }
            steps {
                sh 'scripts/release-javadoc.sh $GIT_USR $GIT_PSW'
                sh 'scripts/release-doc.sh $GIT_USR $GIT_PSW'
            }
        }
        stage('Release Library Snapshot') {
            when {
                branch 'develop'
            }
            steps {
                sh '''
                    ./gradlew publishReleasePublicationToSnapshotsRepository \
                    -PmavenSnapshotsRepoUrl=https://repo.gini.net/nexus/content/repositories/snapshots \
                    -PrepoUser=$NEXUS_MAVEN_USR \
                    -PrepoPassword=$NEXUS_MAVEN_PSW \
                    -Dorg.gradle.java.home=$JAVA11
                '''
            }
        }
        stage('Release Library') {
            when {
                expression {
                    def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                    return status == 0
                }
                expression {
                    boolean publish = false
                    try {
                        def version = sh(returnStdout: true, script: './gradlew -q printLibraryVersion -Dorg.gradle.java.home=$JAVA11').trim()
                        def sha = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                        input "Release ${version} from branch ${env.BRANCH_NAME} commit ${sha}?"
                        publish = true
                    } catch (final ignore) {
                        publish = false
                    }
                    return publish
                }
            }
            steps {
                sh '''
                    ./gradlew publishReleasePublicationToOpenRepository \
                    -PmavenOpenRepoUrl=https://repo.gini.net/nexus/content/repositories/open \
                    -PrepoUser=$NEXUS_MAVEN_USR \
                    -PrepoPassword=$NEXUS_MAVEN_PSW \
                    -Dorg.gradle.java.home=$JAVA11
                '''
            }
        }
    }
}
