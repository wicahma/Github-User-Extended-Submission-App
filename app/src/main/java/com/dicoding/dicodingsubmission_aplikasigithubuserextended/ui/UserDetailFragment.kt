package com.dicoding.dicodingsubmission_aplikasigithubuserextended.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.R
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.Result
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.databinding.FragmentUserDetailBinding
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.utils.Event
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.utils.SettingPreferences
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.utils.dataStore
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailFragment : Fragment() {

    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_user_following,
            R.string.tab_user_followers
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance(requireActivity().dataStore)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), pref)
        val userDetailViewModel: UserDetailViewModel by viewModels {
            factory
        }

        val dataUsername = UserDetailFragmentArgs.fromBundle(arguments as Bundle).username

        val userFollowPagerAdapter = activity?.let { UserFollowPagerAdapter(it, dataUsername) }
        binding.viewPager.adapter = userFollowPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        userDetailViewModel.getUserData(dataUsername).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val user = result.data
                        val profileUrl = user?.avatar_url
                            ?: "https://source.boringavatars.com/beam/120?colors=264653,2a9d8f,e9c46a,f4a261,e76f51"
                        Glide.with(requireContext()).load(profileUrl).into(binding.imgUserDetail)
                        val repo = "${user?.public_repos.toString()} Repo"
                        val followers = "${user?.followers.toString()} Followers"
                        val following = "${user?.following.toString()} Following"
                        binding.tvUsernameDetail.text = user?.login
                        binding.tvFollowersDetail.text = followers
                        binding.tvFollowingsDetail.text = following
                        binding.tvNameDetail.text = user?.name
                        binding.tvRepoDetail.text = repo
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        setSnackBar(Event(result.error))
                    }
                }
            }
        }

        binding.btnBrowser.setOnClickListener {
            val shareIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("http://github.com/$dataUsername"))
            startActivity(shareIntent)
        }

        binding.btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(
                Intent.EXTRA_TEXT,
                "Hai, aku menemukan user github dengan profile yang unik, cek disini: http://github.com/$dataUsername"
            )
            startActivity(Intent.createChooser(shareIntent, "Share Via"))
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
}