package com.caunb163.mxh.ui.main.messenger

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.repository.RepositoryUser
import com.caunb163.domain.model.Group
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.caunb163.mxh.state.State
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject

@InternalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class MessengerFragment : BaseFragment(R.layout.fragment_messenger), OnGroupClickListener {
    private val TAG = "MessengerFragment"

    private val viewModel: MessengerViewModel by inject()
    private val localStorage: LocalStorage by inject()
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var createBtn: MaterialButton
    private lateinit var glide: RequestManager
    private lateinit var groupAdapter: GroupAdapter
    private val list = mutableListOf<Group>()
    private val repositoryUser: RepositoryUser by inject()

    override fun initView(view: View) {
        recyclerView = view.findViewById(R.id.message_recyclerview)
        createBtn = view.findViewById(R.id.message_createbtn)
        progressBar = view.findViewById(R.id.message_progressbar)

        glide = Glide.with(this)
        glide.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
        )

        groupAdapter = GroupAdapter(glide, localStorage.getAccount()!!, this, repositoryUser)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
        }
        groupAdapter.updateData(list)

    }

    override fun initListener() {
        createBtn.setOnClickListener {
            findNavController().navigate(R.id.action_messengerFragment_to_createGroupFragment)
        }
    }

    override fun initObserve() {
        viewModel.listener.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccessListener(state.data as Group)
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onLoading() {}

    private fun onSuccessListener(group: Group) {
        if (group.name.isEmpty()) {
            val index = checkGroupIndex(group)
            if (index != -1) {
                list.removeAt(index)
                groupAdapter.notifyItemRemoved(index)
            }
        } else if (checkListAdd(group)) {
            val index = checkGroupIndex(group)
            if (index != -1) {
                list[index] = group
                groupAdapter.notifyItemChanged(index)
            }
        } else {
            list.add(group)
            groupAdapter.notifyItemInserted(list.size - 1)
        }

//        list.sortByDescending { it.createDate }
//        groupAdapter.notifyDataSetChanged()
    }

    private fun checkGroupIndex(group: Group): Int {
        for (index in 0 until list.size)
            if (list[index].groupId == group.groupId) {
                return index
            }
        return -1
    }

    private fun checkListAdd(group: Group): Boolean {
        list.forEach {
            if (it.groupId == group.groupId) {
                return true
            }
        }
        return false
    }

    private fun onFailure(message: String) {
        progressBar.visibility = View.INVISIBLE
        recyclerView.visibility = View.VISIBLE
        showToast(message)
    }

    override fun onGroupClick(group: Group) {
        val action = MessengerFragmentDirections.actionMessengerFragmentToChatFragment(group)
        findNavController().navigate(action)
    }
}