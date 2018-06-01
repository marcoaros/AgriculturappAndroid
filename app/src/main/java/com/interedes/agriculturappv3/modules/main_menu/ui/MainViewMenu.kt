package com.interedes.agriculturappv3.modules.main_menu.ui

import android.content.Context
import android.support.v7.app.AlertDialog
import com.google.firebase.auth.FirebaseUser
import com.interedes.agriculturappv3.modules.main_menu.ui.events.RequestEventMainMenu
import com.interedes.agriculturappv3.modules.models.sincronizacion.QuantitySync
import com.interedes.agriculturappv3.modules.models.usuario.Usuario

/**
 * Created by EnuarMunoz on 8/03/18.
 */
interface MainViewMenu {


    interface MainView {
        fun onConnectivity()
        fun offConnectivity()

        //ProgresHud
        fun showProgressHud()
        fun hideProgressHud()

        fun showProgressBar()
        fun hideProgressBar()

        fun onMessageOk(colorPrimary: Int, message: String?)
        fun onMessageError(colorPrimary: Int, message: String?)

        fun requestResponseOK()
        fun requestResponseError(error: String?)

        //Alerts Dialogs
        //Verifcate conection
        fun verificateConnection(): AlertDialog?
        fun  verificateSync(quantitySync: QuantitySync?): AlertDialog?
       // fun showAlertTypeChat(): AlertDialog?
        fun showAlertTypeChat()
        fun showAlertDialogSyncDataConfirm()

        fun setQuantitySync(quantitySync: QuantitySync?)

        fun getListasIniciales()
    }

    interface Presenter {
        fun onCreate()
        fun onDestroy(context: Context)
        fun onResume(context: Context)
        fun onEventMainThread(event: RequestEventMainMenu?)

        //syncData
        fun syncData()
        fun syncQuantityData()

        fun makeUserOnline()
        fun makeUserOffline()
        fun logOut(usuario: Usuario?)

        //Conection
        fun checkConnection(): Boolean?

        //
        fun getListasIniciales()
        fun getLastUserLogued(): Usuario?
        fun getListSyncEnfermedadesAndTratamiento()




    }

    interface Interactor {
        fun makeUserOnline(checkConection:Boolean)
        fun makeUserOffline(checkConection:Boolean)
        fun getListasIniciales()
        fun syncQuantityData()
        fun syncData()
        fun getLastUserLogued(): Usuario?
        fun getListSyncEnfermedadesAndTratamiento()


        fun logOut(usuario: Usuario?)
    }


    interface Repository {
        //firebase
        fun makeUserOnline(checkConection:Boolean)
        fun makeUserOffline(checkConection:Boolean)
        fun verificateUserLoguedFirebaseFirebase(): FirebaseUser?
        fun loginFirebase(usuario:Usuario?)
        fun logOut(usuario: Usuario?)



        fun getListasIniciales()
        fun syncQuantityData()
        fun syncData()
        fun getLastUserLogued(): Usuario?
        fun getListSyncEnfermedadesAndTratamiento()
    }
}