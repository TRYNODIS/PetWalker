package hu.nagyi.petwalker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.CollectionReference
import hu.nagyi.petwalker.R
import hu.nagyi.petwalker.data.User
import hu.nagyi.petwalker.databinding.RowFarmersListBinding
import hu.nagyi.petwalker.databinding.RowWalkersListBinding
import hu.nagyi.petwalker.fragment.AddUserDataFragment

class FarmersAdapter(
    var context: Context,
    var currentUID: String,
    var collection: CollectionReference
) :
    RecyclerView.Adapter<FarmersAdapter.ViewHolder>() {

    //region VARIABLES

    var usersList = mutableListOf<User>()
    var userKeys = mutableListOf<String>()

    //endregion

    //region METHODS

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowFarmersListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return this.usersList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = this.usersList[holder.adapterPosition]

        if (user.type == "Farmer") {
            holder.typeTV.text = user.type
            holder.firstNameTV.text = user.firstName
            holder.lastNameTV.text = user.lastName
            holder.emailTV.text = user.email
            holder.phoneNoTV.text = user.phoneNo


            if (this.currentUID == user.uid) {
                holder.modifyBtn.visibility = View.VISIBLE
                holder.detailsBtn.visibility = View.GONE
                holder.deleteBtn.visibility = View.VISIBLE
            } else {
                holder.modifyBtn.visibility = View.GONE
                holder.detailsBtn.visibility = View.VISIBLE
                holder.deleteBtn.visibility = View.GONE
            }

            holder.modifyBtn.setOnClickListener {
                ((context) as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.mainFrameLayout,
                        AddUserDataFragment.newInstance(),
                        AddUserDataFragment.TAG
                    )
                    .addToBackStack(null)
                    .commit();
            }

            holder.detailsBtn.setOnClickListener {
                println("details")
            }

            holder.deleteBtn.setOnClickListener {
                this.removeUser(holder.adapterPosition)
            }
        } else {
            holder.itemView.visibility = View.GONE
        }
    }

    fun addUser(user: User, key: String) {
        this.usersList.add(user)
        this.userKeys.add(key)
        this.notifyItemInserted(this.usersList.lastIndex)
    }

    private fun removeUser(index: Int) {
        this.collection.document(this.userKeys[index]).delete()

        this.usersList.removeAt(index)
        this.userKeys.removeAt(index)
        this.notifyItemRemoved(index)
    }

    fun removeUserByKey(key: String) {
        val index = this.userKeys.indexOf(key)
        if (index != -1) {
            this.usersList.removeAt(index)
            this.userKeys.removeAt(index)
            this.notifyItemRemoved(index)
        }
    }

    //endregion

    //region INNER CLASS VIEW HOLDER

    inner class ViewHolder(private val binding: RowFarmersListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var typeTV = this.binding.typeTV
        var firstNameTV = this.binding.firstNameTV
        var lastNameTV = this.binding.lastNameTV
        var emailTV = this.binding.emailTV
        var phoneNoTV = this.binding.phoneNoTV

        var detailsBtn = this.binding.detailsBtn
        var modifyBtn = this.binding.modifyBtn
        var deleteBtn = this.binding.deleteBtn
    }

    //endregion
}