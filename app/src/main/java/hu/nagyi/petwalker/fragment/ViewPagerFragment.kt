package hu.nagyi.petwalker.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import hu.nagyi.petwalker.MainFragment
import hu.nagyi.petwalker.R
import hu.nagyi.petwalker.adapter.MainFragmentStatePagerAdapter
import hu.nagyi.petwalker.databinding.FragmentViewPagerBinding
import hu.nagyi.petwalker.transformation.HorizontalFlipTransformation

class ViewPagerFragment : MainFragment() {

    //region VARIABLES

    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!
    private lateinit var pageNames: Array<String>

    companion object {
        val TAG: String? = ViewPagerFragment::class.simpleName

        @JvmStatic
        fun newInstance() =
            ViewPagerFragment()
    }

    //endregion

    //region METHODS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this._binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.pageNames = this.resources.getStringArray(R.array.tab_names)

        this.binding.mainVP.adapter =
            MainFragmentStatePagerAdapter(this@ViewPagerFragment, 2)

        this.binding.mainVP.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        this.binding.mainVP.setPageTransformer(HorizontalFlipTransformation())


        TabLayoutMediator(this.binding.mainTL, this.binding.mainVP) { tab, position ->
            tab.text = this.pageNames[position]
        }.attach()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        this._binding = null
    }

    //endregion
}