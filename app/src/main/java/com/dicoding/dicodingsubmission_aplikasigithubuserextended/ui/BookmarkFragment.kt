package com.dicoding.dicodingsubmission_aplikasigithubuserextended.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.entity.UserBookmarkEntity
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.databinding.FragmentBookmarkBinding
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.utils.Event
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.utils.SettingPreferences
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.utils.dataStore
import com.google.android.material.snackbar.Snackbar

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingPreferences.getInstance(requireActivity().dataStore)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), pref)
        val bookmarkViewModel: BookmarkViewModel by viewModels { factory }

        bookmarkViewModel.getBookmarkedUser().observe(viewLifecycleOwner) { bookmarkedUser ->
            setUserListData(bookmarkedUser, bookmarkViewModel)
        }
    }

    private fun setUserListData(
        userResponse: List<UserBookmarkEntity>,
        viewModel: BookmarkViewModel
    ) {
        val adapter = BookmarkListAdapter(this)
        adapter.submitList(userResponse)
        binding.rvBookmark.adapter = adapter

        adapter.setOnItemClickCallback(object : BookmarkListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserBookmarkEntity, view: View) {
                val toUserDetailFragment =
                    BookmarkFragmentDirections.actionBookmarkFragmentToUserDetailFragment(
                        data.login
                    )
                view.findNavController().safeNavigate(toUserDetailFragment)
            }
        })

        adapter.setOnItemBookmarkCallback(object : BookmarkListAdapter.OnBookmarkLongPressCallback {
            override fun onItemLongPressed(data: UserBookmarkEntity, view: View) {
                if (data.isBookmarked) {
                    viewModel.deleteUser(data)
                    setSnackBar(Event("Bookmark berhasil dihapus!"))
                } else {
                    viewModel.saveUser(data)
                }
            }

        })
    }

    private fun setSnackBar(e: Event<String>) {
        e.getContentIfNotHandled()?.let {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
        }
    }

    fun NavController.safeNavigate(direction: NavDirections) {
        Log.d("clickTag", "Click happened")
        currentDestination?.getAction(direction.actionId)?.run {
            Log.d("clickTag", "Click Propagated")
            navigate(direction)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}