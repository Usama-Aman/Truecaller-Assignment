package com.truecaller.assignment.utilities

sealed class RepositoryResource<out T> {
    data class Success<out T>(val value: T) : RepositoryResource<T>()
    data class Error(val error: String) : RepositoryResource<Nothing>()
}