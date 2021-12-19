# FlexingLyrics
FlexingLyrics is an Android MVVM sample application created for learning purposes only.\
This application is based on [Happi.dev](https://happi.dev/) API and provides users with ability to search information about their favorite tracks: lyrics, artist information, album information.


## Screenshots
<p float="left">
  <img src="screenshots/screen1.png" width=250/>
  <img src="screenshots/screen2.png" width=250/> 
  <img src="screenshots/screen3.png" width=250/>
</p>

## Tech stack and concepts
* **[Kotlin](https://kotlinlang.org/)** as programming language.
* **[Kotlin coroutines](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/)** as framework for asynchronous jobs.
* Single activity support.
  * **[Fragments](https://developer.android.com/jetpack/androidx/releases/fragment)**.
  * **[Navigation components](https://developer.android.com/jetpack/androidx/releases/navigation)**.
* Modern UI toolkit
  * **[Material components](https://material.io/develop/android)**
  * **ViewBinding** for binding .xml views to objects in Activities and Fragments
* API-based remote data layer.
  * **[Retrofit](https://square.github.io/retrofit/)** for network queries.
  * **[GSON](https://github.com/google/gson)** for parsing JSON.\
  NOTE: considering switch to **Moshi**.
* **[Room](https://developer.android.com/jetpack/androidx/releases/room)** for local data layer.
* **[Lifecycle components](https://developer.android.com/jetpack/androidx/releases/lifecycle)**.
  * **ViewModel** for implementing MVVM pattern.
  * **Livedata** as lifecycle-aware observable for UI layer.\
  NOTE: considering switch to **Flow** (*StateFlow*, *SharedFlow*).
* **[Glide](https://github.com/bumptech/glide)** for working with images.
* Testing.
  * **JUnit**.
  * **[Android testing library](https://developer.android.com/jetpack/androidx/releases/test)**.
  * **Espresso** for UI-testing.
  * **Mockito** for mocking third-party classes (e.g. *NavController*).
  * Specific testing artifacts for other libraries.
* **[Hilt](https://dagger.dev/hilt/)** for dependency injection.

## Architecture
The application is divided into 3 layers:
1. **UI** (.ui): MainActivity, Fragments and ViewModels associated with them.
2. **Domain** (.repository): Repositories (abstractions and implementations).
3. **Data** (.data, .api): API service, DTOs for remote data; Database, DAOs, Models for local data.

For dependency flow there is .di package with corresponding modules. 

## Project structure
<img src="screenshots/project_structure.png" width=750/>

## How to build?
In order to build the application you need to provide an API key:
1. Get it from [Happi.dev](https://happi.dev/).
2. Create file `credentials.properties`
3. Insert your key as 
```
API_KEY = "YOUR_API_KEY"
```

## License
```
MIT License

Copyright (c) 2021 Shvyndia Andrii

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```