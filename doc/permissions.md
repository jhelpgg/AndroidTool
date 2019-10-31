# Permissions

1. [Idea/aim](#ideaaim)

1. [Initialization](#initialization)

1. [Launch a task that require a permission](#launch-a-task-that-require-a-permission)

1. [Do something just before the Android dialog system](#do-something-just-before-the-android-dialog-system)

### Idea/aim

The aim her is to simplify the permission management.
Be able request permission when its necessary for the first time from everywhere.
No more need to send a message to actual Activity to do the request, then when answer is received, do the action.

We suggest to treat all permission on same way, even if a permission is not dangerous today, it can be tomorrow.
This to avoid change the application code later just for that.

Here developer just write the action as if the permission is granted. 
And when the action must be called, just ask to this library to be callback when permission granted.

The library will react like this :
* If permission is granted (Previous request or declare in Android manifest is enough), callback immediately
* Else if permission not already request, do the request first, then if user agree, do the callback
* Else its mean user have already denied the permission (Or permission not declared in Android manifest), no callback is made

It is possible to do something just before the the dialog system appear. 
To let the opportunity, by example, to explain why the permission is need.

It is also possible to know that user denied a permission.

### Initialization

For the system work, if have to be initialize. 
This can be done on two places, depends on your project.

If the project overrides the default Application, it can be initialized in ```AApplication.onCreate``` :

```Kotlin
import jhelp.thread.permission.PermissionManager

// ...

   override fun onCreate()
   {
      super.onCreate()
      PermissionManager.initialize(this)
      // ...
   }
```

If project not overrides the default Application or the project not need those tasks are played when wakeup by a JobSceduller or other background system,
it can be initialized from any Activity. 
We recommend, to initialize it, at least, from any Activity can be start on first. 
So obviously from main Activity (called when user launch application via the icon).
And any Activity can be started by example a deep-link. 
It's not an issue to call the initialization several times, since the method is thread-safe, 
and the real initialization will be done only the first time, other called are just ignored.

In ```Activity.onCreate``` :

```Kotlin
import jhelp.thread.permission.PermissionManager

// ...

   override fun onCreate(savedInstanceState: Bundle?)
   {
      super.onCreate(savedInstanceState)
      PermissionManager.initialize(this)
      // ...
   }
```

### Launch a task that require a permission

Now the library is initialized, its possible to use it from anywhere of the code, from any thread.

For example, the application need to do a geograpical location, to react where the user is. 
This operation, requires the permission ```Manifest.permission.ACCESS_FINE_LOCATION```.

First step, write the method that do the job :

```Kotlin
fun doGeographicLocation()
{
   // ...
}
```

Then when the function need to be called, just do :

```Kotlin
import jhelp.thread.permission.onPermission

// ...
this::doGeographicLocation.onPermission(Manifest.permission.ACCESS_FINE_LOCATION)
// ...
```

That's all, the library will do the rest.

Now you may want the ```doGeographicLocation``` return a boolean to indicates the registration is succeed and exploit the answer.
And/or you may want react if user deny the permission.

For this notice that the method ```onPermission``` returns a 
```jhelp.thread.promise.FutureResult``` (See [Threads explanation](thread.md) for more details)

With this object, its possible to do something after the method succeed, or react on failure.

If user denied the permission, the  ```FutureResult``` will be on error, the exception given have the permission name has message.

By example :

```Kotlin
fun doGeographicLocation() : Boolean
{
   // ...
}

fun showDialogIfRegistrationFailed(registrationSucceed:Boolean)
{
// ...
}

fun reactToUserDenialLocation(exception:Exception)
{
// ...
}
```

Can be called by:
```kotlin
      this::doGeographicLocation.onPermission(Manifest.permission.ACCESS_FINE_LOCATION)
         .then(this::showDialogIfRegistrationFailed)
         .onError(this::reactToUserDenialLocation)
```

Last point on this topic, if the launch action need a parameter., in our example it can be request location parameters.

So its now something like:

```kotlin
   fun doGeographicLocation(parameters : LocationParameters) : Boolean
   {
      // ...
   }

   fun showDialogIfRegistrationFailed(registrationSucceed:Boolean)
   {
      // ...
   }

   fun reactToUserDenialLocation(exception:Exception)
   {
      // ...
   }
``` 

In this case, the call of ```this::doGeographicLocation.onPermission(Manifest.permission.ACCESS_FINE_LOCATION)```
not returns directly a ```FutureResult```, but a function that take a ```LocationParameters``` in parameter and return a ```FutureResult```
To let the opportunity to call the same instance with different parameters. Usage, become:

```kotlin
      val actionDoGeographicLocation =  this::doGeographicLocation.onPermission(Manifest.permission.ACCESS_FINE_LOCATION)
      // ...     
      actionDoGeographicLocation(locationParameters)
         .then(this::showDialogIfRegistrationFailed)
         .onError(this::reactToUserDenialLocation)
```

### Do something just before the Android dialog system

It's a good practice, and make users more agree to accept permission, to explain why a permission is asked.
The library gives the possibility to do it just before do the request, and let the application decide if do the request or cancel it.

If cancel, the permission is considered as temporary denied. It will do the process next time application is launched.

To register an action to do before show the Android dialog, just use ```PermissionManager.registerForReactJustBeforeAskPermissionToUser``` 

Only one action can be registered for a given permission. 
If called more than once for same permission, the new action will replace the old one.

Obviously, call it before register any action need the permission. Else it can't be guaranteed to be used.

The given ```BeforeAskPermissionAction``` in the action parameter is the object to use for launch the Android dialog or cancel it.

