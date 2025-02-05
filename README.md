# iWatch

This project is an app for managing movie lists. You can create multiple lists and add movies to them. Movie data is fetched from TMDB APIs.

## Project structure
This is a Kotlin Multiplatform project targeting Android, iOS, Web, Desktop, Server.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* `/server` is for the Ktor server application.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here too.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

## Setting up the environment
To run or work on this project, you'll need the following:
- Install and setup MongoDB
- Create a database named `iwatch`
- Install Android Studio
- Install the [Kotlin Multiplatform plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform) for Android Studio
 
## Running

### Server
Start the server by running the `:server:run` Gradle task.

### Android app
Select the composeApp run configuration in Android Studio, select an emulator o device, and click the Run button.

## iOS app
Select the iosApp run configuration in Android Studio, and click the Run button.

### Desktop app
Start the desktop application using the `:composeApp:run` Gradle task.

> For more details on running the app, see the [official documentation](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-create-first-app.html#run-your-application).

## Screenshots

Login screen             |  Lists screen
:-----------------------:|:-------------------------:
![Login screen](/assets/screenshots/login.jpg?raw=true) | ![Lists screen](/assets/screenshots/lists.jpg?raw=true)

Movies screen            |  Search screen
:-----------------------:|:-------------------------:
![Movies screen](/assets/screenshots/movies.jpg?raw=true) | ![Search screen](/assets/screenshots/search.jpg?raw=true)
