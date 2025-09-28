# Project Architecture

This document outlines the architectural structure of the PokePedia project. The project follows a modular and layered architecture, promoting separation of concerns, maintainability, and scalability.

## Overall Architecture

The project is structured around a clean architecture approach, dividing the codebase into distinct layers:

*   **Domain Layer**: Contains the core business logic and entities, independent of any specific framework or technology.
*   **Data Layer**: Responsible for data retrieval and persistence, providing implementations for interfaces defined in the Domain Layer.
*   **Presentation Layer**: Handles the user interface and presentation logic, reacting to user input and displaying data from the Domain Layer.

This layered approach is applied consistently across the entire project, including the core functionalities and feature-specific modules.

## Core Module (`composeApp/src/commonMain/kotlin/com/thesomeshkumar/pokepedia/core`)

The `core` module provides foundational components and utilities used throughout the application.

### Domain Layer (`core/domain`)

*   **Error Handling**: Implements a robust error handling strategy using `Error.kt`, `Result.kt`, and `DataError.kt`. This provides a standardized way to manage and communicate operation outcomes, including success and various error states.
*   **Core Entities/Interfaces**: Defines common domain entities and interfaces that are shared across different features.

### Data Layer (`core/data`)

*   **HTTP Client**: Contains `HttpClientExt.kt` and `HttpClientFactory.kt`, indicating the use of a dedicated HTTP client for network operations. This layer is responsible for making network requests and handling raw data.
*   **Data Sources**: Provides abstractions or implementations for common data sources if any.

### Presentation Layer (`core/presentation`)

*   **UI Utilities**: Includes `UiText.kt` (for handling localized strings), `PulseAnimation.kt` (for UI animations), and `DataErrorToStringResource.kt` (for mapping data errors to user-friendly messages). These components promote a consistent and polished user experience.

## Pokemon Module (`composeApp/src/commonMain/kotlin/com/thesomeshkumar/pokepedia/pokemon`)

The `pokemon` module is a feature-specific module dedicated to displaying and managing pokemon-related content. It adheres to the same layered architecture as the `core` module.

### Domain Layer (`pokemon/domain`)

*   **Pokemon Entity**: Defines the `Pokemon.kt` data class, representing the core entity for pokemon information within the application.
*   **PokemonRepository Interface**: `PokemonRepository.kt` defines an interface for data operations related to pokemon, abstracting the underlying data sources. This is a key component of the Repository pattern.

### Data Layer (`pokemon/data`)

*   **DTOs (`dto`)**: Contains Data Transfer Objects for mapping network or database responses to domain-specific entities.
*   **Mappers (`mappers`)**: Provides mapping logic to convert DTOs to domain entities and vice-versa, ensuring a clean separation between data models and domain models.
*   **Network (`network`)**: Implements the network data source for fetching pokemon data from remote APIs.
*   **Database (`database`)**: Implements the local database data source for caching or persisting pokemon data.
*   **Repository Implementation (`repository`)**: Contains the concrete implementation of the `PokemonRepository` interface, orchestrating data retrieval from network and/or database sources based on business logic.

### Presentation Layer (`pokemon/presentation`)

*   **Pokemon List UI (`pokemon_list`)**: Responsible for displaying a list of pokemon, handling user interactions, and managing the state related to the pokemon list. This typically includes a ViewModel and Composable UI components.
*   **Pokemon Detail UI (`pokemon_detail`)**: Manages the display of detailed information for a single pokemon, including its ViewModel and Composable UI components.

## Dependency Injection

While not explicitly explored in detail, the presence of a `di` directory at the project root (`composeApp/src/commonMain/kotlin/com/thesomeshkumar/pokepedia/di`) suggests the use of a Dependency Injection framework (e.g., Koin, Hilt) to manage dependencies and facilitate a modular and testable codebase.

This architectural overview provides a high-level understanding of how the PokePedia project is structured and how its different components interact.
