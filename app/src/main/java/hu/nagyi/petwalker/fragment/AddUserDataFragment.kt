package hu.nagyi.petwalker.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.isVisible
import hu.nagyi.petwalker.MainFragment
import hu.nagyi.petwalker.R
import hu.nagyi.petwalker.data.User
import hu.nagyi.petwalker.databinding.FragmentAddUserDataBinding

class AddUserDataFragment : MainFragment() {

    //region VARIABLES

    private var _binding: FragmentAddUserDataBinding? = null
    private val binding get() = this._binding!!

    companion object {
        val TAG: String? = AddUserDataFragment::class.simpleName

        @JvmStatic
        fun newInstance() =
            AddUserDataFragment()
    }

    //endregion

    //region METHODS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this._binding = FragmentAddUserDataBinding.inflate(this.layoutInflater)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.getUserData()
        this.binding.saveBtn.setOnClickListener(View.OnClickListener {
            this.uploadUserData()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this._binding = null
    }

    private fun getUserData() {
        this.collection.get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    this.users.addAll(it.toObjects(User::class.java))
                    Log.d(
                        TAG,
                        String.format(this.getString(R.string.onSuccess), ": ", { this.users })
                    )
                    this.setControlsValue()
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    this.requireActivity(),
                    String.format(this.getString(R.string.error), { it.message }),
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun setControlsValue() {
        for (index in this.users.indices) {
            if (this.currentUser!!.uid == this.users[index].uid) {
                this.binding.firstNameTIET.setText(this.users[index].firstName)
                this.binding.lastNameTIET.setText(this.users[index].lastName)
                this.binding.addressTIET.setText(this.users[index].address)
                this.binding.cityTIET.setText(this.users[index].city)
                this.binding.postCodeTIET.setText(this.users[index].postCode)
                this.binding.phoneTIET.setText(this.users[index].phoneNo)

                if (this.users[index].type == this.binding.walkerRB.text) {
                    this.typeWalkerCase(index)
                } else {
                    this.typeFarmerCase()
                }

                this.binding.typeRG.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
                    when {
                        this.binding.walkerRB.id == radioGroup.checkedRadioButtonId -> {
                            this.typeWalkerCase(index)
                        }
                        this.binding.farmerRB.id == radioGroup.checkedRadioButtonId -> {
                            this.typeFarmerCase()
                        }
                    }
                })
            }
        }
    }

    private fun typeWalkerCase(index: Int) {
        this.binding.walkerRB.isChecked = true
        this.binding.priceTIET.visibility = View.VISIBLE
        this.binding.priceTIET.setText(this.users[index].price)
    }

    private fun typeFarmerCase() {
        this.binding.farmerRB.isChecked = true
        this.binding.priceTIET.visibility = View.GONE
        this.binding.priceTIET.setText("")
    }

    private fun uploadUserData() {
        this.collection.document(this.currentUser!!.uid).set(
            User(
                this.currentUser.uid,
                this.currentUser.email!!,
                this.binding.firstNameTIET.text.toString(),
                this.binding.lastNameTIET.text.toString(),
                this.binding.addressTIET.text.toString(),
                this.binding.cityTIET.text.toString(),
                this.binding.postCodeTIET.text.toString(),
                this.binding.phoneTIET.text.toString(),
                if (this.binding.priceTIET.isVisible) this.binding.priceTIET.text.toString() else "0",
                if (this.binding.walkerRB.isChecked)
                    this.binding.walkerRB.text.toString()
                else
                    this.binding.farmerRB.text.toString()
            )
        )
            .addOnSuccessListener {
                Toast.makeText(
                    this.requireActivity(),
                    this.getString(R.string.userSaved), Toast.LENGTH_LONG
                ).show()

                this.requireActivity().supportFragmentManager.popBackStack()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this.requireActivity(),
                    String.format(this.getString(R.string.error), { it.message }),
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    //endregion

}