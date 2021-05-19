Getting started
===============

Installation
------------

To install add our Maven repo to the root build.gradle file and add it as a dependency to your app
module's build.gradle.

build.gradle:

.. code-block:: groovy

    repositories {
        maven {
            url 'https://repo.gini.net/nexus/content/repositories/open
        }
    }

app/build.gradle:

.. code-block:: groovy

    dependencies {
        implementation 'net.gini:gini-pay-business-sdk:1.0.0-beta04'
    }
