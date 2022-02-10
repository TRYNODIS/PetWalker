package hu.nagyi.petwalker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import hu.nagyi.petwalker.databinding.ActivityMainBinding
import hu.nagyi.petwalker.fragment.StartFragment
import hu.nagyi.petwalker.services.MainForegroundService

class MainActivity : AppCompatActivity() {

    //region VARIABLES

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 1001
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    //endregion

    //region METHODS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(this.layoutInflater)
        this.setContentView(this.binding.root)

        if (savedInstanceState == null) {
            this.supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.mainFrameLayout, StartFragment.newInstance(), StartFragment.TAG)
                .addToBackStack(null)
                .commit();
        }

        if (this.allPermissionsGranted()) {
            this.startService(Intent(this, MainForegroundService::class.java))
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (this.allPermissionsGranted()) {
                this.startService(Intent(this, MainForegroundService::class.java))
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                this.finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        this.stopService(Intent(this, MainForegroundService::class.java))
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (this.supportFragmentManager.backStackEntryCount == 1) {
            this.supportFragmentManager.popBackStack()
        }

        super.onBackPressed()
    }

    //endregion
}