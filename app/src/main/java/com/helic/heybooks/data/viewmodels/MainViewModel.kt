package com.helic.heybooks.data.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.helic.heybooks.R
import com.helic.heybooks.data.models.book.Book
import com.helic.heybooks.data.models.book.ListOfBooks
import com.helic.heybooks.data.models.user.User
import com.helic.heybooks.utils.AddOrRemoveBookAction
import com.helic.heybooks.utils.Constants.FIRESTORE_BOOKS_DOCUMENT
import com.helic.heybooks.utils.Constants.FIRESTORE_DATABASE
import com.helic.heybooks.utils.Constants.FIRESTORE_USERS_DATABASE
import com.helic.heybooks.utils.Constants.LIST_OF_BOOKS
import com.helic.heybooks.utils.Constants.USERNAME_FIELD
import com.helic.heybooks.utils.Constants.USER_BIO_FIELD
import com.helic.heybooks.utils.Constants.USER_IMAGE
import com.helic.heybooks.utils.LoadingState
import com.helic.heybooks.utils.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    var selectedBook: MutableState<Book> = mutableStateOf(Book())

    @SuppressLint("MutableCollectionMutableState")
    private var _booksList: MutableStateFlow<MutableList<Book>> =
        MutableStateFlow(mutableListOf())
    var booksList = _booksList.asStateFlow()

    var gettingListOfBooksState = MutableStateFlow(LoadingState.IDLE)

    private var _userInfo: MutableStateFlow<User> = MutableStateFlow(User())
    var userInfo = _userInfo.asStateFlow()

    fun getUserInfo(context: Context, snackbar: (String, SnackbarDuration) -> Unit) {
        val db = Firebase.firestore
        val currentUser = Firebase.auth.currentUser
        val data = currentUser?.let { db.collection(FIRESTORE_USERS_DATABASE).document(it.uid) }
        if (hasInternetConnection(getApplication<Application>())) {
            if (currentUser != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        data?.addSnapshotListener { value, error ->
                            if (error != null) {
                                return@addSnapshotListener
                            }
                            if (value != null && value.exists()) {
                                _userInfo.value =
                                    value.toObject(User::class.java) ?: User()
                            } else {
                                snackbar(
                                    context.getString(R.string.error_occurred),
                                    SnackbarDuration.Short
                                )
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            snackbar(
                                getApplication<Application>().getString(R.string.error_occurred),
                                SnackbarDuration.Short
                            )
                        }
                    }
                }
            }
        } else {
            snackbar(context.getString(R.string.device_not_connected), SnackbarDuration.Short)
        }
    }

    fun updateUserDetails(
        context: Context,
        ownerName: String,
        ownerBio: String,
        ownerImage: String,
        snackbar: (String, SnackbarDuration) -> Unit
    ) {
        val db = Firebase.firestore
        val currentUser = Firebase.auth.currentUser
        val data = currentUser?.let { db.collection(FIRESTORE_USERS_DATABASE).document(it.uid) }

        if (hasInternetConnection(getApplication<Application>())) {
            if (currentUser != null) {
                CoroutineScope(Dispatchers.IO).launch {

                    var image = false

                    data?.update(USER_IMAGE, ownerImage)
                        ?.addOnSuccessListener {
                            image = true
                        }?.addOnFailureListener {

                            image = false
                        }
                    var name = false
                    data?.update(USERNAME_FIELD, ownerName)
                        ?.addOnSuccessListener {
                            name = true

                        }?.addOnFailureListener {
                            name = false
                        }
                    var bio = false
                    data?.update(USER_BIO_FIELD, ownerBio)
                        ?.addOnSuccessListener {
                            bio = true

                        }?.addOnFailureListener {
                            bio = false
                        }
                }
            }
        } else {
            snackbar(context.getString(R.string.device_not_connected), SnackbarDuration.Short)
        }
    }

    fun addOrRemoveBookFromFirebase(
        context: Context,
        snackbar: (String, SnackbarDuration) -> Unit,
        action: AddOrRemoveBookAction,
        book: Book
    ) {
        val db = Firebase.firestore
        val data = db.collection(FIRESTORE_DATABASE).document(FIRESTORE_BOOKS_DOCUMENT)
        if (hasInternetConnection(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    when (action) {
                        AddOrRemoveBookAction.ADD -> {
                            data.update(
                                LIST_OF_BOOKS,
                                FieldValue.arrayUnion(book)
                            )
                                .addOnSuccessListener {

                                }.addOnFailureListener {
                                    snackbar(
                                        "Something went wrong: $it",
                                        SnackbarDuration.Short
                                    )
                                }
                        }
                        AddOrRemoveBookAction.REMOVE -> {
                            data.update(
                                LIST_OF_BOOKS,
                                FieldValue.arrayRemove(book)
                            )
                                .addOnSuccessListener {
                                }.addOnFailureListener {
                                    snackbar(
                                        "Something went wrong: $it",
                                        SnackbarDuration.Short
                                    )
                                }
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        withContext(Dispatchers.Main) {
                            snackbar(
                                context.getString(R.string.error_occurred),
                                SnackbarDuration.Short
                            )
                        }
                    }
                }
            }
        } else {
            snackbar(context.getString(R.string.device_not_connected), SnackbarDuration.Short)
        }
    }

    fun addOrRemoveBookForUser(
        context: Context,
        snackbar: (String, SnackbarDuration) -> Unit,
        action: AddOrRemoveBookAction,
        book: Book
    ) {
        val db = Firebase.firestore
        val currentUser = Firebase.auth.currentUser
        val data = currentUser?.let { db.collection(FIRESTORE_USERS_DATABASE).document(it.uid) }
        if (hasInternetConnection(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    when (action) {
                        AddOrRemoveBookAction.ADD -> {
                            data?.update(
                                LIST_OF_BOOKS,
                                FieldValue.arrayUnion(book)
                            )?.addOnSuccessListener {

                            }?.addOnFailureListener {
                                snackbar(
                                    "Something went wrong: $it",
                                    SnackbarDuration.Short
                                )
                            }
                        }
                        AddOrRemoveBookAction.REMOVE -> {
                            data?.update(
                                LIST_OF_BOOKS,
                                FieldValue.arrayRemove(book)
                            )?.addOnSuccessListener {
                            }?.addOnFailureListener {
                                snackbar(
                                    "Something went wrong: $it",
                                    SnackbarDuration.Short
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        withContext(Dispatchers.Main) {
                            snackbar(
                                context.getString(R.string.error_occurred),
                                SnackbarDuration.Short
                            )
                        }
                    }
                }
            }
        } else {
            snackbar(context.getString(R.string.device_not_connected), SnackbarDuration.Short)
        }

    }


    fun getListOfBooksFromFirebase(
        context: Context,
        snackbar: (String, SnackbarDuration) -> Unit
    ) {
        val db = Firebase.firestore
        val data = db.collection(FIRESTORE_DATABASE).document(FIRESTORE_BOOKS_DOCUMENT)

        if (hasInternetConnection(getApplication<Application>())) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    gettingListOfBooksState.emit(LoadingState.LOADING)

                    data.addSnapshotListener { value, error ->
                        if (error != null) {
                            snackbar("Error occurred: $error", SnackbarDuration.Short)
                            return@addSnapshotListener
                        }
                        if (value != null && value.exists()) {
                            _booksList.value =
                                value.toObject(ListOfBooks::class.java)?.listOfBooks
                                    ?: mutableListOf()
                        } else {
                            snackbar(
                                context.getString(R.string.error_occurred),
                                SnackbarDuration.Short
                            )
                        }
                    }
                    gettingListOfBooksState.emit(LoadingState.LOADED)
                } catch (e: Exception) {
                    gettingListOfBooksState.emit(LoadingState.ERROR)
                    withContext(Dispatchers.Main) {
                        snackbar(
                            context.getString(R.string.error_occurred),
                            SnackbarDuration.Short
                        )
                    }
                }
            }
        } else {
            snackbar(context.getString(R.string.device_not_connected), SnackbarDuration.Short)
        }
    }

    private var _addedByUser: MutableStateFlow<User> = MutableStateFlow(User())
    var addedByUser = _addedByUser.asStateFlow()

    fun getAddedByUserInfo(context: Context, snackbar: (String, SnackbarDuration) -> Unit) {
        val db = Firebase.firestore
        val data = db.collection(FIRESTORE_USERS_DATABASE).document(selectedBook.value.userID)
        if (hasInternetConnection(getApplication<Application>())) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    data.addSnapshotListener { value, error ->
                        if (error != null) {
                            return@addSnapshotListener
                        }
                        if (value != null && value.exists()) {
                            _addedByUser.value =
                                value.toObject(User::class.java) ?: User()
                        } else {
                            snackbar(
                                context.getString(R.string.error_occurred),
                                SnackbarDuration.Short
                            )
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        snackbar(
                            getApplication<Application>().getString(R.string.error_occurred),
                            SnackbarDuration.Short
                        )
                    }
                }
            }

        } else {
            snackbar(context.getString(R.string.device_not_connected), SnackbarDuration.Short)
        }
    }
}