# AGENTS.md - Architecture Guide

This document provides official architectural guidelines for Cursor IDE when working with Kotlin Multiplatform projects. Follow these patterns and principles to maintain clean, scalable, and testable code.

## Architecture Overview

Follow Clean Architecture principles with clear separation of concerns across three main layers:

### 1. Data Layer
- **Repositories**: Implement repository interfaces defined in domain layer
- **Data Sources**: Remote (API) and Local (Database/Cache) data sources
- **DTOs**: Data Transfer Objects for API responses
- **Mappers**: Convert between DTOs and Domain models

```kotlin
// Repository Implementation
class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource,
    private val mapper: UserMapper
) : UserRepository {
    override suspend fun getUser(id: String): Result<User> {
        return try {
            val userDto = remoteDataSource.getUser(id)
            val user = mapper.toDomain(userDto)
            localDataSource.saveUser(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### 2. Domain Layer
- **Models**: Pure business logic entities
- **Repository Interfaces**: Abstract contracts for data access
- **Mappers**: Interface definitions for data transformations

```kotlin
// Domain Model
data class User(
    val id: String,
    val name: String,
    val email: String
)

// Repository Interface
interface UserRepository {
    suspend fun getUser(id: String): Result<User>
    suspend fun updateUser(user: User): Result<Unit>
}
```

### 3. Presentation Layer
- **ViewModels/Presenters**: Handle UI logic and state management
- **UI State**: Immutable state representations
- **Actions**: User interactions and events
- **UI Components**: Platform-specific UI implementations

## State Management Patterns

### UI State Pattern
Create immutable state classes that represent the complete UI state:

```kotlin
data class UserScreenState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val isRefreshing: Boolean = false
)
```

### Action Pattern
Define sealed classes for user actions:

```kotlin
sealed class UserAction {
    object LoadUser : UserAction()
    object RefreshUser : UserAction()
    data class UpdateUser(val user: User) : UserAction()
}
```

### Single Source of Truth
Maintain single source of truth in ViewModels:

```kotlin
class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(UserScreenState())
    val state: StateFlow<UserScreenState> = _state.asStateFlow()
    
    fun handleAction(action: UserAction) {
        when (action) {
            is UserAction.LoadUser -> loadUser()
            is UserAction.RefreshUser -> refreshUser()
            is UserAction.UpdateUser -> updateUser(action.user)
        }
    }
    
    private fun loadUser() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            userRepository.getUser("123").fold(
                onSuccess = { user ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        user = user
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            )
        }
    }
}
```

## Mapper Pattern

### Domain to DTO Mapping
```kotlin
interface UserMapper {
    fun toDomain(dto: UserDto): User
    fun toDto(domain: User): UserDto
}

class UserMapperImpl : UserMapper {
    override fun toDomain(dto: UserDto): User {
        return User(
            id = dto.userId,
            name = dto.fullName,
            email = dto.emailAddress
        )
    }
    
    override fun toDto(domain: User): UserDto {
        return UserDto(
            userId = domain.id,
            fullName = domain.name,
            emailAddress = domain.email
        )
    }
}
```

## Repository Interface Pattern

Define repository interfaces in the domain layer:

```kotlin
interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
    suspend fun getUser(id: String): Result<User>
    suspend fun saveUser(user: User): Result<Unit>
    suspend fun deleteUser(id: String): Result<Unit>
}

interface ProductRepository {
    suspend fun getProducts(): Flow<List<Product>>
    suspend fun getProduct(id: String): Result<Product>
    suspend fun searchProducts(query: String): Result<List<Product>>
}
```

## Dependency Injection

Structure your DI modules by layers:

```kotlin
// Data Layer Module
val dataModule = module {
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }
    single { UserRemoteDataSource(get()) }
    single { UserLocalDataSource(get()) }
    single<UserMapper> { UserMapperImpl() }
}

// Presentation Layer Module
val presentationModule = module {
    viewModel { UserViewModel(get()) }
}
```

## File Structure

Organize your multiplatform project following this structure:

```
src/
├── commonMain/kotlin/
│   ├── data/
│   │   ├── repository/
│   │   ├── datasource/
│   │   ├── dto/
│   │   └── mapper/
│   ├── domain/
│   │   ├── model/
│   │   └── repository/
│   └── presentation/
│       ├── viewmodel/
│       ├── state/
│       └── action/
├── androidMain/kotlin/
│   └── ui/
└── iosMain/kotlin/
    └── ui/
```

## Testing Guidelines

### Unit Testing
- Test each layer independently
- Mock dependencies using interfaces
- Focus on business logic in ViewModels and Repositories

```kotlin
class UserViewModelTest {
    private val mockRepository = mockk<UserRepository>()
    private val viewModel = UserViewModel(mockRepository)
    
    @Test
    fun `should update state when loading user succeeds`() = runTest {
        // Given
        val expectedUser = User("1", "John", "john@example.com")
        coEvery { mockRepository.getUser("123") } returns Result.success(expectedUser)
        
        // When
        viewModel.handleAction(UserAction.LoadUser)
        
        // Then
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(expectedUser, state.user)
        assertNull(state.error)
    }
}
```

## Key Principles

1. **Separation of Concerns**: Each layer has single responsibility
2. **Dependency Inversion**: Depend on abstractions, not concretions
3. **Single Source of Truth**: State flows from one direction
4. **Immutable State**: UI state should be immutable
5. **Error Handling**: Use Result/Either types for error handling
6. **Testability**: Design for easy unit testing

## Common Patterns to Follow

- Use `Result<T>` or `Either<Error, Success>` for error handling
- Implement repository pattern with interfaces in domain layer
- Use StateFlow/Flow for reactive state management
- Apply mapper pattern for data transformations
- Structure actions as sealed classes
- Maintain immutable UI state classes

## Avoid These Anti-patterns

- Direct database/API calls from ViewModels
- Mutable state exposure from ViewModels  
- Business logic in UI layer
- Tight coupling between layers
- God classes with multiple responsibilities
- Exposing implementation details across layer boundaries

Follow these guidelines to create maintainable, testable, and scalable Kotlin Multiplatform applications.