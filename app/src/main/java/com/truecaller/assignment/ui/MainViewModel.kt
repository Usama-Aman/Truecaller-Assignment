package com.truecaller.assignment.ui

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.truecaller.assignment.data.MainRepository
import com.truecaller.assignment.di.DefaultDispatcher
import com.truecaller.assignment.di.IoDispatcher
import com.truecaller.assignment.utilities.RepositoryResource
import com.truecaller.assignment.utilities.Utility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var urlContent: String = ""

    private val _dataRead: MutableLiveData<Boolean> = MutableLiveData(false)
    val dataRead: LiveData<Boolean>
        get() = _dataRead

    private val _dataError: MutableLiveData<String> = MutableLiveData("")
    val dataError: LiveData<String>
        get() = _dataError

    private val _loadingStatus: MutableLiveData<Boolean> = MutableLiveData(false)
    val loadingStatus: LiveData<Boolean>
        get() = _loadingStatus

    private val _countCompleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val countCompleted: LiveData<Boolean>
        get() = _countCompleted

    var task1: ObservableField<String> = ObservableField()
    var task2: ObservableField<String> = ObservableField()

    // Created two list for display purpose, easier to deal with as compared to hashmap
    val words: ArrayList<String> = ArrayList()
    val wordsCount: ArrayList<Int> = ArrayList()


    fun readURL(urlString: String = Utility.DataURL) {
        _loadingStatus.value = true
        viewModelScope.launch(ioDispatcher) {
            when (val response = mainRepository.getURLContent(urlString)) {
                is RepositoryResource.Success -> {
                    urlContent = response.value
                    _loadingStatus.postValue(false)
                    _dataRead.postValue(true)
                    _dataError.postValue("")
                }

                is RepositoryResource.Error -> {
                    _loadingStatus.postValue(false)
                    _dataError.postValue(response.error)
                }
            }
        }
    }

    /*
    * This function is responsible to trigger the tasks as stated in the assignment
    * Tasks are performed under Coroutine scope i.e viewModelScope
    * Each function displays the result on the screen as soon as it is done.
    * */
    fun getRequiredData() {
        if (urlContent.isEmpty()) {
            readURL()
            return
        }
        findNthCharacter(content = urlContent)
        findEveryNthCharacter(content = urlContent)
        findOccurrences(content = urlContent)
    }

    /*
    * Finding the 15th or Nth character from the data string, using the get([] operator) function of string
    * */
    fun findNthCharacter(content: String, characterLocationToFind: Int = 15) {
        if (characterLocationToFind < 1)
            task1.set("Please enter a valid index")
        else if (content.length < characterLocationToFind) {
            task1.set("Data length is smaller than the $characterLocationToFind")
            return
        } else
            task1.set(content[characterLocationToFind - 1].toString()) // Cox the string starts from 0
    }

    /*
    * Finding the every 15th or Nth character in the data string,
    * As the string starts from 0 so the first character will be found at the 14th index not 15th
    * And the second character will be found at 29th index not 30th and so on
    * Using the Coroutine scope (Default) to avoid the work on main thread
    * Setting the data in the activity using data binding, With Main Thread context
    * */
    fun findEveryNthCharacter(content: String, characterLocationToFind: Int = 15) {
        var everyCharacter = ""

        if (characterLocationToFind < 1)
            task2.set("Please enter a valid index")
        else if (content.length < characterLocationToFind) {
            task2.set("Data length is smaller than the $characterLocationToFind")
            return
        } else
            viewModelScope.launch(defaultDispatcher) {
                var i = characterLocationToFind - 1
                while (i < content.length) {
                    everyCharacter += content[i]
                    i += characterLocationToFind
                }
                withContext(Dispatchers.Main) {
                    task2.set(everyCharacter)
                }
            }
    }

    /*
    * Splitting the data string into list of string on the basis of
    * space, tab and line break or multiple of these in the same line
    * Finding the occurrences of each string and saving the output in an hashmap
    * Adding the hashmap value in two separate lists to display on the screen
    * */
    fun findOccurrences(content: String) {
        viewModelScope.launch(defaultDispatcher) {
            val splitStrings = content.split("[\\s\\n\\r]+".toRegex())
            val hashMap: HashMap<String, Int> = LinkedHashMap()
            for (word in splitStrings) {
                var count = hashMap[word]
                if (count == null) {
                    count = 0
                }
                hashMap[word] = count + 1
            }

            words.clear()
            wordsCount.clear()

            hashMap.forEach { (s, i) ->
                words.add(s)
                wordsCount.add(i)
            }

            withContext(Dispatchers.Main) {
                _countCompleted.postValue(true)
            }
        }
    }
}