# Assignment

The app should provide a button to load the content of a website below and perform 3 tasks in
parallel. The app should display the result of each task as soon as it is done. You can choose how
you want to display the result.
URL: https://www.truecaller.com/blog/life-at-truecaller/life-as-an-android-engineer
Consider the content of the website as plain text.

*Language - Kotlin  
Architecture - MVVM  
Dependency Injection - dagger Hilt  
Kotlin coroutines
minSdk = 24  
targetSdk = 34  
Testing on Api level - 25,28,29,30,31,33,34  
Gradle Version - 8.0  
Dependencies used - Androidx Activity, Lifecycle ,DaggerHilt, Kotlin Coroutines, Test Dependencies (JUnit,Arch Core, Mockito)    
AppIcon is Truecaller icon   
App Name is Usama_Aman*

## Explanation

In the main screen, there is a button which loads the content of the URL i.e
(https://www.truecaller.com/blog/life-at-truecaller/life-as-an-android-engineer)
URL content is fetched by simple *java.net* URL function read text and saved in a string.
Assumption-(Checked already that the string can hold all the data on the page).
Application also observes the network change in case of no internet connection or connection lost
If there is an error , application handles the exception and show it on the screen in a textview.
Tasks are run in Coroutine scope i.e viewModelScope
Unit tests of each task is added [Path](app/src/test/java/com/truecaller/assignment/MainViewModelTest.kt)

#### Tasks

Provided that data is loaded, viewModel triggers 3 tasks in parallel in coroutine scope to prevent main thread blockage

1. *Truecaller15thCharacterRequest:*
   A method is written that takes the content String and character location to find.
   The function handles the bases cases and simply return the character at the specific location, in our case the 15th.
   It is done by the simple string method .get() or []
   As the string starts from 0 index so 15th character will be found at 14th index,
   The result is dispatched to UI in an observable field using the *Two way binding*, as task1. And displayed in a TextView
   in scrollview

2. *TruecallerEvery15thCharacterRequest:*
   A method is written in the viewModel that takes the content and character location to find.
   The function handles the bases cases and store the every nth index in a string, in our case the 15th
   As the string starts from 0 index so first 15th character will be found at 14 index and the second 15th character will be 
   found at 29th index and so on. 
   The result is dispatched to UI in an observableField using the *Two way binding* as task2. And displayed in a TextView

3. *TruecallerWordCounterRequest:*
   An method is written for this task in the viewModel that takes the content, handles the base cases.
   Content is then spitted on basis of space, tab or line break or multiple of these. It is done by .split() method of string
   which return the list of string. Which is then stored in hashmaps with occurrences of each unique string the value of the
   hashmap. Then the hashmaps keys and values are added in two separate list to display the unique keys and their occurrences
   in the RecyclerView.

The results of each tasks are shown on the screen using the scrollview on the tasks result not on the screen to provide a
better user experience
Unit tests are added for all the tasks in the assignment.

##### Thanks And Regards

Though these task can be done in many other ways, but i found these more precise and not over engineered.
Hope you can have an idea about my work and consider me for the next interview.  
Thanks for reviewing my code.  
Have a great day.  
Happy Coding!!!

