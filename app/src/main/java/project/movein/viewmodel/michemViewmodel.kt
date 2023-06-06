package project.movein.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import project.movein.backend.michem
import java.io.InputStream


private const val STATE_KEY_RESULT = " state_key_result "

class michemViewmodel( state : SavedStateHandle) : ViewModel() {

    private val _Result : MutableLiveData <michemResult > = state.getLiveData ( STATE_KEY_RESULT ,
        michemResult.EmptyResult )
    val Result : LiveData < michemResult > = _Result
    val Michem = michem()
    fun calculer(plan : InputStream,depart :String ,arv : String) {
        val Michem = michem()
        val data = Michem.getData(plan)
        val graph = Michem.getGraph(data["chemins"] as List<List<String>>)

        _Result.value =michemResult.Calc(Michem.tab2req(data, Michem.dijkstra(graph,depart,arv) as MutableList<String>)!!)
    }

}