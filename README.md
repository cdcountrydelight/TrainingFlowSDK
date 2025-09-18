# TrainingSDK for Android

[![](https://jitpack.io/v/cdcountrydelight/TrainingFlowSDK.svg)](https://jitpack.io/#cdcountrydelight/TrainingFlowSDK)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0+-purple.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg?style=flat&logo=android)](https://developer.android.com)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2025.09.00-blue.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Material Design 3](https://img.shields.io/badge/Material%20Design-3-orange.svg?style=flat&logo=materialdesign)](https://m3.material.io)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A powerful Android SDK for creating interactive training flows with step-by-step tutorials, visual annotations, and progress tracking. Built with Jetpack Compose and modern Android development practices.

## ✨ Features

- **Interactive Training Flows**: Step-by-step tutorials with visual annotations and instructions
- **Progress Tracking**: Track user progress across multiple training flows (started, in-progress, completed)
- **Visual Annotations**: Support for circles, rectangles, and rounded corners with customizable colors and stroke width
- **Pull-to-Refresh**: Built-in refresh functionality for training flow lists
- **Material Design 3**: Modern UI following Material Design guidelines
- **Image Loading**: Optimized image loading with Coil
- **Offline Support**: Graceful handling of network connectivity issues
- **Responsive Design**: Adapts to different screen sizes and orientations

## 📱 Screenshots

*Screenshots will be added in a future release*

## 🚀 Installation

Add JitPack repository to your project's `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Add the dependency to your app's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.cdcountrydelight:TrainingFlowSDK:1.0.1")
}
```

## 🏗️ Quick Start

### Basic Integration

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YourAppTheme {
                TrainingFlowNavGraph(
                    authToken = "your_jwt_token_here",
                    appName = "Your App Name",
                    packageName = packageName
                ) {
                    // Handle back navigation
                    finish()
                }
            }
        }
    }
}
```

### Advanced Configuration

```kotlin
TrainingFlowNavGraph(
    authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    appName = "My Training App",
    packageName = "com.example.myapp",
    unAuthorizedExceptionCodes = listOf(401, 403), // Custom unauthorized codes
    onBackPressed = {
        // Custom back navigation logic
        navController.popBackStack()
    }
)
```

## 📖 Usage Guide

### Authentication Setup

The SDK requires a JWT token for authentication. Ensure your token contains:

- **user_id**: Unique identifier for the user
- **Proper expiration**: Token should have appropriate expiry time
- **Valid signature**: Token should be properly signed

```kotlin
// Example token payload
{
    "user_id": "12345",
    "exp": 1234567890,
    "iat": 1234567890
}
```

### Training Flow Structure

Training flows consist of:

1. **Flow List Screen**: Displays available training flows with progress indicators
2. **Flow Detail Screen**: Shows step-by-step training content with annotations
3. **Completion Screen**: Congratulations screen after completing training

### Progress States

Each training flow can have three states:
- **Not Started**: User hasn't begun the training
- **In Progress**: User has started but not completed
- **Completed**: User has finished the training

## 🔧 API Reference

### TrainingFlowNavGraph

Main entry point for the SDK.

```kotlin
@Composable
fun TrainingFlowNavGraph(
    authToken: String,                              // JWT authentication token
    appName: String,                               // Display name for your app
    packageName: String,                           // Your app's package name
    unAuthorizedExceptionCodes: List<Int> = listOf(401), // Custom unauthorized error codes
    onBackPressed: () -> Unit                      // Back navigation callback
)
```

### Data Models

#### FlowListResponseContent
```kotlin
data class FlowListResponseContent(
    val id: Int,                                   // Unique flow identifier
    val name: String?,                             // Flow display name
    val description: String?,                      // Flow description
    val isActive: Boolean?,                        // Whether flow is active
    val stepCount: Int?,                          // Number of steps in flow
    val userProgress: UserProgressResponseContent? // User's progress data
)
```

#### StepsResponseContent
```kotlin
data class StepsResponseContent(
    val id: Int,                                   // Step identifier
    val stepNumber: Int,                           // Step sequence number
    val screenshotUrl: String,                     // Screenshot image URL
    val instructions: List<String>,                // Step instructions
    val annotation: AnnotationResponseContent?,    // Visual annotations
    val height: Double,                            // Image height
    val width: Double                              // Image width
)
```

## ⚙️ Configuration

### Required Permissions

Add these permissions to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### ProGuard Rules

If you're using ProGuard, add these rules:

```pro
-keep class com.cd.trainingsdk.** { *; }
-dontwarn com.cd.trainingsdk.**
```

### Network Configuration

For Android 9 (API level 28) and above, add network security config if using HTTP:

```xml
<application
    android:networkSecurityConfig="@xml/network_security_config"
    ... >
</application>
```

## 📋 Requirements

- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **Kotlin**: 1.9.0+
- **Jetpack Compose**: 2025.09.00+
- **Java**: 11

## 🛠️ Dependencies

The SDK uses these key libraries:

- **Jetpack Compose**: Modern UI toolkit
- **Ktor Client**: HTTP client for networking
- **Coil**: Image loading library
- **Material Design 3**: UI components
- **Kotlinx Serialization**: JSON serialization

## 🔍 Error Handling

The SDK provides comprehensive error handling:

```kotlin
// Custom error codes
TrainingFlowNavGraph(
    authToken = token,
    appName = "My App",
    packageName = packageName,
    unAuthorizedExceptionCodes = listOf(401, 403, 419) // Custom codes
) {
    onBackPressed()
}
```

Common error scenarios:
- **401/403**: Authentication failures
- **Network errors**: Connectivity issues
- **Malformed data**: Invalid response format

## 🔄 Pull-to-Refresh

The SDK includes built-in pull-to-refresh functionality:

- Swipe down on the flow list to refresh
- Visual loading indicators during refresh
- Maintains existing data during refresh operations

## 🎨 Theming

The SDK follows Material Design 3 principles and adapts to your app's theme:

```kotlin
// Your app theme will be automatically applied
YourAppTheme {
    TrainingFlowNavGraph(...)
}
```

## 🧪 Testing

To test the SDK integration:

1. **Setup**: Ensure you have a valid JWT token
2. **Package Name**: Use your actual app package name
3. **Network**: Test with both good and poor connectivity
4. **Flows**: Verify training flows are configured on your backend

## 📝 Changelog

### Version 1.0.1
- Fixed dropdown dismissal during navigation
- Improved back navigation handling
- Enhanced UI responsiveness

### Version 1.0.0
- Initial release
- Basic training flow functionality
- Progress tracking
- Pull-to-refresh support

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

If you encounter any issues or have questions:

1. Check our [documentation](https://github.com/cdcountrydelight/TrainingFlowSDK/wiki)
2. Search [existing issues](https://github.com/cdcountrydelight/TrainingFlowSDK/issues)
3. Create a [new issue](https://github.com/cdcountrydelight/TrainingFlowSDK/issues/new)

## 🔗 Links

- [JitPack Repository](https://jitpack.io/#cdcountrydelight/TrainingFlowSDK)
- [Sample App](https://github.com/cdcountrydelight/TrainingFlowSDK/tree/main/app)
- [API Documentation](https://github.com/cdcountrydelight/TrainingFlowSDK/wiki/API-Reference)

---

Made with ❤️ by [Country Delight](https://github.com/cdcountrydelight)