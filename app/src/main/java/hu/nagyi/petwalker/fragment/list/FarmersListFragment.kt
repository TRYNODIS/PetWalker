package hu.nagyi.petwalker.fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.firestore.*
import hu.nagyi.petwalker.MainFragment
import hu.nagyi.petwalker.R
import hu.nagyi.petwalker.adapter.FarmersAdapter
import hu.nagyi.petwalker.data.User
import hu.nagyi.petwalker.databinding.FragmentFarmersListBinding
import hu.nagyi.petwalker.fragment.AddUserDataFragment
import hu.nagyi.petwalker.fragment.ViewPagerFragment

class FarmersListFragment : MainFragment() {

    //region VARIABLES

    private var _binding: FragmentFarmersListBinding? = null
    private val binding get() = this._binding!!

    private lateinit var adapter: FarmersAdapter

    private lateinit var eventListener: EventListener<QuerySnapshot>
    private var listenerReg: ListenerRegistration? = null

    companion object {
        val TAG: String? = FarmersListFragment::class.simpleName

        @JvmStatic
        fun newInstance() =
            FarmersListFragment()
    }

    //endregion

    //region METHODS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        this._binding = FragmentFarmersListBinding.inflate(inflater, container, false)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initAdapterAndSetItToRV()

        this.initFirebaseQuery()

        this.binding.userFAB.setOnClickListener {
            this.requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.mainFrameLayout,
                    AddUserDataFragment.newInstance(),
                    AddUserDataFragment.TAG
                )
                .addToBackStack(null)
                .commit();
        }
    }

    private fun initAdapterAndSetItToRV() {
        this.adapter =
            FarmersAdapter(this.requireActivity(), this.currentUser!!.uid, this.collection)
        this.binding.farmersRV.adapter = this.adapter
    }

    private fun addUserToAdapter(documents: QuerySnapshot) {
        for (document in documents) {
            val user = document.toObject(User::class.java)
            this.adapter.addUser(user, document.id)
        }
    }

    private fun showToastError(exception: Exception) {
        Toast.makeText(
            this@FarmersListFragment.requireActivity(),
            "Error: ${exception.message}",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun initFirebaseQuery() {
        this.eventListener = object : EventListener<QuerySnapshot> {
            override fun onEvent(querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
                if (e != null) {
                    Toast.makeText(
                        this@FarmersListFragment.requireActivity(), "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }

                for (docChange in querySnapshot?.documentChanges!!) {
                    when (docChange.type) {
                        DocumentChange.Type.ADDED -> {
                            val advertisement =
                                docChange.document.toObject(User::class.java)
                            this@FarmersListFragment.adapter.addUser(
                                advertisement, docChange.document.id
                            )
                        }
                        DocumentChange.Type.REMOVED -> {
                            this@FarmersListFragment.adapter.removeUserByKey(
                                docChange.document.id
                            )
                        }
                        DocumentChange.Type.MODIFIED -> {

                        }
                    }
                }

            }
        }

        this.listenerReg = this.collection.addSnapshotListener(this.eventListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this._binding = null
    }

    //endregion

}