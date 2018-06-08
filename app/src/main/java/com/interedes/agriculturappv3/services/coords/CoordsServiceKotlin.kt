package com.interedes.agriculturappv3.services.coords

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.os.*
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.interedes.agriculturappv3.R
import com.interedes.agriculturappv3.modules.models.Coords
import com.interedes.agriculturappv3.services.Const
import android.view.WindowManager



/**
 * Created by usuario on 13/03/2018.
 */
class CoordsServiceKotlin(): Service(), LocationListener {

    var latitud: Double = 0.toDouble()
    var longitud: Double = 0.toDouble()
    var texto: TextView?=null
    private val PACKAGE_NAME = "com.interedes.agriculturappv3"
    //
    var serviceLocalizacionRun:Boolean? = false
    val location: Location?=null
    internal var gpsActivo: Boolean = false
    var locationManager: LocationManager? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Servicio Iniciado", Toast.LENGTH_SHORT).show()
        getLocation(applicationContext)
        // If we get killed, after returning from here, restart
        return START_STICKY
        //return Service.START_REDELIVER_INTENT
    }

    fun closeService() {
        this.stopSelf()
        if (locationManager != null) {
            locationManager!!.removeUpdates(this)
        }
    }

    fun setView(v: View) {
        texto = v as TextView
        texto!!.text = "Coordenadas: $latitud,$longitud"
    }

    @SuppressLint("MissingPermission")
    fun getLocation(activity: Context) {
        try {
            locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            gpsActivo = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

        } catch (ex: Exception) {

        }

        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)

    }


    override fun onLocationChanged(location: Location) {
        setLocation(location)
    }

    override fun onStatusChanged(s: String, status: Int, bundle: Bundle) {
        when (status) {
            LocationProvider.AVAILABLE ->  Log.i("GPS", "Disponible")
            LocationProvider.OUT_OF_SERVICE -> showStatusGPS("Proveedor fuera de servicio de localización")
            LocationProvider.TEMPORARILY_UNAVAILABLE -> {
                //showStatusGPS("Temporalmente no disponible el servicio de GPS")
            }
        }
    }

    override fun onProviderEnabled(s: String) {
        Toast.makeText(applicationContext, "GPS Activado", Toast.LENGTH_SHORT).show()
        val is_enabled_gps=true
        val retIntent = Intent(Const.SERVICE_LOCATION)
        retIntent.setPackage(PACKAGE_NAME)
        retIntent.putExtra("is_enabled_gps", is_enabled_gps)
        applicationContext?.sendBroadcast(retIntent)

    }

    override fun onProviderDisabled(s: String) {
        Toast.makeText(applicationContext,"GPS Desactivado",Toast.LENGTH_SHORT).show();

        val is_enabled_gps=false
        val retIntent = Intent(Const.SERVICE_LOCATION)
        retIntent.setPackage(PACKAGE_NAME)
        retIntent.putExtra("is_enabled_gps", is_enabled_gps)
        applicationContext?.sendBroadcast(retIntent)

    }

    fun setLocation(loc: Location) {
        serviceLocalizacionRun = true
       // location = loc
        latitud = loc.latitude
        longitud = loc.longitude

        val retIntent = Intent(Const.SERVICE_LOCATION)
        retIntent.setPackage(PACKAGE_NAME)
        retIntent.putExtra("latitud", latitud)
        retIntent.putExtra("longitud", longitud)
        //retIntent.putExtra("initial", "" );
        applicationContext?.sendBroadcast(retIntent)
    }


    private fun showStatusGPS(msg: String) {
        val dialog = android.app.AlertDialog.Builder(baseContext)
        dialog.setMessage(msg).setCancelable(false)
                .setPositiveButton("OK") { dialogInterface, i -> dialogInterface.dismiss() }
        val alert = dialog.create()
        alert.show()
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        closeService()
        Toast.makeText(this, "Servicio GPS Cerrrado", Toast.LENGTH_SHORT).show()
    }
}