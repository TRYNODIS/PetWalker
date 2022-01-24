package hu.nagyi.petwalker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.nagyi.petwalker.databinding.ActivityMainBinding
import hu.nagyi.petwalker.fragment.StartFragment

class MainActivity : AppCompatActivity() {

    //region VARIABLES

    private lateinit var binding: ActivityMainBinding

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
    }

    //endregion
}