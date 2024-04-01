package com.dicoding.dicodingsubmission_aplikasigithubuserextended.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.R
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.entity.UserEntity
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.databinding.GithubCardRowBinding


class UserListAdapter(private val fragment: Fragment) :
    ListAdapter<UserEntity, UserListAdapter.UserViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var onItemLongPressCallback: OnBookmarkLongPressCallback


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setOnItemBookmarkCallback(onItemLongPressCallback: OnBookmarkLongPressCallback) {
        this.onItemLongPressCallback = onItemLongPressCallback
    }

    class UserViewHolder(
        val binding: GithubCardRowBinding,
        private val fragment: Fragment,
        private val onItemClickCallback: OnItemClickCallback
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserEntity) {
            val id = "ID - ${user.id}"
            binding.tvUsername.text = user.login
            binding.tvID.text = id
            binding.tvNodeID.text = user.node_id
            Glide.with(fragment).load(user.avatar_url).into(binding.imgDiscordCard)
            binding.root.setOnClickListener { view ->
                onItemClickCallback.onItemClicked(
                    user,
                    view
                )
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val binding =
            GithubCardRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding, fragment, onItemClickCallback)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        val ivBookmark = holder.binding.favButton

        if (user.isBookmarked) {
            val img = ContextCompat.getDrawable(
                ivBookmark.context,
                R.drawable.ic_fav_enabled
            )
            img?.setTint(Color.RED)
            ivBookmark.setImageDrawable(img)

        } else {
            val img = ContextCompat.getDrawable(
                ivBookmark.context,
                R.drawable.ic_fav_disable
            )
            img?.setTint(Color.parseColor("#f7ebeb"))
            ivBookmark.setImageDrawable(img)
        }
        holder.binding.root.setOnLongClickListener { view ->
            onItemLongPressCallback.onItemLongPressed(user, view)
            true
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserEntity>() {
            override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserEntity, view: View)
    }

    interface OnBookmarkLongPressCallback {
        fun onItemLongPressed(data: UserEntity, view: View)
    }
}
