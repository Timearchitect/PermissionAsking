package se.gritacademy.permissionasking

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    val MY_REQUEST_CODE = 1;
    val perm :String = android.Manifest.permission.RECORD_AUDIO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ListAllPermissions()

        //if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

        //checkSelfPermission retunerar -1 eller 0(har tillstånd)
        Log.d("Alrik",ContextCompat.checkSelfPermission(this, perm).toString())
        //requestAudioPermissions()



        findViewById<Button>(R.id.button).setOnClickListener { v->
            run {
                revokePermission()
            }
        }
    }


    private fun requestAudioPermissions() {


        if (ContextCompat.checkSelfPermission( this, perm )== PackageManager.PERMISSION_DENIED
        ) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    perm )
            ) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG)
                    .show()

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(
                    this, arrayOf<String>(perm),
                    MY_REQUEST_CODE
                )
            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(
                    this, arrayOf<String>(perm), MY_REQUEST_CODE
                )
            }
        } else if (ContextCompat.checkSelfPermission(this,perm) == PackageManager.PERMISSION_GRANTED
        ) {

            Log.d("Alrik","vi har tillåtelse redan för $perm")

        }
    }

    fun revokePermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { //i api 33 går att dra tillbaka tillståndet
            if(ContextCompat.checkSelfPermission(this,perm) == PackageManager.PERMISSION_GRANTED) {
                this.revokeSelfPermissionOnKill(android.Manifest.permission.RECORD_AUDIO)
                Toast.makeText(this, "${perm.lowercase()} is revoked now", Toast.LENGTH_SHORT)
                    .show()
            }else Toast.makeText(this, "the permission is not granted", Toast.LENGTH_SHORT)
                .show()
        }else Toast.makeText(this, "api level ${Build.VERSION.SDK_INT} is to low for the feature revoking permission", Toast.LENGTH_SHORT)
            .show()
    }

    fun ListAllPermissions(){
        val context: Context = this
        val pm = context.packageManager
        var csPermissionGroupLabel: CharSequence
        var csPermissionLabel: CharSequence

        val lstGroups = pm.getAllPermissionGroups(0)
        for (pgi in lstGroups) {
            csPermissionGroupLabel = pgi.loadLabel(pm)
            Log.e("perm", pgi.name + ": " + csPermissionGroupLabel.toString() ) //danger
            try {
                val lstPermissions = pm.queryPermissionsByGroup(pgi.name, 0)
                for (pi in lstPermissions) {
                    csPermissionLabel = pi.loadLabel(pm)
                    when(pi.protection){
                    1-> Log.i("perm", "   ${pi.name}:${pi.protection} ,  $csPermissionLabel")
                    2-> Log.d("perm", "   ${pi.name}:${pi.protection} ,  $csPermissionLabel")
                    3-> Log.w("perm", "   ${pi.name}:${pi.protection} ,  $csPermissionLabel")
                    4-> Log.e("perm", "   ${pi.name}:${pi.protection} ,  $csPermissionLabel")
                        else -> Log.v("perm", "   ${pi.name}:${pi.protection} ,  $csPermissionLabel" )
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}