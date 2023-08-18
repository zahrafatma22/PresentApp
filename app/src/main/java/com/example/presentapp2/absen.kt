package com.example.presentapp2

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.presentapp2.data.Present
import com.example.presentapp2.data.PresentViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Locale

class absen : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mMap  : GoogleMap //VAR MAP
    lateinit var mLocationRequest: LocationRequest //VAR MINTA LOKASI
    private lateinit var mPresentViewModel : PresentViewModel
    private lateinit var myPosition : LatLng

    internal var mFusedLocationClient: FusedLocationProviderClient? = null  // IDK

    private var status =""
    var mLastLocation: Location? = null // VAR LOKASI TERAKHIR
    var mCurrentLocationMarker: Marker? = null  // MARKER LOKASI SAAT INI
    var cevestLocationMarker : Marker? = null   // MARKER LOKASI KANTOR
    var garisJarak : Polyline? = null           // GARIS
    var checkIn : String = ""

    internal var mLocationCallback: LocationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult){
            val locationList = locationResult?.locations
            Log.d("CEKUP","INTERNAL")
            if(locationList!!.isNotEmpty()){
                val location = locationList.last()
                Log.i("cek",location.toString())
                mLastLocation = location
                if(mCurrentLocationMarker != null){
                    mCurrentLocationMarker?.remove()
                }

                if(cevestLocationMarker !=null){
                    cevestLocationMarker?.remove()
                }

                if(garisJarak != null){
                    garisJarak?.remove()
                }
                // ============================= OUR LOCATION START
                myPosition = LatLng(location.latitude,location.longitude)
                val markerOptions = MarkerOptions()
                markerOptions.position(myPosition)

                markerOptions.title("Posisi sekarang "+location.latitude+","+location.longitude.toString())
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                mCurrentLocationMarker = mMap.addMarker(markerOptions)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition,15.0F))
                // ============================= OUR LOCATION END
                // ============================= KANTOR LOCATION START
                val bbpvpLocation = LatLng(-6.2347677,106.987864)

                cevestLocationMarker = mMap.addMarker(
                    MarkerOptions()
                        .position(bbpvpLocation)
                        .title("BBPVP BEKASI")
                        .icon(
                            BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                        )
                )

                val circleOptions = CircleOptions()
                circleOptions.center(bbpvpLocation)

                circleOptions.radius(1000.0)
                circleOptions.strokeColor(Color.RED)
                mMap.addCircle(circleOptions)
                // =========================== KANTOR LOCATION END
                // =========================== GARIS JARAK START
                val distance = FloatArray(2)
                Location.distanceBetween(
                    bbpvpLocation.latitude,bbpvpLocation.longitude,location.latitude,location.longitude,distance
                )

                if (distance[0] > circleOptions.radius){
                    status = "WFH"
                }else{
                    status = "WFO"
                }

                garisJarak = mMap.addPolyline(
                    PolylineOptions().add(bbpvpLocation,
                        LatLng(location.latitude, location.longitude)
                    )
                        .width(5.0F)
                        .color(Color.YELLOW)
                )
                // ========================== GARIS JARAK END
                val geocoder: Geocoder
                val addresses: List<Address>?
                val latitude = location.latitude
                val longitude = location.longitude
                geocoder = Geocoder(this@absen, Locale.getDefault())

                addresses = geocoder.getFromLocation(latitude,longitude,1)

                val address =
                    addresses!![0].getAddressLine(0)

                checkIn = "<b>Posisi anda Saat ini</b> : $address "
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absen)
        Log.d("CEKUP","INI ONCREATE")
        var buttonCheck = findViewById<Button>(R.id.butt_check)
        var tvLokasi = findViewById<TextView>(R.id.tv_lok)
        var tvStatus = findViewById<TextView>(R.id.tv_status)
        mPresentViewModel = ViewModelProvider(this).get(PresentViewModel::class.java)

        buttonCheck.setOnClickListener(){
            insertDataToDatabase(status)
            val pesan = "Status Bekerja anda saat ini : $status"
            tvStatus.text = pesan
            tvLokasi.text = Html.fromHtml(checkIn)
            buttonCheck.text = "Sudah Absen"
            buttonCheck.setEnabled(false)
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType= GoogleMap.MAP_TYPE_HYBRID
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 50000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return Toast.makeText(this,"Izinkan Akses lokasi", Toast.LENGTH_LONG).show()
        }
        mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
        googleMap.isMyLocationEnabled = true
    }

    private fun insertDataToDatabase(status : String){
        val username = arrayOf("Fatma","Jaka","Aji","Ray","Muti","Jae","arif","Samsul","Bagas","Panji","Cikal","Keisar",
            "Azis","Dipo","Debora","Rizal")
        val calendar = Calendar.getInstance()
        val current = LocalDateTime.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND))
        val tgl = "${current.dayOfMonth} ${current.month} ${current.year}"
        val jam = "${current.hour}:${current.minute}"
        val presensi = Present(0,tgl,jam,status)

//        Log.d("CEKUP," "$tgl, $jam , $status")
        mPresentViewModel.addPresent(presensi)
    }
}