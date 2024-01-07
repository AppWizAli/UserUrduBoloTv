package com.hiskytech.userurdubolo.Data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.hiskytech.userurdubolo.Model.Admin
import com.hiskytech.userurdubolo.Model.ModelDrama
import com.hiskytech.userurdubolo.Model.ModelSeason
import com.hiskytech.userurdubolo.Model.ModelUser
import com.hiskytech.userurdubolo.Model.ModelVideo
import java.lang.reflect.Type

class SharedPrefManager(var context: Context) {


    private val sharedPref: SharedPreferences = context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()



    fun getDramaList(): List<ModelDrama>{

        val json = sharedPref.getString("ListDrama", "") ?: ""
        val type: Type = object : TypeToken<List<ModelDrama?>?>() {}.getType()
        return Gson().fromJson(json, type)
    }

    fun putDramaList(list: List<ModelDrama>) {
        editor.putString("ListDrama", Gson().toJson(list))
        editor.commit()
    }
    fun putAllSeasonsList(list: List<ModelSeason>) {
        editor.putString("AllSeasons", Gson().toJson(list))
        editor.commit()
    }
    fun putAllSeasonsList(): List<ModelSeason>{

        val json = sharedPref.getString("AllSeasons", "") ?: ""
        val type: Type = object : TypeToken<List<ModelSeason?>?>() {}.getType()
        return Gson().fromJson(json, type)
    }
    fun putHudutsuzVideoList(list: List<ModelVideo>) {
        editor.putString("HudutsuzVideoList", Gson().toJson(list))
        editor.commit()
    }
    fun getHudutsuzVideoList(): List<ModelVideo>{

        val json = sharedPref.getString("HudutsuzVideoList", "") ?: ""
        val type: Type = object : TypeToken<List<ModelVideo?>?>() {}.getType()
        return Gson().fromJson(json, type)
    }

    fun putUserList(list: List<ModelUser>) {
        editor.putString("ListUsers", Gson().toJson(list))
        editor.commit()
    }  fun putPrivateVideoList(list: List<ModelVideo>) {
        editor.putString("PrivateVideos", Gson().toJson(list))
        editor.commit()
    }
    fun getPrivateVideoList(): List<ModelVideo> {
        val json = sharedPref.getString("PrivateVideos", "") ?: ""
        val type: Type = object : TypeToken<List<ModelVideo?>?>() {}.type

        return Gson().fromJson(json, type) ?: emptyList()
    }  fun getUserList(): List<ModelUser> {
        val json = sharedPref.getString("ListUsers", "") ?: ""
        val type: Type = object : TypeToken<List<ModelUser?>?>() {}.type

        return Gson().fromJson(json, type) ?: emptyList()
    }
    fun getSeasonList(): List<ModelSeason> {
        val json = sharedPref.getString("ListSeasons", "") ?: ""
        val type: Type = object : TypeToken<List<ModelSeason?>?>() {}.type

        return Gson().fromJson(json, type) ?: emptyList()
    }


    fun putSeasonList(list: List<ModelSeason>) {
        editor.putString("ListSeasons", Gson().toJson(list))
        editor.commit()
    }
    fun getPublicVideoList(): ArrayList<ModelVideo> {
        val json = sharedPref.getString("PublicListVideo", "") ?: ""
        val type: Type = object : TypeToken<ArrayList<ModelVideo>>() {}.type

        return Gson().fromJson(json, type) ?: ArrayList()
    }



    fun putPublicVideoList(list: ArrayList<ModelVideo>) {
        editor.putString("PublicListVideo", Gson().toJson(list))
        editor.commit()
    }


    public  fun saveUserLogin(isLoggedIn:Boolean)
    {
        editor.putBoolean("isLoggedIn",isLoggedIn)
        editor.commit()
    }
    public  fun SaveCelebration(isLoggedIn:Boolean) {
        editor.putBoolean("IsCelebration", isLoggedIn)
        editor.commit()
    }
        fun saveUser(user: ModelUser) {

            editor.putString("user", Gson().toJson(user))
            editor.commit()


        }

    fun getUser(): ModelUser {
        val json = sharedPref.getString("user", "") ?: ""

        // If the JSON string is empty or null, return a default InvestmentModel object
        if (json.isEmpty()) {
            return ModelUser() // Replace this with your default InvestmentModel constructor
        }

        // Try to deserialize the JSON string into an InvestmentModel object
        return try {
            Gson().fromJson(json, ModelUser::class.java)
        } catch (e: JsonSyntaxException) {
            // If the deserialization fails, return a default InvestmentModel object
            ModelUser() // Replace this with your default InvestmentModel constructor
        }
    }



    public  fun isLoggedIn():Boolean
    {
      return  sharedPref.getBoolean("isLoggedIn",false)
    }
    public  fun isCeleBration():Boolean
    {
      return  sharedPref.getBoolean("IsCelebration",false)!!
    }

    public fun clearWholeSharedPrefrences()

    {
        editor.clear()
        editor.commit()
    }
}