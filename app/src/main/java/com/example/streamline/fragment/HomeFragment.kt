package com.example.streamline.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.streamline.R
import com.example.streamline.activity.BookmarkActivity
import com.example.streamline.activity.MainActivity
import com.example.streamline.activity.changedTab
import com.example.streamline.activity.checkForInternet
import com.example.streamline.adapter.BookmarkAdapter
import com.example.streamline.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.bind(view)

        return view
    }

    override fun onResume() {
        super.onResume()

        val mainActivityRef = requireActivity() as MainActivity

        MainActivity.tabsBtn.text = MainActivity.tabsList.size.toString()
        MainActivity.tabsList[MainActivity.myPager.currentItem].name = "Home"

        mainActivityRef.binding.topSearchBar.setText("")
        binding.searchView.setQuery("",false)
        mainActivityRef.binding.webIcon.setImageResource(R.drawable.ic_search)

        mainActivityRef.binding.refreshBtn.visibility = View.GONE

        binding.searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(result: String?): Boolean {
                if (checkForInternet(requireContext()))
                   changedTab(result!!, BrowseFragment(result))
                else
                    Snackbar.make(binding.root, "Internet is Not Connected\uD83D\uDE03", 3000).show()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        mainActivityRef.binding.goBtn.setOnClickListener(){
            if (checkForInternet(requireContext()))
                changedTab(mainActivityRef.binding.topSearchBar.text.toString(),
                    BrowseFragment(mainActivityRef.binding.topSearchBar.text.toString())
                )
            else
                Snackbar.make(binding.root, "Internet is Not Connected\uD83D\uDE03", 3000).show()
        }



        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.setItemViewCacheSize(5)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 5) // number of bookmark in one line
        binding.recyclerView.adapter = BookmarkAdapter(requireContext())

        if (MainActivity.bookmarkList.size < 1)
            binding.viewAllBtn.visibility = View.GONE
        binding.viewAllBtn.setOnClickListener{
            startActivity(Intent(requireContext(), BookmarkActivity::class.java))
        }




    }
}