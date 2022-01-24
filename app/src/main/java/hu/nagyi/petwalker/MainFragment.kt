package hu.nagyi.petwalker

import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hu.nagyi.petwalker.data.User

open class MainFragment : Fragment() {

    //region VARIABLES

    val collection = FirebaseFirestore.getInstance().collection("users")
    val currentUser = FirebaseAuth.getInstance().currentUser
    var users = mutableListOf<User>()

    //endregion

    //region METHODS

    //endregion

}