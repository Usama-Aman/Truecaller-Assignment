package com.truecaller.assignment.data

import com.truecaller.assignment.utilities.RepositoryResource
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton


class MainRepository @Inject constructor() {

    /*
    * Using the simple Java.Net URL to fetch the content from the URL
    * */
    suspend fun getURLContent(urlString: String): RepositoryResource<String> {
        val urlContent = URL(urlString).readText()
        return try {
            if (urlContent.isNotEmpty())
                withContext(Dispatchers.Main) {
                    RepositoryResource.Success(urlContent)
                }
            else
                RepositoryResource.Error("Something went wrong")
        } catch (e: Exception) {
            e.printStackTrace()
            RepositoryResource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

}