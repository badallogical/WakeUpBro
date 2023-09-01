package com.example.wakeupbro.data

import android.content.Context

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object Datastore {

    private val Context.userdatastore: DataStore<Preferences> by preferencesDataStore(
        name = "alarm_settings"
    )


    val math = booleanPreferencesKey("MATH_METHOD")
    val qr = stringPreferencesKey("QR_DATA")


    suspend fun saveMethodMaths(context: Context, selection: Boolean) {
            context.userdatastore.edit {
                it[math] = selection
            }
    }

    suspend fun saveQrResult( context: Context, qr_result : String ){
        context.userdatastore.edit {
            it[qr] = qr_result
        }
    }

    fun getBooleanValues(context: Context, key: Preferences.Key<Boolean>): Flow<Boolean> {
        return context.userdatastore.data.map {
            it[key] ?: false
        }
    }

    fun getStringValue( context: Context, key: Preferences.Key<String>) : Flow<String>{
        return context.userdatastore.data.map {
            it[key] ?: ""
        }
    }

}
