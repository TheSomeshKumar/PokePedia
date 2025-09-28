
# PokePedia

PokePedia is a Kotlin Multiplatform project that showcases the use of modern technologies to build a simple, yet powerful application. It is a simple app that displays a list of Pokémon and their details.

## Features

- Browse a list of Pokémon.
- View details of a specific Pokémon.
- Search for a Pokémon.
- Offline support.

## Architecture

This project follows the principles of Clean Architecture, using the Model-View-ViewModel (MVVM) pattern. The architecture is divided into three main layers:

- **Data Layer:** Responsible for providing data to the application. It includes the repository pattern, which abstracts the data sources.
- **Domain Layer:** Contains the business logic of the application.
- **Presentation Layer:** The UI of the application, built with Compose Multiplatform.

## Tech Stack

- **Kotlin:** The programming language used for the project.
- **Compose Multiplatform:** For building the user interface for Android, iOS, and Desktop.
- **Ktor:** For making network requests.
- **SQLDelight:** For local database storage.
- **Koin:** For dependency injection.
- **Coroutines & Flow:** For asynchronous programming.

## Screenshots

| List | Detail |
| --- | --- |
| <img src="art/list.png" width="250"/> | <img src="art/detail.png" width="250"/> |

## Getting Started

To get a local copy up and running, please follow these simple steps.

### Prerequisites

- Android Studio
- Kotlin Multiplatform Mobile plugin
- Xcode for iOS development

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/TheSomeshKumar/PokePedia.git
   ```
2. Open the project in Android Studio and wait for Gradle to sync.
3. Run the app on your desired platform (Android, iOS, or Desktop).

## License

Distributed under the MIT License. See `LICENSE` for more information.
