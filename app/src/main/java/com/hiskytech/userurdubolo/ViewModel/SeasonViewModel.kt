package com.hiskytech.userurdubolo.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.admin.Constants
import com.admin.Data.Repo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.hiskytech.userurdubolo.Data.SharedPrefManager
import com.hiskytech.userurdubolo.Model.ModelSeason

class SeasonViewModel(context: Application) : AndroidViewModel(context) {

    private val repo = Repo(context)
    private val sharedPrefManager = SharedPrefManager(context)
    private var constants = Constants()
    private var context = context


    suspend fun addSeason(modelSeason: ModelSeason): LiveData<Boolean> {
        return repo.addSeason(modelSeason)
    }

    suspend fun getSeasonList(docId:String): Task<QuerySnapshot> {

        return  repo.getSeasonList(docId)
    }

    suspend fun updateSeason(modelSeason: ModelSeason): LiveData<Boolean>
    {
       return  repo.updateSeason(modelSeason)
    }   fun deleteSeason(modelSeason: ModelSeason): LiveData<Boolean>
    {
       return  repo.deleteSeason(modelSeason)
    }


    suspend fun getSeasonbyId(type:String):Task<DocumentSnapshot>
    {
        return repo.getSeasonbyId(type)
    }
    suspend fun getHudutsuzVideoList(name:String):Task<QuerySnapshot>
    {
        return repo.getHudutsuzVideoList(name)
    }
    suspend fun getAllSeasons():Task<QuerySnapshot>
    {
        return repo.getAllSeasonsname()
    }
}