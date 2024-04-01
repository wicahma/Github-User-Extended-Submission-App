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
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.data.local.entity.UserBookmarkEntity
import com.dicoding.dicodingsubmission_aplikasigithubuserextended.databinding.GithubCardRowBinding

class BookmarkListAdapter(private val fragment: Fragment) :
    ListAdapter<UserBookmarkEntity, BookmarkListAdapter.BookmarkViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var onItemLongPressCallback: OnBookmarkLongPressCallback


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setOnItemBookmarkCallback(onItemLongPressCallback: OnBookmarkLongPressCallback) {
        this.onItemLongPressCallback = onItemLongPressCallback
    }

    class BookmarkViewHolder(
        val binding: GithubCardRowBinding,
        private val fragment: Fragment,
        private val onItemClickCallback: OnItemClickCallback
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserBookmarkEntity) {
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
    ): BookmarkViewHolder {
        val binding =
            GithubCardRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding, fragment, onItemClickCallback)
    }


    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserBookmarkEntity>() {
            override fun areItemsTheSame(
                oldItem: UserBookmarkEntity,
                newItem: UserBookmarkEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UserBookmarkEntity,
                newItem: UserBookmarkEntity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserBookmarkEntity, view: View)
    }

    interface OnBookmarkLongPressCallback {
        fun onItemLongPressed(data: UserBookmarkEntity, view: View)
    }
}