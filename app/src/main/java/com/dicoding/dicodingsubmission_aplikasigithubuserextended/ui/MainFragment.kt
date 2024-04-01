package com.dicoding.dicodingsubmission_aplikasigithubuserextended.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.Result
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.entity.UserEntity
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.databinding.FragmentMainBinding
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.utils.Event
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.utils.SettingPreferences
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.utils.dataStore
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance(requireActivity().dataStore)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), pref)
        val mainViewModel: MainViewModel by viewModels { factory }

        mainViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        mainViewModel.getAllUser().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val userData = result.data
                        setUserListData(userData, mainViewModel)
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        setSnackBar(Event(result.error))
                    }
                }
            }
        }

        searchViewListener(mainViewModel)

        binding.bookmarkButton.setOnClickListener {
            val toBookmarkFragment =
                MainFragmentDirections.actionMainFragmentToBookmarkFragment()
            view.findNavController().navigate(toBookmarkFragment)
        }
        binding.switchTheme.setOnCheckedChangeListener { buttonView, isChecked ->
            mainViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun setSnackBar(e: Event<String>) {
        e.getContentIfNotHandled()?.let {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun searchViewListener(viewModel: MainViewModel) {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    binding.searchBar.setText(searchView.text)
                    searchView.hide()
                    viewModel.searchUser(searchView.text.toString())
                    false
                }
        }
    }

    private fun setUserListData(userResponse: List<UserEntity>, viewModel: MainViewModel) {
        val adapter = UserListAdapter(this)
        adapter.submitList(userResponse)
        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserEntity, view: View) {
                val toUserDetailFragment =
                    MainFragmentDirections.actionMainFragmentToUserDetailFragment(
                        data.login
                    )
                view.findNavController().safeNavigate(toUserDetailFragment)
            }
        })

        adapter.setOnItemBookmarkCallback(object : UserListAdapter.OnBookmarkLongPressCallback {
            override fun onItemLongPressed(data: UserEntity, view: View) {
                if (data.isBookmarked) {
                    viewModel.deleteUser(data)
                } else {
                    viewModel.saveUser(data)
                }
            }

        })
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