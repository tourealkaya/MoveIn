package project.movein.viewmodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


sealed interface michemResult{
    @Parcelize
    object EmptyResult : michemResult, Parcelable

    @Parcelize
    class Calc(val result : String) : michemResult, Parcelable
}