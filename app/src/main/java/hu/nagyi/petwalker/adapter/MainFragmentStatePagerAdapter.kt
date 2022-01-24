package hu.nagyi.petwalker.adapter

import androidx.viewpager2.adapter.FragmentStateAdapter
import hu.nagyi.petwalker.MainFragment
import hu.nagyi.petwalker.fragment.list.FarmersListFragment
import hu.nagyi.petwalker.fragment.list.WalkersListFragment

class MainFragmentStatePagerAdapter(fragment: MainFragment, private val fragmentsCount: Int) :
    FragmentStateAdapter(fragment) {

    //region METHODS

    override fun getItemCount(): Int {
        return this.fragmentsCount
    }

    override fun createFragment(position: Int): MainFragment {
        return if (position == 0) {
            WalkersListFragment.newInstance()
        } else {
            FarmersListFragment.newInstance()
        }
    }

    //endregion

}