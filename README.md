Gini Pay Business SDK for Android
===============================

Installation
------------

To install add our Maven repo to the root build.gradle file and add it as a dependency to your app
module's build.gradle.

build.gradle:

```
repositories {
    maven {
        url 'https://repo.gini.net/nexus/content/repositories/open
    }
}
```

app/build.gradle:

```
dependencies {
    implementation 'net.gini:gini-pay-business-sdk:1.0.5'
}
```

Example Apps
---

### Business

The bussiness example app is in the `:app` module. 
It needs `app/src/main/resources/client.properties` with credentials:
```
clientId=*******
clientSecret=*******
```
Note: `resources` needs to be a java resources folder, not a regular folder.

### Bank

In order to pass the Requirements a bank app needs to be installed on the device.

An example bank app is available in the Gini Pay Bank SDK's
[repository](https://github.com/gini/gini-pay-bank-sdk-android) called
[`appcreenapi`](https://github.com/gini/gini-pay-bank-sdk-android/tree/main/appscreenapi).

## License

Gini Pay Business SDK is available under a commercial license.
See the LICENSE file for more info.
