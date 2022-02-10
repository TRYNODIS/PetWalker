package hu.nagyi.petwalker.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import hu.nagyi.petwalker.BuildConfig
import hu.nagyi.petwalker.MainFragment
import hu.nagyi.petwalker.R
import hu.nagyi.petwalker.databinding.FragmentStartBinding

class StartFragment : MainFragment() {

    //region VARIABLES

    private var _binding: FragmentStartBinding? = null
    private val binding get() = this._binding!!

    companion object {
        val TAG: String? = StartFragment::class.simpleName

        @JvmStatic
        fun newInstance() =
            StartFragment()
    }

    //endregion

    //region METHODS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this._binding = FragmentStartBinding.inflate(inflater, container, false)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding.logInBtn.setOnClickListener(View.OnClickListener {
            this.logIn()

        })

        this.binding.registerBtn.setOnClickListener(View.OnClickListener {
            this.register()
        })

        this.binding.versionTV.text = String.format(
            this.getString(R.string.version), ": ", BuildConfig.VERSION_NAME
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this._binding = null
    }

    private fun logIn() {
        if (!this.isFormValid()) {
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            this.binding.emailTIET.text.toString(),
            this.binding.pwTIET.text.toString()
        ).addOnSuccessListener {
            this.requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.mainFrameLayout,
                    ViewPagerFragment.newInstance(),
                    ViewPagerFragment.TAG
                )
                .addToBackStack(null)
                .commit();
        }.addOnFailureListener {
            Toast.makeText(
                this.requireActivity(),
                String.format(this.getString(R.string.error), ": ", it.message),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun register() {
        if (!this.isFormValid()) {
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            this.binding.emailTIET.text.toString(),
            this.binding.pwTIET.text.toString()
        ).addOnSuccessListener {
            Toast.makeText(
                this.requireActivity(),
                this.getString(R.string.registrationSuccessful),
                Toast.LENGTH_LONG
            ).show()
        }.addOnFailureListener {
            Toast.makeText(
                this.requireActivity(),
                String.format(this.getString(R.string.error), ": ", { it.message }),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun isFormValid(): Boolean {
        return when {
            this.binding.emailTIET.text!!.isEmpty() -> {
                this.binding.emailTIET.error = this.getString(R.string.thisFieldCanNotBeEmpty)
                false
            }
            this.binding.pwTIET.text!!.isEmpty() -> {
                this.binding.pwTIET.error = this.getString(R.string.thisFieldCanNotBeEmpty)
                false
            }
            else -> true
        }
    }

    //endregion

}