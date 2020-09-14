## Development principle, pattern
- SOLID
- Pattern
  - Dependency injection
  - Singleton
  - Observer
  - Facade
  - Factory Method
  - Adapter

## Using
- Architect component: Databinding, LiveData, ViewModel
- DB: Room
- Testing: Espresso, JUnit, Coroutines Test
- Network: Retrofit 2, Okhttp3, Moshi
- Kotlin Coroutines for background operations.

## Folder structure
- Two product flavors, mock and prod, to ease development and testing 
- A presentation layer that contains a fragment (View) and a ViewModel per screen (or feature).
- Reactive UIs using LiveData observables and Data Binding.
- A data layer with a repository and two data sources remote and local

## Step to run
- To run app, select build variant as prodDebug before installing app
- To build APK file, select build variant prodRelease on build process
- To run unit test, select build variant mockDebug before running test

## Checklist Item
- Build project using kotlin 
- App architecture MVVM and Android architect component library
- Apply LiveData mechanism
- UI follow as requirement
- UnitTest ViewModel class
- Instrumental Test for Search Fragment
- Error handling for API request call
- Caching handling (memory cached, db cache). Refresh cache after 1 day
- Accessibility for disabililty support (Scaling text)
- Enable Proguard with custom proguard rules
