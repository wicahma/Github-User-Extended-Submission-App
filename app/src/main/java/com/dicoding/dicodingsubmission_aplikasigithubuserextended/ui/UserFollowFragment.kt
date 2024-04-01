package com.dicoding.dicodingsubmission_aplikasigithubuserextended.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.Result
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.entity.UserEntity
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.databinding.FragmentUserFollowBinding
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.utils.Event
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.utils.SettingPreferences
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.utils.dataStore
import com.google.android.material.snackbar.Snackbar

class UserFollowFragment : Fragment() {
    private var _binding: FragmentUserFollowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentName = arguments?.getString(FOLLOW_TYPE)
        val username = arguments?.getString(USERNAME)

        val pref = SettingPreferences.getInstance(requireActivity().dataStore)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), pref)
        val userFollowViewModel: UserFollowViewModel by viewModels {
            factory
        }

        if (fragmentName != null && username != null) {
            userFollowViewModel.getListFollow(username, fragmentName)
                .observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }

                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val userData = result.data
                                setUserListData(userData, userFollowViewModel)
                            }

                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                setSnackBar(Event(result.error))
                            }
                        }
                    }
                }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setSnackBar(e: Event<String>) {
        e.getContentIfNotHandled()?.let {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setUserListData(userResponse: List<UserEntity>, viewModel: UserFollowViewModel) {
        val adapter = UserListAdapter(this)
        adapter.submitList(userResponse)
        binding.rvFollowers.adapter = adapter

        adapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserEntity, view: View) {
            }
        })

        adapter.setOnItemBookmarkCallback(object : UserListAdapter.OnBookmarkLongPressCallback {
            override fun onItemLongPressed(data: UserEntity, view: View) {
            }
        })
    }

    companion object {
        const val FOLLOW_TYPE = "follow_type"
        const val USERNAME = "username"
    }
}
